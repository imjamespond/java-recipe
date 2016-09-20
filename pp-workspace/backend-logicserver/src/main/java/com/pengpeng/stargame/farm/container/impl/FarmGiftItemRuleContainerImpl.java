package com.pengpeng.stargame.farm.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.farm.container.IFarmGiftItemRuleContainer;
import com.pengpeng.stargame.gift.container.IGiftItemRuleContainer;
import com.pengpeng.stargame.player.rule.BaseGiftRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-3下午8:10
 */
@Component
public class FarmGiftItemRuleContainerImpl extends HashMapContainer<String,BaseGiftRule> implements IFarmGiftItemRuleContainer {

    @Autowired
    private IGiftItemRuleContainer giftRuleContainer;

    @Override
    public void addElement(BaseGiftRule element) {
        super.addElement(element);
        giftRuleContainer.addElement(element);
    }

    @Override
    public void addElement(Collection<BaseGiftRule> colls) {
        super.addElement(colls);
        giftRuleContainer.addElement(colls);
    }

    @Override
    public void removeElement(BaseGiftRule element) {
        super.removeElement(element);
        giftRuleContainer.removeElement(element);
    }

    @Override
    public void removeElement(String s) {
        super.removeElement(s);
        giftRuleContainer.removeElement(s);
    }
}
