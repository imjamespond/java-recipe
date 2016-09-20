package com.james.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.ProducerCallback;
import org.springframework.jms.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {
	private Logger log = Logger.getLogger(MessageSender.class);
	
	@Value("${jms.queue.name}")
	private String strQueue;
	
	public MessageSender() {

	}

	public void send(final String text) {
		JmsTemplate jmsTemplate = DaoFactory.getJmsTemplate();
		if (null == jmsTemplate)
			return;
		jmsTemplate.convertAndSend(text);

	}

	public void sendNcallback() {
		log.trace("Starting Example.");

		log.info("Sending msg to ExampleQueue.");

		JmsTemplate jmsTemplate = DaoFactory.getJmsTemplate();
		if (null == jmsTemplate)
			return;

		jmsTemplate.execute(new ProducerCallback<Object>() {
			@Override
			public Object doInJms(Session session, MessageProducer producer)
					throws JMSException {
				for (int i = 0; i < 10; i++) {
					TextMessage msg = session.createTextMessage("Message" + i);
					log.trace("Sending msg: " + msg);
					producer.send(msg);
				}
				return null;
			}

		});

		log.info("Receiving msg from ExampleQueue.");
		boolean startConn = true;
        jmsTemplate.execute(new SessionCallback<Object>() {
                @Override
                public Object doInJms(Session session) throws JMSException {
                        Queue queue = session.createQueue(strQueue);
                        MessageConsumer consumer = session.createConsumer(queue);
                        for (int i = 0; i < 10; i++) {
                                Message msg = consumer.receive();
                                log.trace("Received msg: " + msg);
                        }
                        return null;
                }
        }, startConn);


	}

}
