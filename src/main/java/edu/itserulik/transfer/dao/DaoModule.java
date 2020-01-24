package edu.itserulik.transfer.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import edu.itserulik.transfer.dao.impl.AccountDaoImpl;
import edu.itserulik.transfer.db.DbModule;
import edu.itserulik.transfer.model.document.Account;

public class DaoModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new DbModule());
        bind(new Key<GenericDao<Account>>(){}).to(AccountDaoImpl.class);
    }
}
