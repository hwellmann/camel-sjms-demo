package com.blogspot.hwellmann.purejms.camel;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.camel.CamelContext;

import com.blogspot.hwellmann.purejms.calculator.Calculator;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class Bootstrap {

    @Inject
    private CamelContext context;

    @Inject
    @Named("calculatorProxy")
    private Calculator calculator;

    @PostConstruct
    public void init() {
        String name = context.getName();
        System.out.println("created Camel context " + name);

        int sum = calculator.add(30, 12);
        System.out.println("result: " + sum);
    }
}
