package edu.itserulik.transfer.db;

import com.mongodb.reactivestreams.client.MongoClient;

public interface MongoProvider {

    MongoClient getMongoClient();
}
