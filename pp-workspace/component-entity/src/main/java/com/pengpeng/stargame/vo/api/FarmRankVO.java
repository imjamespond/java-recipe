package com.pengpeng.stargame.vo.api;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-9-23
 * Time: 下午5:03
 */
@Desc("农场等级排行VO")
public class FarmRankVO {
    @Desc("排名")
    private int rank;
    @Desc("网站玩家Id")
    private int uid;
    @Desc("农场等级")
    private int farmLevel;
    @Desc("玩家名字")
    private String uname;
    @Desc("偶像名称")
    private String starName;
    @Desc("明星Id")
    private int starId;
    @Desc("农场经验 游戏这边排序用")
    private int exp;


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getFarmLevel() {
        return farmLevel;
    }

    public void setFarmLevel(int farmLevel) {
        this.farmLevel = farmLevel;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public int getStarId() {
        return starId;
    }

    public void setStarId(int starId) {
        this.starId = starId;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
