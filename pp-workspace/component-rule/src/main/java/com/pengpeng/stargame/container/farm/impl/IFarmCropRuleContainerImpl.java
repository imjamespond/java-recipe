package com.pengpeng.stargame.container.farm.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.container.farm.IFarmCropRuleContainer;
import com.pengpeng.stargame.rule.farm.FarmCropRule;

/**
 * User: mql
 * Date: 13-4-26
 * Time: 下午2:58
 */
public class IFarmCropRuleContainerImpl extends HashMapContainer<String,FarmCropRule> implements IFarmCropRuleContainer {
    @Override
    public FarmCropRule getFarmCropRuleById(String id) {
        return this.getElement(id);
    }
}
