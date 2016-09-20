package org.copycat.framework.nosql;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.copycat.framework.Page;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class SimpleNosqlTemplate {
	private DB db;

	public SimpleNosqlTemplate(NosqlTemplate nosqlTemplate) {
		this.db = nosqlTemplate.getDB();
	}

	public SimpleQuery query(String name) {
		return new SimpleQuery(name, db);
	}

	public DBObject get(String name, String id) {
		DBCollection collection = db.getCollection(name);
		return collection.findOne(new BasicDBObject("_id", new ObjectId(id)));

	}

	public String save(String name, DBObject object) {
		DBCollection collection = db.getCollection(name);
		DBObject dbObject = new BasicDBObject();
		collection.save(dbObject);
		return dbObject.get("_id").toString();
	}

	public void update(String name, DBObject object) {
		DBCollection collection = db.getCollection(name);
		DBObject queryObject = new BasicDBObject("_id", object.get("_id"));
		collection.update(queryObject, object);
	}

	public void delete(String name, String id) {
		DBCollection collection = db.getCollection(name);
		collection.remove(new BasicDBObject("_id", new ObjectId(id)));
	}

	public List<DBObject> list(String name) {
		DBCollection collection = db.getCollection(name);
		DBCursor cursor = collection.find();
		List<DBObject> list = new ArrayList<DBObject>();
		while (cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			list.add(dbObject);
		}
		return list;
	}

	public List<DBObject> list(String name, Page page) {
		DBCollection collection = db.getCollection(name);
		int total = (int) collection.count();
		page.setTotal(total);
		DBCursor cursor = collection.find().sort(new BasicDBObject("_id", -1))
				.skip(page.getOffset()).limit(page.getLimit());
		List<DBObject> list = new ArrayList<DBObject>();
		while (cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			list.add(dbObject);
		}
		return list;
	}

}
