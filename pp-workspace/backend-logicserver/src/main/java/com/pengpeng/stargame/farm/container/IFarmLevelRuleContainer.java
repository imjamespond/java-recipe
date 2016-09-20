package com.pengpeng.stargame.farm.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.farm.rule.FarmLevelRule;
import com.pengpeng.stargame.model.farm.FarmPlayer;

/**
 * User: mql
 * Date: 13-4-24
 * Time: 下午3:43
 */
public interface IFarmLevelRuleContainer extends IMapContainer<Integer, FarmLevelRule> {
    /**
     *
     * @param farmPlayer
     * @throws AlertException
     */
    public  void check(FarmPlayer farmPlayer) throws AlertException;

    /**
     * 升级
     * @param farmPlayer
     */
    public void upgrade(FarmPlayer farmPlayer);

    /**
     * 通过 农场等级 获取 该等级需要的经验
     * @param level
     * @return
     */
    int getNeedExpByLevel(int level);

    /**
     * 农场 加经验
     * @param farmPlayer
     * @param exp
     */
    public  void addFarmExp(FarmPlayer farmPlayer,int exp);

    /**
     * 添加田地
     * @param farmPlayer
     */
    public void addField(FarmPlayer farmPlayer);
}
