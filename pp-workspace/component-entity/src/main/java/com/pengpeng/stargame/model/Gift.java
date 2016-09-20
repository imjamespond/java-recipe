package com.pengpeng.stargame.model;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-3下午5:27
 */
public class Gift {
    //赠送人(好友id)
    private String fid;
    //赠送的物品
    private String itemId;
    //数量
    private int num;
    //赠送时间
    private Date giveDate;
    //有效期
    private Date validityDate;
    //留言
    private String message;
    //是否保留
    private int type;
    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
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

    public Date getGiveDate() {
        return giveDate;
    }

    public void setGiveDate(Date giveDate) {
        this.giveDate = giveDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
