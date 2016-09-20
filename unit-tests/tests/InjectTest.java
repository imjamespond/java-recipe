package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class InjectTest {
	private static ApplicationContext context;
	public static void main(String[] args) throws Exception {
		context = new ClassPathXmlApplicationContext("applicationInject.xml");
	}
}
