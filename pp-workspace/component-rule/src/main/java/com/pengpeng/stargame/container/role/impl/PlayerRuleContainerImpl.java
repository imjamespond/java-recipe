package com.pengpeng.stargame.container.role.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.container.role.IPlayerRuleContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.Player;
import com.pengpeng.stargame.rule.role.PlayerRule;
import com.pengpeng.stargame.rule.IUpgradeRule;
import org.springframework.stereotype.Component;

/**
 * 玩家升级规则容器
 * @author 林佛权
 *
 */
@Component
public class PlayerRuleContainerImpl extends HashMapContainer<String, PlayerRule> implements IPlayerRuleContainer,IUpgradeRule<Player, Player, Player, String> {
    @Override
    public PlayerRule getRule(int level, int type) {
        for(Entry<String,PlayerRule> entry : items.entrySet()){
            PlayerRule rule = entry.getValue();
            if (rule.getType()==type&&rule.getLevel()==level){
                return rule;
            }
        }
        return null;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public void setKey(String key) {
        //TODO:方法需要实现
    }

    @Override
    public void checkUpgrade(Player bean) throws GameException {
        //TODO:方法需要实现
    }

    @Override
    public boolean canUpgrade(Player bean) {
        PlayerRule rule = getRule(1,bean.getType());
        if (rule==null){
            return false;
        }
        return rule.canUpgrade(bean);
    }

    @Override
    public void consumeUpgrade(Player bean) {
        PlayerRule rule = getRule(1,bean.getType());
        if (rule==null){
            return ;
        }
        rule.consumeUpgrade(bean);
    }

    @Override
    public void effectUpgrade(Player bean) {
        PlayerRule rule = getRule(1,bean.getType());
        if (rule==null){
            return ;
        }
        rule.effectUpgrade(bean);
    }
}
