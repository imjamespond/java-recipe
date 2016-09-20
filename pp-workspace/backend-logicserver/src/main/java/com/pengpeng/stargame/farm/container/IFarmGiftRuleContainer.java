package com.pengpeng.stargame.farm.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.rule.FarmGiftRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.GiftPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-3下午8:11
 */
public interface IFarmGiftRuleContainer extends IMapContainer<String,FarmGiftRule> {

    /**
     * 检查是否可以赠送礼物
     *
     *
     * @param num
     * @throws com.pengpeng.stargame.exception.RuleException
     */
    public void checkGive(String pid,int num) throws RuleException;

    /**
     * 赠送礼物
     * @param player
     * @throws com.pengpeng.stargame.exception.RuleException
     */
    public void give(GiftPlayer myGiftPlayer, GiftPlayer giftPlayer, String itemId) throws RuleException;

    /**
     * 接受礼物
     * @param fp
     * @param player
     * @throws com.pengpeng.stargame.exception.RuleException
     */
    public void accept(FarmPackage fp, GiftPlayer player, String fid) throws RuleException;

    /**
     * 拒绝或忽略礼物
     * @param player
     * @throws com.pengpeng.stargame.exception.RuleException
     */
    public void reject(GiftPlayer player, String fid) throws RuleException;

    /**
     * 获取最大送礼物次数
     * @param pid
     * @return
     */
    public  int getMaxSendNum(String pid);

    void acceptOne(GiftPlayer giftPlayer, FarmPackage farmPackage, FashionCupboard fashionCupboard, int order) throws AlertException;

}
