package edu.itserulik.transfer.controller;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import edu.itserulik.transfer.controller.impl.GetAccountController;
import edu.itserulik.transfer.controller.impl.SaveAccountController;
import edu.itserulik.transfer.controller.impl.TransferController;
import edu.itserulik.transfer.service.ServiceModule;

public class ControllerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ServiceModule());

        Multibinder<AbstractController> controllerBinder = Multibinder.newSetBinder(binder(), AbstractController.class);
        controllerBinder.addBinding().to(GetAccountController.class);
        controllerBinder.addBinding().to(TransferController.class);
        controllerBinder.addBinding().to(SaveAccountController.class);
    }
}
