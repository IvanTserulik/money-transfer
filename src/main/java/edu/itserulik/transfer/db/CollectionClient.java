package edu.itserulik.transfer.db;

import com.mongodb.reactivestreams.client.MongoCollection;

public interface CollectionClient {

    <T> MongoCollection<T> getMongoCollection(Class<T> clazz);
}
