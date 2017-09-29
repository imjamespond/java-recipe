package com.metasoft.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.metasoft.model.annotation.AutoId;
import com.metasoft.model.annotation.Column;
import com.metasoft.model.annotation.Id;
import com.metasoft.model.annotation.Index;
import com.metasoft.model.annotation.Table;

/**
 * @author metasoft
 * Generic data accessing object
 * @param <T>
 */
public class GenericDaoHelper{

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
				Assert.notNull(col, f.getName());
				return new Attr(f);
			}
			AutoId autoid = f.getAnnotation(AutoId.class);
			if(null!=autoid){
				Assert.notNull(col, f.getName());
				return new Attr(f);
			}
		}
		return null;
	}
	
	public static AutoId GetAutoId(Class<?> clazz){
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
				list.add(new Attr(f));
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
			if(col!=null&&col.desc().length()>0)
				list.add(new Attr(f));
		}
		
		Assert.isTrue(list.size()>0,"Dao缺少字段信息");
		return list;
	}
	
	public static class GenericDao { }
	
	public static class Attr implements Comparable<Attr> {
		
 
		public String name;
		public String desc;
		public int prior;
		private Field field;
		
		public Attr(String name, String desc, int prior) {
			this.name = name;
			this.desc = desc;
			this.prior = prior;
		}
		
		public Attr(String attr, String desc) {
			this(attr, desc, 0);
		}
		public Attr(String name, Field f) {
			this.name = name;
			this.field = f;
		}
		public Attr(Field f) {
			this.field = f;
			this.name = f.getName();
		}

		public String getColumn(){
			Column col = field.getAnnotation(Column.class);
			Assert.notNull(col, "");
			return col.value();
		}
		
		public String getColumnType(){
			Column col = field.getAnnotation(Column.class);
			Assert.notNull(col, "");
			return col.type();
		}
		
		public String getDesc(){
			Column col = field.getAnnotation(Column.class);
			Assert.notNull(col, "");
			return col.desc();
		}
		
		public Id getId(){
			Id id = field.getAnnotation(Id.class);
			return id;
		}
		public AutoId getAutoId(){
			AutoId id = field.getAnnotation(AutoId.class);
			return id;
		}
		public Index getIndex(){
			return field.getAnnotation(Index.class);
		}
		
		@Override
		public int compareTo(Attr other) {
			if (this.prior > other.prior) 
				return 1;
			else if (this.prior < other.prior)
				return -1;
			else
				return this.name.compareTo(other.name);
		}
		
	}
}