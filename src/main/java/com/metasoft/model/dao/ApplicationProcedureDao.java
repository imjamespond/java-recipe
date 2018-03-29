package com.metasoft.model.dao;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

import com.metasoft.model.GenericDao;
import com.metasoft.util.Commons;

@Table("DS_APPLICATION_PROCEDURE")
public class ApplicationProcedureDao extends GenericDao{

	@Id("APPL_ID")
	String appl_id;
	@Column("USER_ID")
	String user_id;
	@Column("DOMAIN_ID")
	String domain_id;
	@Column("AUDITOR_ID")
	String auditorId;
	@Column("AUDIT_DATE")
	long auditDate;
	@Column("PROC_STATE")
	String proc_state = State.STAGING.name;
	@Column("PROC_TOKEN")
	int proc_token;
	@Column("OPINION")
	String opinion;
	
	String title;
	String date;
	public String auditor;
	public String domain;

	public ApplicationProcedureDao() {
		super();
	}
	/**
	 * 申请
	 * @param appl_id
	 * @param user_id
	 * @param domain_id
	 * @param state
	 * @param proc_token
	 */
	public ApplicationProcedureDao(String appl_id, String user_id, String domain_id, State state, int proc_token) {
		super();
		this.appl_id = appl_id;
		this.user_id = user_id;
		this.domain_id = domain_id;
		this.auditorId = "";
		this.proc_state = state.name;
		this.proc_token = proc_token;
		this.opinion = "";
	}


	public String getAuditorId() {
		return auditorId;
	}
	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}
	public long getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(long auditDate) {
		this.auditDate = auditDate;
		this.date = Commons.GetRegularDate(auditDate);
	}
	public String getProc_state() {
		return proc_state;
	}
	public void setProc_state(String state) {
		this.proc_state = state;
		if(this.proc_state.equals(State.TOPROCESS.name))
			this.title = "租户内审批";
		else if(this.proc_state.equals(State.CROSS_DOMAIN.name))
			this.title = "跨租户审批";
//		else if(this.proc_state.equals(State.CROSS_DOMAIN_INNER.name))
//			this.title = "跨租户审批,通过后请完成库内数据区配置";
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getAppl_id() {
		return appl_id;
	}
	public void setAppl_id(String appl_id) {
		this.appl_id = appl_id;
	}
	public int getProc_token() {
		return proc_token;
	}
	public void setProc_token(int proc_token) {
		this.proc_token = proc_token;
	}

	public String getTitle() {
		return title;
	}
	public String getDate() {
		return date;
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

	public void setTitle(String title) {
		this.title = title;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public enum State{
		//FINISHED("finished"),CROSS_DOMAIN("cross-domain"),CROSS_DOMAIN_INNER("cross-domain-inner"),STAGING("staging"),TOPROCESS("to-process");
		FINISHED("finished"),CROSS_DOMAIN("cross-domain"),STAGING("staging"),TOPROCESS("to-process");
		public String name;
		State(String name){
			this.name = name;
		}
	}
	public enum Token{
		STAGGING(0),OK(1),REJECTED(2),INNER_DB(4);
		public int val;
		Token(int val){
			this.val = val;
		}
	}
}
