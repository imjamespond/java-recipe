package com.metasoft.model.dao;

import org.copycat.framework.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Table;

import com.metasoft.model.GenericDao;
import com.metasoft.util.Commons;

@Table("DS_APPLICATION")
public class ApplicationDao extends GenericDao{
	@Id(value="APPL_ID",title="序号")
	String appl_id;
	@Column(value="APPL_TYPE")
	String appl_type = "";
	@Column(value="APPL_STATE")
	String appl_state = "";
	@Column(value="USER_ID")
	String user_id;
	@Column(value="DOMAIN_ID")
	String domain_id;
	@Column(value="CREATE_DATE")
	long create_date;
	@Column(value="FINISH_DATE")
	long finish_date;
	@Column(value="TITLE",title="标题")
	String title = "";
	@Column(value="REASON")
	String reason = "";
	
	@Column(title="申请人")
	public String username;
	@Column(title="申请租户")
	public String domain;
	//@Column(title="申请单类型")
	public String applType;
	@Column(title="创建时间")
	public String createDate;
	@Column(title="完成时间")
	public String finishDate;
	
	public List<ApplicationObjectDao> applObjects = new ArrayList<>();

	public String getAppl_id() {
		return appl_id;
	}
	public void setAppl_id(String appl_id) {
		this.appl_id = appl_id;
	}
	public String getAppl_type() {
		return appl_type;
	}
	public void setAppl_type(String appl_type) {
		this.appl_type = appl_type;
		if(appl_type.equals(Type.APPLYDATA.name))
			this.applType = "数据权限申请单";
		else if(appl_type.equals(Type.INNERDATA.name))
			this.applType = "库内数据区申请单";
	}
	public String getAppl_state() {
		return appl_state;
	}
	public void setAppl_state(String appl_state) {
		this.appl_state = appl_state;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public long getCreate_date() {
		return create_date;
	}
	public void setCreate_date(long create_date) {
		this.create_date = create_date;
		this.createDate = Commons.GetRegularDate(create_date);
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDomain_id() {
		return domain_id;
	}
	public void setDomain_id(String domain_id) {
		this.domain_id = domain_id;
	}
	public long getFinish_date() {
		return finish_date;
	}
	public void setFinish_date(long finish_date) {
		this.finish_date = finish_date;
		this.finishDate = Commons.GetRegularDate(finish_date);
	}
	public List<ApplicationObjectDao> getApplicationObjects() {
		return applObjects;
	}
	public void addApplicationObject(ApplicationObjectDao applObject) {
		this.applObjects.add(applObject);
	}
	
	public enum State{
		TOAPPLY("to-apply"),APPLY("apply");
		public String name;
		State(String name){
			this.name = name;
		}
	}
	public enum Type{
		APPLYDATA("apply-data"),INNERDATA("inner-data");
		public String name;
		Type(String name){
			this.name = name;
		}
	}
}
