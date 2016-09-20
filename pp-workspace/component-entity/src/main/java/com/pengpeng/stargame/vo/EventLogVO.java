package com.pengpeng.stargame.vo;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-19 上午10:17
 */
@Desc("事件日志")
public class EventLogVO {

//	@Desc("玩家id")
//	private String pid;

	@Desc("0：其它，1：好友，2：礼物，3：货币，null：全部")
	private Integer eventType;

	@Desc("时间")
	private String createTime;

	@Desc("内容")
	private String content;

//	public String getPid() {
//		return pid;
//	}
//
//	public void setPid(String pid) {
//		this.pid = pid;
//	}

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "EventLogVO{" +
//				"pid='" + pid + '\'' +
				", eventType=" + eventType +
				", createTime='" + createTime + '\'' +
				", content='" + content + '\'' +
				'}';
	}
}
