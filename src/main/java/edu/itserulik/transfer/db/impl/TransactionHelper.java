package edu.itserulik.transfer.db.impl;

import com.google.inject.Inject;
import com.mongodb.reactivestreams.client.MongoClient;
import reactor.core.publisher.Mono;

public class TransactionHelper {

    private MongoClient client;

    @Inject
    public TransactionHelper(MongoClient client) {
        this.client = client;
    }

    public <T> Mono<T> doInTransaction(Mono<T> doTo) {
        return Mono.from(client.startSession()).flatMap(session -> {
            session.startTransaction();
            return doTo
                    .onErrorResume(e -> Mono.from(session.abortTransaction())
                            .then(Mono.error(e)))
                    .flatMap(val -> Mono.from(session.commitTransaction())
                            .then(Mono.just(val)))
                    .doFinally(signal -> session.close());
        });
    }
}
