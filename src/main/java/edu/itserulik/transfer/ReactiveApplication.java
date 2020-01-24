package edu.itserulik.transfer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.itserulik.transfer.db.EmbeddedMongoStarter;
import edu.itserulik.transfer.http.HttpModule;
import edu.itserulik.transfer.http.NettyServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReactiveApplication {

    public static void main(String[] args) throws Exception {
        log.info("Google Guice processes injections..");
        Injector injector = Guice.createInjector(new HttpModule());

        log.info("Staring MongoDB..");
        var mongoStarter = injector.getInstance(EmbeddedMongoStarter.class);
        mongoStarter.startMongoEnv();

        log.info("Creating Netty server..");
        injector.getInstance(NettyServer.class)
                .getServer()
                .onDispose()
                .block();
    }
}
