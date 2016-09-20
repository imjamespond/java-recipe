package com.pengpeng.stargame.farm.container.impl;

import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmProcessRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.farm.rule.DropItem;
import com.pengpeng.stargame.farm.rule.FarmProcessRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.farm.process.FarmProcessQueue;
import com.pengpeng.stargame.model.farm.process.OneQueue;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.rpc.BroadcastHolder;
import com.pengpeng.stargame.rsp.RspFactory;
import com.pengpeng.stargame.rsp.RspFarmFactory;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.util.RandomUtil;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.farm.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-11-13
 * Time: 下午12:00
 */
@Component
public class FarmProcessRuleContainerImpl extends HashMapContainer<String, FarmProcessRule> implements IFarmProcessRuleContainer {

    public static final int OPEN_GRID=50; //开一个格子需要的 达人币

    public static final int MAX_NUM=8;//最多格子的数量
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private RspFarmFactory rspFactory;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private RspFarmFactory rsp;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;
    @Override
    public List<FarmProcessRule> getByType(int type) {
        List<FarmProcessRule> list=new ArrayList<FarmProcessRule>();
        for(FarmProcessRule farmProcessRule:items.values()){
            if(farmProcessRule.getType()==type){
                list.add(farmProcessRule);
            }
        }
        return list;
    }

    @Override
    public void checkStart(FarmProcessQueue farmProcessQueue,String [] processIds) throws AlertException {
       FarmPackage farmPackageTemp=farmPackageDao.getBean(farmProcessQueue.getPid());
        if(processIds.length+farmProcessQueue.getOneQueueList().size()>farmProcessQueue.getNum()+ FarmConstant.GRID_NUM){
            exceptionFactory.throwAlertException("队列已经满了");
        }

        for(String processid:processIds){
            if(getElement(processid)==null){
                exceptionFactory.throwAlertException("不存在加工队列");
            }
        }
        /**
         * 材料的检测
         */
        for(String processid:processIds){
            FarmProcessRule farmProcessRule=getElement(processid);
            for(ItemData itemData:farmProcessRule.getItemDataList()){
                if(farmPackageTemp.getSumByNum(itemData.getItemId())<itemData.getNum()){
                    exceptionFactory.throwAlertException("材料不足！");
                }
                farmPackageTemp.deduct(itemData.getItemId(),itemData.getNum());
            }

        }
    }

    @Override
    public void start(FarmProcessQueue farmProcessQueue, String[] processIds,FarmPackage farmPackage) {

        //扣除物品
        //开始队列
        for(String processid:processIds){
            FarmProcessRule farmProcessRule=getElement(processid);
            for(ItemData itemData:farmProcessRule.getItemDataList()){
                farmPackage.deduct(itemData.getItemId(),itemData.getNum());
            }

        }
        for(String processid:processIds){
            FarmProcessRule farmProcessRule=getElement(processid);
            farmProcessQueue.addOneQueue(processid,farmProcessRule.getTime());

            }

    }

    @Override
    public void checkCancel(FarmProcessQueue farmProcessQueue, String id) throws AlertException {
        OneQueue oneQueue=farmProcessQueue.getOneQueue(id);
        if(oneQueue==null){
            exceptionFactory.throwAlertException("不存在此队列");
        }
        if(oneQueue.getStatus()==3){
            exceptionFactory.throwAlertException("完成的队列不能取消");
        }
    }

    @Override
    public void cancel(FarmProcessQueue farmProcessQueue, String id) {
        farmProcessQueue.cancelQueue(id);
    }

    @Override
    public void finishOne(FarmProcessQueue farmProcessQueue,String id, Player player) throws AlertException {
        /**
         * 达人币判断
         */
          int needGold=getOneNeedgold(farmProcessQueue, id);
          if(player.getGoldCoin()<needGold){
              exceptionFactory.throwAlertException("goldcoin.notenough");
          }
//        player.decGoldCoin(needGold);
        playerRuleContainer.decGoldCoin(player,needGold, PlayerConstant.GOLD_ACTION_5);
         farmProcessQueue.finishOne(id);

    }

    @Override
    public int getNeedgold(FarmProcessQueue farmProcessQueue) {
        Date now=new Date();
        float hour=  ((farmProcessQueue.getEndTime().getTime()-now.getTime())/(3600f*1000f));
        int gold= (int) (hour*20);
        if(gold<=0){
            gold=5;
        }
        return gold;
    }

    @Override
    public int getOneNeedgold(FarmProcessQueue farmProcessQueue, String id) throws AlertException {
        OneQueue oneQueue=farmProcessQueue.getOneQueue(id);
        if(oneQueue==null){
            exceptionFactory.throwAlertException("队列不存在！");
        }
        Date now=new Date();

        int time=oneQueue.getTime();

        if(oneQueue.getStatus()==2){
           time-=((now.getTime()-farmProcessQueue.getStartTime().getTime())/1000);
        }

        float hour=  time/(3600f);

        int gold= (int) (hour*20);
        if(gold<=0){
            gold=5;
        }
        return gold;
    }

    @Override
    public void finish(FarmProcessQueue farmProcessQueue, Player player) throws AlertException {
        int gold=getNeedgold(farmProcessQueue);
        if(player.getGoldCoin()<gold){
            exceptionFactory.throwAlertException("goldcoin.notenough");
        }
//        player.decGoldCoin(gold);
        playerRuleContainer.decGoldCoin(player,gold,PlayerConstant.GOLD_ACTION_8);
        farmProcessQueue.finish();

    }

    @Override
    public void openGrid(FarmProcessQueue farmProcessQueue, Player player) throws AlertException {
        if(player.getGoldCoin()<OPEN_GRID){
            exceptionFactory.throwAlertException("goldcoin.notenough");
        }
        if(farmProcessQueue.getNum()+FarmConstant.GRID_NUM>=MAX_NUM){
            exceptionFactory.throwAlertException("farm.process.max");
        }

//        player.decGoldCoin(OPEN_GRID);
        playerRuleContainer.decGoldCoin(player,OPEN_GRID,PlayerConstant.GOLD_ACTION_7);
        farmProcessQueue.setNum(farmProcessQueue.getNum()+1);

    }

    @Override
    public void getProecessGoods(FarmProcessQueue farmProcessQueue, FarmPackage farmPackage,RoomPackege roomPackege, FashionCupboard fashionCupboard,FarmDecoratePkg farmDecoratePkg) throws AlertException {

       //判断是否有完成的队列
        List<OneQueue> finishOneQueueList=farmProcessQueue.getFinish();
        if(finishOneQueueList.size()<=0){
            exceptionFactory.throwAlertException("farm.process.nofinish");
        }
        List<ItemData> itemDataList=new ArrayList<ItemData>();
        for(OneQueue oneQueue:finishOneQueueList){
            FarmProcessRule farmProcessRule=getElement(oneQueue.getProcessid());
            ItemData itemData=new ItemData();
            itemData.setItemId(farmProcessRule.getItems());
            itemData.setNum(1);
            itemDataList.add(itemData);
        }
        //背包检测
        if(baseItemRulecontainer.checkFarmPackge(farmProcessQueue.getPid(),itemDataList)){
            exceptionFactory.throwAlertException("farm.process.noget");
        }


        //添加物品
        for(ItemData itemData:itemDataList){
            BaseItemRule baseItemRule=baseItemRulecontainer.getElement(itemData.getItemId());
            farmPackage.addItem(itemData.getItemId(),itemData.getNum(),baseItemRule.getOverlay(),baseItemRule.getValidDete());
        }
        /**
         * 掉宝功能
         */
        boolean  has=false;
        RewardVO rewardVO = rsp.getRewardVO(11);
        for(OneQueue oneQueue:finishOneQueueList){
            FarmProcessRule farmProcessRule=getElement(oneQueue.getProcessid());
            if (farmProcessRule.getDropItemList().size() > 0) {
                for (DropItem dropItem : farmProcessRule.getDropItemList()) {
                    int randomNum = RandomUtil.range(0, 1000);
                    if (dropItem.getProbability() < randomNum) {
                        continue;
                    }
                    has=true;
                    rewardVO.addGoodsVO(rsp.getGoodsVo(dropItem.getItemId(), dropItem.getNum()));
                    baseItemRulecontainer.addGoodsNoSave(dropItem.getItemId(),dropItem.getNum(),farmPackage,roomPackege,fashionCupboard, farmDecoratePkg);
                }
            }
        }
        if(has){
            BroadcastHolder.add(rewardVO);
        }

        //删除队列
        farmProcessQueue.deleteFinish(finishOneQueueList);

        /**
         * 成就统计
         */
        successRuleContainer.updateSuccessNum(farmProcessQueue.getPid(),2,finishOneQueueList.size(),"");
    }

    @Override
    public List<GoodsVO> getFinish(FarmProcessQueue farmProcessQueue) {
        List<OneQueue> finishOneQueueList=farmProcessQueue.getFinish();
        List<GoodsVO> goodsVOs=new ArrayList<GoodsVO>();
        for(OneQueue oneQueue:finishOneQueueList){
            FarmProcessRule farmProcessRule=getElement(oneQueue.getProcessid());
            BaseItemRule baseItemRule=baseItemRulecontainer.getElement(farmProcessRule.getItems());
            rspFactory.getGoodsVo(baseItemRule.getItemsId(),1);

            goodsVOs.add(rspFactory.getGoodsVo(baseItemRule.getItemsId(),1));
        }
        return goodsVOs;
    }

    @Override
    public void speedAll(Player player, FarmProcessQueue farmProcessQueue) throws AlertException {
        if(player.getGoldCoin()<20){
            exceptionFactory.throwAlertException("goldcoin.notenough");
        }
//        player.decGoldCoin(20);
        playerRuleContainer.decGoldCoin(player,20,PlayerConstant.GOLD_ACTION_6);
        Date now=new Date();

        farmProcessQueue.setSpeedTime(DateUtil.addMinute(now,24*60));
//        farmProcessQueue.setSpeedTime(DateUtil.addMinute(now,2));
        farmProcessQueue.setSpeedStartTime(now);
        farmProcessQueue.speedAll1();

    }
}
