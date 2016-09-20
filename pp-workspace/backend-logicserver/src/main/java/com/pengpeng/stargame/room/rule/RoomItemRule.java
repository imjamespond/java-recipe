package com.pengpeng.stargame.room.rule;

import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.RoomPackege;

import javax.persistence.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-22下午4:45
 */
@Entity
@Table(name = "sg_rule_item_room")
@PrimaryKeyJoinColumn(name = "itemsId")
public class RoomItemRule extends BaseItemRule {

    //道具豪华度
    @Column
    private int luxuryDegree;
    //道具图片名称
    @Column
    private String image;
    @Column
    private int rotation;
    @Column
    private int repeatPurchase;
    @Column
    private int largestNumber;
    @Column
    private int stack;

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

//    public void consumerBuy(Player player) {
//        player.decGameCoin(this.getGamePrice());
//        player.decGoldCoin(this.getGoldPrice());
//    }

    public void effectBuy(RoomPackege pkg, int num) {
        pkg.addItem(getItemsId(), num);
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getRepeatPurchase() {
        return repeatPurchase;
    }

    public void setRepeatPurchase(int repeatPurchase) {
        this.repeatPurchase = repeatPurchase;
    }

    public int getLargestNumber() {
        return largestNumber;
    }

    public void setLargestNumber(int largestNumber) {
        this.largestNumber = largestNumber;
    }


    public int getStack() {
        return stack;
    }

    public void setStack(int stack) {
        this.stack = stack;
    }

    public boolean checkSale(RoomPackege pkg, int num) {
        int count = pkg.count(getItemsId());
        if (count < num) {
            return false;
        }
        return true;
    }

    public void consumerSale(RoomPackege pkg, int num) {
        pkg.decItem(getItemsId(), num);
    }

//    public void effectSale(Player player, int num) {
//        int coin = this.getRecyclingPrice() * num;
//        player.incGameCoin(coin);
//    }
}
