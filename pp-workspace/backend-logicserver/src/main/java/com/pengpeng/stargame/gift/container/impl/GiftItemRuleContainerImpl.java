package com.pengpeng.stargame.gift.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.gift.container.IGiftItemRuleContainer;
import com.pengpeng.stargame.player.rule.BaseGiftRule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 替换原来的礼物系统,对原来的礼物系统做抽象
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-26下午12:24
 */
@Component
public class GiftItemRuleContainerImpl extends HashMapContainer<String,BaseGiftRule> implements IGiftItemRuleContainer {

    @Override
    public List<BaseGiftRule> getByPresentType(int type) {
        List<BaseGiftRule> list = new ArrayList<BaseGiftRule>();
        for(BaseGiftRule rule: items.values()){
            if (rule.getPresentType()==type){
                list.add(rule);
            }
        }
        return list;
    }
}
