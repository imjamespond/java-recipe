package com.pengpeng.stargame.stall.container.impl;

import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.stall.*;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.stall.container.IStallPriceContainer;
import com.pengpeng.stargame.stall.dao.IStallAdvertisementDao;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:15
 */

@Component
public class StallPriceContainerImpl implements IStallPriceContainer {
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IStallAdvertisementDao stallAdvertisementDao;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;

    private static Map<Integer,Integer> advPrices = new HashMap<Integer, Integer>();

    private static Map<Integer,Integer> assistantPrices = new HashMap<Integer, Integer>();  //day->gold

    private static List<BaseItemRule> randomItem = new ArrayList<BaseItemRule>();

    @Override
    public void setAdvPrice(int key, int val) {
        advPrices.put(key,val);
    }

    @Override
    public void setAssistantPrices(int key, int val) {
        assistantPrices.put(key,val);
    }

    @Override
    public void addRandomItem(BaseItemRule rule) {
        if(rule.getShelfPrice()==0)
            return;
        randomItem.add(rule);
    }


    @Override
    public void checkEnable(FarmPlayer fp, Player p) throws AlertException {
        if(fp.getLevel()<StallConstant.ENABLE_LEVEL){
            if(p.getGoldCoin()<StallConstant.ENABLE_GOLD_COIN){
                exceptionFactory.throwAlertException("gold.coin.less.than.100");
            } else{
                //p.decGoldCoin(StallConstant.ENABLE_GOLD_COIN);
                playerRuleContainer.decGoldCoin(p,StallConstant.ENABLE_GOLD_COIN, PlayerConstant.GOLD_ACTION_42);

            }
        }
        if(p.getGameCoin()<StallConstant.ENABLE_GAME_COIN){
            exceptionFactory.throwAlertException("game.coin.less.than.50w");
        }
        p.decGameCoin(StallConstant.ENABLE_GAME_COIN);
    }

    @Override
    public void checkHitShelf(String itemId, int price) throws AlertException {

        BaseItemRule rule = baseItemRulecontainer.getElement(itemId);
        if(null == rule){
            exceptionFactory.throwAlertException("invalid.item");
        }
        if(rule.getShelfPrice() == 0){
            exceptionFactory.throwAlertException("unable.hit.shelf");
        }

    }

    @Override
    public PlayerShelf hitShelf(FarmPackage fp, PlayerStall ps, String itemId, int itemNum, int price, int shelfOrder, int shelfType) throws AlertException {

        PlayerShelf shelf = ps.getPlayerShelfByOrder(shelfOrder,shelfType);
        if(null==shelf){
            exceptionFactory.throwAlertException("shelf.out.of.rang");//货架序数超出范围
        }
        //上架次数判断
        boolean isVip = payMemberRuleContainer.isPayMember(ps.getPid());
        if(isVip){
            if(ps.getHitShelfNum()>=StallConstant.HIT_SHELF_VIP_NUM){
                exceptionFactory.throwAlertException("hit.shelf.over.time");//超过次数
            }
        }else{
            if(ps.getHitShelfNum()>=StallConstant.HIT_SHELF_NUM){
                exceptionFactory.throwAlertException("hit.shelf.over.time");//超过次数
            }
        }


        int num = fp.getSumByNum(itemId);
        if(num>=itemNum){
            fp.deduct(itemId, itemNum);//扣减仓库物品

            ps.setHitShelfNum(ps.getHitShelfNum()+1);//上架次数加1
            ps.setHitShelfTime(System.currentTimeMillis());

            shelf.setItemId(itemId);
            shelf.setItemNum(itemNum);
            shelf.setItemPrice(price);
            shelf.setState(StallConstant.SHELF_ON_SALE);
            shelf.setHitShelfTime(System.currentTimeMillis());//下架时用
            shelf.setBuyerId(null);
            shelf.setBuyingTime(0l);
        }else{
            exceptionFactory.throwAlertException("farm.item.notenough");
        }

        return shelf;
    }



    @Override
    public String offShelf(FarmPackage fp, Player player, PlayerStall ps, int shelfOrder, int shelfType) throws AlertException {

        PlayerShelf shelf = ps.getPlayerShelfByOrder(shelfOrder, shelfType);
        if(null==shelf){
            exceptionFactory.throwAlertException("shelf.out.of.rang");//货架序数超出范围
        }
        if(shelf.getState() != StallConstant.SHELF_ON_SALE){
            exceptionFactory.throwAlertException("shelf.state.invalid");//必须在出售状态才可下架
        }
        if(shelf.getItemId()==null){
            exceptionFactory.throwAlertException("shelf.item.invalid");
        }
        long diff = System.currentTimeMillis() - shelf.getHitShelfTime();
        if(diff<StallConstant.OFF_SHELF_TIME){
            if(player.getGoldCoin() < StallConstant.OFF_SHELF_GOLD){
                exceptionFactory.throwAlertException("goldcoin.notenough");
            }
            //player.decGoldCoin(StallConstant.OFF_SHELF_GOLD);
            playerRuleContainer.decGoldCoin(player,StallConstant.OFF_SHELF_GOLD, PlayerConstant.GOLD_ACTION_44);
        }

        BaseItemRule baseItemRule = baseItemRulecontainer.getElement(shelf.getItemId());
        if (baseItemRulecontainer.addItemAndCheckNoSaving(baseItemRule,shelf.getItemNum(),fp,null,null,null)){
            resetShelf(shelf);
        }else{
            exceptionFactory.throwAlertException("package.farm.full");
        }

        return baseItemRule.getItemsId();
    }
    @Override
    public void checkBuy(PlayerStall playerStall) throws AlertException{
        boolean isVip = payMemberRuleContainer.isPayMember(playerStall.getPid());
        if(isVip) {
        if(playerStall.getBuyingTimes()>StallConstant.BUYING_VIP_NUM){
            exceptionFactory.throwAlertException("stall.buying.over.times");//
        }
        }else{
            if(playerStall.getBuyingTimes()>StallConstant.BUYING_NUM){
                exceptionFactory.throwAlertException("stall.buying.over.times");//
            }
        }
    }
    @Override
    public void adddBuyingTime(PlayerStall playerStall) throws AlertException{
        playerStall.incBuyingTimes();
        playerStall.setBuyingDate(new Date());
    }
    @Override
    public String buy(Player player,FarmPackage myPackage, PlayerStall sellerStall, int shelfOrder, int shelfType) throws AlertException {
        PlayerShelf shelf = sellerStall.getPlayerShelfByOrder(shelfOrder, shelfType);
        if(null==shelf){
            exceptionFactory.throwAlertException("shelf.out.of.rang");//货架序数超出范围
        }
        if(shelf.getItemId()==null){
            exceptionFactory.throwAlertException("itemId.invalid");
        }
        if(shelf.getState() != StallConstant.SHELF_ON_SALE){
            exceptionFactory.throwAlertException("shelf.state.invalid");//必须在出售状态
        }
        int needMoney = shelf.getItemPrice();
        if(player.getGameCoin() < needMoney){
            exceptionFactory.throwAlertException("gamecoin.notenough");
        }

        if(shelf.getItemNum()<=0){
            exceptionFactory.throwAlertException("shelf.num.invalid"+sellerStall.getPid()+"_"+shelfOrder+"_"+shelfType);
        }

        BaseItemRule baseItemRule = baseItemRulecontainer.getElement(shelf.getItemId());
        if (baseItemRulecontainer.addItemAndCheckNoSaving(baseItemRule,shelf.getItemNum(),myPackage,null,null,null)) {
            player.decGameCoin(needMoney);
            shelf.setBuyerId(player.getId());
            shelf.setBuyingTime(System.currentTimeMillis());
            shelf.setState(StallConstant.SHELF_SOLD);
        } else {
            exceptionFactory.throwAlertException("package.farm.full");
        }

        return shelf.getItemId();
    }

    @Override
    public void assistantBuy(Player player,FarmPackage myPackage, String itemId, int itemNum, int needMoney) throws AlertException {
        if(player.getGameCoin() < needMoney){
            exceptionFactory.throwAlertException("gamecoin.notenough");
        }
        BaseItemRule baseItemRule = baseItemRulecontainer.getElement(itemId);
        if (baseItemRulecontainer.addItemAndCheckNoSaving(baseItemRule,itemNum,myPackage,null,null,null)) {
            player.decGameCoin(needMoney);
        } else {
            exceptionFactory.throwAlertException("package.farm.full");
        }
    }


    @Override
    public void sysBuy(PlayerStall playerStall){
        boolean isVip = payMemberRuleContainer.isPayMember(playerStall.getPid());
        if(!isVip){
            return;
        }

        //当日
        Calendar today = Calendar.getInstance();
        int day = today.get(Calendar.DAY_OF_YEAR);

        //上次登陆时间
        Calendar lastLogin = Calendar.getInstance();
        lastLogin.setTime(playerStall.getLoginDate());
        int lastLoginDay = lastLogin.get(Calendar.DAY_OF_YEAR);

        //连续登陆
        if(day == lastLoginDay+1){
            playerStall.incDay(1);
        if(playerStall.getDay()>=3){ //连续登陆3天
            List<PlayerShelf> ranList = playerStall.getPlayerShelfByState(StallConstant.SHELF_ON_SALE);
            if(ranList.size()>0){
                int count = ranList.size();
                int index = (int)(count*Math.random());
                PlayerShelf shelf = ranList.get(index);
                shelf.setBuyerId("qinma_10000");
                shelf.setBuyingTime(System.currentTimeMillis());
                shelf.setState(StallConstant.SHELF_SOLD);
                playerStall.setDay(0);
            }
        }
        }
        //reset
        if(day > lastLoginDay+1){
            playerStall.setDay(0);
        }

        playerStall.setLoginDate(new Date());
    }

    @Override
    public boolean isAvailable (PlayerStall playerStall){
        return playerStall.getAnyPlayerShelfByState(StallConstant.SHELF_ON_SALE);
    }

    @Override
    public List<PlayerShelf> generateMomShelf(PlayerStall playerStall){
        //当日
        Calendar today = Calendar.getInstance();
        int day = today.get(Calendar.DAY_OF_YEAR);

        //上次
        Calendar last = Calendar.getInstance();
        last.setTime(playerStall.getMomShelfDate());
        int ref = last.get(Calendar.DAY_OF_YEAR);

        if(day == ref){
            return playerStall.getPlayerMomShelf();
        }
        playerStall.setMomShelfDate(new Date());

        List<PlayerShelf> list= new ArrayList<PlayerShelf>();//playerStall.getPlayerMomShelf();
        //随机货架数
        int count = (int)(Math.random()*5) + 1;
        int randomCount = randomItem.size();
        if(randomCount==0)
            return null;
        for(int i=0;i<count;++i){
            PlayerShelf shelf = new PlayerShelf();
            shelf.setState(StallConstant.SHELF_ON_SALE);
            //随机货品
            int index = (int)(Math.random()*randomCount);
            int num = (int)(Math.random()*3) + 1;
            BaseItemRule rule = randomItem.get(index);
            shelf.setItemId(rule.getItemsId());
            shelf.setItemNum(num);
            shelf.setItemPrice((rule.getShelfPrice()>>1)*3);
            list.add(shelf);
        }
        return list;
    }

    @Override
    public void getMoney(Player player, PlayerStall sellerStall, int shelfOrder, int shelfType) throws AlertException {
        PlayerShelf shelf = sellerStall.getPlayerShelfByOrder(shelfOrder, shelfType);
        if(null==shelf){
            exceptionFactory.throwAlertException("shelf.out.of.range");//货架序数超出范围
        }
        if(shelf.getState() != StallConstant.SHELF_SOLD) {
            exceptionFactory.throwAlertException("shelf.state.invalid");
        }
        int money = shelf.getItemPrice();
        player.incGameCoin(money);

        //成就统计  摆摊收入游戏币
        successRuleContainer.updateSuccessNum(player.getId(),19,money,"");

        resetShelf(shelf);
    }

    @Override
    public void advertise(Player player, PlayerStall sellerStall, int shelfOrder, int shelfType, int advType) throws AlertException {

        int advPrice = 0;
        if(!advPrices.containsKey(advType)){
            exceptionFactory.throwAlertException("advertisement.invalid");
        }
        advPrice = advPrices.get(advType);
        if(player.getGoldCoin() < advPrice){
            exceptionFactory.throwAlertException("goldcoin.notenough");
        }
        PlayerShelf shelf = sellerStall.getPlayerShelfByOrder(shelfOrder, shelfType);
        if(null==shelf){
            exceptionFactory.throwAlertException("shelf.out.of.rang");//货架序数超出范围
        }
        BaseItemRule rule = baseItemRulecontainer.getElement(shelf.getItemId());
        if(null == rule){
            return;
        }

        //扣达人币
        //player.decGoldCoin(advPrice);
        playerRuleContainer.decGoldCoin(player,advPrice, PlayerConstant.GOLD_ACTION_41);

        //广告
        PlayerStallAdvertisement psa = new PlayerStallAdvertisement();
        psa.setPid(player.getId());
        psa.setItemID(shelf.getItemId());
        psa.setItemNum(shelf.getItemNum());
        psa.setItemName(rule.getName());
        psa.setType(advType);
        psa.setEndTime(System.currentTimeMillis() + (long)advType*60l*1000l);
        psa.setName(player.getNickName());

        //货架
        shelf.setAdvTime(psa.getEndTime());

        StallAdvertisement stallAdvertisement = stallAdvertisementDao.getStallAdvertisement();
        List<PlayerStallAdvertisement> list = stallAdvertisement.getAdvList();
        list.add(psa);
        Collections.sort(list);
        stallAdvertisementDao.saveBean(stallAdvertisement);
    }

    @Override
    public PlayerStallAdvertisement[] getAdvertisement(int page, int size) throws AlertException {

        StallAdvertisement stallAdvertisement = stallAdvertisementDao.getStallAdvertisement();
        List<PlayerStallAdvertisement> list = stallAdvertisement.getAdvList();
        Iterator<PlayerStallAdvertisement> it = list.iterator();
        long now = System.currentTimeMillis();
        boolean update = false;
        PlayerStallAdvertisement[] psas = new PlayerStallAdvertisement[size];
        int i = 0;
        while (it.hasNext()){
            PlayerStallAdvertisement psa = it.next();
            if(psa.getEndTime()<now){
                it.remove();
                update = true;
                continue;
            }
            if(i/size == page){
                int index = i%size;
                psas[index] = psa;
            }
            i++;
        }
        if(update){
            stallAdvertisementDao.saveBean(stallAdvertisement);
        }

        return psas;
    }

    @Override
    public void enableAssistant(Player player, PlayerStall playerStall,int type) throws AlertException {
         if(assistantPrices.containsKey(type)){
             int price = assistantPrices.get(type);
             if(player.getGoldCoin()<price){
                 exceptionFactory.throwAlertException("goldcoin.notenough");
             }
             //player.decGoldCoin(price);
             playerRuleContainer.decGoldCoin(player,price, PlayerConstant.GOLD_ACTION_43);
             playerStall.setAssistantTime(new Date(System.currentTimeMillis()+type*3600*24*1000l));
         }else {
             exceptionFactory.throwAlertException("assistant.price.invalid");
         }
    }

        @Override
    public void addAssistantInfo(StallAssistant sa,String pid, int order,int type,PlayerShelf shelf) throws AlertException {
        PlayerAssistant ai = new PlayerAssistant();
        ai.setNum(shelf.getItemNum());
        ai.setShelfOrder(order);
        ai.setShelfType(type);
        ai.setPrice(shelf.getItemPrice());
        sa.getShelfMap().put(pid,ai);
    }

    @Override
    public void removeAssistantInfo(StallAssistant sa,String pid) throws AlertException {
       if(sa.getShelfMap().containsKey(pid)){
           sa.getShelfMap().remove(pid);
       }
    }

    public void resetShelf(PlayerShelf shelf) {
        shelf.setState(StallConstant.SHELF_FREE);
        shelf.setItemPrice(0);
        shelf.setItemId(null);
        shelf.setBuyerId(null);
        shelf.setAdvTime(0l);
        shelf.setItemNum(0);
        shelf.setHitShelfTime(0l);
    }

}

