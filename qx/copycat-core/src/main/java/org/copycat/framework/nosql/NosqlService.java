package org.copycat.framework.nosql;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.copycat.framework.BeanMapper;
import org.copycat.framework.CollectionMap;
import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DBCollection;

public abstract class NosqlService<T> {
	protected NosqlTemplate nosqlTemplate;
	protected DBCollection collection;
	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public NosqlService() {
		this.clazz = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Autowired
	public void setNosqlTemplate(NosqlTemplate nosqlTemplate) {
		this.nosqlTemplate = nosqlTemplate;
		CollectionMap map = BeanMapper.convertCollection(clazz);
		collection = nosqlTemplate.getCollection(map.getCollectionName());
	}

	protected Query<T> query() {
		return nosqlTemplate.query(clazz);
	}

	public T get(String id) {
		return nosqlTemplate.get(clazz, id);
	}

	public String save(T object) {
		return nosqlTemplate.save(object);
	}

	public void delete(String id) {
		nosqlTemplate.delete(clazz, id);
	}

	public void update(T object) {
		nosqlTemplate.update(object);
	}

	public List<T> list() {
		return nosqlTemplate.list(clazz);
	}

	public List<T> list(Page page) {
		return nosqlTemplate.list(clazz, page);
	}
}
