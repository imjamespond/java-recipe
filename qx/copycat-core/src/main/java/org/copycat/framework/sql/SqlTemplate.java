package org.copycat.framework.sql;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.copycat.framework.BeanMapper;
import org.copycat.framework.Page;
import org.copycat.framework.PropertyUtils;
import org.copycat.framework.TableMap;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class SqlTemplate {
	private NamedParameterJdbcTemplate jdbcTemplate;

	public SqlTemplate(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		PropertyUtils.init();
	}

	public <T> Query<T> query(Class<T> clazz) {
		return new Query<T>(clazz, this);
	}

	public <T> T get(Class<T> clazz, Object id) {
		TableMap map = BeanMapper.convertTable(clazz);
		return queryForObject(map.toSelectWidthId(), clazz, id);
	}

	public void save(Object id, Object object) {
		TableMap map = BeanMapper.convertTable(object.getClass());
		PropertyUtils.setProperty(object, map.getId(), id);
		saveObject(map.toInsert(), object);
	}

	public Long save(Object object) {
		TableMap map = BeanMapper.convertTable(object.getClass());
		long id = queryForLong(map.toSequnce());
		PropertyUtils.setProperty(object, map.getId(), id);
		saveObject(map.toInsert(), object);
		return id;
	}

	public int update(Object object) {
		TableMap map = BeanMapper.convertTable(object.getClass());
		return updateObject(map.toUpdateWidthId(), object);
	}

	public int delete(Class<?> clazz, Object id) {
		TableMap map = BeanMapper.convertTable(clazz);
		return deleteArray(map.toDeleteWidthId(), id);
	}

	public <T> List<T> list(Class<T> clazz) {
		TableMap map = BeanMapper.convertTable(clazz);
		return queryForList(map.toList(), clazz);
	}

	public <T> List<T> list(Class<T> clazz, Page page) {
		TableMap map = BeanMapper.convertTable(clazz);
		int total = this.queryForInt(map.toCount());
		page.setTotal(total);
		return queryForList(map.toListWidthPage(), clazz, page.getOffset(),
				page.getLimit());
	}

	public int saveObject(String sql, Object object) {
		return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(
				object));
	}

	public int saveMap(String sql, Map<String, ?> paramMap) {
		return jdbcTemplate.update(sql, paramMap);
	}

	public int saveArray(String sql, Object... args) {
		return jdbcTemplate.getJdbcOperations().update(sql, args);
	}

	public int updateObject(String sql, Object object) {
		return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(
				object));
	}

	public int updateMap(String sql, Map<String, ?> paramMap) {
		return jdbcTemplate.update(sql, paramMap);
	}

	public int updateArray(String sql, Object... args) {
		return jdbcTemplate.getJdbcOperations().update(sql, args);
	}

	public int deleteObject(String sql, Object object) {
		return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(
				object));
	}

	public int deleteMap(String sql, Map<String, ?> paramMap) {
		return jdbcTemplate.update(sql, paramMap);
	}

	public int deleteArray(String sql, Object... args) {
		return jdbcTemplate.getJdbcOperations().update(sql, args);
	}

	public <T> T queryForObject(String sql, Class<T> clazz,
			Map<String, ?> paramMap) {
		try {
			return jdbcTemplate.queryForObject(sql, paramMap,
					BeanPropertyRowMapper.newInstance(clazz));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public <T> T queryForObject(String sql, Class<T> clazz, Object... args) {
		try {
			return jdbcTemplate.getJdbcOperations().queryForObject(sql,
					BeanPropertyRowMapper.newInstance(clazz), args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Map<String, Object> queryForMap(String sql, Object... args) {
		return jdbcTemplate.getJdbcOperations().queryForMap(sql, args);
	}

	public <T> List<T> queryForList(String sql, Class<T> clazz,
			Map<String, ?> paramMap) {
		return jdbcTemplate.query(sql, paramMap,
				BeanPropertyRowMapper.newInstance(clazz));
	}

	public <T> List<T> queryForList(String sql, Class<T> clazz, Object... args) {
		return jdbcTemplate.getJdbcOperations().query(sql,
				BeanPropertyRowMapper.newInstance(clazz), args);
	}

	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		return jdbcTemplate.getJdbcOperations().queryForList(sql, args);
	}

	public int queryForInt(String sql, Object... args) {
		return query(sql, Integer.class, args);
	}

	public long queryForLong(String sql, Object... args) {
		return query(sql, Long.class, args);
	}

	public <T> T query(String sql, Class<T> requiredType,
			Map<String, ?> paramMap) {
		return jdbcTemplate.queryForObject(sql, paramMap, requiredType);
	}

	public <T> T query(String sql, Class<T> requiredType, Object... args) {
		return jdbcTemplate.getJdbcOperations().queryForObject(sql,
				requiredType, args);
	}
}
