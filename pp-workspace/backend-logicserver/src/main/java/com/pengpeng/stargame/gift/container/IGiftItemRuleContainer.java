package com.pengpeng.stargame.gift.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.player.rule.BaseGiftRule;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-26下午12:23
 */
public interface IGiftItemRuleContainer extends IMapContainer<String,BaseGiftRule> {

    public List<BaseGiftRule> getByPresentType(int type);
}
