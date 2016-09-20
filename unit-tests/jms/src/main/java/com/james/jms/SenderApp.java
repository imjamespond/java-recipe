package com.james.jms;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import com.james.commons.utils.SpringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SenderApp {
	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "/applicationContext.xml"});		
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String text;

			text = br.readLine();
			while (!text.isEmpty()) {
				System.out.println(String.format("send message: %s", text));
				SpringUtils.getBeanOfType(MessageSender.class).sendNcallback();//.send(text);
				text = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
