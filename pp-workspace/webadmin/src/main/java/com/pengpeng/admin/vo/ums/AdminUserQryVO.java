package com.pengpeng.admin.vo.ums;

import com.pengpeng.admin.vo.CommonBaseVO;

/**
 * 用户信息
 * @author kenny
 *
 */

public class AdminUserQryVO extends CommonBaseVO {
	
	private Integer id;
	
	private String username;
	
	private Integer groupId;
	
	private String name;
	
	private Integer rank;
	
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	

}
