package com.metasoft.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationService.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {

		return applicationContext;
	}

	public static Object getBean(String name) {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(name);
	}

	public static <T> T getBean( Class<T> requiredType) {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(requiredType);
	}

	public static String[] getBeanNamesOfType(Class<?> type) {
		if (applicationContext == null) {
			return new String[0];
		}
		return applicationContext.getBeanNamesForType(type);
	}


}