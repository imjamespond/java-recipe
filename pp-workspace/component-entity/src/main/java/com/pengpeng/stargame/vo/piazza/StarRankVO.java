package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-7-4
 * Time: 下午2:47
 */
@Desc("明星排行VO")
public class StarRankVO {
    @Desc("明星名字 ")
    private String star;
    @Desc("明星粉丝值 ")
    private int fansValues;
    @Desc("排行 ")
    private int rank;
    @Desc("明星头像 ")
    private String icon ;

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public int getFansValues() {
        return fansValues;
    }

    public void setFansValues(int fansValues) {
        this.fansValues = fansValues;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
