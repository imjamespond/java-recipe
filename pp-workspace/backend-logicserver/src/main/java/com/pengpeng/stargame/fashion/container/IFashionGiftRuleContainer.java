package com.pengpeng.stargame.fashion.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.fashion.rule.FashionGiftRule;
import com.pengpeng.stargame.model.room.FashionPkg;
import com.pengpeng.stargame.model.GiftPlayer;
import com.pengpeng.stargame.model.player.Player;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-3下午8:11
 */
public interface IFashionGiftRuleContainer extends IMapContainer<String,FashionGiftRule> {

    /**
     * 检查是否可以赠送礼物
     * @param num
     * @throws com.pengpeng.stargame.exception.RuleException
     */
    public void checkGive(int num) throws RuleException;

    /**
     * 赠送礼物
     * @param player
     * @throws com.pengpeng.stargame.exception.RuleException
     */
    public void give(Player player, GiftPlayer giftPlayer, String itemId) throws RuleException;
    public void give2(Player player, GiftPlayer giftPlayer, String itemId,int num, String msg) throws AlertException;
    public void qinMaGive(String pid,String itemId);

    /**
     * 接受礼物
     * @param fp
     * @param player
     * @throws com.pengpeng.stargame.exception.RuleException
     */
    public void accept(FashionPkg fp, GiftPlayer player, String fid) throws RuleException;

    /**
     * 拒绝或忽略礼物
     * @param player
     * @throws com.pengpeng.stargame.exception.RuleException
     */
    public void reject(GiftPlayer player, String fid) throws RuleException;


}
