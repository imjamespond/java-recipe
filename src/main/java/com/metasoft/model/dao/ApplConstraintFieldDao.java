package com.metasoft.model.dao;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

import com.metasoft.model.GenericDao;

@Table("DS_APPLICATION_PROCEDURE")
public class ApplConstraintFieldDao extends GenericDao{
	@Id
	String id;
	@Column("obj_id")
	String obj_id;
	@Column("field_name")
	String field_name;
	@Column("token")
	int token;
	@Column("field_type")
	String field_type;
	@Column("remark")
	String remark;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getObj_id() {
		return obj_id;
	}
	public void setObj_id(String obj_id) {
		this.obj_id = obj_id;
	}
	public String getField_name() {
		return field_name;
	}
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
	public int getToken() {
		return token;
	}
	public void setToken(int token) {
		this.token = token;
	}
	public String getField_type() {
		return field_type;
	}
	public void setField_type(String field_type) {
		this.field_type = field_type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
