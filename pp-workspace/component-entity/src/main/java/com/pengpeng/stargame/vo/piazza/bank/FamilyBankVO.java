package com.pengpeng.stargame.vo.piazza.bank;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-12-5
 * Time: 上午10:19
 */
@Desc("家族银行面板VO")
public class FamilyBankVO {
    @Desc("家族Id")
    private String familyId;
    @Desc("存款上限")
    private int maxValue;
    @Desc("自己的存款")
    private int myValue;
    @Desc("家族银行等级")
    private int rankLevel;
    @Desc("最大等级")
    private int maxRankLevel;
    @Desc("家族经费")
    private int fund;
    @Desc("最大经费")
    private int maxFund;

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMyValue() {
        return myValue;
    }

    public void setMyValue(int myValue) {
        this.myValue = myValue;
    }

    public int getRankLevel() {
        return rankLevel;
    }

    public void setRankLevel(int rankLevel) {
        this.rankLevel = rankLevel;
    }

    public int getMaxRankLevel() {
        return maxRankLevel;
    }

    public void setMaxRankLevel(int maxRankLevel) {
        this.maxRankLevel = maxRankLevel;
    }

    public int getFund() {
        return fund;
    }

    public void setFund(int fund) {
        this.fund = fund;
    }

    public int getMaxFund() {
        return maxFund;
    }

    public void setMaxFund(int maxFund) {
        this.maxFund = maxFund;
    }
}
