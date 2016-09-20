package com.pengpeng.stargame.vo.smallgame;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("小游戏排行")
public class PlayerSmallGameRankVO implements Comparable<PlayerSmallGameRankVO>{
	@Desc("玩家id")
	private String id;
    @Desc("玩家昵称")
    private String nickName;
    @Desc("玩家头像")
    private String portrait;
    @Desc("玩家排行")
    private int rank;
    @Desc("玩家分数")
    private int score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank+1;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    @Override
    public int compareTo(PlayerSmallGameRankVO p) {
        int tmp = p.getScore()-this.score;
        return tmp;
    }
}
