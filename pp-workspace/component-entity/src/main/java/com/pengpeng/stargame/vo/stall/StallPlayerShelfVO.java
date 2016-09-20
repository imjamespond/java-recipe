package com.pengpeng.stargame.vo.stall;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("玩家货架")
public class StallPlayerShelfVO {
    @Desc("货架序号")
    private int order;
    @Desc("货架类型0普通 1达人币 2好友 3超粉")
    private int type;
	@Desc("购买者/在stall.assistant.info中表示卖家的pid")
    private  String buyer;
    @Desc("购买者头像")
    private  String portrait;
    @Desc("物品id")
    private  String itemId;
	@Desc("上架数量")
    private  int itemNum;
	@Desc("物品名称")
    private  String itemName;
    @Desc("上架价格")
    private int price;
	@Desc("货架状态0空闲 1出售中 2已被购买")
    private int state;
    @Desc("广告时间")
    private long advTime;
    @Desc("上架时间是否超过48小时")
    private Boolean overTime;

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public Boolean getOverTime() {
        return overTime;
    }

    public void setOverTime(Boolean overTime) {
        this.overTime = overTime;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
}
