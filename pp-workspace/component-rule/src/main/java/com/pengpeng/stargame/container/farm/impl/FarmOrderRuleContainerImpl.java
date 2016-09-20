package com.pengpeng.stargame.container.farm.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.container.farm.IFarmOrderRuleContainer;
import com.pengpeng.stargame.rule.farm.FarmOrderRule;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-3-7下午6:26
 */
@Component
public class FarmOrderRuleContainerImpl extends HashMapContainer<String,FarmOrderRule> implements IFarmOrderRuleContainer {
}
