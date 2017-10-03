package com.metasoft;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.GenericMessage;

//@SpringBootApplication
//@EnableBinding(Source.class)
public class StreamdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamdemoApplication.class, args);
    }

    //@Bean
    //@InboundChannelAdapter(value = Source.OUTPUT)
    public MessageSource<String> timerMessageSource() {
        return () -> new GenericMessage<>(new SimpleDateFormat().format(new Date()));
    }

}