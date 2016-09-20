package com.pengpeng.stargame.giftbag.container.impl;


import com.pengpeng.stargame.common.Constant;


import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.giftbag.container.IGiftBagRuleContainer;
import com.pengpeng.stargame.giftbag.rule.GiftBagRule;
import com.pengpeng.stargame.model.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pengpeng.stargame.model.giftBag.PlayerGiftGag;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.rsp.RspFarmFactory;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.farm.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import java.util.HashMap;

/**
 * User: mql
 * Date: 13-12-10
 * Time: 上午11:43
 */
@Component
public class GiftBagRuleContainerImpl extends HashMapContainer<String, GiftBagRule> implements IGiftBagRuleContainer {
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private RspFarmFactory rsp;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Override
    public RewardVO openGiftBag(Player player,PlayerGiftGag playerGiftGag, String giftBagid) throws AlertException {
        RewardVO rewardVO  =new RewardVO();

        GiftBagRule giftBagRule = getElement(giftBagid);
        /**
         * 背包容量的判断
         */
        if(baseItemRulecontainer.checkFarmPackgeByCell(player.getId(),giftBagRule.getItemDataList())){
            exceptionFactory.throwAlertException("领取失败，仓库至少要预留"+baseItemRulecontainer.getCapacity(giftBagRule.getItemDataList())+"个容量");
        }
        for (ItemData itemData : giftBagRule.getItemDataList()) {
            baseItemRulecontainer.addGoods(player, itemData.getItemId(), itemData.getNum());
        }
        for (ItemData itemData : giftBagRule.getItemDataList()) {
            if(itemData.getItemId().equals(Constant.GAME_MONEY_ID)){
                rewardVO.setGold(itemData.getNum());
                continue;
            }
            rewardVO.addGoodsVO(rsp.getGoodsVo(itemData.getItemId(), itemData.getNum()));
        }
        playerGiftGag.getHistoryGet().put(giftBagid,DateUtil.getDateFormat(new Date(),null));
        return rewardVO;

    }
}
