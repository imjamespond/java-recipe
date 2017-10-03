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

package com.metasoft.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;

import com.metasoft.ProductProcessor;

/**
 * @author Marius Bogoevici
 */
@EnableBinding({Processor.class, ProductProcessor.class})
public class KafkaService {
	public static Logger logger = LoggerFactory.getLogger(KafkaService.class);

	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	public Message<String> sendTransform(Message<String> message) {
		logger.info(message.getPayload());
		return message;
	}
	
//    @StreamListener(Processor.INPUT)
//    public void input(Message<String> message) {
//        System.out.println("一般监听收到：" + message.getPayload());
//    }
//	
    @StreamListener(ProductProcessor.INPUT_PRODUCT_ADD)
    public void inputProductAdd(Message<String> message) {
        System.out.println("新增产品监听收到：" + message.getPayload());
    }
}
