package com.pengpeng.stargame.fashion.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.fashion.container.IFashionGiftItemRuleContainer;
import com.pengpeng.stargame.fashion.rule.FashionGiftItemRule;
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
public class FashionGiftItemRuleContainerImpl extends HashMapContainer<String,BaseGiftRule> implements IFashionGiftItemRuleContainer {

    @Autowired
    private IGiftItemRuleContainer giftItemRuleContainer;

    @Override
    public void addElement(BaseGiftRule element) {
        super.addElement(element);
        giftItemRuleContainer.addElement(element);
    }

    @Override
    public void addElement(Collection<BaseGiftRule> colls) {
        super.addElement(colls);
        giftItemRuleContainer.addElement(colls);
    }

    @Override
    public void removeElement(BaseGiftRule element) {
        super.removeElement(element);
        giftItemRuleContainer.removeElement(element);
    }

    @Override
    public void removeElement(String s) {
        super.removeElement(s);
        giftItemRuleContainer.removeElement(s);
    }
}
