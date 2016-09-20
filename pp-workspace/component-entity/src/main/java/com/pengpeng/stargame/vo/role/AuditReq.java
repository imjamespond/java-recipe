package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @author jinli.yuan@com.pengpeng.com
 * @since 13-3-26 下午2:36
 */
@Desc("审核")
public class AuditReq extends BaseReq {
	@Desc("对象id(例如角色,玩家id,npcid,或其它约定id)")
	private String id;

	@Desc("主站用户Id")
	private Integer userId;

	@Desc("主站用户ID集合")
	private Integer [] userIds;

	@Desc("对象id集合")
	private String [] ids;

	@Desc("状态，1：通过，0：不通过,2:拒绝")
	private Integer status;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer[] getUserIds() {
		return userIds;
	}

	public void setUserIds(Integer[] userIds) {
		this.userIds = userIds;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
}
