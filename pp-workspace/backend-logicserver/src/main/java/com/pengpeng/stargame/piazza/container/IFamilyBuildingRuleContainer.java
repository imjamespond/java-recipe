package com.pengpeng.stargame.piazza.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.piazza.rule.FamilyBuildingRule;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-27下午2:13
 */
public interface IFamilyBuildingRuleContainer extends IMapContainer<String,FamilyBuildingRule> {

    /**
     * 检查是否可升级
     * @param family
     * @param info
     * @throws RuleException
     */
    public void checkUpgrade(Family family,FamilyBuildInfo info,int type) throws RuleException;

    public void upgrade(Family family, FamilyBuildInfo info,int type) throws RuleException;

    public FamilyBuildingRule getElement(int type, int level);
}
