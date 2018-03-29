package com.metasoft.util;

public class SQLHelper {

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
	
	public static void main(String [] args) {
		String sql = "select * from as_user";
		System.out.println(genPaginationSQL(sql));
	}
	
}
