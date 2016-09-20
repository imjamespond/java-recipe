package com.pengpeng.stargame.piazza.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-26下午4:26
 */
@Entity
@Table(name="sg_rule_moneytree")
public class MoneyTreeRule extends BaseEntity<Integer> {
    //等级
    @Id
    private int level;
    //祝福值上限
    @Column
    private int blessingMax;
    //祝福次数
    @Column
    private int numberOfBlessing;
    //每次祝福需要消耗的游戏币
    @Column
    private  int gameCoin;
    //每次祝福获取的贡献值
    @Column
    private int acquireDevote;
    //每次祝福值
    @Column
    private int blessing;
    //总产出参数
    @Column
    private int outputPar;
    //奖励参数1
    @Column
    private float rewardPar1;
    //奖励参数2
    @Column
    private float rewardPar2;
    //奖励参数3
    @Column
    private float rewardPar3;
    //掉落参数
    @Column
    private float dropPar;
    //掉落面额
    @Column
    private int dropDenomination;
    //道具奖励编辑
    @Column
    private String rewardEdit;
    //每隔多少秒摇钱树最多掉落一次游戏币
    @Column
    private int dropFrequency ;
    //每次摇树掉落游戏币的几率，必须满足掉落频率才执行几率计算，格式：N ,表N%
    @Column
    private int  dropChance ;

    @Column
    private String dropPosition;
    @Transient
    String [] positions;
    @Override
    public Integer getId() {
        return level;
    }

    @Override
    public void setId(Integer id) {
        this.level = id;
    }

    @Override
    public Integer getKey() {
        return level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBlessingMax() {
        return blessingMax;
    }

    public void setBlessingMax(int blessingMax) {
        this.blessingMax = blessingMax;
    }

    public int getNumberOfBlessing() {
        return numberOfBlessing;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getAcquireDevote() {
        return acquireDevote;
    }

    public void setAcquireDevote(int acquireDevote) {
        this.acquireDevote = acquireDevote;
    }

    public void setNumberOfBlessing(int numberOfBlessing) {
        this.numberOfBlessing = numberOfBlessing;
    }

    public int getBlessing() {
        return blessing;
    }

    public void setBlessing(int blessing) {
        this.blessing = blessing;
    }

    public int getOutputPar() {
        return outputPar;
    }

    public void setOutputPar(int outputPar) {
        this.outputPar = outputPar;
    }

    public float getRewardPar1() {
        return rewardPar1;
    }

    public void setRewardPar1(float rewardPar1) {
        this.rewardPar1 = rewardPar1;
    }

    public float getRewardPar2() {
        return rewardPar2;
    }

    public void setRewardPar2(float rewardPar2) {
        this.rewardPar2 = rewardPar2;
    }

    public float getRewardPar3() {
        return rewardPar3;
    }

    public void setRewardPar3(float rewardPar3) {
        this.rewardPar3 = rewardPar3;
    }

    public float getDropPar() {
        return dropPar;
    }

    public void setDropPar(float dropPar) {
        this.dropPar = dropPar;
    }

    public int getDropDenomination() {
        return dropDenomination;
    }

    public void setDropDenomination(int dropDenomination) {
        this.dropDenomination = dropDenomination;
    }

    public String getRewardEdit() {
        return rewardEdit;
    }

    public void setRewardEdit(String rewardEdit) {
        this.rewardEdit = rewardEdit;
    }

    public boolean canBlessing(int num){
        return numberOfBlessing<num;
    }

    public int getDropFrequency() {
        return dropFrequency;
    }

    public void setDropFrequency(int dropFrequency) {
        this.dropFrequency = dropFrequency;
    }

    public int getDropChance() {
        return dropChance;
    }

    public void setDropChance(int dropChance) {
        this.dropChance = dropChance;
    }

    public String getDropPosition() {
        return dropPosition;
    }

    public void setDropPosition(String dropPosition) {
        this.dropPosition = dropPosition;
    }

    /**
     * 摇树获得游戏币计算
     */
    public int getGold(){
        return 0;
    }
    /**
     * 掉落游戏币计算
     */
    public int dropGold(){
        return 0;
    }

    public int dropOverground(){
        return 0;
    }

    public String[] getPositions() {
        return positions;
    }

    public void setPositions(String[] positions) {
        this.positions = positions;
    }

    public void initPositions(){
        this.positions=this.dropPosition.split(";");
    }
}
