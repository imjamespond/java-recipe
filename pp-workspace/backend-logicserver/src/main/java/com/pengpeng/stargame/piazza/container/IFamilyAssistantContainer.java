package com.pengpeng.stargame.piazza.container;

import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.vip.PlayerVip;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-12-6
 * Time: 上午10:24
 */
public interface IFamilyAssistantContainer {
    void check(FamilyMemberInfo familyMemberInfo,FarmPlayer farmPlayer,boolean isVip) throws RuleException;
}
