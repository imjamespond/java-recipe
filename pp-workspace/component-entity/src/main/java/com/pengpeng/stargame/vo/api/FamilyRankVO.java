package com.pengpeng.stargame.vo.api;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-8-14
 * Time: 下午6:05
 */
@Desc("家族排行VO")
public class FamilyRankVO {
    @Desc("排名")
    private int rank;
    @Desc("明星")
    private int starId;
    @Desc("粉丝值")
   private int fansValue;
    @Desc("明星名字")
    private String starName;
    @Desc("明星头像地址")
    private String iconUrl;

    public int getStarId() {
        return starId;
    }

    public void setStarId(int starId) {
        this.starId = starId;
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

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
