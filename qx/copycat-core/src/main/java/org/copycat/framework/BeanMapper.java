package org.copycat.framework;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.copycat.framework.annotation.Collection;
import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Element;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

public class BeanMapper {
	private static Map<Class<?>, TableMap> tableCache = new HashMap<Class<?>, TableMap>();
	private static Map<Class<?>, CollectionMap> collectionCache = new HashMap<Class<?>, CollectionMap>();

	public static CollectionMap convertCollection(Class<?> clazz) {
		if (collectionCache.containsKey(clazz)) {
			return collectionCache.get(clazz);
		}
		CollectionMap map = new CollectionMap();
		if (clazz.isAnnotationPresent(Collection.class)) {
			String collectionName = clazz.getAnnotation(Collection.class)
					.value();
			map.setCollectionName(collectionName);
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String key = field.getName();
			if (field.isAnnotationPresent(Element.class)) {
				String column = field.getAnnotation(Element.class).value();
				map.addElement(key, column);
			}
		}
		collectionCache.put(clazz, map);
		return map;
	}

	public static TableMap convertTable(Class<?> clazz) {
		if (tableCache.containsKey(clazz)) {
			return tableCache.get(clazz);
		}
		TableMap map = new TableMap();
		if (clazz.isAnnotationPresent(Table.class)) {
			String tableName = clazz.getAnnotation(Table.class).value();
			map.setTableName(tableName);
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				String key = field.getName();
				if (field.isAnnotationPresent(Column.class)) {
					String column = field.getAnnotation(Column.class).value();
					map.addColumn(key, column);
				}
				if (field.isAnnotationPresent(Id.class)) {
					String value = field.getAnnotation(Id.class).value();
					map.setSequnce(field.getName().toLowerCase(), value);
				}
			}
		}
		tableCache.put(clazz, map);
		return map;
	}
}
