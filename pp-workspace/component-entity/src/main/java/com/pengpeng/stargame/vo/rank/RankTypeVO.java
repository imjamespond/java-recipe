package com.pengpeng.stargame.vo.rank;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 14-4-15
 * Time: 下午4:54
 */
@Desc("排行类型VO")
public class RankTypeVO {
    private int type;
    private RankVO [] rankVOs;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public RankVO[] getRankVOs() {
        return rankVOs;
    }

    public void setRankVOs(RankVO[] rankVOs) {
        this.rankVOs = rankVOs;
    }
}
