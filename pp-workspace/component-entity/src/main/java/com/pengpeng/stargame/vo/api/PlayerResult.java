package com.pengpeng.stargame.vo.api;

import com.pengpeng.stargame.annotation.Desc;

import java.util.Date;

/**
 * User: mql
 * Date: 13-8-15
 * Time: 下午2:14
 */
public class PlayerResult extends BaseResult {
    @Desc("角色名称")
    private String name;
    @Desc("偶像名称")
    private String starName;
    @Desc("明星Id")
    private int starId;
    @Desc("豪华度")
    private int luxuryDegree;
    @Desc("家族职位")
    private String position;
    @Desc("游戏币")
    private int gameCoin;
    @Desc("达人币")
    private int goldCoin;
    @Desc("农场等级")
    private int farmLevel;
    @Desc("1 是VIP  0不是")
    private int vip;
    @Desc("VIP结束时间  毫秒数")
    private Date endTime;


    public PlayerResult(){

    }
    public    PlayerResult(int status,String msg){
        this.status=status;
        this.msg=msg;

    }


    public int getStarId() {
        return starId;
    }

    public void setStarId(int starId) {
        this.starId = starId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public int getLuxuryDegree() {
        return luxuryDegree;
    }

    public void setLuxuryDegree(int luxuryDegree) {
        this.luxuryDegree = luxuryDegree;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(int goldCoin) {
        this.goldCoin = goldCoin;
    }

    public int getFarmLevel() {
        return farmLevel;
    }

    public void setFarmLevel(int farmLevel) {
        this.farmLevel = farmLevel;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
