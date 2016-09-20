package org.copycat.framework.nosql;

import java.util.List;

import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DBObject;

public abstract class SimpleNosqlService {
	@Autowired
	protected SimpleNosqlTemplate simpleNosqlTemplate;
	private String name;

	public SimpleNosqlService(String name) {
		this.name = name;
	}

	protected SimpleQuery query() {
		return simpleNosqlTemplate.query(name);
	}

	public DBObject get(String id) {
		return simpleNosqlTemplate.get(name, id);
	}

	public String save(DBObject object) {
		return simpleNosqlTemplate.save(name, object);
	}

	public void delete(String id) {
		simpleNosqlTemplate.delete(name, id);
	}

	public void update(DBObject object) {
		simpleNosqlTemplate.update(name, object);
	}

	public List<DBObject> list() {
		return simpleNosqlTemplate.list(name);
	}

	public List<DBObject> list(Page page) {
		return simpleNosqlTemplate.list(name, page);
	}
}
