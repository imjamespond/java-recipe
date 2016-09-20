package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * User: mql
 * Date: 13-6-28
 * Time: 下午2:55
 */
@Desc("摇钱树的VO")
@EventAnnotation(name="event.moneytree.update",desc="树的状态更新")
public class MoneyTreeVO {
    @Desc("摇钱树的等级")
    private int level;
    @Desc("摇钱树的状态 1 生长  2成熟")
    private int status;
    @Desc("树的当前祝福数值")
    private int blessingValue;
    @Desc("树的最大祝福数值")
    private int blessingMax;
    @Desc("下次祝福冷却时间 毫秒数 ，如果是 0表示没有冷却时间")
    private long  nextBlessingTime;
    @Desc("如果摇钱树成熟，显示成熟剩余时间 毫秒数")
    private long ripeSurplusTime;
    @Desc("摇钱状态  0未摇过  1摇过")
    private int  shakeStatus;
    @Desc("玩家可以祝福的最大次数")
    private int playerBlessingMax;
    @Desc("玩家已经祝福的次数")
    private int playerBlessingValue;
    @Desc(" 离 成熟还有 多久")
    private long ripeTime;

    @Desc("掉落的钱的信息")
    private MoneyPickInfoVO [] moneyPickInfoVOs;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBlessingValue() {
        return blessingValue;
    }

    public void setBlessingValue(int blessingValue) {
        this.blessingValue = blessingValue;
    }

    public int getBlessingMax() {
        return blessingMax;
    }

    public void setBlessingMax(int blessingMax) {
        this.blessingMax = blessingMax;
    }

    public long getNextBlessingTime() {
        return nextBlessingTime;
    }

    public void setNextBlessingTime(long nextBlessingTime) {
        this.nextBlessingTime = nextBlessingTime;
    }

    public long getRipeSurplusTime() {
        return ripeSurplusTime;
    }

    public void setRipeSurplusTime(long ripeSurplusTime) {
        this.ripeSurplusTime = ripeSurplusTime;
    }

    public int getShakeStatus() {
        return shakeStatus;
    }

    public void setShakeStatus(int shakeStatus) {
        this.shakeStatus = shakeStatus;
    }

    public MoneyPickInfoVO[] getMoneyPickInfoVOs() {
        return moneyPickInfoVOs;
    }

    public void setMoneyPickInfoVOs(MoneyPickInfoVO[] moneyPickInfoVOs) {
        this.moneyPickInfoVOs = moneyPickInfoVOs;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPlayerBlessingMax() {
        return playerBlessingMax;
    }

    public void setPlayerBlessingMax(int playerBlessingMax) {
        this.playerBlessingMax = playerBlessingMax;
    }

    public int getPlayerBlessingValue() {
        return playerBlessingValue;
    }

    public void setPlayerBlessingValue(int playerBlessingValue) {
        this.playerBlessingValue = playerBlessingValue;
    }

    public long getRipeTime() {
        return ripeTime;
    }

    public void setRipeTime(long ripeTime) {
        this.ripeTime = ripeTime;
    }
}
