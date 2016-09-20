package com.james.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class PersistObject implements Persistable {
	public abstract Serializable id();

	@SuppressWarnings("unchecked")
	protected static <T extends Persistable> T get(Class<T> clazz, Serializable id) {
		Session session = DaoFactory.getGenericDao().getCurrentSession();
		Transaction tr = session.beginTransaction();		
		T t = (T) session.get(clazz, id);
		tr.commit();
		return t;
	}

	protected static <T extends Persistable> List<T> query(String hql,
			Object[] args,int start, int size) {
		Session session = DaoFactory.getGenericDao().getCurrentSession();
		Transaction tr = session.beginTransaction();
		Query query = session.createQuery(hql);
		query.setFirstResult(start).setMaxResults(size);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		@SuppressWarnings("unchecked")
		List<T> list = query.list();
		tr.commit();
		return list;
	}

	public <T> void delete() {
		Session session = DaoFactory.getGenericDao().getCurrentSession();
		Transaction tr = session.beginTransaction();
		session.delete(this);
		tr.commit();
	}
	
	public <T> void update() {
		Session session = DaoFactory.getGenericDao().getCurrentSession();
		Transaction tr = session.beginTransaction();
		session.update(this);
		tr.commit();
	}

	public void save() {
		Session session = DaoFactory.getGenericDao().getCurrentSession();
		Transaction tr = session.beginTransaction();
		session.save(this);
		tr.commit();
	}
}