package com.ets.topic;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author yusufakhond
 */
public class MyJMSTopicReceiver {

    public static void main(String[] args) {
        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        p.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
        p.put(Context.PROVIDER_URL, "iiop://localhost:3700");

        System.out.println("Starting message receiver...");

        try {
            Context ctx = new InitialContext(p);
            //1.Create and start connection
            //myTopicConnectionFactory was setup in glassfish
            TopicConnectionFactory f = (TopicConnectionFactory) ctx.lookup("myTopicConnectionFactory");
            TopicConnection con = f.createTopicConnection();
            con.start();

            //2.Create session
            TopicSession session = con.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            //3.Obtain topic object
            Topic topic = (Topic) ctx.lookup("myTopic");

            TopicSubscriber receiver = session.createSubscriber(topic);
            receiver.setMessageListener((msg) -> {
                try {
                    TextMessage message = (TextMessage) msg;
                    System.out.println("Received:>>" + message.getText());
                } catch (JMSException ex) {
                    Logger.getLogger(MyJMSTopicReceiver.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            while (true) {
                Thread.sleep(1000);
            }
        } catch (NamingException | JMSException | InterruptedException ex) {
            Logger.getLogger(MyJMSTopicReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
