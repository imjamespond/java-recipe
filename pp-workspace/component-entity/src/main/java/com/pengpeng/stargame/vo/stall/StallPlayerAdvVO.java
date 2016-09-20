package com.pengpeng.stargame.vo.stall;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("摆摊广告")
public class StallPlayerAdvVO {

    @Desc("pid")
    private String pid; //
    @Desc("人名")
    private String name;  //人名
    @Desc("物品id")
    private String itemID;  //物品id
    @Desc("物品名")
    private String itemName;   //物品名
    @Desc("物品数量")
    private int itemNum;    //物品数量
    @Desc("物品价格")
    private int itemPrice; //物品价格
    @Desc("广告时间类型")
    private int type;     //广告时间类型
    @Desc("截止时间")
    private long endTime;   //截止时间

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
