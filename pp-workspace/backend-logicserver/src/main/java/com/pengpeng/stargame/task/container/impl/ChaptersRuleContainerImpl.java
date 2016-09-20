package com.pengpeng.stargame.task.container.impl;

import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.cmd.FarmRpc;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.container.IFarmRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.fashion.container.IFashionItemRuleContainer;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.fashion.dao.IFashionPlayerDao;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.player.cmd.PlayerRpc;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.container.IRoomItemRuleContainer;
import com.pengpeng.stargame.room.dao.IRoomPlayerDao;
import com.pengpeng.stargame.rpc.BroadcastHolder;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.rsp.RspTaskFactory;
import com.pengpeng.stargame.task.container.IChaptersRuleContainer;
import com.pengpeng.stargame.task.rule.ChaptersRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 13-8-2
 * Time: 下午2:27
 */
@Component
public class ChaptersRuleContainerImpl extends HashMapContainer<String, ChaptersRule> implements IChaptersRuleContainer{

    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private IFashionPlayerDao fashionPlayerDao;
    @Autowired
    private IRoomPlayerDao roomPlayerDao;
    @Autowired
    private IFashionItemRuleContainer fashionItemRuleContainer;
    @Autowired
    private IRoomItemRuleContainer roomItemRuleContainer;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private RspTaskFactory rspTaskFactory;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFarmRuleContainer farmRuleContainer;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private PlayerRpc playerRpc;
    @Autowired
    private FarmRpc farmRpc;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Override
    public ChaptersRule getChaptersRuleByTaskId(String taskId) {
        for(ChaptersRule temp:items.values()){
            if(temp.getTaskId().equals(taskId)){
                return temp;
            }
        }
        return null;
    }

    @Override
    public void addChaptersReward(String chaptersId, String pid) {
        ChaptersRule chaptersRule = getElement(chaptersId);
        FashionCupboard fashionCupboard = fashionCupboardDao.getBean(pid);

        Player player = playerDao.getBean(pid);

        for (ItemData itemReward : chaptersRule.getItemRewardList()) {
            baseItemRulecontainer.addGoods(player,itemReward.getItemId(),itemReward.getNum());
        }

        if (chaptersRule.getGameCoin() > 0) {
            player.incGameCoin(chaptersRule.getGameCoin());
            playerDao.saveBean(player);
            BroadcastHolder.add(roleFactory.newPlayerVO(player));
        }
        if (chaptersRule.getFarmExp() > 0) {
            FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());
            farmLevelRuleContainer.addFarmExp(farmPlayer, chaptersRule.getFarmExp());
            farmPlayerDao.saveBean(farmPlayer);
            BroadcastHolder.add(farmRpc.getFarmVoByPlayerId(pid));
        }
        if (chaptersRule.getFamilyDevote() > 0) {
            FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
            Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
            if (familyRuleContainer.isMember(family, familyMemberInfo)) {
                familyMemberInfo.incDevote(chaptersRule.getFamilyDevote(), new Date());
                familyMemberInfoDao.saveBean(familyMemberInfo);
            }
        }

        if (chaptersRule.getFamilyFunds() > 0) {
            FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
            Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
            if (familyRuleContainer.isMember(family, familyMemberInfo)) {
//                family.incFunds(chaptersRule.getFamilyFunds());
                familyRuleContainer.addFailyFunds(family,chaptersRule.getFamilyFunds());
                familyDao.saveBean(family);
            }
        }

        fashionCupboardDao.saveBean(fashionCupboard);
    }
}
