package edu.itserulik.transfer.http;

import com.google.inject.AbstractModule;
import edu.itserulik.transfer.controller.ControllerModule;

public class HttpModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ControllerModule());
    }
}
