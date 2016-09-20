package com.pengpeng.stargame.player.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.player.container.ITransferRuleContainer;
import com.pengpeng.stargame.player.rule.TransferRule;
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

    @Override
    public TransferRule getEnterTransfer(String mapId) {
        Collection<TransferRule> colls = items.values();
        for(TransferRule rule:colls){
            if (rule.getTargetId().equals(mapId)){
                return rule;
            }
        }
        return null;
    }
}
