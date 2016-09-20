package com.pengpeng.stargame.rank.container;

import com.pengpeng.stargame.vo.rank.RankVO;

/**
 * User: mql
 * Date: 14-4-9
 * Time: 下午6:04
 */
public interface IRankRuleContainer {

    RankVO [] getRankVO(int type);

    public void initRankValue(String pid);
}
