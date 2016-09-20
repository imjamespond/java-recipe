package com.james.jms;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ReceiverApp {
    public static void main( String[] args )
    {
        new ClassPathXmlApplicationContext("applicationContext.xml", "springJMSReceiver.xml");
    }
}
