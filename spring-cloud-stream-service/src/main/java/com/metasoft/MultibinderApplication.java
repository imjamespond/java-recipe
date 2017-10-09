/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.metasoft;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.support.GenericMessage;

@SpringBootApplication
public class MultibinderApplication implements CommandLineRunner{
	Logger log = LoggerFactory.getLogger(MultibinderApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MultibinderApplication.class);
        app.setWebEnvironment(false); 
        app.run(args);//ConfigurableApplicationContext ctx = app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info(Arrays.toString(args));
	}
	
	@Autowired
	ProductProcessor processor;

    //@RequestMapping("/send")
    public String send() {
    	processor.outputProductAdd().send(MessageBuilder.withPayload(
    			new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(new Date())).build());
		return "test send";
	}

    @Bean
    @InboundChannelAdapter(value = Processor.OUTPUT, 
    	poller = @Poller(fixedDelay = "3000", maxMessagesPerPoll = "1"))
    public MessageSource<String> timerMessageSource() {
        return () -> MessageBuilder.withPayload("短消息-" + new Date()).build();
    }
    
    //@Bean
    //@InboundChannelAdapter(value = Source.OUTPUT)
    public MessageSource<String> testTimerMessageSource() {
        return () -> new GenericMessage<>(new SimpleDateFormat().format(new Date()));
    }
}