package edu.itserulik.transfer.db;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.reactivestreams.client.MongoClient;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Feature;
import de.flapdoodle.embed.mongo.distribution.Versions;
import de.flapdoodle.embed.process.distribution.GenericVersion;
import de.flapdoodle.embed.process.runtime.Network;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import static de.flapdoodle.embed.mongo.tests.MongosSystemForTestFactory.ADMIN_DATABASE_NAME;

public class EmbeddedMongoStarter {

    private MongoClient client;
    private String ip;
    private int port;

    @Inject
    public EmbeddedMongoStarter(MongoClient client,
                                @Named("de.flapdoodle.ip") String ip,
                                @Named("de.flapdoodle.port") int port) {
        this.client = client;
        this.ip = ip;
        this.port = port;
    }

    public List<MongodProcess> startMongoEnv() throws Exception {
        var starter = MongodStarter.getDefaultInstance();

        var listOfMongoProcesses = new ArrayList<MongodProcess>();

        var list = new ArrayList<IMongodConfig>();
        list.add(buildMongoConfig(port));
        list.add(buildMongoConfig(new ServerSocket(0).getLocalPort()));

        for (IMongodConfig conf : list) {
            listOfMongoProcesses.add(starter.prepare(conf).start());
        }
        Thread.sleep(3000);

        var mongoAdminDB = client.getDatabase(ADMIN_DATABASE_NAME);

        Mono.from(mongoAdminDB.runCommand(new BasicDBObject("isMaster", 1))).block();

        // Build BSON object replica set settings
        DBObject replicaSetSetting = new BasicDBObject();
        replicaSetSetting.put("_id", "rs0");
        BasicDBList members = new BasicDBList();
        int i = 0;
        for (IMongodConfig mongoConfig : list) {
            DBObject host = new BasicDBObject();
            host.put("_id", i++);
            host.put("host", mongoConfig.net().getServerAddress().getHostName()
                    + ":" + mongoConfig.net().getPort());
            members.add(host);
        }

        replicaSetSetting.put("members", members);
        // Initialize replica set
        Mono.from(mongoAdminDB.runCommand(new BasicDBObject("replSetInitiate", replicaSetSetting))).block();
        Mono.from(mongoAdminDB.runCommand(new BasicDBObject("replSetGetStatus", 1))).block();
        Thread.sleep(15000);
        return listOfMongoProcesses;
    }

    private IMongodConfig buildMongoConfig(int port) throws IOException {
        var version = Versions.withFeatures(new GenericVersion("3.7.9"),
                Feature.ONLY_WITH_SSL, Feature.ONLY_64BIT, Feature.NO_HTTP_INTERFACE_ARG, Feature.STORAGE_ENGINE,
                Feature.MONGOS_CONFIGDB_SET_STYLE, Feature.NO_CHUNKSIZE_ARG);

        return new MongodConfigBuilder()
                .version(version)
                .net(new Net(ip, port, Network.localhostIsIPv6()))
                .withLaunchArgument("--quiet")
                .replication(new Storage(null, "rs0", 0))
                .shardServer(true)
                .cmdOptions(new MongoCmdOptionsBuilder()
                        .useNoPrealloc(false)
                        .useSmallFiles(false)
                        .useNoJournal(false)
                        .useStorageEngine("wiredTiger")
                        .verbose(false)
                        .build())
                .build();
    }
}
