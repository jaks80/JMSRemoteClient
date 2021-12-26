/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ets.queue;

import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
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
public class MyJMSReceiver {

    public static void main(String[] args) throws UnknownHostException {

        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        p.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
        p.put(Context.PROVIDER_URL, "iiop://localhost:3700");

        try {
            System.out.println("Starting message receiver...");

            Context ctx = new InitialContext(p);
            //1.Create and start connection
            //myQueueConnectionFactory was setup in glassfish
            QueueConnectionFactory f = (QueueConnectionFactory) ctx.lookup("myQueueConnectionFactory");
            QueueConnection con = f.createQueueConnection();
            con.start();

            //2.Create session
            QueueSession session = con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            //3. Obtain queue object
            Queue q = (Queue) ctx.lookup("myQueue");

            //4. Create queue receiver
            QueueReceiver receiver = session.createReceiver(q);
            
            receiver.setMessageListener((msg) -> {
                try {
                    TextMessage message = (TextMessage) msg;
                    System.out.println("Received:>>" + message.getText());
                } catch (JMSException ex) {
                    Logger.getLogger(MyJMSReceiver.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            while (true) {
                Thread.sleep(1000);
                //You have to force stop application
                
            }
        } catch (NamingException | JMSException | InterruptedException ex) {
            Logger.getLogger(MyJMSReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
