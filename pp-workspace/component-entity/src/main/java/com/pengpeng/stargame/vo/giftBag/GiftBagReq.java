package com.pengpeng.stargame.vo.giftBag;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-12-12
 * Time: 下午4:45
 */
@Desc("领取礼包Req")
public class GiftBagReq extends BaseReq {
    @Desc("1 领取圣诞礼包 2领取元旦礼包")
    private int type;
    @Desc("礼包Id")
    private String giftId;

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
