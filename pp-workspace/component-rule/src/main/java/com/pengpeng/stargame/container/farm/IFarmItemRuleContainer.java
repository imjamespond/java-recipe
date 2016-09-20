package com.pengpeng.stargame.container.farm;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rule.farm.FarmItemRule;
import com.pengpeng.stargame.model.Player;

/**
 * 存放商品规则的容器
 * @author Administrator
 *
 */
public interface IFarmItemRuleContainer extends IMapContainer<String, FarmItemRule> {

//    public boolean canBuy(Player player,String itemId,int num);
//
//    public void consumerBuy(Player player,String itemId,int num);
//
//    public void effectBuy(Player player,String itemId,int num);
//
//    public boolean canSale(Player player,String itemId,int num);
//    public void consumerSale(Player player,String itemId,int num);
//    public void effectSale(Player player,String itemId,int num);

    public void buy(Player player, String id, int num) throws GameException;

    public void sale(Player player, String id, int num) throws GameException;

    public void checkBuy(Player player, String id, int num) throws GameException;

    public void checkSale(Player player, String id, int num) throws GameException;

    public void use(Player player, String id, int num) throws GameException;

    public void checkUse(Player player, String id, int num) throws GameException;
}
