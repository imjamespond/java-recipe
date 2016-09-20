package com.pengpeng.stargame.piazza.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyBuildingRuleContainer;
import com.pengpeng.stargame.piazza.rule.FamilyBuildingRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 家族建筑规则
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-27下午2:14
 */
@Component
public class FamilyBuildingRuleContainerImpl extends HashMapContainer<String,FamilyBuildingRule> implements IFamilyBuildingRuleContainer {

    @Autowired
    private IExceptionFactory exceptionFactory;

    @Override
    public void checkUpgrade(Family family, FamilyBuildInfo info,int type) throws RuleException {
        //String id = null;
        int lv = info.getLevel(type)+1;//当前等级
        FamilyBuildingRule rule = this.getElement(type,lv);
        if (rule==null){
            exceptionFactory.throwRuleException("family.build.maxlevel");
        }
        int level = info.getLevel(FamilyConstant.BUILD_CASTLE);
        boolean up = rule.canLevelUp(level,family.getFunds());
        if (!up){
            exceptionFactory.throwRuleException("family.build.notup");
        }
        if (lv>rule.getMaxLevel()){
            exceptionFactory.throwRuleException("family.build.maxlevel");
        }
    }

    @Override
    public void upgrade(Family family, FamilyBuildInfo info,int type) throws RuleException {
        int level = info.getLevel(type)+1;
        FamilyBuildingRule rule = this.getElement(type,level);
        if (rule==null){
            exceptionFactory.throwRuleException("family.build.maxlevel");
        }
        int funds = rule.getLevelFunds();
        family.decFunds(funds);
        info.setLevel(type,level);
    }

    @Override
    public FamilyBuildingRule getElement(final int type, final int level) {
        for(FamilyBuildingRule rule:items.values()){
            if (rule.getType() ==type&& rule.getLevel()==level){
                return rule;
            }
        }
        return null;
    }
}
