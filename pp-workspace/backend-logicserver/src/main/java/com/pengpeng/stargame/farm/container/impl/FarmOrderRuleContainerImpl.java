package com.pengpeng.stargame.farm.container.impl;

import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.container.IFarmOrderRuleContainer;
import com.pengpeng.stargame.farm.container.IFarmRuleContainer;
import com.pengpeng.stargame.farm.rule.FarmOrderRule;
import com.pengpeng.stargame.farm.rule.OrderGoods;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.farm.FarmOrder;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.farm.OneOrder;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.rpc.BroadcastHolder;
import com.pengpeng.stargame.rsp.RspFarmFactory;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.util.Uid;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vo.RewardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-3-7下午6:26
 */
@Component
public class FarmOrderRuleContainerImpl extends HashMapContainer<String, FarmOrderRule> implements IFarmOrderRuleContainer {
    private final static int ORDER_NUM = 5; //订单的数量
    private final static int DEC_GOLD = 5;//快速货运 扣除的 达人币数量
    public  static int ADD_P=50;//快速货运添加的奖励百分比
    private final static int MUL = 600;//刷新订单扣除的 游戏币 是农场的多少倍
    @Autowired
    private IFarmRuleContainer farmRuleContainer;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private RspFarmFactory rsp;
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    public Date getOrderNextTime() {
        return DateUtil.addSecond(new Date(), 2);
    }

    @Override
    public boolean getFarmOrder(FarmOrder farmOrder, long time, int farmLevel) {

        if (farmOrder.getNextRefreshNumTime() == null || time > farmOrder.getNextRefreshNumTime().getTime()) {
            farmOrder.setFinishNum(0);
            farmOrder.setNextRefreshNumTime(DateUtil.getNextCountTime());
            List<OneOrder> oneOrders = getOneOrders(farmLevel);
            farmOrder.refresh(oneOrders);
            return true;
        }
//        if (farmOrder.getOrderList().size()<ORDER_NUM&&time > farmOrder.getNextTime().getTime()) {
        if (farmOrder.getOrderList().size() < ORDER_NUM) {
            getOneOrder(farmLevel, farmOrder);
            farmOrder.setNextTime(getOrderNextTime());
            return true;
        }
        return false;
    }

    @Override
    public void checkFresh(Player player, int farmLevel) throws AlertException {
        int gamecoin = farmLevel * MUL;
        if (player.getGameCoin() < gamecoin) {
            exceptionFactory.throwAlertException("gamecoin.notenough");
        }
    }

    @Override
    public List<OneOrder> getOneOrders(int farmLevel) {
        List<FarmOrderRule> myFarmOrders = new ArrayList<FarmOrderRule>();
        Collection<FarmOrderRule> c = this.items.values();
        Iterator it = c.iterator();
        while (it.hasNext()) {
            FarmOrderRule farmOrderRule = (FarmOrderRule) it.next();
            if (farmOrderRule.getFarmLevel() <= farmLevel) {
                myFarmOrders.add(farmOrderRule);
            }
        }

        List<OneOrder> oneOrders = new ArrayList<OneOrder>();

        if (myFarmOrders.size() < ORDER_NUM) {
            for (FarmOrderRule farmOrderRule : myFarmOrders) {
                OneOrder oneOrder = new OneOrder(Uid.uuid(), farmOrderRule.getId());
                oneOrders.add(oneOrder);
            }

        } else {
            List<String> tempIds = new ArrayList<String>();
            while (oneOrders.size() < ORDER_NUM) {
                int index = new Random().nextInt(myFarmOrders.size());
                FarmOrderRule farmOrderRule = myFarmOrders.get(index);
                if (!tempIds.contains(farmOrderRule.getId())) {
                    OneOrder oneOrder = new OneOrder(Uid.uuid(), farmOrderRule.getId());
                    oneOrders.add(oneOrder);
                    tempIds.add(farmOrderRule.getId());
                }

            }

        }

        return oneOrders;

    }

    @Override
    public void getOneOrder(int farmLevel, FarmOrder farmOrder) {

        List<OneOrder> orderList = farmOrder.getOrderList();
        if (orderList.size() >= ORDER_NUM) {
            return;
        }

        List<FarmOrderRule> myFarmOrders = new ArrayList<FarmOrderRule>();
        Collection<FarmOrderRule> c = this.items.values();
        Iterator it = c.iterator();
        while (it.hasNext()) {
            FarmOrderRule farmOrderRule = (FarmOrderRule) it.next();
            if (farmOrderRule.getFarmLevel() <= farmLevel) {
                myFarmOrders.add(farmOrderRule);
            }
        }
        List<String> tempIds = new ArrayList<String>();
        for (OneOrder oneOrder : orderList) {
            tempIds.add(oneOrder.getOrderId());
        }
        if (tempIds.size() >= myFarmOrders.size()) {
            return;
        }
        while (true) {
            int index = new Random().nextInt(myFarmOrders.size());
            FarmOrderRule farmOrderRule = myFarmOrders.get(index);
            if (!tempIds.contains(farmOrderRule.getId())) {
                OneOrder oneOrder = new OneOrder(Uid.uuid(), farmOrderRule.getId());
                orderList.add(oneOrder);
                tempIds.add(farmOrderRule.getId());
                break;
            }
        }
    }

    @Override
    public void refresh(Player player, FarmOrder farmOrder, int farmLevel) {

//        player.decGameCoin(farmLevel * MUL);
        playerRuleContainer.decGameCoin(player,farmLevel*MUL);

        List<OneOrder> oneOrders = getOneOrders(farmLevel);
        farmOrder.refresh(oneOrders);
        farmOrder.setNextTime(getOrderNextTime());

        //日志
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_13, player.getId(), String.valueOf(farmLevel * MUL)));
    }

    @Override
    public void checkFinishOrder(Player player, FarmPackage farmPackage, String orderId, int type) throws AlertException {

        FarmOrderRule farmOrderRule = getElement(orderId);
        farmOrderRule.checkFinishOrder(farmPackage);
        if (type == 2) {
            if (player.getGoldCoin() < DEC_GOLD) {
                exceptionFactory.throwAlertException("goldcoin.notenough");
            }
        }
    }

    @Override
    public void finishOrder(FarmPlayer farmPlaye, Player player, FarmPackage farmPackage, FarmOrder farmOrder, String orderUId, int type) {
        OneOrder oneOrder = farmOrder.getOneOrder(orderUId);
        FarmOrderRule farmOrderRule = getElement(oneOrder.getOrderId());
        int gamemoney = 0;
        int exp = 0;
        double addP=((double)100+ADD_P)/100;
        if (type == 1) {
            exp = farmOrderRule.getExpReward();
            gamemoney = farmOrderRule.getCurrencyReward();
        }
        if (type == 2) {
//            player.decGoldCoin(DEC_GOLD);
            playerRuleContainer.decGoldCoin(player,DEC_GOLD, PlayerConstant.GOLD_ACTION_4);
            exp = (int) (farmOrderRule.getExpReward() * addP);
            gamemoney = ((int) (farmOrderRule.getCurrencyReward() * addP));
        }
        farmLevelRuleContainer.addFarmExp(farmPlaye,exp);
//        player.incGameCoin(gamemoney);
        playerRuleContainer.incGameCoin(player,gamemoney);
        RewardVO rewardVO = rsp.getRewardVO(9);
        rewardVO.setGold(gamemoney);
        rewardVO.setFarmExp(exp);
        BroadcastHolder.add(rewardVO);

        farmOrder.deleteOneOrder(oneOrder);
        for (OrderGoods orderGoods : farmOrderRule.getOrderGoodsList()) {
            farmPackage.deduct(orderGoods.getItemId(), orderGoods.getNum());
        }
        farmOrder.setFinishNum(farmOrder.getFinishNum() + 1);

        //日志
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_14, player.getId(), farmOrderRule.getId()));
    }

    @Override
    public int getMaxOrderNum(String pid) {
        int maxNum= FarmConstant.ORDER_ALL_NUM;
        if(payMemberRuleContainer.isPayMember(pid)){
            maxNum=payMemberRuleContainer.getOrderMaxNum(pid);
        }
        return maxNum;
    }
}
