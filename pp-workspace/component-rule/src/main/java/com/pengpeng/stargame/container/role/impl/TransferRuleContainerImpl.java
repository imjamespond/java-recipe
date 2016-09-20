package com.pengpeng.stargame.container.role.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.container.role.ITransferRuleContainer;
import com.pengpeng.stargame.rule.role.TransferRule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-1-16下午12:26
 */
@Component
public class TransferRuleContainerImpl extends HashMapContainer<String,TransferRule> implements ITransferRuleContainer {
    @Override
    public List<TransferRule> getTransferByScene(String mapId) {
        List<TransferRule> rules = new ArrayList<TransferRule>();
        if (null==mapId){
            return rules;
        }
        Collection<TransferRule> colls = items.values();
        for(TransferRule rule:colls){
            if (rule.getCurrId().equals(mapId)){
                rules.add(rule);
            }
        }
        return rules;
    }
}
