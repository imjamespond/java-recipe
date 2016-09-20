package org.copycat.framework;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.bson.BasicBSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BeanUtils {

	public static boolean isPrimitive(Class<?> clazz) {
		return clazz.isPrimitive() || clazz.equals(String.class)
				|| clazz.equals(Integer.class) || clazz.equals(Byte.class)
				|| clazz.equals(Long.class) || clazz.equals(Double.class)
				|| clazz.equals(Float.class) || clazz.equals(Character.class)
				|| clazz.equals(Short.class) || clazz.equals(Boolean.class)
				|| clazz.equals(Date.class) || clazz.equals(byte[].class)
				|| Map.class.isAssignableFrom(clazz);
	}

	public static <T> T convert(Class<T> clazz, BasicBSONObject dbObject) {
		CollectionMap map = BeanMapper.convertCollection(clazz);
		try {
			T bean = clazz.newInstance();
			Set<String> keySet = map.keySet();
			for (String key : keySet) {
				String element = map.get(key);
				Object value = dbObject.get(element);
				if (value == null) {
					continue;
				}
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

	public static DBObject convert(Object object) {
		CollectionMap map = BeanMapper.convertCollection(object.getClass());
		DBObject dbObject = new BasicDBObject();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			Object value = PropertyUtils.getProperty(object, key);
			String element = map.get(key);
			if (element.equals("_id") && value != null) {
				dbObject.put("_id", value);
			} else {
				dbObject.put(map.get(key), value);
			}
		}
		return dbObject;
	}
}
