package com.james.jms;

import javax.jms.*;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class MessageReceiver implements MessageListener {
	private Logger log = Logger.getLogger(MessageReceiver.class);
	
    public void onMessage(Message message) {
        if(message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                String text = textMessage.getText();
                log.info(String.format("Received: %s",text));
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
