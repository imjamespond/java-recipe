package com.metasoft.util;

import java.util.Iterator;
import java.util.List;

import com.metasoft.model.annotation.Index;
import com.metasoft.util.GenericDaoHelper.Attr;
 

/**
 * @author metasoft
 * Generic data accessing object
 * @param <T>
 */
public class PsqlHelper{

	public static String SelectSql(PsqlDaoService<?> service) {
		return  "select * from "+service.kTable + " ";
	}

	public static String CountSql(PsqlDaoService<?> service) {
		return  "select count(1) from "+service.kTable + " ";
	}

	public static String InsertSql(PsqlDaoService<?> service) {
		String insertFields = "";
        String insertHolders = "";
		
		List<Attr> attrList = GenericDaoHelper.GetFieldList(service.kDaoClass);
		Iterator<Attr> it = attrList.iterator();
		while(it.hasNext()){
			Attr attr = it.next();
			insertFields+=attr.getColumn();
			insertHolders+="?";//":"+attr.name;
			if(it.hasNext()){
				insertFields+=",";
				insertHolders+=",";
			}
		}
		
		return "insert into "+service.kTable+"("+insertFields+") values("+insertHolders+")";
	}
	
	public static String UpdateSql(PsqlDaoService<?> service) {
		List<Attr> attrList = GenericDaoHelper.GetFieldList(service.kDaoClass);
        String updateFields = "";
        for(Attr attr : attrList){
        	if(updateFields.length()>0)
        		updateFields+=",";
        	updateFields+=(attr.getColumn()+" = ?");
        }
		return "update "+service.kTable+" set "+updateFields + " ";
	}

	public static String DeleteSql(PsqlDaoService<?> service) {
		return "delete from "+service.kTable + " ";
	}
	
	public static String UpdateByIdSql(PsqlDaoService<?> service) {
		if(null==service.kId)
			return null;
		return service.kUpdateSql+"where "+service.kId.getColumn()+"=?";
	}
	public static String DeleteByIdSql(PsqlDaoService<?> service) {
		if(null==service.kId)
			return null;
		return service.kDeleteSql+"where "+service.kId.getColumn()+"=?";
	}
	public static String SelectByIdSql(PsqlDaoService<?> service) {
		if(null==service.kId)
			return null;
		return service.kSelectSql+"where "+service.kId.getColumn()+"=?";
	}

	public static String InsertByAutoIdSql(PsqlDaoService<?> service) {
		String insertFields = "";
        String insertHolders = "";
		
		List<Attr> attrList = GenericDaoHelper.GetFieldList(service.kDaoClass);
		Iterator<Attr> it = attrList.iterator();
		while(it.hasNext()){
			Attr attr = it.next();
			if(attr.getAutoId()!=null){
				insertFields+=attr.getColumn();
				insertHolders+="nextval('"+service.kSeq+"')";//::text
			}else{
				insertFields+=attr.getColumn();
				insertHolders+="?";//":"+attr.name;
			}
			
			if(it.hasNext()){
				insertFields+=",";
				insertHolders+=",";
			}
		}
		return "insert into "+service.kTable+"("+insertFields+") "
				+ "values( "+insertHolders+")";
	}
	
	public static String InsertOnConflictSql(Class<?> clazz, String insertSql, String fields) {
		List<Attr> attrList = GenericDaoHelper.GetFieldList(clazz);
        String updateFields = "";
        for(Attr attr : attrList){
        	if(attr.getAutoId()!=null || attr.getId()!=null) continue;
        	if(updateFields.length()>0) updateFields+=",";
        	updateFields+=(attr.getColumn()+" = EXCLUDED."+attr.getColumn());
        }
		return insertSql + " ON CONFLICT ("+fields+") DO UPDATE set "+updateFields;
	}

	public static String CreateSql(PsqlDaoService<?> service) {
		String createSql = "CREATE TABLE IF NOT EXISTS "+service.kTable+" (";
		List<Attr> attrList = GenericDaoHelper.GetFieldList(service.kDaoClass);
		Iterator<com.metasoft.util.GenericDaoHelper.Attr> it = attrList.iterator();
		while(it.hasNext()){
			Attr attr = it.next();
			createSql += attr.getColumn() + " "+ attr.getColumnType();
			createSql += attr.getId()==null?"":" PRIMARY KEY";
			createSql += attr.getAutoId()==null?"":" PRIMARY KEY";
			if(it.hasNext())
				createSql += ",";
		}
		createSql+=")";
		return createSql;
	}
	
	public static String CreateIndexSql(final String table, final String field, Index idx) {
		final String index = table+"_"+field;
		final String method = idx.method().length()>0?" using "+idx.method():"";
		final String sql = "CREATE "+ idx.value() +" INDEX "+index+" ON "+table+method+" ("+field+")";
		return sql;
	}
}