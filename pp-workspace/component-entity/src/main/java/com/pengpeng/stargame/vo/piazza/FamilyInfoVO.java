package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-6-26
 * Time: 下午5:10
 */
@Desc("家族信息VO")
public class FamilyInfoVO {
    @Desc("家族id")
    private String id;
    @Desc("家族名字")
    private String name;
    @Desc("家族明星id")
    private int starId;
    @Desc("家族明星icon")
    private String starIcon;
    @Desc("家族明星")
    private String starName;
    @Desc("家族等级")
    private int level;
    @Desc("家族QQ")
    private String qq;
    @Desc("家族YY")
    private String yy;
    @Desc("家族公告")
    private String notice;
    @Desc("家族经费")
    private int funds;
    @Desc("最高经费")
    private int maxFunds;

    @Desc("是否已领取福利")
    private boolean booned;
    @Desc("每周一次免费")
    private boolean weekFree;

    public boolean getBooned() {
        return booned;
    }

    public void setBooned(boolean booned) {
        this.booned = booned;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String star) {
        this.starName = star;
    }

    public int getFunds() {
        return funds;
    }

    public int getMaxFunds() {
        return maxFunds;
    }

    public void setMaxFunds(int maxFunds) {
        this.maxFunds = maxFunds;
    }

    public void setFunds(int funds) {
        this.funds = funds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getYy() {
        return yy;
    }

    public void setYy(String yy) {
        this.yy = yy;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getStarId() {
        return starId;
    }

    public void setStarId(int starId) {
        this.starId = starId;
    }

    public String getStarIcon() {
        return starIcon;
    }

    public void setStarIcon(String starIcon) {
        this.starIcon = starIcon;
    }

    public boolean isWeekFree() {
        return weekFree;
    }

    public void setWeekFree(boolean weekFree) {
        this.weekFree = weekFree;
    }
}
