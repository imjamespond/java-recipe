package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-5-6
 * Time: 上午11:00
 */
@Desc("一个订单VO ")
public class OneOrderVO {
    @Desc("订单Id")
    private String id;
    @Desc("订单名称")
    private String name;
    @Desc("订单奖励的经验")
    private int exp;
    @Desc("订单奖励的游戏币")
    private int gameMoney;
    @Desc("订单里面需要的 物品列表")
    private GoodsVO[] goodsVOList;

    public OneOrderVO(){
    }

    public OneOrderVO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getGameMoney() {
        return gameMoney;
    }

    public void setGameMoney(int gameMoney) {
        this.gameMoney = gameMoney;
    }

    public GoodsVO[] getGoodsVOList() {
        return goodsVOList;
    }

    public void setGoodsVOList(GoodsVO[] goodsVOList) {
        this.goodsVOList = goodsVOList;
    }
}
