package com.blogspot.hwellmann.purejms.camel;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.jms.ConnectionFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.ManagementStatisticsLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.ProxyHelper;
import org.apache.camel.component.ejb.EjbComponent;
import org.apache.camel.component.sjms.SjmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import com.blogspot.hwellmann.purejms.calculator.Calculator;

public class CamelContextProducer {

    @Resource(mappedName = "java:jboss/exported/ConnectionFactory")
    private ConnectionFactory cf;

    @Produces
    @ApplicationScoped
    public CamelContext camelContext() throws Exception {
        DefaultCamelContext context = new DefaultCamelContext();
        context.getManagementStrategy().setStatisticsLevel(ManagementStatisticsLevel.Off);

        SjmsComponent jmsComponent = new SjmsComponent();
        jmsComponent.setConnectionFactory(cf);

        context.addComponent("sjms", jmsComponent);
        context.addComponent("ejb", new EjbComponent());

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:calculatorProxy")
                    .routeId("calculatorSource")
                    .log("calculator proxy called")
                    .to("sjms:calculator-queue?exchangePattern=InOut&responseTimeOut=1000000&synchronous=false");
                from("sjms:calculator-queue?exchangePattern=InOut").routeId("calculatorSink")
                    .log("calling calculator impl")
                    .to("ejb://java:global/sjms-demo2/CalculatorImpl");
            }

        });
        context.start();
        return context;
    }

    @Produces
    @Named("calculatorProxy")
    public Calculator calculator(CamelContext context) throws Exception {
        Endpoint endpoint = context.getEndpoint("direct:calculatorProxy");
        Calculator proxy = ProxyHelper.createProxy(endpoint, Calculator.class);
        return proxy;
    }

    public void stopCamelContext(@Disposes CamelContext context) throws Exception {
        context.stop();
    }
}
