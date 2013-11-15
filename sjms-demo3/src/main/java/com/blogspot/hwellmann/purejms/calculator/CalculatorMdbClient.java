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

import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

/**
 * @author Harald Wellmann
 * 
 */
@RequestScoped
@Named("calculatorClient")
public class CalculatorMdbClient implements Calculator {

    @Resource(mappedName = "jms/ConnectionFactory")
    private ConnectionFactory cf;

    @Resource(mappedName = "jms/calculator")
    private Queue queue;

    @Override
    public Future<Integer> add(int op1, int op2) {
        Future<Integer> result = null;
        try {
            Connection connection = cf.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(queue);
            TextMessage msg = session.createTextMessage(String.format("%d+%d", op1, op2));
            TemporaryQueue tempQueue = session.createTemporaryQueue();
            msg.setJMSReplyTo(tempQueue);
            producer.send(msg);

            MessageConsumer consumer = session.createConsumer(tempQueue);
            Message reply = consumer.receive();
            if (reply instanceof ObjectMessage) {
                ObjectMessage om = (ObjectMessage) reply;
                Integer sum = (Integer) om.getObject();
                result = new Result<Integer>(sum);
            }
            session.close();
            connection.close();
        }
        catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

}
