package com.ets.queue;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author yusufakhond
 */
public class MyJMSSender {

    public static void main(String[] args) {
        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        p.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
        p.put(Context.PROVIDER_URL, "iiop://localhost:3700");

        try {
            System.out.println("Starting message receiver");

            Context ctx = new InitialContext(p);
            //1.Create and start connection
            //myQueueConnectionFactory was setup in glassfish
            QueueConnectionFactory f = (QueueConnectionFactory) ctx.lookup("myQueueConnectionFactory");
            QueueConnection connection = f.createQueueConnection();
            connection.start();

            //2.Create session
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            //3. Obtain queue object
            Queue q = (Queue) ctx.lookup("myQueue");

            QueueSender queueSender = session.createSender(q);
            TextMessage message = session.createTextMessage();
            message.setText("I am sending JMS message...");
            queueSender.send(message);
            System.out.println("Message was sent...");
            connection.close();

        } catch (NamingException | JMSException ex) {
            Logger.getLogger(MyJMSReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
