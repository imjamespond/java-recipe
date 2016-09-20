package com.pengpeng.stargame.integral.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.integral.rule.IntegralRule;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午4:55
 */
public interface IIntegralRuleContainer extends IMapContainer<String,IntegralRule> {
    public void addIntegralAction(String pid,int type,int num) throws Exception;
}
