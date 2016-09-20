/**
 * 
 */
package com.chitu.chess;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.webserver.HttpServer;


/**
 * @author ivan
 *
 */
public class ChessServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
		applicationContext.registerShutdownHook();
		
		HttpServer httpServer = SpringUtils.getBeanOfType(HttpServer.class);
		httpServer.start();
		
		

		//ChessRoomPlayer c = new ChessRoomPlayer();
		//int[] arr = {0,4,8,12,16, 5,9,13,17,21, 44,45,46};
		//int[] arr = {0,4,8,12,16, 5,9,13,17,21, 44,45,48};
		//int[] arr = {0,8,12,16,20, 5,9,13,17,21, 44,45,48};
		//int[] arr = {0,1,2, 4,5,6, 12,13,14, 20,30,40,48};
		//c.playerCardSequence = arr;
		//c.cardAIThink();
	}

}
