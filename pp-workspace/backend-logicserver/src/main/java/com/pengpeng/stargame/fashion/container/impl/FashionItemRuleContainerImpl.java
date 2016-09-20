package com.pengpeng.stargame.fashion.container.impl;

import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.fashion.container.IFashionItemRuleContainer;
import com.pengpeng.stargame.fashion.rule.FashionItemRule;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.FashionItem;
import com.pengpeng.stargame.model.room.FashionPkg;
import com.pengpeng.stargame.model.room.FashionPlayer;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.rpc.BroadcastHolder;
import com.pengpeng.stargame.rsp.RspFashionPkgFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:30
 */
@Component
public class FashionItemRuleContainerImpl extends HashMapContainer<String, FashionItemRule> implements IFashionItemRuleContainer {
    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private RspFashionPkgFactory factory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer ;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Override
    public List<FashionItemRule> getAll() {
        return new ArrayList<FashionItemRule>(items.values());
    }

    @Override
    public void checkChange(FashionPlayer fashionPlayer, FashionCupboard fashionCupboard, String itemId, String id) throws AlertException {
        FashionItemRule fashionItemRule = this.getElement(itemId);
        FashionPkg fashionPkg = fashionCupboard.getFashionPkg(String.valueOf(fashionItemRule.getItemtype()));
        FashionItem fashionItem=(FashionItem)fashionPkg.getItemByGrid(id);

        if (!fashionPkg.existByGrid(id)) {
            exceptionFactory.throwAlertException("不存在物品！");
        }
        if(!fashionItem.getItemId().equals(itemId)){
            exceptionFactory.throwAlertException("请求出错！");
        }
        if(fashionItem.getStatus()==1){
            exceptionFactory.throwAlertException("此服装已经是穿戴状态！");
        }

    }

    @Override
    public void change(FashionPlayer fashionPlayer, FashionCupboard fashionCupboard, String itemId, String id) throws AlertException {
        FashionItemRule fashionItemRule = this.getElement(itemId);
        String type = String.valueOf(fashionItemRule.getItemtype());
        /**
         * 换装
         * 1.换下同类型的 服装 ，如果是 套装 先换 套装 ，在换下上装 或者 下装
         *
         * 2.如果换的是上装 或者 下装 先换掉 套装
         */
        /**
         * 卸掉 服装
         */
        FashionPkg fashionPkg = fashionCupboard.getFashionPkg(type);
        FashionItem fashionItem = (FashionItem) fashionPkg.getItemByGrid(fashionPlayer.getFashion(type));
        if(fashionItem!=null){ //如果有穿的 服装，卸掉
            fashionPkg.updateStatus(fashionPlayer.getFashion(type));
            fashionPlayer.removeFashion(type);
        }


        /**
         * 穿上 新的 服装
         */
        fashionPlayer.setFashion(type, id);
        fashionPkg.updateStatus(id);

        /**
         *
         * 3时装道具
         1)发部
         2)脸部
         3)上装
         4)下装
         5)套装
         6)配饰
         7)背饰
         8)脚底

         */
        /**
         *
         * 如果是上装   或者 下装     需要卸掉套装
         */
        if (type.equals("3") || type.equals("4")) {
            if (fashionPlayer.hasFashion("5")) {
                FashionPkg fashionPkgtemp = fashionCupboard.getFashionPkg("5");
                FashionItem fashionItemtemp = (FashionItem) fashionPkgtemp.getItemByGrid(fashionPlayer.getFashion("5"));
                if (fashionItemtemp.getStatus() == 1) {
                    fashionPkgtemp.updateStatus(fashionPlayer.getFashion("5"));
                    fashionPlayer.removeFashion("5") ;
                }
            }
            FashionPkg fashionPkg5 = fashionCupboard.getFashionPkg("5");
            for(FashionItem fashionItem5:fashionPkg.getItemAll()){
                if(fashionItem5.getStatus()==1){
                    fashionPkg5.updateStatus(fashionItem5.getGrid());
                }
            }
            BroadcastHolder.add(factory.getFashionPkg(fashionPkg5,"5"));

        }
        /**
         * 如果是套装
         */
        else if (type.equals("5")) {
            if (fashionPlayer.hasFashion("3")) {
                FashionPkg fashionPkgtemp = fashionCupboard.getFashionPkg("3");
                FashionItem fashionItemtemp = (FashionItem) fashionPkgtemp.getItemByGrid(fashionPlayer.getFashion("3"));
                if (fashionItemtemp.getStatus() == 1) {
                    fashionPkgtemp.updateStatus(fashionPlayer.getFashion("3"));
                    fashionPlayer.removeFashion("3"); ;
                }
            }
            if (fashionPlayer.hasFashion("4")) {
                FashionPkg fashionPkgtemp = fashionCupboard.getFashionPkg("4");
                FashionItem fashionItemtemp = fashionPkgtemp.getItemByGrid(fashionPlayer.getFashion("4"));
                if (fashionItemtemp.getStatus() == 1) {
                    fashionPkgtemp.updateStatus(fashionPlayer.getFashion("4"));
                    fashionPlayer.removeFashion("4"); ;
                }
            }
            FashionPkg fashionPkg3 = fashionCupboard.getFashionPkg("3");
            for(FashionItem fashionItem3:fashionPkg.getItemAll()){
                if(fashionItem3.getStatus()==1){
                    fashionPkg3.updateStatus(fashionItem3.getGrid());
                }
            }
            FashionPkg fashionPkg4 = fashionCupboard.getFashionPkg("4");
            for(FashionItem fashionItem4:fashionPkg.getItemAll()){
                if(fashionItem4.getStatus()==1){
                    fashionPkg4.updateStatus(fashionItem4.getGrid());
                }
            }
            BroadcastHolder.add(factory.getFashionPkg(fashionPkg3,"3"));
            BroadcastHolder.add(factory.getFashionPkg(fashionPkg4,"4"));

        }

    }

    @Override
    public void takeOff(FashionPlayer fashionPlayer, FashionCupboard fashionCupboard, String id, String type) throws AlertException {
        FashionPkg fashionPkg = fashionCupboard.getFashionPkg(type);
        FashionItem fashionItem = (FashionItem) fashionPkg.getItemByGrid(fashionPlayer.getFashion(type));
        if(null==fashionItem){
            exceptionFactory.throwAlertException("服装已经被脱掉了");
        }
        if (fashionItem.getStatus() == 1) {
            fashionPkg.updateStatus(id);
            fashionPlayer.removeFashion(type);
        }

    }

    @Override
    public void takeOffAll(FashionPlayer fashionPlayer, FashionCupboard fashionCupboard) {
        List<String> types=new ArrayList<String>();
        for (String type : fashionPlayer.getFashionKeys()) {
            FashionPkg fashionPkg = fashionCupboard.getFashionPkg(type);
            FashionItem fashionItem = fashionPkg.getItemByGrid(fashionPlayer.getFashion(type));
            types.add(type);
            if (fashionItem!=null&&fashionItem.getStatus() == 1) {
                fashionPkg.updateStatus(fashionPlayer.getFashion(type));
            }
        }
        for(String type:types){
            fashionPlayer.removeFashion(type);
        }

        for(int i=1;i<9;i++){
            String type =String.valueOf(i);
            FashionPkg fashionPkg = fashionCupboard.getFashionPkg(type);
            for(FashionItem fashionItem:fashionPkg.getItemAll()){
                if(fashionItem.getStatus()==1){
                    fashionPkg.updateStatus(fashionItem.getGrid());
                }
            }
        }

    }

    @Override
    public void checkRandomFromPkg(FashionPlayer fashionPlayer, FashionCupboard fashionCupboard) throws AlertException {
        int num = 0;
        for (String key : fashionCupboard.getFashionPkgMap().keySet()) {
            FashionPkg fashionPkg = fashionCupboard.getFashionPkg(key);
            num += fashionPkg.consumeByItemSize();
        }
        if (num < 10) {
            exceptionFactory.throwAlertException("fashion.not.RandomFromPkg");
        }
    }

    @Override
    public void randomFromPkg(FashionPlayer fashionPlayer, FashionCupboard fashionCupboard) {
        this.takeOffAll(fashionPlayer, fashionCupboard);

        /**
         * 3时装道具
         1)发部
         2)脸部
         3)上装
         4)下装
         5)套装
         6)配饰
         7)背饰
         8)脚底

         */
        String[] types = {"1", "2", "3", "4", "6", "7", "8"};
        for (String type : types) {
            FashionPkg fashionPkg = fashionCupboard.getFashionPkg(type);
            List list = fashionPkg.getItemAll();
            if (list.size() > 0) {
                int index = new Random().nextInt(list.size());
                FashionItem fashionItem = (FashionItem) list.get(index);
                fashionPkg.updateStatus(fashionItem.getGrid());
                fashionPlayer.setFashion(type, fashionItem.getGrid());
            }

        }
        /**
         * 如果 没有上装 或者 下装  ,那么 穿套装
         */
        if(!fashionPlayer.hasFashion("3")&&!fashionPlayer.hasFashion("4")){
            FashionPkg fashionPkg = fashionCupboard.getFashionPkg("5");
            List list = fashionPkg.getItemAll();
            if (list.size() > 0) {
                int index = new Random().nextInt(list.size());
                FashionItem fashionItem = (FashionItem) list.get(index);
                fashionPkg.updateStatus(fashionItem.getGrid());
                fashionPlayer.setFashion("5", fashionItem.getGrid());
            }
        }

    }

    @Override
    public void checkBuy(FashionCupboard fashionCupboard, Player player, String[] itemIds) throws AlertException {

        int game = 0;
        int gold = 0;
        Map<String, Integer> grids = new HashMap<String, Integer>();
        for (String itemId : itemIds) {
            FashionItemRule fashionItemRule = getElement(itemId);
            String type=  String.valueOf(fashionItemRule.getItemtype());

            fashionItemRule.checkBuy();
            game+=fashionItemRule.getGamePrice();
            gold+=fashionItemRule.getGoldPrice();
            if(grids.containsKey(type)){
                 grids.put(type,grids.get(type)+1);
            }else {
                grids.put(type,1);
            }
        }

        if (player.getGameCoin()<game){
            //游戏币不足
            exceptionFactory.throwAlertException("gamecoin.notenough");
        }
        if (player.getGoldCoin()<gold){
            //达人币不足
            exceptionFactory.throwAlertException("goldcoin.notenough");
        }

        for(String type:grids.keySet()){
            FashionPkg fashionPkg = fashionCupboard.getFashionPkg(type);
            if(fashionPkg.consumeByItemSize()+grids.get(type)>fashionPkg.getSize()){
                exceptionFactory.throwAlertException("fashion.max");
            }
        }



    }


    @Override
    public void checkSale(FashionCupboard fashionCupboard, String itemId,int num,String id) throws AlertException{
        FashionItemRule fashionItemRule = getElement(itemId);
        fashionItemRule.checkSales();
        FashionPkg fashionPkg=fashionCupboard.getFashionPkg(String.valueOf(fashionItemRule.getItemtype()));
        FashionItem fashionItem= (FashionItem) fashionPkg.getItemByGrid(id);
        if(fashionItem==null||fashionItem.getNum()<num){
            exceptionFactory.throwAlertException("非法请求");
        }

    }

    @Override
    public void buy(FashionCupboard fashionCupboard, Player player,  String[] itemIds) {
        int game = 0;
        int gold = 0;
        for (String itemId : itemIds) {
            FashionItemRule fashionItemRule = getElement(itemId);
            String type=  String.valueOf(fashionItemRule.getItemtype());
            game+=fashionItemRule.getGamePrice();
            gold+=fashionItemRule.getGoldPrice();
            /**
             * 获取 指定类型的 背包
             */
            FashionPkg fashionPkg = fashionCupboard.getFashionPkg(type);
            /**
             * 添加物品
             */
            BaseItemRule baseItemRule=  baseItemRulecontainer.getElement(itemId);
            fashionPkg.addItem(itemId,1,baseItemRule.getOverlay(),baseItemRule.getValidDete());
            //日志
            String value=itemId+ GameLogger.SPLIT+String.valueOf(1)+ GameLogger.SPLIT + baseItemRule.getType()+GameLogger.SPLIT + baseItemRule.getItemtype();
            gameLoggerWrite.write(new GameLogger(GameLogger.LOG_15, player.getId(), value));

        }
        /**
         * 扣钱
         */
//        player.decGameCoin(game);
//        player.decGoldCoin(gold);

        playerRuleContainer.decGameCoin(player,game);
        playerRuleContainer.decGoldCoin(player,gold, PlayerConstant.GOLD_ACTION_9);


    }

    @Override
    public void sale(FashionCupboard fashionCupboard, Player player, String itemId, int num,String id) {
        FashionItemRule fashionItemRule = getElement(itemId);
        FashionPkg fashionPkg=fashionCupboard.getFashionPkg(String.valueOf(fashionItemRule.getItemtype()));
        fashionPkg.deductById(id,num);
//        fashionItemRule.addGameMoney(player,num);
        playerRuleContainer.incGameCoin(player,fashionItemRule.getRecyclingPrice()*num);

    }

    @Override
    public int getFasionValue(FashionPlayer fashionPlayer, FashionCupboard fashionCupboard) {
        int fashionValue=0 ;
        for(String key:fashionPlayer.getFashionKeys()){

            FashionPkg fashionPkg=fashionCupboard.getFashionPkg(key);
            FashionItem fashionItem= fashionPkg.getItemByGrid(fashionPlayer.getFashion(key));
            if(fashionItem==null){
                continue;
            }
            FashionItemRule fashionItemRule=getElement(fashionItem.getItemId()) ;
            fashionValue+=fashionItemRule.getFashionIndex();

        }
        return fashionValue;
    }
}
