package com.metasoft.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.copycat.framework.annotation.AutoId;
import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Table;

import com.keymobile.dataSharingMgr.interfaces.resource.TableModelColumn;
import com.metasoft.model.GenericDao;

@Table("DS_APPLICATION_OBJECT")
public class ApplicationObjectDao extends GenericDao{
	@AutoId
	String id;
	@Column("USER_ID")
	String user_id;
	@Column("DOMAIN_ID")
	String domain_id;
	@Column("APPL_ID")
	String appl_id;
	@Column("OBJ_ID")
	String obj_id;
	@Column("OBJ_NAME")
	String obj_name;
	@Column("OBJ_TYPE")
	String obj_type;
	@Column("OBJ_MODE")
	int obj_mode;
	@Column("REMARK")
	String remark;
	@Column("CONSTRAINT_")
	int constraint;
	
	String objType;
	int opTypeAllowed;
	
	List<Integer> columnsIndices = new ArrayList<>();
	List<TableModelColumn> columns;
	
	public enum Type{
		Table("table-obj","数据表"),Schema("schema-obj","架构"),DataArea("data-area","数据区");
		public String name;
		public String title;
		Type(String name,String title){
			this.name = name;
			this.title = title;
		}
	}
	
	public enum Mode{
		Approved(16);
		public int value;
		Mode(int val){
			this.value = val;
		}
	}
	
	public ApplicationObjectDao() {
		super();
	}
	public ApplicationObjectDao(String obj_id, Type type, String userId, String domainId) {
		super();
		this.obj_id = obj_id;
		this.appl_id = "";
		this.obj_name = "";
		this.obj_type = type.name;
		this.obj_mode = 0;
		this.remark = "";
		this.constraint = 0;
		this.user_id = userId;
		this.domain_id = domainId;
	}
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
	public String getAppl_id() {
		return appl_id;
	}
	public void setAppl_id(String appl_id) {
		this.appl_id = appl_id;
	}
	public String getObj_name() {
		return obj_name;
	}
	public void setObj_name(String obj_name) {
		this.obj_name = obj_name;
	}
	public String getObj_type() {
		return obj_type;
	}
	public void setObj_type(String obj_type) {
		this.obj_type = obj_type;
		if(obj_type.equals(Type.Table.name))
			objType = Type.Table.title;
		else if(obj_type.equals(Type.Schema.name))
			objType = Type.Schema.title;
		else if(obj_type.equals(Type.DataArea.name))
			objType = Type.DataArea.title;
		
	}
	public int getObj_mode() {
		return obj_mode;
	}
	public void setObj_mode(int obj_mode) {
		this.obj_mode = obj_mode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getConstraint() {
		return constraint;
	}
	public void setConstraint(int constraint) {
		this.constraint = constraint;
	}

	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getDomain_id() {
		return domain_id;
	}
	public void setDomain_id(String domain_id) {
		this.domain_id = domain_id;
	}

	public int getOpTypeAllowed() {
		return opTypeAllowed;
	}

	public void setOpTypeAllowed(int opTypeAllowed) {
		this.opTypeAllowed = opTypeAllowed;
	}
	
	public List<Integer> getColumnsIndices() {
		return columnsIndices;
	}
	
	public void addColumnIndex(Integer index) {
		columnsIndices.add(index);
	}
	
	public void setColumnsIndices(List<Integer> indices) {
		columnsIndices = indices;
	}
	
	public void setColumns(List<TableModelColumn> columns) {
		this.columns = columns;
	}
	
}
