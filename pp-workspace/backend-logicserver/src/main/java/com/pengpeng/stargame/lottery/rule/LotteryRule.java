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
@Table(name="sg_rule_lottery")
public class LotteryRule extends BaseEntity<String> {

    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String itemId;
    @Column
    private int probability;
    @Column
    private int sex;
    @Column
    private int type;
    @Column
    private int num;
    @Column
    private int gameCoin;
    @Column
    private int groupId;
    @Column
    private boolean speaker;

    @Transient
    public static final int TYPE_FREE = 2;
    @Transient
    public static final int TYPE_GOLD = 1;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSpeaker() {
        return speaker;
    }

    public void setSpeaker(boolean speaker) {
        this.speaker = speaker;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
