package com.pengpeng.stargame.farm.container.impl;

import com.pengpeng.stargame.constant.BaseItemConstant;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmGiftRuleContainer;
import com.pengpeng.stargame.farm.container.IFarmWareHouseContainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.farm.rule.FarmGiftRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.Gift;
import com.pengpeng.stargame.model.GiftPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-3下午8:12
 */
@Component
public class FarmGiftRuleContainerImpl extends HashMapContainer<String,FarmGiftRule> implements IFarmGiftRuleContainer {
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    private static final String KEY = "farm.giftrule";
	FarmGiftRule rule = null;

    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private IFarmWareHouseContainer farmWareHouseContainer;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;

    public FarmGiftRuleContainerImpl(){
        rule = new FarmGiftRule();
        rule.setId(KEY);
		rule.setGiveCount(20);
		rule.setExpireDay(3);
        this.addElement(rule);
    }

    @Override
    public void checkGive(String pid,int num) throws RuleException {
//        boolean can = rule.canGift(num);
//        if (can){
//            exceptionFactory.throwRuleException("gift.give.max");
//        }
        if(num>getMaxSendNum(pid)){
            exceptionFactory.throwRuleException("gift.give.max");
        }
    }

    @Override
    public void give(GiftPlayer myGiftplayer, GiftPlayer friendGiftPlayer, String itemId) throws RuleException {
        Gift gift = new Gift();
        gift.setItemId(itemId);
        gift.setNum(1);
        gift.setGiveDate(new Date());
//        long time = System.currentTimeMillis()+itemRule.getValidityTime()*24*60*60*1000;
		long time = System.currentTimeMillis()  + ((24*60*60*1000) * rule.getExpireDay());
        gift.setValidityDate(new Date(time));
        gift.setFid(myGiftplayer.getKey());
        friendGiftPlayer.add2GiftList(gift);
        myGiftplayer.addDayGift(friendGiftPlayer.getId());
        myGiftplayer.incGive();
    }

    @Override
    public void accept(FarmPackage fp ,GiftPlayer player, String fid) throws RuleException {
        Gift gift = player.getGift(fid);
        if (null==gift){
            exceptionFactory.throwRuleException("gift.accept.already");
        }
        int max = farmWareHouseContainer.getElement(fp.getLevel()).getCapacity();
        BaseItemRule baseItemRule=baseItemRulecontainer.getElement(gift.getItemId());
        fp.addItem(gift.getItemId(),gift.getNum(),baseItemRule.getOverlay(),baseItemRule.getValidDete());
        player.removeGift(fid);
    }
    @Override
    public void acceptOne(GiftPlayer giftPlayer, FarmPackage farmPackage, FashionCupboard fashionCupboard, int order) throws AlertException {
        Gift gift = giftPlayer.getFromGiftList(order);//我的礼物
        if (null == gift) {
            exceptionFactory.throwAlertException("gift.accept.already");
        }
        if(gift.getType() == 0){ //可删除的
            giftPlayer.set2GiftList(order,null);
        }else if(gift.getType() == 2) {//已经领取
            exceptionFactory.throwAlertException("gift.accept.already");
        }
        gift.setType(2);

        BaseItemRule rule = baseItemRulecontainer.getElement(gift.getItemId());
        if (!baseItemRulecontainer.addItemAndCheckNoSaving(rule, gift.getNum(), farmPackage, null, fashionCupboard, null)) {
            if (rule.getItemtype() == BaseItemConstant.TYPE_FARM)
                exceptionFactory.throwAlertException("package.farm.full");
            else if (rule.getItemtype() == BaseItemConstant.TYPE_FASHION)
                exceptionFactory.throwAlertException("package.fashion.full");
            else
                exceptionFactory.throwAlertException("item.add.failed");
        }
    }

    @Override
    public void reject(GiftPlayer player, String fid) throws RuleException {
        //拒绝,删除,忽略好友的礼物
        player.removeGift(fid);
    }

    @Override
    public int getMaxSendNum(String pid) {
        int maxNum= FarmConstant.MAX_SEND_NUM;
        if(payMemberRuleContainer.isPayMember(pid)){
            maxNum=payMemberRuleContainer.getSendGiftMaxNum(pid);
        }
        return maxNum;
    }
}
