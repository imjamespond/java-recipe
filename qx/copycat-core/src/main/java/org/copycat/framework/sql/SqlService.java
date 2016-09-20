package org.copycat.framework.sql;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SqlService<T, PK extends Serializable> {
	@Autowired
	protected SqlTemplate sqlTemplate;
	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public SqlService() {
		this.clazz = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected Query<T> query() {
		return sqlTemplate.query(clazz);
	}

	public T get(PK id) {
		return sqlTemplate.get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public PK save(T object) {
		return (PK) sqlTemplate.save(object);
	}

	public void save(PK id, T object) {
		sqlTemplate.save(id, object);
	}

	public void delete(PK id) {
		sqlTemplate.delete(clazz, id);
	}

	public void update(T object) {
		sqlTemplate.update(object);
	}

	public List<T> list() {
		return sqlTemplate.list(clazz);
	}

	public List<T> list(Page page) {
		return sqlTemplate.list(clazz, page);
	}
}
