/*
 * Copyright 2013 Harald Wellmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blogspot.hwellmann.purejms.calculator;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @author Harald Wellmann
 * 
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/calculator"),
    @ActivationConfigProperty(propertyName = "useJndi", propertyValue = "true") })
public class CalculatorMdb implements MessageListener {

    @Inject
    @Named("calculatorImpl")
    private Calculator calculator;

    @Inject
    private TextBean textBean;

    @Resource(mappedName = "java:/jms/ConnectionFactory")
    private ConnectionFactory cf;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String text = ((TextMessage) message).getText();
                textBean.setText(text);
                String[] words = text.split("\\+");
                int op1 = Integer.parseInt(words[0]);
                int op2 = Integer.parseInt(words[1]);
                Future<Integer> sum = calculator.add(op1, op2);

                Destination replyTo = message.getJMSReplyTo();
                Connection connection = cf.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer producer = session.createProducer(replyTo);
                ObjectMessage reply = session.createObjectMessage(sum.get());
                producer.send(reply);
                session.close();
                connection.close();
            }
        }
        catch (JMSException exc) {

        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
