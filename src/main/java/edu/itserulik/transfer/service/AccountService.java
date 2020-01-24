package edu.itserulik.transfer.service;

import com.mongodb.reactivestreams.client.Success;
import edu.itserulik.transfer.model.document.Account;
import reactor.core.publisher.Mono;

public interface AccountService {

    Mono<Account> getAccountById(String id);

    Mono<Account> saveAccount(Account account);
}
