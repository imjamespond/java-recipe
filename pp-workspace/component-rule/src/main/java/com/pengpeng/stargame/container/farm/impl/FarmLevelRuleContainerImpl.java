package com.pengpeng.stargame.container.farm.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.container.farm.IFarmLevelRuleContainer;
import com.pengpeng.stargame.rule.farm.FarmLevelRule;
import com.pengpeng.stargame.rule.role.PlayerRule;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-4-24
 * Time: 下午3:55
 */
@Component
public class FarmLevelRuleContainerImpl extends HashMapContainer<Integer, FarmLevelRule>  implements IFarmLevelRuleContainer {
    @Override
    public FarmLevelRule getFarmlevelRuleById(int level) {
        return this.getElement(level);
    }

    @Override
    public int getNeedExpByLevel(int level) {
        FarmLevelRule fr=this.getElement(level);
        return  fr.getNeedExp();
    }
}
