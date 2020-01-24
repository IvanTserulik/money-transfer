package edu.itserulik.transfer.db;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.reactivestreams.client.MongoClient;
import edu.itserulik.transfer.common.PropertiesModule;
import edu.itserulik.transfer.db.impl.CollectionClientImpl;
import edu.itserulik.transfer.db.impl.MongoProviderImpl;

public class DbModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new PropertiesModule());

        bind(MongoProvider.class).to(MongoProviderImpl.class);

        bind(CollectionClient.class).to(CollectionClientImpl.class);
    }

    @Provides
    @Singleton
    public MongoClient getMongoClient(MongoProvider mongoProvider){
        return mongoProvider.getMongoClient();
    }
}
