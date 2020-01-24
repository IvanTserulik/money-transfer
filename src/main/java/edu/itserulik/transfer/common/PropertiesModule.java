package edu.itserulik.transfer.common;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PropertiesModule extends AbstractModule {

    protected void configure() {
        String conf = "/application.properties";
        Properties defaults = new Properties();
        try {
            Properties props = new Properties(defaults);
            props.load(getClass().getResourceAsStream(conf));
            Names.bindProperties(binder(), props);
        } catch (IOException e) {
            log.error("Could not load config: ", e);
            throw new InternalError("Could not load config " + conf);
        }
    }
}
