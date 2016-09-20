package com.pengpeng.stargame.room.container.impl;

import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.room.DecoratePosition;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.room.container.IRoomItemRuleContainer;
import com.pengpeng.stargame.room.rule.RoomItemRule;
import com.pengpeng.stargame.util.Uid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-22下午5:19
 */
@Component
public class RoomItemRuleContainerImpl extends HashMapContainer<String,RoomItemRule> implements IRoomItemRuleContainer {
    @Autowired
    private GameLoggerWrite gameLoggerWrite;

    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer ;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Autowired
    private IExceptionFactory exceptionFactory;
    private RoomItemRule getRule(String itemId) throws RuleException {
        RoomItemRule rule = this.getElement(itemId);
        if (rule==null){
            exceptionFactory.throwRuleException("room.rule.notfound");
        }
        return rule;
    }

    @Override
    public void decorate(RoomPlayer roomPlayer, RoomPackege pkg, String itemId,String position) throws RuleException, AlertException {
//        if (!pkg.hasItem(itemId)){//包裹里没有找到此物品
//            exceptionFactory.throwRuleException("roompkg.item.notfound");
//        }
        pkg.useItem(itemId);
        String id = Uid.uuid();
        roomPlayer.add(new DecoratePosition(id,itemId,position));
    }

    @Override
    public void removeDecorate(RoomPlayer roomPlayer, RoomPackege pkg, String id) throws RuleException, AlertException {
        if (!roomPlayer.hasDecorate(id)){//房间里没有此物品
            exceptionFactory.throwRuleException("room.item.notfound");
        }
        DecoratePosition item = roomPlayer.getItem(id);
        roomPlayer.remove(id);
        pkg.addItem(item.getItemId(),1);
    }

    @Override
    public void checkBuy(Player player,String itemId, int num) throws RuleException, AlertException {
        RoomItemRule rule = getRule(itemId);
        rule.checkMoney(player, num);
    }

    @Override
    public void checkSale(String itemId) throws RuleException, AlertException {
        RoomItemRule rule = getRule(itemId);
        rule.checkSales();
    }

    @Override
    public void buy(Player player,RoomPlayer roomPlayer,RoomPackege pkg, String itemId, int num) throws RuleException, AlertException {
        RoomItemRule rule = getRule(itemId);
        rule.checkBuy();
        rule.checkMoney(player, num);
//        rule.consumerBuy(player);
//          player.decGameCoin(this.getGamePrice());
//        player.decGoldCoin(this.getGoldPrice());
        playerRuleContainer.decGoldCoin(player,rule.getGoldPrice(),PlayerConstant.GOLD_ACTION_14);
        playerRuleContainer.incGameCoin(player,rule.getGamePrice());
        rule.effectBuy(pkg, num);
    }

    @Override
    public void sale(Player player,RoomPlayer roomPlayer,RoomPackege pkg, String itemId, int num) throws RuleException {
        RoomItemRule rule = getRule(itemId);
        //1.检查
        boolean sale = rule.checkSale(pkg,num);
        if(!sale){
            exceptionFactory.throwRuleException("roompkg.item.notfound");
        }
//        rule.effectSale(player,num);
        //        int coin = this.getRecyclingPrice() * num;
//        player.incGameCoin(coin);
        playerRuleContainer.incGameCoin(player,rule.getRecyclingPrice()*num);
        for(int i=0;i<num;i++){
            pkg.useItem(itemId);
        }
    }

    @Override
    public void saleByRoom(Player player, RoomPlayer roomPlayer, String id) throws RuleException, AlertException {
        DecoratePosition dp = roomPlayer.getItem(id);
        if (dp==null){
            exceptionFactory.throwAlertException("room.item.notfound");
        }
        checkSale(dp.getItemId());
        RoomItemRule rule = this.getRule(dp.getItemId());
//        rule.effectSale(player,1);
        playerRuleContainer.incGameCoin(player,rule.getRecyclingPrice());
        roomPlayer.remove(id);
        BaseItemRule baseItemRule = baseItemRulecontainer.getElement(dp.getItemId());
        //日志
        String value = dp.getItemId() + GameLogger.SPLIT + String.valueOf(1)+ GameLogger.SPLIT + baseItemRule.getType()+GameLogger.SPLIT + baseItemRule.getItemtype();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_16, player.getId(), value));

    }

    @Override
    public void checkBuy(List<String> itemIds, Player player) throws RuleException, AlertException {
        int gold = 0;
        int game = 0;
        for(String itemId:itemIds){
            RoomItemRule rule = this.getRule(itemId);
            rule.checkBuy();
            gold+=rule.getGoldPrice();
            game+=rule.getGamePrice();
        }
        if (player.getGameCoin()<game|| player.getGoldCoin()<gold){
            //达人币或游戏币不足
            exceptionFactory.throwRuleException("room.coin.notenough");
        }
    }

    @Override
    public void consumerBuy(List<String> itemIds, Player player,RoomPackege roomPackege) throws RuleException, AlertException {
        checkBuy(itemIds, player);
        int gold = 0;
        int game = 0;
        for(String itemId:itemIds){
            RoomItemRule rule = this.getRule(itemId);
            gold+=rule.getGoldPrice();
            game+=rule.getGamePrice();
            roomPackege.addItem(itemId,1);

            BaseItemRule baseItemRule=  baseItemRulecontainer.getElement(itemId);
            //日志
            String value=itemId+ GameLogger.SPLIT+String.valueOf(1)+ GameLogger.SPLIT + baseItemRule.getType()+GameLogger.SPLIT + baseItemRule.getItemtype();
            gameLoggerWrite.write(new GameLogger(GameLogger.LOG_15, player.getId(), value));
        }
//        player.decGameCoin(game);
//        player.decGoldCoin(gold);
        playerRuleContainer.decGameCoin(player,game);
        playerRuleContainer.decGoldCoin(player,gold, PlayerConstant.GOLD_ACTION_14);


    }

    @Override
    public int getLuxuryValue(RoomPlayer roomPlayer) {
        int luxuryDegree=0;
        for(DecoratePosition decoratePosition:roomPlayer.getDecoratePositionList()){
            RoomItemRule roomItemRule=getElement(decoratePosition.getItemId());
            if (roomItemRule!=null){
                luxuryDegree+=roomItemRule.getLuxuryDegree();
            }
        }
        return luxuryDegree;
    }
}
