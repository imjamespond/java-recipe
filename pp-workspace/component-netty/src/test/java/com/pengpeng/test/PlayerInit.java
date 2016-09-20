package com.pengpeng.test;

import com.pengpeng.ServerConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PlayerInit {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"context-stargame.xml","beanRefPlayerDataAccess.xml","beanRefRuleDataAccess.xml"});
//        MingXingDataDao dao = (MingXingDataDao)context.getBean("mingXingDataDao");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServerConfig.class);
        context.scan("com.com.pengpeng.stargame","org.springframework");
    }

}
