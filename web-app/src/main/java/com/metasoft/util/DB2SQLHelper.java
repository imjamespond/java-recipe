package com.metasoft.util;

import java.util.List;

import com.metasoft.model.annotation.AutoId;
import com.metasoft.util.GenericDaoHelper.Attr;

public class DB2SQLHelper {
	public static String SelectSql(DB2DaoService<?> service) {
		return  "select * from "+service.kTable;
	}

	public static String CountSql(DB2DaoService<?> service) {
		return  "select count(1) from "+service.kTable;
	}

	public static String InsertSql(DB2DaoService<?> service) {
		List<Attr> attrList = GenericDaoHelper.GetFieldList(service.kDaoClass);
		String insertFields = "";
        String insertHolders = "";
        if(service.kId!=null){
        	insertFields+=service.kId.getColumn();
        	insertHolders+=":"+service.kId.name;
        }
		for(Attr attr : attrList){
        	if(insertFields.length()>0)
        		insertFields+=",";
        	insertFields+=attr.getColumn();
        	if(insertHolders.length()>0)
        		insertHolders+=",";
        	insertHolders+=":"+attr.name;
		}
		return "insert into "+service.kTable+"("+insertFields+") values("+insertHolders+")";
	}
	
	public static String UpdateSql(DB2DaoService<?> service) {
		List<Attr> attrList = GenericDaoHelper.GetFieldList(service.kDaoClass);
        String updateFields = "";
        for(Attr attr : attrList){
        	if(updateFields.length()>0)
        		updateFields+=",";
        	updateFields+=(attr.getColumn()+" = ?");
        }
		return "update "+service.kTable+" set "+updateFields;
	}

	public static String DeleteSql(DB2DaoService<?> service) {
		return "delete from "+service.kTable;
	}
	
	public static String UpdateByIdSql(DB2DaoService<?> service) {
		if(null==service.kId)
			return null;
		return service.kUpdateSql+" where "+service.kId.getColumn()+"=?";
	}
	public static String DeleteByIdSql(DB2DaoService<?> service) {
		if(null==service.kId)
			return null;
		return service.kDeleteSql+" where "+service.kId.getColumn()+"=?";
	}
	public static String SelectByIdSql(DB2DaoService<?> service) {
		if(null==service.kId)
			return null;
		return service.kSelectSql+" where "+service.kId.getColumn()+"=?";
	}

	public static String InsertByAutoIdSql(DB2DaoService<?> service) {
		AutoId autoid = GenericDaoHelper.GetAutoId(service.kDaoClass); 
		if(null==autoid)
			return null;
		List<Attr> attrList = GenericDaoHelper.GetFieldList(service.kDaoClass);
		String insertFields = "";
        String insertHolders = "";
		for(Attr attr : attrList){
        	insertFields+=","+attr.getColumn();
	        insertHolders+=",:"+attr.name;
		}
		return "insert into "+service.kTable+"("+autoid.value()+insertFields+") "
				+ "values( TRIM(CAST(CAST(NEXT VALUE FOR "+service.kSeq+" AS CHAR(50)) AS VARCHAR(50)))"+insertHolders+")";
	}
	
	public static String genPaginationSQL(String originSQL) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (");
		sb.append("select t.*, ROW_NUMBER() OVER() as rownum from (");
		sb.append(originSQL);
		sb.append(") t");
		sb.append(") ");
		sb.append("where rownum <= ? and rownum > ?");
		
		return sb.toString();
	}
	
	
}
