package com.pengpeng.stargame.piazza.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.piazza.rule.ActiveControlRule;

import java.util.List;

/**
 * User: mql
 * Date: 13-7-2
 * Time: 下午4:30
 */
public interface IActiveControlRuleContainer extends IMapContainer<String,ActiveControlRule> {

    public List<ActiveControlRule> getAll(int level);
}
