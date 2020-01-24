package edu.itserulik.transfer.service.impl;

import com.google.inject.Inject;
import edu.itserulik.transfer.dao.GenericDao;
import edu.itserulik.transfer.model.document.Account;
import edu.itserulik.transfer.service.AccountService;
import reactor.core.publisher.Mono;

public class AccountServiceImpl implements AccountService {

    private GenericDao<Account> accountDao;

    @Inject
    public AccountServiceImpl(GenericDao<Account> accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Mono<Account> getAccountById(String id) {
        return accountDao.getById(id);
    }

    @Override
    public Mono<Account> saveAccount(Account account) {
        return accountDao.save(account).map(s -> account);
    }
}
