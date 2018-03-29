package com.metasoft.model.dao;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

import com.metasoft.model.GenericDao;

@Table("DS_APPLICATION_INNERDB")
public class ApplicationInnerDbDao extends GenericDao{
	@Id(value="APPL_ID")
	String appl_id;
	@Column(value="DB_NAME")
	String db_name;
	@Column(value="DB_ID")
	String db_id;
	@Column(value="DB_TYPE")
	String db_type;
	@Column(value="DB_URL")
	String db_url="";
	@Column(value="DB_USER")
	String db_user="";
	@Column(value="DB_PASSWD")
	String db_passwd="";
	@Column(value="DB_SCHEMA")
	String db_schema="";

	public String getAppl_id() {
		return appl_id;
	}
	public void setAppl_id(String appl_id) {
		this.appl_id = appl_id;
	}
	public String getDb_name() {
		return db_name;
	}
	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}
	
	public String getDb_id() {
		return db_id;
	}
	public void setDb_id(String db_id) {
		this.db_id = db_id;
	}
	public String getDb_type() {
		return db_type;
	}
	public void setDb_type(String db_type) {
		this.db_type = db_type;
	}
	public String getDb_url() {
		return db_url;
	}
	public void setDb_url(String db_url) {
		this.db_url = db_url;
	}
	public String getDb_user() {
		return db_user;
	}
	public void setDb_user(String db_user) {
		this.db_user = db_user;
	}
	public String getDb_passwd() {
		return db_passwd;
	}
	public void setDb_passwd(String db_passwd) {
		this.db_passwd = db_passwd;
	}
	public String getDb_schema() {
		return db_schema;
	}
	public void setDb_schema(String db_schema) {
		this.db_schema = db_schema;
	}

}