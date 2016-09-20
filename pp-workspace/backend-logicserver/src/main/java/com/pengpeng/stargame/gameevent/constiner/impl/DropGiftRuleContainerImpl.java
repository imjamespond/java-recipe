package com.pengpeng.stargame.gameevent.constiner.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.gameevent.constiner.IDropGfitRuleContainer;
import com.pengpeng.stargame.gameevent.rule.DropGiftRule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-12-20
 * Time: 上午10:40
 */
@Component
public class DropGiftRuleContainerImpl extends HashMapContainer<String, DropGiftRule> implements IDropGfitRuleContainer {
    @Override
    public List<DropGiftRule> getByType(int type) {
        List<DropGiftRule> dropGiftRuleList=new ArrayList<DropGiftRule>();
        for(DropGiftRule dropGiftRule:items.values()){
            if(dropGiftRule.getType()==type){
                dropGiftRuleList.add(dropGiftRule);
            }
        }
        return dropGiftRuleList;
    }
}
