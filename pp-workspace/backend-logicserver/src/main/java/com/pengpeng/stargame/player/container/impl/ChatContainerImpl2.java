package com.pengpeng.stargame.player.container.impl;

import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.player.PlayerChat;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.player.container.IChatContainer;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 聊天内容检查规则
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-22上午10:41
 */
@Component
public class ChatContainerImpl2 implements IChatContainer {
    private Logger logger = Logger.getLogger(ChatContainerImpl2.class);
    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private SiteDao siteDao;

    private static int COST = 10;
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Override
    public void init() {
    }

    @Override
    public void addWord(Collection<String> words) {
    }

    @Override
    public void addWord(String word) {
    }

    @Override
    public void removeWord(String word) {
    }

    @Override
    public void checkTalk(OtherPlayer player) throws RuleException {
        if (!canTalk(player)) {
            exceptionFactory.throwRuleException("chat.notalk");
        }
    }

    @Override
    public boolean canTalk(OtherPlayer player) {
        if (player.getSpeakTime() <= 0) {
            return true;
        }
        if (System.currentTimeMillis() > player.getSpeakTime()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasSensitive(String msg) {
        if (StringUtils.isBlank(msg)) {
            return false;
        }
        String word = siteDao.dropIllegalWord(msg);
        return !word.equals(msg);
    }

    @Override
    public void checkSensitive(String msg) throws RuleException {
        if (hasSensitive(msg)) {
            exceptionFactory.throwRuleException("chat.illegal.info");
        }
    }

    @Override
    public String filter(String msg) {
        String word = siteDao.dropIllegalWord(msg);
        return word;
    }

    @Override
    public void checkCoin(Player player, PlayerChat playerChat) throws AlertException {


        if (player.getGoldCoin() < COST) {
            exceptionFactory.throwAlertException("goldcoin.notenough");
        }
//        player.decGoldCoin(COST);
        playerRuleContainer.decGoldCoin(player,COST, PlayerConstant.GOLD_ACTION_12);


    }

    @Override
    public void checFreeNum(Player player, PlayerChat playerChat) throws GameException {
        if(getFreeNum(player.getId())-playerChat.getNum()<=0){
            exceptionFactory.throwAlertException("免费次数已经用完！");
        }

       playerChat.setNum(playerChat.getNum() + 1);

    }

    @Override
    public int getFreeNum(String pid) {
        int maxNum = 0;
        /**
         *VIP的免费次数
         */
        if (payMemberRuleContainer.isPayMember(pid)) {
            maxNum += payMemberRuleContainer.getSonorusMaxNum(pid);
        }
        /**
         * 明星助理的免费次数
         */
        FamilyMemberInfo familyMemberInfo=familyMemberInfoDao.getFamilyMember(pid);
        if(familyMemberInfo.getIdentity()== FamilyConstant.TYPE_ZL){
            maxNum+=3;
        }
        return maxNum;
    }

}
