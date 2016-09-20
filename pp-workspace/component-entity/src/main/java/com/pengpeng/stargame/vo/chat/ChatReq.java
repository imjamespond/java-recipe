package com.pengpeng.stargame.vo.chat;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-20上午10:59
 */
@Desc("聊天信息请求")
public class ChatReq extends BaseReq {

    @Desc("发送人的id")
    private String pid;
    @Desc("接受消息人")
    private String to;
    //@Desc("频道:世界world,组队team,信息info,私聊person,提示tip,公告post,对话dialog,工会union")
    @Desc("当前类型(不输入值),粉丝团fans")
    private String type;
    @Desc("千里传音的类型 1达人币 2免费次数")
    private int sendType;
    @Desc("消息内容")
    private String msg;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }
}
