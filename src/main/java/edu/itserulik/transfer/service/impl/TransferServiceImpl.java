package edu.itserulik.transfer.service.impl;

import com.google.inject.Inject;
import edu.itserulik.transfer.dao.GenericDao;
import edu.itserulik.transfer.db.impl.TransactionHelper;
import edu.itserulik.transfer.model.document.Account;
import edu.itserulik.transfer.model.dto.TransferDto;
import edu.itserulik.transfer.service.TransferService;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class TransferServiceImpl implements TransferService {

    private GenericDao<Account> accountDao;
    private TransactionHelper helper;

    @Inject
    public TransferServiceImpl(GenericDao<Account> accountDao,
                               TransactionHelper helper) {
        this.accountDao = accountDao;
        this.helper = helper;
    }

    @Override
    public Mono<List<Account>> transferMoney(TransferDto dto) {
        var accountFromMono = accountDao.getById(dto.getAccountFromId());
        var accountToMono = accountDao.getById(dto.getAccountToId());

        var updateFromMono = accountFromMono.flatMap(account -> {
            account.setBalance(account.getBalance().subtract(dto.getSum()));
            return accountDao.update(account);
        });

        var updateToMono = accountToMono.flatMap(account -> {
            account.setBalance(account.getBalance().add(dto.getSum()));
            return accountDao.update(account);
        });

        var newAccountFromMono = accountDao.getById(dto.getAccountToId());
        var newAccountToMono = accountDao.getById(dto.getAccountFromId());

        return helper.doInTransaction(updateFromMono.then(updateToMono)
                .flatMap(v -> newAccountFromMono)
                .flatMap(accountFrom -> newAccountToMono.map(accountTo -> {
                    var list = new ArrayList<Account>();
                    list.add(accountFrom);
                    list.add(accountTo);
                    return list;
                }))
        );
    }
}
