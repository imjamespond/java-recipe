package com.james.hibernate;

import org.hibernate.SessionFactory;
import com.james.commons.utils.SpringUtils;

public class DaoFactory 
 {
	private static SessionFactory genericDao;
	public static SessionFactory getGenericDao() {
		if (genericDao == null) {
			genericDao = (SessionFactory) SpringUtils.getBean("sessionFactory",
					SessionFactory.class);
		}
		return genericDao;
	}

}