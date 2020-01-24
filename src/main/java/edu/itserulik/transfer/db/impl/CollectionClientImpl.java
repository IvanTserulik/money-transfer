package edu.itserulik.transfer.db.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import edu.itserulik.transfer.db.CollectionClient;

public class CollectionClientImpl implements CollectionClient {

    private String databaseName;
    private String collectionName;
    private MongoClient mongoClient;

    @Inject
    public CollectionClientImpl(MongoClient mongoClient,
                                @Named("mongo.databaseName") String databaseName,
                                @Named("mongo.collectionName") String collectionName) {
        this.databaseName = databaseName;
        this.collectionName = collectionName;
        this.mongoClient = mongoClient;
    }

    @Override
    public <T> MongoCollection<T> getMongoCollection(Class<T> clazz) {
        return mongoClient.getDatabase(databaseName)
                .getCollection(collectionName, clazz);
    }

}
