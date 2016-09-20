package com.james.jms;

import org.springframework.jms.core.JmsTemplate;

import com.james.commons.utils.SpringUtils;

public class DaoFactory {
	private static JmsTemplate jmsTemplate;
	public static JmsTemplate getJmsTemplate() {
		if (jmsTemplate == null) {
			jmsTemplate = SpringUtils.getBean("jmsTemplate",JmsTemplate.class);
		}
		return jmsTemplate;
	}
}
