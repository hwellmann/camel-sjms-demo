package com.blogspot.hwellmann.purejms.camel;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.ConnectionFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.ManagementStatisticsLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.ProxyHelper;
import org.apache.camel.component.ejb.EjbComponent;
import org.apache.camel.component.sjmsee.SjmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import com.blogspot.hwellmann.purejms.calculator.Calculator;

public class CamelContextProducer {

    @Inject
    private ApplicationNameHolder applicationNameHolder;

    @Inject
    private ConnectionFactory cf;

    @Produces
    @ApplicationScoped
    public CamelContext camelContext() throws Exception {
        DefaultCamelContext context = new DefaultCamelContext();
        context.setName("camel-" + applicationNameHolder.getApplicationName());
        context.getManagementStrategy().setStatisticsLevel(ManagementStatisticsLevel.Off);

        SjmsComponent jmsComponent = new SjmsComponent();
        jmsComponent.setConnectionFactory(cf);

        context.addComponent("sjms", jmsComponent);
        context.addComponent("ejb", new EjbComponent());

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:calculatorProxy").routeId("calculatorSource")
                    .log("calculator proxy called")
                    .to("sjms:calculator-queue?exchangePattern=InOut");
                from("sjms:calculator-queue?exchangePattern=InOut&synchronous=false")
                    .routeId("calculatorSink").log("calling calculator impl")
                    .to(ejb("CalculatorImpl"));
            }

        });
        context.start();
        return context;
    }

    public String ejb(String simpleName) {
        return String.format("ejb://java:global/%s/%s", applicationNameHolder.getApplicationName(),
            simpleName);
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
