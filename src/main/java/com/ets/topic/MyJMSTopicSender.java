package com.ets.topic;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author yusufakhond
 */
public class MyJMSTopicSender {

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

            TopicPublisher publisher = session.createPublisher(topic);

            TextMessage message = session.createTextMessage();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.println("Enter message:");
                String s = reader.readLine();
                if ("Exit".equals(s)) {
                    break;
                }

                message.setText(s);
                publisher.publish(message);
                System.out.println("Message sent.");
            }
            con.close();
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(MyJMSTopicReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyJMSTopicSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
