package com.pengpeng.stargame.fashion.container.impl;

import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.fashion.container.IFashionGiftRuleContainer;
import com.pengpeng.stargame.fashion.rule.FashionGiftRule;
import com.pengpeng.stargame.model.Gift;
import com.pengpeng.stargame.model.GiftPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionPkg;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IGiftPlayerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-3下午8:12
 */
@Component
public class FashionGiftRuleContainerImpl extends HashMapContainer<String,FashionGiftRule> implements IFashionGiftRuleContainer {
    private static final String KEY = "farm.giftrule";
	FashionGiftRule rule = null;

    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Autowired
    private IGiftPlayerDao giftPlayerDao;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;

    public FashionGiftRuleContainerImpl(){
        rule = new FashionGiftRule();
        rule.setId(KEY);
		rule.setGiveCount(20);
		rule.setExpireDay(3);
        this.addElement(rule);
    }

    @Override
    public void checkGive(int num) throws RuleException {
        boolean can = rule.canGift(num);
        if (can){
            exceptionFactory.throwRuleException("gift.give.max");
        }
    }

    @Override
    public void give(Player friend, GiftPlayer giftPlayer, String itemId) throws RuleException {
        Gift gift = new Gift();
        gift.setItemId(itemId);
        gift.setNum(1);
//        long time = System.currentTimeMillis()+itemRule.getValidityTime()*24*60*60*1000;
        long time = System.currentTimeMillis()  + ((24*60*60*1000) * rule.getExpireDay());
        gift.setValidityDate(new Date(time));
        gift.setFid(friend.getId());
        giftPlayer.addGift(gift);
    }

    @Override
    public void give2(Player me, GiftPlayer giftPlayer, String itemId, int num, String msg) throws AlertException {
        BaseItemRule baseItemRule=baseItemRulecontainer.getElement(itemId);

        int gold = baseItemRule.getGoldPrice()*num;
        if(me.getGoldCoin()<=gold){
            exceptionFactory.throwAlertException("gold.coin.notenough");
        }
        playerRuleContainer.decGoldCoin(me,gold, PlayerConstant.GOLD_ACTION_9);

        gold = baseItemRule.getGamePrice()*num;
        if(me.getGameCoin()<=gold){
            exceptionFactory.throwAlertException("game.coin.notenough");
        }
        playerRuleContainer.decGameCoin(me,gold);

        Gift gift = new Gift();
        gift.setMessage(msg);
        gift.setItemId(itemId);
        gift.setNum(num);
        gift.setType(1);
//        long time = System.currentTimeMillis()+itemRule.getValidityTime()*24*60*60*1000;
        //long time = System.currentTimeMillis()  + ((24*60*60*1000) * rule.getExpireDay());
        //gift.setValidityDate(new Date(time));
        gift.setGiveDate(new Date());
        gift.setFid(me.getId());
        giftPlayer.add2GiftList(gift);
    }


    @Override
    public void qinMaGive(String pid, String itemId) {
        GiftPlayer giftPlayer=giftPlayerDao.getBean(pid);

        Gift gift = new Gift();
        gift.setItemId(itemId);
        gift.setNum(1);
//        long time = System.currentTimeMillis()+itemRule.getValidityTime()*24*60*60*1000;
        long time = System.currentTimeMillis()  + ((24*60*60*1000) * rule.getExpireDay());
        gift.setValidityDate(new Date(time));
        gift.setFid(Constant.QINMA_ID);
        giftPlayer.add2GiftList(gift);

        giftPlayerDao.saveBean(giftPlayer);


    }

    @Override
    public void accept(FashionPkg fp ,GiftPlayer player, String fid) throws RuleException {
        Gift gift = player.getGift(fid);
        if (null==gift){
            exceptionFactory.throwRuleException("gift.accept.already");
        }
        BaseItemRule baseItemRule=baseItemRulecontainer.getElement(gift.getItemId());
        fp.addItem(gift.getItemId(),gift.getNum(),baseItemRule.getOverlay(),baseItemRule.getValidDete());
        player.removeGift(fid);
    }

    @Override
    public void reject(GiftPlayer player, String fid) throws RuleException {
        //拒绝,删除,忽略好友的礼物
        player.removeGift(fid);
    }

}
