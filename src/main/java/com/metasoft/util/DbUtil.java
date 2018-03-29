package com.metasoft.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.copycat.framework.annotation.AutoId;
import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;
import org.springframework.util.Assert;

public class DbUtil {
    
	public static String GetTable(Class<?> clazz){
		Table tb = clazz.getAnnotation(Table.class);
		if(null==tb)
			return "none_table";
		return tb.value();
	}
	
	public static Attr GetIdField(Class<?> clazz){
		Field[] fileds = clazz.getDeclaredFields();
		for(Field f:fileds){
			Column col = f.getAnnotation(Column.class);
			Id id = f.getAnnotation(Id.class);
			if(null!=id){
				Assert.isNull(col);
				return new Attr(f.getName(), id.title(), id.value());
			}
			AutoId autoid = f.getAnnotation(AutoId.class);
			if(null!=autoid){
				Assert.isNull(col);
				return new Attr(f.getName(), autoid.title(), autoid.value());
			}
		}
		return null;
	}
	
	public static AutoId GetAutoIdField(Class<?> clazz){
		Field[] fileds = clazz.getDeclaredFields();
		for(Field f:fileds){
			AutoId id = f.getAnnotation(AutoId.class);
			if(null != id)
				return id;
		}
		return null;
	}
	
	public static String GetIdName(Class<?> clazz){
		Field[] fileds = clazz.getDeclaredFields();
		for(Field f:fileds){
			Id id = f.getAnnotation(Id.class);
			if(null!=id)
				return f.getName();
			AutoId autoid = f.getAnnotation(AutoId.class);
			if(null!=autoid)
				return f.getName();
		}
		return "none_name";
	}
	
	public static List<Attr> GetFieldList(Class<?> clazz){
		List<Attr> list = new ArrayList<Attr>();
		Field[] fileds = clazz.getDeclaredFields();
		for(Field f:fileds){
			Column col = f.getAnnotation(Column.class);
			if(col!=null&&col.value().length()>0)
				list.add(new Attr(f.getName(), col.title(), col.value()));
		}
		Assert.isTrue(list.size()>0,"Dao缺少字段信息");
		return list;
	}
	
	public static List<Attr> GeAttrList(Class<?> clazz){
		List<Attr> list = new ArrayList<Attr>();
		Attr id = GetIdField(clazz);
		if(null!=id)
			list.add(id);
		Field[] fileds = clazz.getDeclaredFields();
		for(Field f:fileds){
			Column col = f.getAnnotation(Column.class);
			if(col!=null&&col.title().length()>0)
				list.add(new Attr(f.getName(), col.title(), col.value()));
		}
		
		Assert.isTrue(list.size()>0,"Dao缺少字段信息");
		return list;
	}
}
