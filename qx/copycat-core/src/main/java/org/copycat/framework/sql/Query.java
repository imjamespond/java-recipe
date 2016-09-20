package org.copycat.framework.sql;

import java.util.List;
import java.util.Map;

import org.copycat.framework.BeanMapper;
import org.copycat.framework.TableMap;

import com.google.common.collect.Maps;

public class Query<T> {
	private SqlTemplate sqlTemplate;
	private Class<T> requiredType;
	private StringBuilder sb;
	private TableMap map;
	private Map<String, Object> paramMap = Maps.newHashMap();
	private boolean setAble = false;
	private int sqlType = 0;

	protected Query(Class<T> clazz, SqlTemplate sqlTemplate) {
		this.requiredType = clazz;
		this.sqlTemplate = sqlTemplate;
		this.map = BeanMapper.convertTable(requiredType);
	}

	public Query<T> select() {
		sb = new StringBuilder();
		sb.append(map.toSelect());
		sqlType = 1;
		return this;
	}

	public Query<T> count() {
		sb = new StringBuilder();
		sb.append(map.toCount());
		sqlType = 1;
		return this;
	}

	public Query<T> update() {
		sb = new StringBuilder();
		sb.append("update ");
		sb.append(map.getTalbe());
		sb.append(" set ");
		sqlType = 2;
		return this;
	}

	public Query<T> delete() {
		sb = new StringBuilder();
		sqlType = 3;
		sb.append("delete from ");
		sb.append(map.getTalbe());
		return this;
	}

	public Query<T> set(String field, Object value) {
		if (setAble) {
			sb.append(", ");
		} else {
			setAble = true;
		}
		sb.append(map.getColumn(field));
		sb.append(" = :");
		sb.append(field);
		paramMap.put(field, value);
		return this;
	}

	public Query<T> set(String expressions) {
		setAble = true;
		sb.append(expressions);
		return this;
	}

	public Query<T> where() {
		if (sqlType == 2 && !setAble) {
			sb.append(map.toUpdate());
		}
		sb.append(" where ");
		return this;
	}

	public Query<T> where(String expressions) {
		if (sqlType == 2 && !setAble) {
			sb.append(map.toUpdate());
		}
		sb.append(" where ");
		sb.append(expressions);
		return this;
	}

	public Query<T> and() {
		sb.append(" and ");
		return this;
	}

	public Query<T> or() {
		sb.append(" or ");
		return this;
	}

	public Query<T> eq(String field, Object value) {
		sb.append(map.getColumn(field));
		sb.append(" = :");
		sb.append(field);
		paramMap.put(field, value);
		return this;
	}

	public Query<T> ne(String field, Object value) {
		sb.append(map.getColumn(field));
		sb.append(" <> :");
		sb.append(field);
		paramMap.put(field, value);
		return this;
	}

	public Query<T> gt(String field, Object value) {
		sb.append(map.getColumn(field));
		sb.append(" > :");
		sb.append(field);
		paramMap.put(field, value);
		return this;
	}

	public Query<T> ge(String field, Object value) {
		sb.append(map.getColumn(field));
		sb.append(" >= :");
		sb.append(field);
		paramMap.put(field, value);
		return this;
	}

	public Query<T> lt(String field, Object value) {
		sb.append(map.getColumn(field));
		sb.append(" < :");
		sb.append(field);
		paramMap.put(field, value);
		return this;
	}

	public Query<T> le(String field, Object value) {
		sb.append(map.getColumn(field));
		sb.append(" <= :");
		sb.append(field);
		paramMap.put(field, value);
		return this;
	}

	public Query<T> like(String field, Object value) {
		sb.append(map.getColumn(field));
		sb.append(" like :");
		sb.append(field);
		paramMap.put(field, value);
		return this;
	}

	public Query<T> ilike(String field, Object value) {
		sb.append(map.getColumn(field));
		sb.append(" ilike :");
		sb.append(field);
		paramMap.put(field, value);
		return this;
	}

	public Query<T> isNull(String field) {
		sb.append(map.getColumn(field));
		sb.append(" is null");
		return this;
	}

	public Query<T> isNotNull(String field) {
		sb.append(map.getColumn(field));
		sb.append(" is not null");
		return this;
	}

	public Query<T> order(String field, boolean desc) {
		sb.append(" order by ");
		sb.append(map.getColumn(field));
		if (desc) {
			sb.append(" desc");
		} else {
			sb.append(" asc");
		}
		return this;
	}

	public Query<T> offset(int number) {
		sb.append(" offset ");
		sb.append(number);
		return this;
	}

	public Query<T> limit(int number) {
		sb.append(" limit ");
		sb.append(number);
		return this;
	}

	public T get() {
		return sqlTemplate.queryForObject(toSQL(), requiredType, paramMap);
	}

	public T get(Map<String, ?> paramMap) {
		return sqlTemplate.queryForObject(toSQL(), requiredType, paramMap);
	}

	public T get(Object... args) {
		return sqlTemplate.queryForObject(toSQL(), requiredType, args);
	}

	public List<T> list() {
		return sqlTemplate.queryForList(toSQL(), requiredType, paramMap);
	}

	public List<T> list(Map<String, ?> paramMap) {
		return sqlTemplate.queryForList(toSQL(), requiredType, paramMap);
	}

	public List<T> list(Object... args) {
		return sqlTemplate.queryForList(toSQL(), requiredType, args);
	}

	public int toInt() {
		return sqlTemplate.query(toSQL(), Integer.class, paramMap);
	}

	public int toInt(Map<String, ?> paramMap) {
		return sqlTemplate.query(toSQL(), Integer.class, paramMap);
	}

	public int toInt(Object... args) {
		return sqlTemplate.query(toSQL(), Integer.class, args);
	}

	public int excute() {
		return sqlTemplate.updateMap(toSQL(), paramMap);
	}

	public int excute(Map<String, ?> paramMap) {
		return sqlTemplate.updateMap(toSQL(), paramMap);
	}

	public int excute(Object object) {
		return sqlTemplate.updateObject(toSQL(), object);
	}

	public int excute(Object... args) {
		return sqlTemplate.updateArray(toSQL(), args);
	}

	public String toSQL() {
		return sb.toString();
	}

	public void print() {
		System.out.println(sb.toString());
	}

}
