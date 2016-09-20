package org.copycat.framework.nosql;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class SimpleQuery {
	private DBObject condition = new BasicDBObject();
	private BasicDBList conditionList;
	private BasicDBObject subCondition;
	private BasicDBObject update;
	private DBCollection collection;
	private int state = 0;
	private String key;
	private BasicDBObject sort;
	private int type;
	private int skip;
	private int limit;

	protected SimpleQuery(String name, DB db) {
		this.collection = db.getCollection(name);
	}

	public BasicDBObject get() {
		return (BasicDBObject) collection.findOne(condition);
	}

	public long count() {
		return collection.count(condition);
	}

	public List<DBObject> list() {
		DBCursor cursor = collection.find(condition);
		if (sort != null) {
			cursor.sort(sort);
		}
		if (skip > 0) {
			cursor.skip(skip);
		}
		if (limit > 0) {
			cursor.limit(limit);
		}
		List<DBObject> list = new ArrayList<DBObject>();
		while (cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			list.add(dbObject);
		}
		return list;
	}

	public SimpleQuery update() {
		type = 2;
		return this;
	}

	public SimpleQuery delete() {
		type = 3;
		return this;
	}

	public void excute() {
		if (type == 2) {
			collection.findAndModify(condition, update);
		} else if (type == 3) {
			collection.remove(condition);
		}
	}

	public SimpleQuery sort(String key, boolean ascending) {
		int value = -1;
		if (ascending) {
			value = 1;
		}
		if (sort == null) {

			sort = new BasicDBObject(key, value);
		} else {
			sort.put(key, value);
		}
		return this;
	}

	public SimpleQuery skip(int skip) {
		this.skip = skip;
		return this;
	}

	public SimpleQuery limit(int limit) {
		this.limit = limit;
		return this;
	}

	public SimpleQuery where(String key) {
		this.key = key;
		return this;
	}

	public SimpleQuery and(String key) {
		this.key = key;
		this.subCondition = null;
		return this;
	}

	public SimpleQuery or(String key) {
		if (conditionList == null) {
			conditionList = new BasicDBList();
			conditionList.add(condition);
			condition = new BasicDBObject();
			condition.put("$or", conditionList);
		}
		this.state = 2;
		this.key = key;
		this.subCondition = null;
		return this;
	}

	public SimpleQuery set(String key, Object value) {
		if (update == null) {
			update = new BasicDBObject();
		}
		update.put("$set", new BasicDBObject(key, value));
		return this;
	}

	public SimpleQuery inc(String key, Object value) {
		if (update == null) {
			update = new BasicDBObject();
		}
		update.put("$inc", new BasicDBObject(key, value));
		return this;
	}

	public SimpleQuery is(Object value) {
		if (key.equals("_id")) {
			value = new ObjectId(value.toString());
		}
		if (this.state == 2) {
			if (subCondition == null) {
				subCondition = new BasicDBObject(key, value);
			}
			conditionList.add(subCondition);
		} else {
			condition.put(key, value);
		}
		return this;
	}

	public SimpleQuery ne(Object value) {
		return compare("$ne", value);
	}

	public SimpleQuery lt(Object value) {
		return compare("$lt", value);
	}

	public SimpleQuery lte(Object value) {
		return compare("$lte", value);
	}

	public SimpleQuery gt(Object value) {
		return compare("$gt", value);
	}

	public SimpleQuery gte(Object value) {
		return compare("$gte", value);
	}

	public SimpleQuery compare(String operator, Object value) {
		if (key.equals("_id")) {
			value = new ObjectId(value.toString());
		}
		if (this.state == 2) {
			if (subCondition == null) {
				subCondition = new BasicDBObject(key, new BasicDBObject(
						operator, value));
			} else {
				subCondition.append(operator, value);
			}
			conditionList.add(subCondition);
		} else {
			if (subCondition == null) {
				subCondition = new BasicDBObject(operator, value);
				condition.put(key, subCondition);
			} else {
				subCondition.append(operator, value);
			}
		}
		return this;
	}

}
