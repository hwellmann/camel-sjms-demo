package com.blogspot.hwellmann.purejms.calculator;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class CalculatorBootstrap {

    @Inject
    private AsyncExecutor executor;

    @Inject
    private CalculatorConsumer consumer;

    @PostConstruct
    public void init() {
        executor.execute(consumer);
    }

}
