package com.pengpeng.stargame.container.farm;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.rule.farm.FarmCropRule;

/**
 * User: mql
 * Date: 13-4-26
 * Time: 下午2:58
 */
public interface IFarmCropRuleContainer extends IMapContainer<String,FarmCropRule> {

    FarmCropRule getFarmCropRuleById(String id);
}
