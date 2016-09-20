package com.pengpeng.stargame.vo;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-22上午9:53
 */
@Desc("通用id数据请求")
public class IdReq extends BaseReq {
    @Desc("对象id(例如角色,玩家id,npcid,或其它约定id)")
    private String id;

    @Desc("多id参数,只在同时需要传送多个id值才需要使用")
    private String[] ids;

	@Desc("多id参数,只在同时需要传送多个id值才需要使用")
	private Integer [] userIds;

	public Integer[] getUserIds() {
		return userIds;
	}

	public void setUserIds(Integer[] userIds) {
		this.userIds = userIds;
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
