package edu.itserulik.transfer;

import com.google.inject.Guice;
import com.google.inject.Key;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.process.runtime.AbstractProcess;
import edu.itserulik.transfer.dao.GenericDao;
import edu.itserulik.transfer.db.EmbeddedMongoStarter;
import edu.itserulik.transfer.http.NettyServer;
import edu.itserulik.transfer.model.document.Account;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

class AbstractTest {

    private static NettyServer nettyServer;
    private static List<MongodProcess> mongoProcesses;
    static GenericDao<Account> dao;

    @BeforeAll
    static void setup() throws Exception {
        var injector = Guice.createInjector(new TestModule());

        var mongoStarter = injector.getInstance(EmbeddedMongoStarter.class);
        mongoProcesses = mongoStarter.startMongoEnv();

        nettyServer = injector.getInstance(NettyServer.class);
        new Thread(() -> nettyServer.getServer()
                .onDispose()
                .block()).start();

        dao = injector.getInstance(new Key<GenericDao<Account>>() {
        });
    }

    @AfterAll
    static void postTest() {
        if (mongoProcesses != null) {
            mongoProcesses.forEach(AbstractProcess::stop);
        }
    }

}
