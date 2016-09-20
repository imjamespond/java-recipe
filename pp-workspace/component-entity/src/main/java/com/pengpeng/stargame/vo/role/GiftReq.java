package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-4上午11:12
 */
@Desc("礼物模块")
public class GiftReq extends BaseReq {
    @Desc("赠送给谁")
    private String fid;
    @Desc("赠送给谁")
    private String[] to;
    @Desc("赠送的物品")
    private String itemId;
    @Desc("赠送数量")
    private int num;
    @Desc("留言")
    private String message;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
