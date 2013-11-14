package org.hiperion.node.service.action.email;

import com.googlecode.gmail4j.GmailClient;
import com.googlecode.gmail4j.auth.Credentials;
import com.googlecode.gmail4j.javamail.JavaMailGmailMessage;
import org.apache.log4j.Logger;
import org.hiperion.common.processors.ItemProcessor;
import org.hiperion.common.processors.QueueItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * User: iobestar
 * Date: 21.06.13.
 * Time: 11:25
 */
@Component
public class GmailNotificationService {

    private static Logger LOGGER = Logger.getLogger(GmailNotificationService.class);

    private final QueueItemProcessor<JavaMailGmailMessage> messageSender;
    private final GmailClient gmailClient;

    @Autowired
    public GmailNotificationService(GmailClient gmailClient) {
        this.gmailClient = gmailClient;
        this.messageSender = new QueueItemProcessor<JavaMailGmailMessage>(new ItemProcessor<JavaMailGmailMessage>() {
            @Override
            public void process(JavaMailGmailMessage javaMailGmailMessage) {
                try{
                    sendMessage(javaMailGmailMessage);
                    LOGGER.info("Message sent.");
                }catch (Exception e){
                    LOGGER.error(e);
                }
            }
        }, new LinkedBlockingQueue<JavaMailGmailMessage>());
    }

    @PostConstruct
    public void startService(){
        messageSender.start();
        LOGGER.info("Gmail message sender service started.");
    }

    @PreDestroy
    public void stopService(){
        this.messageSender.stop();
        LOGGER.info("Gmail message sender service stopped.");
    }

    private void sendMessage(JavaMailGmailMessage javaMailGmailMessage){
        gmailClient.send(javaMailGmailMessage);
    }

    public void submitForSending(JavaMailGmailMessage javaMailGmailMessage){
        messageSender.queue(javaMailGmailMessage);
    }

}
