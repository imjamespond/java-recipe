package com.pengpeng.stargame.lottery.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:04
 */

@Entity
@Table(name="sg_rule_roulette")
public class RouletteRule extends BaseEntity<String> {

    @Id
    private String id;   //

    @Column
    private String itemId;   //物品
    @Column
    private int itemNum;   //物品数量
    @Column
    private int goldCoin;   //达人币
    @Column
    private int gameCoin;//游戏币
    @Column
    private int credit;//积分
    @Column
    private int farmExp;//农场经验
    @Column
    private int probability;//机率

    @Column
    private int gender;//性别
    @Column
    private int speaker;//广播

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return id;
    }

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

    public int getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(int goldCoin) {
        this.goldCoin = goldCoin;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getFarmExp() {
        return farmExp;
    }

    public void setFarmExp(int farmExp) {
        this.farmExp = farmExp;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int isSpeaker() {
        return speaker;
    }

    public void setSpeaker(int speaker) {
        this.speaker = speaker;
    }
}
