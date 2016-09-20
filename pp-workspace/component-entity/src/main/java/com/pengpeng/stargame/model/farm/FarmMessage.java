package com.pengpeng.stargame.model.farm;

import com.pengpeng.stargame.annotation.Desc;

import java.util.Date;

/**
 * 单个留言
 * User: mql
 * Date: 13-11-5
 * Time: 上午10:48
 */
public class FarmMessage {
    //唯一id
    private String uid;
    //@Desc("留言好友Id")
    private String fid;
    //@Desc("发送时间 ")
    private Date date;
    //"留言内容")
    private String content;
    //类型
    private int type;

    public  FarmMessage() {
    }
    public String getFid() {
        return fid;
    }

    public  FarmMessage(String uid,String fid,String content) {
        this.fid = fid;
        this.uid=uid;
        this.content=content;
        this.date=new Date();
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
