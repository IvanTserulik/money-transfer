package edu.itserulik.transfer.dao.impl;

import com.google.inject.Inject;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.Success;
import edu.itserulik.transfer.common.exception.NotEnoughMoneyException;
import edu.itserulik.transfer.common.exception.NotFoundException;
import edu.itserulik.transfer.dao.GenericDao;
import edu.itserulik.transfer.db.CollectionClient;
import edu.itserulik.transfer.model.document.Account;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class AccountDaoImpl implements GenericDao<Account> {

    private MongoCollection<Account> mongoCollection;

    @Inject
    public AccountDaoImpl(CollectionClient client) {
        mongoCollection = client.getMongoCollection(Account.class);
    }

    @Override
    public Mono<Account> getById(String id) {
        var objectId = new ObjectId(id);
        var bsonDocument = new BsonDocument("_id", new BsonObjectId(objectId));
        return Mono.from(mongoCollection.find(bsonDocument).first())
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    public Mono<Success> save(Account account) {
        if (BigDecimal.ZERO.compareTo(account.getBalance()) > 0) {
            throw new NotEnoughMoneyException();
        }
        return Mono.from(mongoCollection.insertOne(account));
    }

    @Override
    public Mono<UpdateResult> update(Account account) {
        if (BigDecimal.ZERO.compareTo(account.getBalance()) > 0) {
            throw new NotEnoughMoneyException();
        }
        return Mono.from(mongoCollection
                .replaceOne(new BsonDocument().append("_id", new BsonObjectId(account.getId())),
                        account));
    }
}
