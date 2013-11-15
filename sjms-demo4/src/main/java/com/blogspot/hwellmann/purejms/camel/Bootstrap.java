package com.blogspot.hwellmann.purejms.camel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.apache.camel.CamelContext;

@Singleton
@Startup
public class Bootstrap {

    @Inject
    private CamelContext context;

    @PostConstruct
    public void start() {
        System.out.println("started " + context.getName());
    }

    @PreDestroy
    public void stop() {
        try {
            context.stop();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
