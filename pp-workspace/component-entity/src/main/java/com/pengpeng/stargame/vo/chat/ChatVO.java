package com.pengpeng.stargame.vo.chat;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-28下午2:25
 */
@Desc("聊天信息")
@EventAnnotation(name="event.chat.msg",desc="普通聊天信息(不包含千里传音)")
public class ChatVO {
    //@Desc("频道:世界world,组队team,信息info,私聊person,提示tip,公告post,对话dialog,工会union")
    @Desc("当前类型(不输入值),粉丝团fans 世界world")
    private String type;
    @Desc("频道:sys系统频道,family家族频道,p个人聊天")
    private String channel;
    @Desc("发消息人的id")
    private String pid;
    @Desc("发消息人的名称")
    private String nickName;
    @Desc("对谁说,一般紧限于私聊会用到,变量值一般是pid")
    private String to;
    @Desc("对谁说的人的名称")
    private String toName;

    @Desc("消息内容")
    private String msg;

    public ChatVO() {

    }

    public ChatVO(String type,String msg) {
        this.type = type;
        this.msg = msg;
    }

    public ChatVO(String type, String pid,String nickName, String msg) {
        this.type = type;
        this.pid = pid;
        this.nickName = nickName;
        this.msg = msg;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }
}
