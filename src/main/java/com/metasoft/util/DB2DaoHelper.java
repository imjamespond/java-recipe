package com.metasoft.util;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.annotation.AutoId;

public class DB2DaoHelper extends GenericDaoHelper{
	public String kInsertReturnAutoIdSql(GenericDaoService<?> service, String kInsertByAutoIdSql) {
		String kInsertReturnAutoIdSql = "";
		Attr kId = service.kId;
		if(null!=kId)
			kInsertReturnAutoIdSql = "select "+kId.field+" from FINAL TABLE ("+kInsertByAutoIdSql+")";
		else
			kInsertReturnAutoIdSql = null;
		return kInsertReturnAutoIdSql;
	}
	
	public String getNextIdSql(GenericDaoService<?> service) {
		return "select (nextval for "+service.kSeq+") from sysibm.sysdummy1 ";
	}
	
	public String getCreateSeqSql(GenericDaoService<?> service) {
		String kSeq = service.kSeq;
		AutoId id = DbUtil.GetAutoIdField(service.kDaoClass);
		String sql = "create sequence "+kSeq+" AS bigint "
    			+ "START WITH "+id.start()+" INCREMENT BY "+id.stride()+" MINVALUE 1 NO MAXVALUE NO CYCLE NO CACHE ORDER";
		return sql;
	
	}
	
	public String getBatchInsertSql(GenericDaoService<?> service){
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
				+ "values( TRIM(CAST(CAST(NEXT VALUE FOR "+service.kSeq+" AS CHAR(20)) AS VARCHAR(20)))"+insertHolders+")";
	
	}
	public String getPageSql(String originSQL, Page page) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (");
		sb.append("select t.*, ROW_NUMBER() OVER() as rownum from (");
		sb.append(originSQL);
		sb.append(") t");
		sb.append(") ");
		sb.append("where rownum <= "+page.getLimit()+" and rownum > "+page.getOffset());
		return sb.toString();
	}
	
	public String getBITORFunction(String firstParam,String secondParam) {
		return " BITOR("+firstParam+","+secondParam+") ";
	}
	
	public String getBITANDFunction(String firstParam, String secondParam) {
		return " BITAND("+firstParam+","+secondParam+") ";
	}
	
}
