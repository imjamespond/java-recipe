package com.pengpeng.stargame.vo.room;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.BaseItemVO;

/**
 * User: mql
 * Date: 13-5-30
 * Time: 下午4:45
 */
@Desc("房间内的 仓库 物品Vo")
public class RoomItemVO extends BaseItemVO {
    @Desc("道具图标")
    private  String icon;

    @Desc("豪华度")
    private int luxuryDegree;
    @Desc("图片")
    private String image;
    @Desc("0为不可旋转,1为可旋转")
    private int rotation;
    @Desc("0为上面不可叠放东西，1为上面可以放东西")
    private int stack;

    @Desc("回收价格")
    private int recyclingPrice;
    @Desc("游戏币购买价格")
    private int gamePrice;
    @Desc("达人币购买价格")
    private int goldPrice;

    public int getRecyclingPrice() {
        return recyclingPrice;
    }

    public void setRecyclingPrice(int recyclingPrice) {
        this.recyclingPrice = recyclingPrice;
    }

    public int getGamePrice() {
        return gamePrice;
    }

    public void setGamePrice(int gamePrice) {
        this.gamePrice = gamePrice;
    }

    public int getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(int goldPrice) {
        this.goldPrice = goldPrice;
    }

    public int getLuxuryDegree() {
        return luxuryDegree;
    }

    public void setLuxuryDegree(int luxuryDegree) {
        this.luxuryDegree = luxuryDegree;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getStack() {
        return stack;
    }

    public void setStack(int stack) {
        this.stack = stack;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
