package com.pengpeng.stargame.room.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.gift.container.IGiftItemRuleContainer;
import com.pengpeng.stargame.player.rule.BaseGiftRule;
import com.pengpeng.stargame.room.container.IRoomGiftItemRuleContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 房间礼物相关的容器
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-26下午2:49
 */
@Component
public class RoomGiftItemRuleContainerImpl extends HashMapContainer<String,BaseGiftRule> implements IRoomGiftItemRuleContainer {

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
