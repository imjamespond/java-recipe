package com.metasoft.model;

import java.util.ArrayList;
import java.util.List;

import com.keymobile.common.persistence.metadata.Attribute;

public class AccessApplication {
	
	public static final String applState_Staging = "staging";
	public static final String applState_Processing = "processing";
	public static final String applState_finished = "finished";
	public static final String applState_rejected = "rejected";
	
	private String accessApplicationId;
	private String userId;
	private String userName;
	private String userDomainId;
	private String userDomainName;
	private String applDate;
	private String approverId;
	private String approverName;
	private String approverDomainId;
	private String approverDomainName;
	private String finishedDate;
	private String reason;
	private String opinion;
	private String applState;
	
	private String accessRequestsNameList = "";
	
	private List<AccessRequest> accessRequests = new ArrayList<AccessRequest>();
	
	public void setAccessApplicationId(String accessApplicationId) {
		this.accessApplicationId = accessApplicationId;
	}
	
	@Attribute(attr = "accessApplicationId", desc = "申请序号", prior = 1)
	public String getAccessApplicationId() {
		return accessApplicationId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserDomainId(String userDomainId) {
		this.userDomainId = userDomainId;
	}
	
	public String getUserDomainId() {
		return userDomainId;
	}
	
	public void addAccessRequest(AccessRequest accessRequest) {
		accessRequests.add(accessRequest);
	}
	
	public List<AccessRequest> getAccessRequests() {
		return accessRequests;
	}
	
	@Attribute(attr = "userName", desc = "申请人", prior = 2)
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Attribute(attr = "userDomainName", desc = "申请人所属租户", prior = 3)
	public String getUserDomainName() {
		return userDomainName;
	}
	
	public void setUserDomainName(String domainName) {
		this.userDomainName = domainName;
	}
	
	@Attribute(attr = "accessRequestsNameList", desc = "申请资源列表", prior = 4)
	public String getAccessRequestsNameList() {
		return accessRequestsNameList;
	}

	public void setAccessRequestsNameList(String displayList) {
		accessRequestsNameList = displayList;
	}
	
	@Attribute(attr = "reason", desc = "申请原因", prior = 5)
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}

	@Attribute(attr = "applDate", desc = "申请时间", prior = 6)
	public String getApplDate() {
		return applDate;
	}
	
	public void setApplDate(String applDate) {
		this.applDate = applDate;
	}
	
	public String getApproverId() {
		return approverId;
	}
	
	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}
	
	@Attribute(attr = "approverName", desc = "审批人所属租户", prior = 7)
	public String getApproverName() {
		return approverName;
	}
	
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	
	public String getApproverDomainId() {
		return approverDomainId;
	}
	
	public void setApproverDomainId(String domainId) {
		this.approverDomainId = domainId;
	}
		
	public String getApproverDomainName() {
		return approverDomainName;
	}
	
	public void setApproverDomainName(String domainName) {
		this.approverDomainName = domainName;
	}
	
	@Attribute(attr = "finishedDate", desc = "审批时间", prior = 8)
	public String getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(String finishedDate) {
		this.finishedDate = finishedDate;
	}
	
	@Attribute(attr = "opinion", desc = "审批意见", prior = 9)
	public String getOpinion() {
		return opinion;
	}
	
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
	@Attribute(attr = "applState", desc = "审批状态", prior = 10)
	public String getApplState() {
		return applState;
	}
	
	public void setApplState(String state) {
		if (state.equals(applState_Staging)) 
			applState = "待提交";
		else if (state.equals(applState_Processing)) 
			applState = "待审批";
		else if (state.equals(applState_finished))
			applState = "已通过";
		else if (state.equals(applState_rejected))
			applState = "已驳回";
		else 
			applState = state;
	}
	
}
