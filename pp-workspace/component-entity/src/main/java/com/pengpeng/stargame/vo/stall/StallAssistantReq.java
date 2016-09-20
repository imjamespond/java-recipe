package com.pengpeng.stargame.vo.stall;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("小摊助手")
public class StallAssistantReq extends BaseReq {

    @Desc("开启天数")
    private int enableDays;

    @Desc("物品id")
    private String itemId;

    public int getEnableDays() {
        return enableDays;
    }

    public void setEnableDays(int enableDays) {
        this.enableDays = enableDays;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
