package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;

import java.util.Date;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-29上午11:33
 */
@Desc("邮件附件")
public class MailAttachmentVO {
    @Desc("itemId")
    private String itemId;
    @Desc("itemId")
    private String itemName;
    @Desc("数量")
    private int num;

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
