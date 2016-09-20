package com.pengpeng.stargame.model.player;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.Uid;

import java.util.Date;
import java.util.UUID;

/**
 * 事件日志
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-19 上午10:08
 */
public class EventLog extends BaseEntity<String> {
	//uuid
	private String id;

//	private String pid;

	// 0：其它，1：好友，2：礼物，3：货币
	private int eventType;

	private Date createTime;

	private String content;

    public EventLog(){

    }
    public EventLog(String pid,int type,String content){
        id = Uid.uuid();
//        this.pid = pid;
        this.eventType = type;
        this.content = content;
        createTime = new Date();
    }
    public EventLog(String pid,int type,String content,Date date){
        id = Uid.uuid();
//        this.pid = pid;
        this.eventType = type;
        this.content = content;
        createTime = date;
    }

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getKey() {
		return id;
	}

//	public String getPid() {
//		return pid;
//	}
//
//	public void setPid(String pid) {
//		this.pid = pid;
//	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
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
		return "EventLog{" +
				"id='" + id + '\'' +
//				", pid='" + pid + '\'' +
				", eventType='" + eventType + '\'' +
				", createTime=" + createTime +
				", content='" + content + '\'' +
				'}';
	}
}
