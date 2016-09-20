package com.pengpeng.stargame.model.stall;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:07
 */
//货架
public class PlayerShelf {

    private String itemId;     //物品id
    private int itemNum;     //上架数量
    private int itemPrice;     //价格
    private int state; //货架状态
    private long advTime;//广告时间
    private long hitShelfTime;//上架时间
    private String buyerId;//购买者id
    private long buyingTime;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getAdvTime() {
        return advTime;
    }

    public void setAdvTime(long advTime) {
        this.advTime = advTime;
    }

    public long getHitShelfTime() {
        return hitShelfTime;
    }

    public void setHitShelfTime(long hitShelfTime) {
        this.hitShelfTime = hitShelfTime;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public long getBuyingTime() {
        return buyingTime;
    }

    public void setBuyingTime(long buyingTime) {
        this.buyingTime = buyingTime;
    }
}
