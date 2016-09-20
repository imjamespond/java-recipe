package com.pengpeng.stargame.vo.stall;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.fashion.FashionVO;
import com.pengpeng.stargame.vo.fashion.PlayerFashionVO;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("路人")
public class StallPassengerVO {
    @Desc("路人id")
    private int passengerId;     //
    @Desc("路人动作 0已到1行走")
    private int action;     //
    @Desc("路人性别0女1男")
    private int gender;     //
    @Desc("路人时装")
    private FashionVO[] fashionVO;     //
    @Desc("物品id")
    private String itemId;     //
    @Desc("物品名")
    private String itemName;     //
    @Desc("物品数量")
    private int itemNum;     //
    @Desc("库存数量")
    private int inventory;     //
    @Desc("游戏币")
    private int gameCoin;     //
    @Desc("积分")
    private int credit;     //

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public FashionVO[] getFashionVO() {
        return fashionVO;
    }

    public void setFashionVO(FashionVO[] fashionVO) {
        this.fashionVO = fashionVO;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}
