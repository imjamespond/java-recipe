package com.metasoft.model.dao;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Table;

import com.metasoft.model.GenericDao;

@Table("DS_APPLICATION_PROCEDURE")
public class ApplConstraintRowDao extends GenericDao{

	@Column("obj_id")
	String obj_id;
	@Column("appl_sql")
	String appl_sql;
	@Column("token")
	int token;
	@Column("audit_sql")
	String audit_sql;
	public String getObj_id() {
		return obj_id;
	}
	public void setObj_id(String obj_id) {
		this.obj_id = obj_id;
	}
	public String getAppl_sql() {
		return appl_sql;
	}
	public void setAppl_sql(String appl_sql) {
		this.appl_sql = appl_sql;
	}
	public int getToken() {
		return token;
	}
	public void setToken(int token) {
		this.token = token;
	}
	public String getAudit_sql() {
		return audit_sql;
	}
	public void setAudit_sql(String audit_sql) {
		this.audit_sql = audit_sql;
	}

}
