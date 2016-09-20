package com.pengpeng.stargame.giftbag.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.giftbag.rule.GiftBagRule;
import com.pengpeng.stargame.model.giftBag.PlayerGiftGag;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.vo.RewardVO;

/**
 * User: mql
 * Date: 13-12-10
 * Time: 上午11:43
 */
public interface IGiftBagRuleContainer extends IMapContainer<String,GiftBagRule> {


    public RewardVO openGiftBag(Player player,PlayerGiftGag playerGiftGag,String giftBagid) throws AlertException;

}
