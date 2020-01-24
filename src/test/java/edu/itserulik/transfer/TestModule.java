package edu.itserulik.transfer;

import com.google.inject.AbstractModule;
import edu.itserulik.transfer.http.HttpModule;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new HttpModule());
    }
}

