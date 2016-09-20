package com.pengpeng.stargame.container.farm;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.rule.farm.FarmItemRule;
import com.pengpeng.stargame.rule.farm.FarmLevelRule;
import com.pengpeng.stargame.rule.role.PlayerRule;

/**
 * User: mql
 * Date: 13-4-24
 * Time: 下午3:43
 */
public interface IFarmLevelRuleContainer extends IMapContainer<Integer, FarmLevelRule> {
    /**
     * 通过 农场等级 获取 等级规则
     * @param level
     * @return
     */
    FarmLevelRule getFarmlevelRuleById(int level);

    /**
     * 通过 农场等级 获取 该等级需要的经验
     * @param level
     * @return
     */
    int getNeedExpByLevel(int level);
}
