package com.pengpeng.stargame.vo.room;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.BaseShopItemVO;
import com.pengpeng.stargame.vo.farm.FarmShopItemVO;

/**
 * User: mql
 * Date: 13-5-22
 * Time: 下午4:00
 */
@Desc("个人房间 商店 物品 VO")
public class RoomShopItemVO extends BaseShopItemVO{
    @Desc("魅力值")
    private  int glamour;
    @Desc("豪华度")
    private int luxuryDegree;
    @Desc("图片")
    private String image;
    @Desc("0为不可旋转,1为可旋转")
    private int rotation;
    @Desc("0为上面不可叠放东西，1为上面可以放东西")
    private int stack;
    @Desc("0为不可出售，1为可出售")
    private int sell;

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
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

    public int getGlamour() {
        return glamour;
    }

    public void setGlamour(int glamour) {
        this.glamour = glamour;
    }
}
