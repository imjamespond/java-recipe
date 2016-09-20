package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-6-26
 * Time: 下午5:36
 */
@Desc("单个家族Vo ，用于家族列表显示 ")
public class FamilyVO {
    @Desc("家族Id ")
    private String id;
    @Desc("家族名字 ")
    private String name;
    @Desc("家族等级 ")
    private int level;
    @Desc("明星id")
    private int starId;
    @Desc("家族明星")
    private String starName;
    @Desc("家族粉丝数量 ")
    private int  memberNum;
    @Desc("粉丝值 ")
    private int fansValue;
    @Desc("排行 ")
    private int rank;
    @Desc("亨奂家族需要的游戏币")
    private int changeCoin;
    @Desc("明星头像")
    private String starIcon;
    public int getChangeCoin() {
        return changeCoin;
    }

    public void setChangeCoin(int changeCoin) {
        this.changeCoin = changeCoin;
    }

    public int getStarId() {
        return starId;
    }

    public void setStarId(int starId) {
        this.starId = starId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public int getFansValue() {
        return fansValue;
    }

    public void setFansValue(int fansValue) {
        this.fansValue = fansValue;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getStarIcon() {
        return starIcon;
    }

    public void setStarIcon(String starIcon) {
        this.starIcon = starIcon;
    }
}
