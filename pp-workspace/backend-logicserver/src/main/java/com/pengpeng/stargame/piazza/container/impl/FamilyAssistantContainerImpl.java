package com.pengpeng.stargame.piazza.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.player.PlayerInfo;
import com.pengpeng.stargame.model.vip.PlayerVip;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyAssistantContainer;
import com.pengpeng.stargame.piazza.container.IFamilyBuildingRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberDao;
import com.pengpeng.stargame.piazza.rule.FamilyBuildingRule;
import com.pengpeng.stargame.vo.piazza.AssistantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 家族建筑规则
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-27下午2:14
 */
@Component
public class FamilyAssistantContainerImpl implements IFamilyAssistantContainer {

    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IFamilyMemberDao familyMemberDao;

    @Override
    public void check(FamilyMemberInfo familyMemberInfo,FarmPlayer farmPlayer,boolean isVip) throws RuleException {

        //拥有VIP粉丝身份
        if(!isVip){
            exceptionFactory.throwRuleException("require.vip.identity");
        }
        if(familyMemberInfo==null){
            exceptionFactory.throwRuleException("family.notfound.memberInfo");
        }
        if(farmPlayer==null){
            exceptionFactory.throwRuleException("farm.item.notfound");
        }
        //家园等级大于等于25级
        if(farmPlayer.getLevel()<25)  {
            exceptionFactory.throwRuleException("farm.level.less.than.25");
        }

        //本周贡献度大于等于100
        if(familyMemberInfo.getWeekContribution()<100)  {
            exceptionFactory.throwRuleException("week.contribution.less.than.100");
        }
    }

}
