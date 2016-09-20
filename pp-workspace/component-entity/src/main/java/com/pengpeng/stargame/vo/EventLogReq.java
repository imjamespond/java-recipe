package com.pengpeng.stargame.vo;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-19 上午10:57
 */
@Desc("事件日志请求")
public class EventLogReq extends BaseReq {

	@Desc("玩家id")
	private String pid;

	@Desc("0：其它，1：好友，2：礼物，3：货币，null：全部")
	private Integer eventType;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}
}
