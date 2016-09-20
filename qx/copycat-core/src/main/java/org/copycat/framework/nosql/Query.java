package org.copycat.framework.nosql;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.copycat.framework.BeanMapper;
import org.copycat.framework.CollectionMap;
import org.copycat.framework.PropertyUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Query<T> {
	private BasicDBObject condition = new BasicDBObject();
	private BasicDBList conditionList;
	private BasicDBList orList;
	private BasicDBObject subCondition;
	private BasicDBObject update;
	private DBCollection collection;
	private Class<T> clazz;
	private int state = 0;
	private CollectionMap map;
	private String key;
	private BasicDBObject sort;
	private int skip;
	private int limit;

	protected Query(Class<T> clazz, DB db) {
		this.clazz = clazz;
		this.map = BeanMapper.convertCollection(clazz);
		this.collection = db.getCollection(map.getCollectionName());
	}

	protected Query(Class<T> clazz, DB db, String collectionName) {
		this.clazz = clazz;
		this.map = BeanMapper.convertCollection(clazz);
		this.collection = db.getCollection(collectionName);
	}

	public T get() {
		DBObject dbObject = collection.findOne(condition);
		return convert(dbObject);
	}

	public long count() {
//		System.out.println(condition);
		return collection.count(condition);
	}

	public List<T> list() {
//		 System.out.println(condition);
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
		List<T> list = new ArrayList<T>();
		while (cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			list.add(convert(dbObject));
		}
		return list;
	}

	public void update() {
		collection.update(condition, update, true, true);
	}

	public void delete() {
		collection.remove(condition);
	}

	public Query<T> sort(String key, boolean ascending) {
		int value = -1;
		if (ascending) {
			value = 1;
		}
		key = map.get(key);
		if (sort == null) {

			sort = new BasicDBObject(key, value);
		} else {
			sort.put(key, value);
		}
		return this;
	}

	public Query<T> skip(int skip) {
		this.skip = skip;
		return this;
	}

	public Query<T> limit(int limit) {
		this.limit = limit;
		return this;
	}

	public Query<T> where(String key) {
		this.key = map.get(key);
		return this;
	}

	public Query<T> and(String key) {
		this.key = map.get(key);
		if (this.state == 2) {

		}
		// this.subCondition = null;
		return this;
	}

	public Query<T> or() {
		if (conditionList == null) {
			orList = new BasicDBList();
			BasicDBObject orCondition = new BasicDBObject();
			orCondition.put("$or", orList);

			conditionList = new BasicDBList();
			conditionList.add(condition);
			conditionList.add(orCondition);

			condition = new BasicDBObject();
			condition.put("$and", conditionList);
		}
		this.state = 2;
		this.key = map.get(key);
		this.subCondition = new BasicDBObject();
		this.orList.add(subCondition);
		return this;
	}

	// public Query<T> or(String key) {
	// if (conditionList == null) {
	// conditionList = new BasicDBList();
	// conditionList.add(condition);
	// condition = new BasicDBObject();
	// condition.put("$or", conditionList);
	// }
	// this.state = 2;
	// this.key = map.get(key);
	// return this;
	// }

	public Query<T> set(String key, Object value) {
		if (update == null) {
			update = new BasicDBObject();
			update.put("$set", new BasicDBObject(map.get(key), value));
		} else {
			if (update.containsField("$set")) {
				((BasicDBObject) update.get("$set"))
						.append(map.get(key), value);
			}
		}
		return this;
	}

	public Query<T> inc(String key, Object value) {
		if (update == null) {
			update = new BasicDBObject();
			update.append("$inc", new BasicDBObject(map.get(key), value));
		} else {
			if (update.containsField("$inc")) {
				((BasicDBObject) update.get("$inc"))
						.append(map.get(key), value);
			}
		}
		return this;
	}

	public Query<T> is(Object value) {
		if (key.equals("_id")) {
			value = new ObjectId(value.toString());
		}
		if (this.state == 2) {
			subCondition.put(key, value);
		} else {
			condition.put(key, value);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public Query<T> in(Object value) {
		if (key.equals("_id")) {
			List<String> idList = (List<String>) value;
			List<ObjectId> objectIdList = new ArrayList<ObjectId>();
			for (String id : idList) {
				objectIdList.add(new ObjectId(id));
			}
			value = objectIdList;
		}
		if (this.state == 2) {
			subCondition.put(key, new BasicDBObject("$in", value));
		} else {
			// if (subCondition == null) {
			// subCondition = new BasicDBObject("$in", value);
			// condition.put(key, subCondition);
			// } else {
			// subCondition.append("$in", value);
			// }
			// condition.put("$in", value);
			condition.put(key, new BasicDBObject("$in", value));
		}
		return this;
	}

	public Query<T> like(Object value) {
		Pattern pattern = Pattern.compile("^.*" + value + ".*$");
		if (this.state == 2) {
			condition.put(key, pattern);
		} else {
			condition.put(key, pattern);
		}
		return this;
	}

	public Query<T> ne(Object value) {
		return compare("$ne", value);
	}

	public Query<T> lt(Object value) {
		return compare("$lt", value);
	}

	public Query<T> lte(Object value) {
		return compare("$lte", value);
	}

	public Query<T> gt(Object value) {
		return compare("$gt", value);
	}

	public Query<T> gte(Object value) {
		return compare("$gte", value);
	}

	public Query<T> compare(String operator, Object value) {
		if (key.equals("_id")) {
			value = new ObjectId(value.toString());
		}
		if (this.state == 2) {
			// if (subCondition == null) {
			// subCondition = new BasicDBObject(key, new BasicDBObject(
			// operator, value));
			// } else {
			// subCondition.append(operator, value);
			// }
			// conditionList.add(subCondition);

			subCondition.put(key, new BasicDBObject(operator, value));
		} else {
			// if (subCondition == null) {
			// subCondition = new BasicDBObject(operator, value);
			// condition.put(key, subCondition);
			// } else {
			// subCondition.append(operator, value);
			// }
			if(condition.containsField(key)){
				((BasicDBObject)condition.get(key)).append(operator, value);
			}else{
				condition.put(key, new BasicDBObject(operator, value));
			}
		}
		return this;
	}

	private T convert(DBObject dbObject) {
		if (dbObject == null) {
			return null;
		}
		try {
			T bean = (T) clazz.newInstance();
			Set<String> keySet = map.keySet();
			for (String key : keySet) {
				String element = map.get(key);
				Object value = dbObject.get(element);
				if (element.equals("_id")) {
					PropertyUtils.setProperty(bean, key, value.toString());
				} else {
					PropertyUtils.setProperty(bean, key, value);
				}
			}
			return bean;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
