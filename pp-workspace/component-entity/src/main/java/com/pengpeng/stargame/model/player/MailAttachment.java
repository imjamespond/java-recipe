package com.pengpeng.stargame.model.player;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-28下午5:00
 */
public class MailAttachment {

    private String itemId;
    private int type;
    private int num;

    public MailAttachment() {

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
