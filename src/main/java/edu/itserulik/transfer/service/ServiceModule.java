package edu.itserulik.transfer.service;

import com.google.inject.AbstractModule;
import edu.itserulik.transfer.dao.DaoModule;
import edu.itserulik.transfer.service.impl.AccountServiceImpl;
import edu.itserulik.transfer.service.impl.TransferServiceImpl;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DaoModule());

        bind(TransferService.class).to(TransferServiceImpl.class);

        bind(AccountService.class).to(AccountServiceImpl.class);
    }
}
