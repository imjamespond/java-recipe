package org.copycat.framework.nosql;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.copycat.framework.BeanMapper;
import org.copycat.framework.BeanUtils;
import org.copycat.framework.CollectionMap;
import org.copycat.framework.Page;
import org.copycat.framework.PropertyUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class NosqlTemplate {
	private DB db;

	public NosqlTemplate(String host, int port, String databaseName) {
		PropertyUtils.init();
		try {
			MongoClient mongoClient = new MongoClient(host, port);
			db = mongoClient.getDB(databaseName);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	protected DB getDB() {
		return this.db;
	}

	public DBCollection getCollection(String name) {
		return db.getCollection(name);
	}

	public <T> Query<T> query(Class<T> clazz) {
		return new Query<T>(clazz, db);
	}

	public <T> Query<T> query(Class<T> clazz, String collectionName) {
		return new Query<T>(clazz, db, collectionName);
	}

	public <T> T get(Class<T> clazz, String id) {
		CollectionMap map = BeanMapper.convertCollection(clazz);
		DBCollection collection = db.getCollection(map.getCollectionName());
		DBObject dbObject = collection.findOne(new BasicDBObject("_id",
				new ObjectId(id)));
		if (dbObject == null) {
			return null;
		}
		return convert(map, clazz, dbObject);
	}

	public <T> T get(String collectionName, Class<T> clazz, String id) {
		CollectionMap map = BeanMapper.convertCollection(clazz);
		DBCollection collection = db.getCollection(collectionName);
		DBObject dbObject = collection.findOne(new BasicDBObject("_id",
				new ObjectId(id)));
		if (dbObject == null) {
			return null;
		}
		return convert(map, clazz, dbObject);
	}

	@SuppressWarnings("unchecked")
	private Object filter(Object object) {
		if (BeanUtils.isPrimitive(object.getClass())) {
			return object;
		}
		if (List.class.isAssignableFrom(object.getClass())) {
			List<Object> list = (List<Object>) object;
			for (int i = 0; i < list.size(); i++) {
				Object value = list.get(i);
				if (!BeanUtils.isPrimitive(value.getClass())) {
					list.set(i, BeanUtils.convert(value));
				}
			}
			return list;
		} else {
			return BeanUtils.convert(object);
		}
	}

	public String save(Object object) {
		CollectionMap map = BeanMapper.convertCollection(object.getClass());
		DBCollection collection = db.getCollection(map.getCollectionName());
		DBObject dbObject = new BasicDBObject();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			Object value = PropertyUtils.getProperty(object, key);
			if (value == null) {
				continue;
			}
			String element = map.get(key);
			if (element.equals("_id") && value != null) {
				dbObject.put("_id", value);
			} else {
				value = filter(value);
				dbObject.put(map.get(key), value);
			}
		}
		collection.save(dbObject);
		return dbObject.get("_id").toString();
	}

	public String save(String collectionName, Object object) {
		CollectionMap map = BeanMapper.convertCollection(object.getClass());
		DBCollection collection = db.getCollection(collectionName);
		DBObject dbObject = new BasicDBObject();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			Object value = PropertyUtils.getProperty(object, key);
			if (value == null) {
				continue;
			}
			String element = map.get(key);
			if (element.equals("_id") && value != null) {
				dbObject.put("_id", value);
			} else {
				filter(value);
				dbObject.put(map.get(key), value);
			}
		}
		collection.save(dbObject);
		return dbObject.get("_id").toString();
	}

	public void update(Object object) {
		CollectionMap map = BeanMapper.convertCollection(object.getClass());
		DBCollection collection = db.getCollection(map.getCollectionName());
		Set<String> keySet = map.keySet();
		DBObject queryObject = null;
		DBObject updateObject = new BasicDBObject();
		for (String key : keySet) {
			Object value = PropertyUtils.getProperty(object, key);
			String element = map.get(key);
			if (element.equals("_id")) {
				queryObject = new BasicDBObject("_id", new ObjectId(
						value.toString()));
			} else {
				if (value == null) {
					continue;
				}
				filter(value);
				updateObject.put(map.get(key), value);
			}
		}
		collection.update(queryObject, updateObject);
	}

	public void update(String collectionName, Object object) {
		CollectionMap map = BeanMapper.convertCollection(object.getClass());
		DBCollection collection = db.getCollection(collectionName);
		Set<String> keySet = map.keySet();
		DBObject queryObject = null;
		DBObject updateObject = new BasicDBObject();
		for (String key : keySet) {
			Object value = PropertyUtils.getProperty(object, key);
			String element = map.get(key);
			if (element.equals("_id")) {
				queryObject = new BasicDBObject("_id", new ObjectId(
						value.toString()));
			} else {
				if (value == null) {
					continue;
				}
				filter(value);
				updateObject.put(map.get(key), value);
			}
		}
		collection.update(queryObject, updateObject);
	}

	public void delete(Class<?> clazz, String id) {
		CollectionMap map = BeanMapper.convertCollection(clazz);
		DBCollection collection = db.getCollection(map.getCollectionName());
		collection.remove(new BasicDBObject("_id", new ObjectId(id)));
	}

	public <T> List<T> list(Class<T> clazz) {
		CollectionMap map = BeanMapper.convertCollection(clazz);
		DBCollection collection = db.getCollection(map.getCollectionName());
		DBCursor cursor = collection.find();
		List<T> list = new ArrayList<T>();
		while (cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			list.add(convert(map, clazz, dbObject));
		}
		return list;
	}

	public <T> List<T> list(Class<T> clazz, Page page) {
		CollectionMap map = BeanMapper.convertCollection(clazz);
		DBCollection collection = db.getCollection(map.getCollectionName());
		int total = (int) collection.count();
		page.setTotal(total);
		DBCursor cursor = collection.find().sort(new BasicDBObject("_id", -1))
				.skip(page.getOffset()).limit(page.getLimit());
		List<T> list = new ArrayList<T>();
		while (cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			list.add(convert(map, clazz, dbObject));
		}
		return list;
	}

	private <T> T convert(CollectionMap map, Class<T> clazz, DBObject dbObject) {
		try {
			T bean = clazz.newInstance();
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
