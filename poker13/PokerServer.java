package com.chitu.poker;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.webserver.HttpServer;

public class PokerServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
		applicationContext.registerShutdownHook();
		
		HttpServer httpServer = SpringUtils.getBeanOfType(HttpServer.class);
		httpServer.start();
	}

}
