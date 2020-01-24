package edu.itserulik.transfer.dao;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.Success;
import reactor.core.publisher.Mono;

public interface GenericDao<T> {

    Mono<T> getById(String id);

    Mono<Success> save(T t);

    Mono<UpdateResult> update(T t);
}
