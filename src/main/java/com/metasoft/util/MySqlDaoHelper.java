package com.metasoft.util;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.annotation.AutoId;

public class MySqlDaoHelper extends GenericDaoHelper{
	
	public  String InsertByAutoIdSql(GenericDaoService<?> service) {
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
		+ "values(nextval("+service.kSeq+insertHolders+")";
	}
	
	public String getNextIdSql(GenericDaoService<?> service) {
		return "select nextval('"+service.kSeq+"') from dual ";
	}
	
	public String getCreateSeqSql(GenericDaoService<?> service) {
		String kSeq = service.kSeq;
		AutoId id = DbUtil.GetAutoIdField(service.kDaoClass);
		return "INSERT INTO sequence VALUES ('"+kSeq+"', "+id.start()+", "+id.stride()+")";
	}
	
	public String getBatchInsertSql(GenericDaoService<?> daoService){
		AutoId autoid = DbUtil.GetAutoIdField(daoService.kDaoClass); 
		if(null==autoid)
			return null;
		List<Attr> attrList = DbUtil.GetFieldList(daoService.kDaoClass);
		String insertFields = "";
        String insertHolders = "";
		for(Attr attr : attrList){
        	insertFields+=","+attr.field;
	        insertHolders+=",:"+attr.attr;
		}
		return "insert into "+daoService.kTable+"("+autoid.value()+insertFields+") "
				+ "values(nextval('"+daoService.kSeq+"')"+insertHolders+")";
	}
	
	public String getPageSql(String originSQL, Page page) {
		return originSQL+" limit "+page.getOffset()+","+page.getLimit()+"";
	}
	
	public String getBITORFunction(String firstParam,String secondParam) {
		return " "+firstParam+"|"+secondParam+" ";
	}
	
	public String getBITANDFunction(String firstParam, String secondParam) {
		return " "+firstParam+"&"+secondParam+" ";
	}
	
}
