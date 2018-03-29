package com.metasoft.util;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.annotation.AutoId;

/**
 * @author metasoft
 * Generic data accessing object
 * @param <T>
 */
public class GenericDaoHelper{

	public  String SelectSql(GenericDaoService<?> service) {
		return  "select * from "+service.kTable + " ";
	}

	public  String CountSql(GenericDaoService<?> service) {
		return  "select count(1) from "+service.kTable + " ";
	}

	public  String InsertSql(GenericDaoService<?> service) {
		List<Attr> attrList = DbUtil.GetFieldList(service.kDaoClass);
		String insertFields = "";
        String insertHolders = "";
        if(service.kId!=null){
        	insertFields+=service.kId.field;
        	insertHolders+=":"+service.kId.attr;
        }
		for(Attr attr : attrList){
        	if(insertFields.length()>0)
        		insertFields+=",";
        	insertFields+=attr.field;
        	if(insertHolders.length()>0)
        		insertHolders+=",";
        	insertHolders+=":"+attr.attr;
		}
		return "insert into "+service.kTable+"("+insertFields+") values("+insertHolders+")";
	}
	
	public  String UpdateSql(GenericDaoService<?> service) {
		List<Attr> attrList = DbUtil.GetFieldList(service.kDaoClass);
        String updateFields = "";
        for(Attr attr : attrList){
        	if(updateFields.length()>0)
        		updateFields+=",";
        	updateFields+=(attr.field+" = ?");
        }
		return "update "+service.kTable+" set "+updateFields + " ";
	}

	public  String DeleteSql(GenericDaoService<?> service) {
		return "delete from "+service.kTable + " ";
	}
	
	public  String UpdateByIdSql(GenericDaoService<?> service) {
		if(null==service.kId)
			return null;
		return service.kUpdateSql+"where "+service.kId.field+"=?";
	}
	public  String DeleteByIdSql(GenericDaoService<?> service) {
		if(null==service.kId)
			return null;
		return service.kDeleteSql+"where "+service.kId.field+"=?";
	}
	public  String SelectByIdSql(GenericDaoService<?> service) {
		if(null==service.kId)
			return null;
		return service.kSelectSql+"where "+service.kId.field+"=?";
	}

	public  String InsertByAutoIdSql(GenericDaoService<?> service, String nextId) {
		AutoId autoid = DbUtil.GetAutoIdField(service.kDaoClass); 
		if(null==autoid)
			return null;
		List<Attr> attrList = DbUtil.GetFieldList(service.kDaoClass);
		String insertFields = "";
        String insertHolders = "";
		for(Attr attr : attrList){
        	insertFields+=","+attr.field;
	        insertHolders+=",:"+attr.attr;
		}
		return "insert into "+service.kTable+"("+autoid.value()+insertFields+") "
				+ "values( "+nextId+insertHolders+")";
	}
	
	public String kInsertReturnAutoIdSql(GenericDaoService<?> service, String kInsertByAutoIdSql) {
		return "";
	}
	
	public String getNextIdSql(GenericDaoService<?> service) {
		return "";
	}
	
	public String getCreateSeqSql(GenericDaoService<?> service) {
		return "";
	}
	
	public String getBatchInsertSql(GenericDaoService<?> service) {
		return "";
	}

	public String getPageSql(String originSQL, Page page) {
		return "";
	}

	public String getBITORFunction(String firstParam,String secondParam) {
		return "";
	}

	public String getBITANDFunction(String firstParam, String secondParam) {
		return "";
	}
	
}