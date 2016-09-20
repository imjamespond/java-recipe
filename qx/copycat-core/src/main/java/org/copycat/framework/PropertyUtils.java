package org.copycat.framework;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;

import com.mongodb.BasicDBObject;

public class PropertyUtils {
	private static Map<String, Method> setmap = new HashMap<String, Method>();
	private static Map<String, Method> getmap = new HashMap<String, Method>();
	private static Map<String, FieldType> fieldMap = new HashMap<String, FieldType>();

	public static void init() {
		setmap.clear();
		getmap.clear();
	}

	public static Object getProperty(Object object, String name) {
		Class<?> clazz = object.getClass();
		String key = clazz.getName() + "." + name;
		if (!getmap.containsKey(key)) {
			String methodName = "get" + name.substring(0, 1).toUpperCase()
					+ name.substring(1);
			getmap.put(key, getDeclaredMethod(clazz, methodName));
		}

		try {
			return getmap.get(key).invoke(object);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static void setProperty(Object object, String name, Object value) {
		if (value == null) {
			return;
		}
		Class<?> clazz = object.getClass();
		String key = clazz.getName() + "." + name;
		if (!setmap.containsKey(key)) {
			String methodName = "set" + name.substring(0, 1).toUpperCase()
					+ name.substring(1);
			try {
				setmap.put(key,
						clazz.getDeclaredMethod(methodName, value.getClass()));
			} catch (NoSuchMethodException e) {
				if (Date.class.isAssignableFrom(value.getClass())) {
					setmap.put(key,
							getDeclaredMethod(clazz, methodName, Date.class));
				} else if (Integer.class.isAssignableFrom(value.getClass())) {
					setmap.put(key,
							getDeclaredMethod(clazz, methodName, int.class));
				} else if (Long.class.isAssignableFrom(value.getClass())) {
					setmap.put(key,
							getDeclaredMethod(clazz, methodName, long.class));
				} else if (Float.class.isAssignableFrom(value.getClass())) {
					setmap.put(key,
							getDeclaredMethod(clazz, methodName, float.class));
				} else if (Double.class.isAssignableFrom(value.getClass())) {
					setmap.put(key,
							getDeclaredMethod(clazz, methodName, double.class));
				} else if (BasicBSONList.class.isAssignableFrom(value
						.getClass())) {
					Field field = getDeclaredField(key, clazz, name);
					setmap.put(
							key,
							getDeclaredMethod(clazz, methodName,
									field.getType()));
				} else {
					Method methods[] = clazz.getMethods();
					for (Method method : methods) {
						if (method.getName().equals(methodName)) {
							setmap.put(key, method);
						}
					}
				}
			}
		}
		try {
			if (fieldMap.containsKey(key)) {
				Class<?> type = fieldMap.get(key).getType();
				List<BasicDBObject> valueList = (List<BasicDBObject>) value;
				List<Object> list = new ArrayList<Object>();
				for (BasicDBObject dbOject : valueList) {
					Object child = BeanUtils.convert(type, dbOject);
					list.add(child);
				}
				setmap.get(key).invoke(object, new Object[] { list });
				return;
			}

			if (BasicDBObject.class.isAssignableFrom(value.getClass())) {
				Method method = setmap.get(key);
				Class<?> type = method.getParameterTypes()[0];
				value = BeanUtils.convert(type, (BasicBSONObject) value);
				setmap.get(key).invoke(object, new Object[] { value });
				return;
			}
			setmap.get(key).invoke(object, new Object[] { value });
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private static Method getDeclaredMethod(Class<?> clazz, String name,
			Class<?>... parameterTypes) {
		try {
			return clazz.getDeclaredMethod(name, parameterTypes);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	private static Field getDeclaredField(String key, Class<?> clazz,
			String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			if (List.class.isAssignableFrom(field.getType())) {
				Type type = field.getGenericType();
				ParameterizedType parameterizedType = (ParameterizedType) type;
				type = parameterizedType.getActualTypeArguments()[0];
				if (!ParameterizedType.class.isAssignableFrom(type.getClass())) {
					Class<?> genericClazz = (Class<?>) type;
					if (!BeanUtils.isPrimitive(genericClazz)) {
						FieldType fieldType = new FieldType();
						fieldType.setList(true);
						fieldType.setType(genericClazz);
						fieldMap.put(key, fieldType);
					}
				}
			}
			return field;
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
	}
}
