package edu.itserulik.transfer.db.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import edu.itserulik.transfer.db.MongoProvider;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoProviderImpl implements MongoProvider {

    private String uri;
    private String mongoPackageScan;

    @Inject
    public MongoProviderImpl(@Named("mongo.uri") String uri,
                             @Named("mongo.mongoPackageScan") String mongoPackageScan) {
        this.uri = uri;
        this.mongoPackageScan = mongoPackageScan;
    }

    public MongoClient getMongoClient() {
        var codecRegistry = fromRegistries(MongoClients.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider
                        .builder()
                        .register(mongoPackageScan)
                        .build()));

        var options = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .codecRegistry(codecRegistry)
                .build();

        return MongoClients.create(options);
    }
}
