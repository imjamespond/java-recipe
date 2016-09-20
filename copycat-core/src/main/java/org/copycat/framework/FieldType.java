package org.copycat.framework;

import java.lang.reflect.Field;

public class FieldType {
	private boolean isList;
	private Class<?> clazz;
	private Field field;

	public void setList(boolean isList) {
		this.isList = isList;
	}

	public boolean isList() {
		return isList;
	}

	public void setType(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getType() {
		return clazz;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}
}
