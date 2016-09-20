package com.pengpeng.stargame.container.farm.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.container.farm.IFarmItemRuleContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.Player;
import com.pengpeng.stargame.rule.farm.FarmItemRule;
import org.springframework.stereotype.Component;


/**
 * 农场物品规则容器
 */
@Component
public class FarmItemRuleContainerImpl extends HashMapContainer<String, FarmItemRule> implements IFarmItemRuleContainer {

    @Override
    public void buy(Player player, String id, int num) throws GameException {
        FarmItemRule rule = this.getElement(id);
    }

    @Override
    public void sale(Player player, String id, int num) throws GameException {
        FarmItemRule rule = this.getElement(id);
    }

    @Override
    public void checkBuy(Player player, String id, int num) throws GameException {
        FarmItemRule rule = this.getElement(id);
    }

    @Override
    public void checkSale(Player player, String id, int num) throws GameException {
        FarmItemRule rule = this.getElement(id);
    }

    @Override
    public void use(Player player, String id,int num) throws GameException {
        FarmItemRule rule = this.getElement(id);
    }

    @Override
    public void checkUse(Player player, String id,int num) throws GameException {
        FarmItemRule rule = this.getElement(id);
    }
}
