package com.pengpeng.stargame.model.gameEvent;

/**
 * User: mql
 * Date: 13-12-20
 * Time: 上午11:24
 */

import com.pengpeng.stargame.annotation.Desc;

import java.util.Date;

/**
 * 单个掉落
 */
public class OneDrop {
    //唯一Id
    private String uid;
    //礼物Id
    private String giftId;
    // 过期时间
    private Date expiration;
    //名字
    private String name;
    //位置")
    private String position;
    //送的玩家id
    private String pid;
    //留言
    private String word;
    //家族名字
    private String fName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
}
