package com.pengpeng.stargame.vo.gm;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @auther james@pengpeng.com
 * @since: 13-10-15上午10:27
 */
@Desc("道具")
public class ItemReq {
    private int uid;
    private String pid;
    private String itemId;
    private int num;

    public ItemReq() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
