package com.pengpeng.stargame.vo.api;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-8-14
 * Time: 下午6:05
 */
@Desc("个人房间排行VO")
public class RoomRankVO {
    @Desc("排名")
    private int rank;
    @Desc("网站玩家Id")
    private int uid;
    @Desc("豪华度")
    private int luxuryDegree;
    @Desc("玩家名字")
    private String uname;
    @Desc("偶像名称")
    private String starName;
    @Desc("明星Id")
    private int starId;


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

    public int getLuxuryDegree() {
        return luxuryDegree;
    }

    public void setLuxuryDegree(int luxuryDegree) {
        this.luxuryDegree = luxuryDegree;
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
}
