package com.metasoft.model.dao;

import org.copycat.framework.annotation.AutoId;
import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Table;

import com.metasoft.model.GenericDao;

/**
 * 同步日志
 */
@Table("SYNC_LOG")
public class SyncLogDao extends GenericDao {

	final public static String STATE_SYNCING = "syncing";
	final public static String STATE_DEALING = "dealing";
	final public static String STATE_CANCEL = "cancel";
	final public static String STATE_DONE = "done";
	final public static String STATE_FAIL = "fail";

	@AutoId("ID")
	private String logid;
	@Column("SPID")
	private String policyId;
	@Column("STATE")
	private String state;
	@Column("CREATE_TIME")
	private String createTime;
	@Column("DEAL_USER")
	private String dealUser;
	@Column("UPDATE_TIME")
	private String updateTime;
	@Column("DOMAIN_ID")
	private String domainId;
	@Column("LOGMSG")
	private String logMsg;

	public static String getStateSyncing() {
		return STATE_SYNCING;
	}

	public static String getStateDealing() {
		return STATE_DEALING;
	}

	public static String getStateCancel() {
		return STATE_CANCEL;
	}

	public static String getStateDone() {
		return STATE_DONE;
	}

	public String getLogid() {
		return logid;
	}

	public void setLogid(String logid) {
		this.logid = logid;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDealUser() {
		return dealUser;
	}

	public void setDealUser(String dealUser) {
		this.dealUser = dealUser;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getDomainId() {
		return domainId;
	}
	
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public void setLogMsg(String logMsg) {
		this.logMsg = logMsg;
	}
	
	public String getLogMsg() {
		return logMsg;
	}
}
