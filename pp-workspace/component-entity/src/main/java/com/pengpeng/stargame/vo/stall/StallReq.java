package com.pengpeng.stargame.vo.stall;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("小摊请求")
public class StallReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;
    @Desc("货架序数0-8")
    private int shelfOrder;
    @Desc("货架类型")
    private int shelfType;
    @Desc("物品id")
    private String itemId;
    @Desc("物品数量")
    private int itemNum;
    @Desc("出价")
    private int price;
    @Desc("广告类型,如:2个小时就填120吧")
    private int advType;
    @Desc("是否达人币下架")
    private Boolean goldOff;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getShelfOrder() {
        return shelfOrder;
    }

    public void setShelfOrder(int shelfOrder) {
        this.shelfOrder = shelfOrder;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Boolean getGoldOff() {
        return goldOff;
    }

    public void setGoldOff(Boolean goldOff) {
        this.goldOff = goldOff;
    }

    public int getAdvType() {
        return advType;
    }

    public void setAdvType(int advType) {
        this.advType = advType;
    }

    public int getShelfType() {
        return shelfType;
    }

    public void setShelfType(int shelfType) {
        this.shelfType = shelfType;
    }
}
