package com.metasoft.model.dao;

import org.copycat.framework.annotation.AutoId;
import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Table;

import com.metasoft.model.GenericDao;

@Table("DSMGR_ACCAPPLS")
public class AccessApplicationDao extends GenericDao{
	
	public static final String applState_Staging = "staging";
	public static final String applState_Processing = "processing";
	public static final String applState_finished = "finished";
	public static final String applState_rejected = "rejected";
	
	@Column("APPLIERID")
	String applierId;
	@Column("APPLIERDOMAIN")
	String applierDomain;
	@Column("REQUESTOBJECT")
	String requestObject;
	@Column("APPLSTATE")
	String applState;
	@AutoId(start="10000")
	@Column("APPLID")
	String applId;
	@Column("APPROVER")
	String approver;
	@Column("FINISHEDTS")
	long finishedTS;
	@Column("REASON")
	String reason;
	@Column("OPINION")
	String opinion;
	public String getApplierId() {
		return applierId;
	}
	public void setApplierId(String applierId) {
		this.applierId = applierId;
	}
	public String getApplierDomain() {
		return applierDomain;
	}
	public void setApplierDomain(String applierDomain) {
		this.applierDomain = applierDomain;
	}
	public String getRequestObject() {
		return requestObject;
	}
	public void setRequestObject(String requestObject) {
		this.requestObject = requestObject;
	}
	public String getApplState() {
		return applState;
	}
	public void setApplState(String applState) {
		this.applState = applState;
	}
	public String getApplId() {
		return applId;
	}
	public void setApplId(String applId) {
		this.applId = applId;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}

	public long getFinishedTS() {
		return finishedTS;
	}
	public void setFinishedTS(long finishedTS) {
		this.finishedTS = finishedTS;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	public static String getApplstateStaging() {
		return applState_Staging;
	}
	public static String getApplstateProcessing() {
		return applState_Processing;
	}
	public static String getApplstateFinished() {
		return applState_finished;
	}
	public static String getApplstateRejected() {
		return applState_rejected;
	}
	
}
