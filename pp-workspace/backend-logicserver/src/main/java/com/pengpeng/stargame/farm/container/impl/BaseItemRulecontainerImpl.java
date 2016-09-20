package com.pengpeng.stargame.farm.container.impl;


import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.constant.BaseItemConstant;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmDecorateRuleContainer;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmDecoratePkgDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.farm.rule.DropItem;
import com.pengpeng.stargame.farm.rule.FarmSeedRule;
import com.pengpeng.stargame.farm.rule.Product;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.farm.*;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.FashionPkg;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.rpc.BroadcastHolder;
import com.pengpeng.stargame.rsp.RspFarmFactory;
import com.pengpeng.stargame.rsp.RspFarmItemFactory;
import com.pengpeng.stargame.statistics.Statistics;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.util.RandomUtil;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vo.HintVO;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.farm.FarmSpeedVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: mql
 * Date: 13-5-2
 * Time: 上午9:23
 */
@Component
public class BaseItemRulecontainerImpl extends HashMapContainer<String, BaseItemRule> implements IBaseItemRulecontainer {
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private RspFarmFactory rsp;

    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private RspFarmItemFactory rspFarmItemFactory;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;

    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private MessageSource message;
    @Autowired
    private Statistics statistics;
    @Autowired
    private IFarmDecoratePkgDao farmDecoratePkgDao;
    @Autowired
    private IFarmDecorateRuleContainer farmDerocateRuleContainer;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;

    @Autowired
    private ISuccessRuleContainer successRuleContainer;

    @Override
    public void checkBuy(String itemId) throws AlertException {
        BaseItemRule baseItemRule = this.getElement(itemId);
        baseItemRule.checkBuy();
    }

    @Override
    public void checkLevel(String itemId, int level) throws AlertException {
        BaseItemRule baseItemRule = this.getElement(itemId);
        baseItemRule.checkLevel(level);

    }

    @Override
    public void checkPackegeSize(FarmPackage farmPackage, String itemId, int num) throws AlertException {
        BaseItemRule baseItemRule = this.getElement(itemId);
        if (baseItemRule.checkPackageSize(baseItemRule.getItemsId(), farmPackage, num)) {
            exceptionFactory.throwAlertException("farmpkg.max1");
        }
    }


    @Override
    public void checkMoney(Player player, String itemId, int num) throws AlertException {
        BaseItemRule baseItemRule = this.getElement(itemId);
        baseItemRule.checkMoney(player, num);

    }

    @Override
    public void checkSales(FarmPackage farmPackage, String id, String itemId, int num) throws AlertException {
        if (num < 0) {
            exceptionFactory.throwAlertException("farmpkg.notenough");
        }
        if (farmPackage.checkByNum(id, num)) {
            exceptionFactory.throwAlertException("farmpkg.notenough");
        }
        BaseItemRule baseItemRule = this.getElement(itemId);
        baseItemRule.checkSales();
    }

    @Override
    public void salesFram(Player player, FarmPackage farmPackage, String itemId, String id, int num) {

        farmPackage.deductById(id, num);
        BaseItemRule baseItemRule = this.getElement(itemId);
//        baseItemRule.addGameMoney(player, num);
        playerRuleContainer.incGameCoin(player,baseItemRule.getRecyclingPrice()*num);

    }

    @Override
    public void checkField(FarmField farmField) throws AlertException {
        if (farmField == null) {
            throw new AlertException("不存在此田地");
        }
        if (farmField.getStatus() != FarmConstant.FIELD_STATUS_FREE) {
            throw new AlertException("田地存在作物，不能种植");
        }
    }

    @Override
    public void chekPlantSeedNum(String itemId, FarmPackage farmPackage) throws AlertException {
        boolean exists = farmPackage.existByItemId(itemId);
        if (!exists) {
            throw new AlertException("没有此种子");
        }
        if (farmPackage.getSumByNum(itemId) < 1) {
            throw new AlertException("没有此种子");
        }
    }

    @Override
    public void plant(String itemId, FarmField farmField, FarmPackage farmPackage) {
        FarmSeedRule farmSeedRule = (FarmSeedRule) this.getElement(itemId);

        /**
         * 扣除种子
         */
        farmPackage.deduct(itemId, 1);

        /**
         *修改田地 信息
         *
         */
        farmField.setPlantTime(new Date());
        farmField.setStatus(FarmConstant.FIELD_STATUS_PLANT);
        farmField.setSeedId(itemId);
        //成熟时间 读取
        farmField.setHarvestTime(DateUtil.convertMsecondToString(System.currentTimeMillis() + farmSeedRule.getGrowthTime() * 1000));


    }

    @Override
    public void checkMyHarvest(FarmField farmField, FarmPackage farmPackage) throws AlertException {
        if (farmField == null) {
            throw new AlertException("不存在此田地");
        }

        if (farmField.getRipeId() == 0) {
            throw new AlertException("未成熟不能收获");
        }
        boolean isH = false;
        for (int i = 0; i < farmField.getHarvestIds().size(); i++) {
            if (farmField.getHarvestIds().get(i) == farmField.getRipeId()) {
                isH = true;
                break;
            }
        }
        if (isH) {
            exceptionFactory.throwAlertException("farm.field.already.harvest");
        }
        FarmSeedRule farmSeedRule = (FarmSeedRule) this.getElement(farmField.getSeedId());
        Product p = farmSeedRule.getOneProduct(farmField.getRipeId());
        if (farmSeedRule.checkPackageSize(farmSeedRule.getOutput(), farmPackage, p.getNum())) {
            exceptionFactory.throwAlertException("farmpkg.max");
        }
    }

    @Override
    public void checkMyHarvestAll(FarmPlayer farmPlayer, FarmPackage farmPackage) throws AlertException {

        boolean isCan = false;
        for (FarmField farmField : farmPlayer.getFields()) {
            /**
             * 未成熟
             */
            if (farmField.getRipeId() == 0) {
                continue;
            }
            /*
           已经收获的 不 算背包里面
            */
            if (farmField.getHarvestIds().contains(farmField.getRipeId())) {
                continue;
            }
            isCan = true;
            FarmSeedRule farmSeedRule = (FarmSeedRule) this.getElement(farmField.getSeedId());
            Product p = farmSeedRule.getOneProduct(farmField.getRipeId());
            if (farmSeedRule.checkPackageSize(farmSeedRule.getOutput(), farmPackage, p.getNum())) {
                exceptionFactory.throwAlertException("farmpkg.max");
            }

            /**
             * 临时 添加 作物
             */
            BaseItemRule baseItemRule = getElement(farmSeedRule.getOutput());
            farmPackage.addItem(farmSeedRule.getOutput(), p.getNum(), farmSeedRule.getOverlay(), baseItemRule.getValidDete());

        }
        if (!isCan) {
            exceptionFactory.throwAlertException("farm.field.noAll");
        }


    }

    @Override
    public void checkFriendHarvest(FarmFriendHarvest farmFriendHarvest, FarmField farmField, FarmPackage myFarmPackage, FarmPackage friendFarmPackage) throws AlertException {

        /**
         * 仓库是否 满了的 判断
         */
        FarmSeedRule farmSeedRule = (FarmSeedRule) this.getElement(farmField.getSeedId());
        BaseItemRule baseItemRule = this.getElement(farmSeedRule.getOutput());

        if (baseItemRule.checkPackageSize(farmSeedRule.getOutput(), myFarmPackage, farmSeedRule.getCropsReward())) {
            exceptionFactory.throwAlertException("farmpkg.max");
        }

        if (farmField.getRipeId() == 0) {
            exceptionFactory.throwAlertException("farm.field.jackleg");
        }
        if (farmField.isHarvestRipe(farmField.getRipeId())) {
            exceptionFactory.throwAlertException("farm.field.already.harvest");
        }

        Product p = farmSeedRule.getOneProduct(farmField.getRipeId());

        if (baseItemRule.checkPackageSize(farmSeedRule.getOutput(), friendFarmPackage, p.getNum())) {
            exceptionFactory.throwAlertException("farmpkg.friend.max");
        }

        if (farmFriendHarvest.getNum() >= getMaxHelpFriendNum(myFarmPackage.getPid())) {
            String msg = message.getMessage("farm.friend.harvest.max", new String[]{String.valueOf(getMaxHelpFriendNum(myFarmPackage.getPid()))}, Locale.CHINA);
            exceptionFactory.throwAlertException(msg);
        }
        if (farmField == null) {
            exceptionFactory.throwAlertException("farm.field.notfound");
        }


        if (farmField.getRipeId() == farmSeedRule.getProductList().size()) {
            exceptionFactory.throwAlertException("farm.field.notharvest");
        }
    }

    @Override
    public void helpFriendHarvestAll(FarmFriendHarvest farmHarvest, FarmPlayer friendFarmPlayer, FarmPackage myFarmPackage, FarmPackage friendFarmPackage, FarmActionInfo farmActionInfo) throws AlertException {
        List<FarmField> canHelp = new ArrayList<FarmField>(); //可以帮忙收获的田地集合
        for (FarmField farmField : friendFarmPlayer.getFields()) {
            if (farmField.getStatus() == FarmConstant.FIELD_STATUS_FREE) {
                continue;
            }
            if (farmField.getRipeId() == 0) {
                continue;
            }
            FarmSeedRule farmSeedRule = (FarmSeedRule) this.getElement(farmField.getSeedId());
            BaseItemRule baseItemRule = this.getElement(farmSeedRule.getOutput());

            if (farmField.isHarvestRipe(farmField.getRipeId())) {
                continue;
            }
            if (farmField.getRipeId() == farmSeedRule.getProductList().size()) {
                continue;
            }
            if (farmField.getRipeId() == farmSeedRule.getProductList().size()) {
                continue;
            }

            canHelp.add(farmField);
        }

        if (canHelp.size() <= 0) {
            exceptionFactory.throwAlertException("没有可以帮忙收获的作物！");
        }

        for (FarmField farmField : canHelp) {
            FarmSeedRule farmSeedRule = (FarmSeedRule) this.getElement(farmField.getSeedId());
            BaseItemRule baseItemRule = this.getElement(farmSeedRule.getOutput());
            Product p = farmSeedRule.getOneProduct(farmField.getRipeId());


            if (baseItemRule.checkPackageSize(farmSeedRule.getOutput(), myFarmPackage, farmSeedRule.getCropsReward())) {
                exceptionFactory.throwAlertException("farmpkg.max");
            }
            if (baseItemRule.checkPackageSize(farmSeedRule.getOutput(), friendFarmPackage, p.getNum())) {
                exceptionFactory.throwAlertException("farmpkg.friend.max");
            }

            if (farmHarvest.getNum() >= getMaxHelpFriendNum(farmHarvest.getpId())) {
                String msg = message.getMessage("farm.friend.harvest.max", new String[]{String.valueOf(getMaxHelpFriendNum(farmHarvest.getId()))}, Locale.CHINA);
                exceptionFactory.throwAlertException(msg);
            }

            /**
             * 自己添加 帮好友收获次数
             */
            farmHarvest.setNum(farmHarvest.getNum() + 1);
            /**
             * 好友田地信息的 修改
             */
            farmField.getHarvestIds().add(farmField.getRipeId());
            /**
             * 自己添加 作物
             */
            int randomint = RandomUtil.range(0, 1000);
            if (farmSeedRule.getfProbability() > randomint) {
                myFarmPackage.addItem(farmSeedRule.getOutput(), farmSeedRule.getCropsReward(), baseItemRule.getOverlay(), baseItemRule.getValidDete());
                RewardVO rewardVO = rsp.getRewardVO(1);
                rewardVO.setFieldId(Integer.parseInt(farmField.getId()));
                rewardVO.addGoodsVO(rsp.getGoodsVo(farmSeedRule.getOutput(), farmSeedRule.getCropsReward()));
                BroadcastHolder.add(rewardVO);
            }

            /**
             * 好友添加 作物
             */

            friendFarmPackage.addItem(farmSeedRule.getOutput(), p.getNum(), baseItemRule.getOverlay(), baseItemRule.getValidDete());


            /**
             * 添加帮助好友 采摘日志
             */
            farmActionInfo.addAction(new FarmAction(myFarmPackage.getId(), 3, farmSeedRule.getOutput(), p.getNum()));

            //任务
            taskRuleContainer.updateTaskNum(myFarmPackage.getPid(), TaskConstants.CONDI_TYPE_9, "", 1);


            //日志
            String value = farmField.getId() + GameLogger.SPLIT + String.valueOf(farmField.getRipeId()) + GameLogger.SPLIT + farmSeedRule.getOutput() + GameLogger.SPLIT + friendFarmPackage.getId();
            gameLoggerWrite.write(new GameLogger(GameLogger.LOG_11, myFarmPackage.getId(), value));

        }
    }

    @Override
    public void checkSpeedUP(Player player, FarmField farmField, FarmPackage farmPackage) throws GameException {
        if (farmField.getStatus() != FarmConstant.FIELD_STATUS_PLANT) {
            exceptionFactory.throwAlertException("不能加速");
        }
        if (farmPackage.getSumByNum(FarmConstant.H_F) <= 0) { //如果没有化肥 需要判断 金币是否 足够
            int gold = getSpeedupNeedGold(player, farmField);
            if (player.getGoldCoin() < gold) {
                exceptionFactory.throwAlertException("goldcoin.notenough");
            }
        }


    }

    @Override
    public int getSpeedupNeedGold(Player player, FarmField farmField) {
        Date now = new Date();
        double hour = (farmField.getHarvestTime().getTime() - now.getTime()) / (60f * 60f * 1000f);
        int gold = (int) (hour * 10);
        if (gold < 2) {
            gold = 2;
        }
        return gold;


    }

    @Override
    public FarmSpeedVO speedUp(Player player, FarmField farmField, FarmPackage farmPackage) {
        FarmSpeedVO farmSpeedVO = new FarmSpeedVO();

        if (farmPackage.getSumByNum(FarmConstant.H_F) <= 0) {   //如果没有化肥  需要扣除 金币  直接催熟


            int gold = getSpeedupNeedGold(player, farmField);
            farmField.setHarvestTime(new Date());

//            player.decGoldCoin(gold);
            playerRuleContainer.decGoldCoin(player,gold, PlayerConstant.GOLD_ACTION_1);
            farmSpeedVO.setType(2);
            farmSpeedVO.setGold(gold);
        } else {
            farmSpeedVO.setType(1);


            farmField.setHarvestTime(DateUtil.addMinute(farmField.getHarvestTime(), -30));
            farmField.setPlantTime(DateUtil.addMinute(farmField.getPlantTime(), -30));
            farmPackage.deduct(FarmConstant.H_F, 1);
        }
        return farmSpeedVO;
    }

    @Override
    public void harvestSeed(FarmField farmField, FarmPackage farmPackage, FarmPlayer farmPlayer, RoomPackege roomPackege, FashionCupboard fashionCupboard,FarmDecoratePkg farmDecoratePkg) throws AlertException {

        FarmSeedRule farmSeedRule = (FarmSeedRule) this.getElement(farmField.getSeedId());


        RewardVO rewardVO = rsp.getRewardVO(1);

        rewardVO.setFieldId(Integer.parseInt(farmField.getId()));
        /**
         * 添加收获的 Id
         */
        int exp = 0;
        farmField.getHarvestIds().add(farmField.getRipeId());
        Product p = farmSeedRule.getOneProduct(farmField.getRipeId());
        int ripId = farmField.getRipeId();
        int addNum=p.getNum();//最终需要添加的数量
        //如果  是最后一次成熟 那么田地变成可以种植的状态
        if (farmField.getRipeId() == farmSeedRule.getProductList().size()) {
            /**
             * 蜜蜂 鼹鼠 影响产量的
             */
            int oneReduceNum=farmDerocateRuleContainer.getCropReduce(farmPlayer.getId());
            int oneAddNum=farmDerocateRuleContainer.getCropAdd(farmPlayer.getId());
            int pNum=oneAddNum-oneReduceNum;
            if(oneReduceNum>0){
                BroadcastHolder.add(new HintVO("很遗憾，这次虫害使您收获的农作物减少"+oneReduceNum+"个"));
            }
            if(oneAddNum>0){
                BroadcastHolder.add(new HintVO("很幸运，这次蜜蜂使您收获的农作物增加"+oneAddNum+"个"));
            }

            addNum+=pNum; //设置 最终产量

            farmField.setStatus(FarmConstant.FIELD_STATUS_FREE);
            farmField.getHarvestIds().clear();
            farmField.setRipeId(0);

            exp = farmSeedRule.getSeedsExp();
            //最后一次成熟农场 加经验
            farmLevelRuleContainer.addFarmExp(farmPlayer, farmSeedRule.getSeedsExp());

            /**
             * 掉宝功能
             */
            if (farmSeedRule.getDropItemList().size() > 0) {
                for (DropItem dropItem : farmSeedRule.getDropItemList()) {
                    int randomNum = RandomUtil.range(0, 1000);
                    if (dropItem.getProbability() < randomNum) {
                        continue;
                    }
                    rewardVO.addGoodsVO(rsp.getGoodsVo(dropItem.getItemId(), dropItem.getNum()));
                    this.addGoodsNoSave(dropItem.getItemId(), dropItem.getNum(), farmPackage, roomPackege, fashionCupboard,farmDecoratePkg);
                }
            }

        }

//        BroadcastHolder.add(rsp.getFieldVO(farmField,farmPlayer, exp));

        /**
         * 添加 作物
         */
        BaseItemRule baseItemRule = getElement(farmSeedRule.getOutput());
        farmPackage.addItem(farmSeedRule.getOutput(), addNum, farmSeedRule.getOverlay(), baseItemRule.getValidDete());

        successRuleContainer.updateSuccessNum(farmPlayer.getId(),16,addNum,farmSeedRule.getOutput());

        rewardVO.setFarmExp(exp);
        rewardVO.addGoodsVO(rsp.getGoodsVo(farmSeedRule.getOutput(), addNum));
        BroadcastHolder.add(rewardVO);

        //日志
        String value = farmField.getId() + GameLogger.SPLIT + String.valueOf(ripId) + GameLogger.SPLIT + farmSeedRule.getOutput();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_10, farmPlayer.getId(), value));

    }

    @Override
    public void harvestSeedAll(FarmPackage farmPackage, FarmPlayer farmPlayer, RoomPackege roomPackege, FashionCupboard fashionCupboard,FarmDecoratePkg farmDecoratePkg) throws AlertException {

        int allReduceNum=0;
        int allAddNum=0;
        for (FarmField farmField : farmPlayer.getFields()) {
            if (farmField.getRipeId() == 0) {
                continue;
            }
            if (farmField.getHarvestIds().contains(farmField.getRipeId())) {
                continue;
            }
            FarmSeedRule farmSeedRule = (FarmSeedRule) this.getElement(farmField.getSeedId());


            RewardVO rewardVO = rsp.getRewardVO(1);

            rewardVO.setFieldId(Integer.parseInt(farmField.getId()));
            /**
             * 添加收获的 Id
             */
            int exp = 0;
            farmField.getHarvestIds().add(farmField.getRipeId());
            Product p = farmSeedRule.getOneProduct(farmField.getRipeId());
            int ripId = farmField.getRipeId();
            int addNum=p.getNum();//最终需要添加的数量
            //如果  是最后一次成熟 那么田地变成可以种植的状态
            if (farmField.getRipeId() == farmSeedRule.getProductList().size()) {
                /**
                 * 蜜蜂 鼹鼠 影响产量的
                 */
                int oneReduceNum=farmDerocateRuleContainer.getCropReduce(farmPlayer.getId());
                int oneAddNum=farmDerocateRuleContainer.getCropAdd(farmPlayer.getId());
                allAddNum+=oneAddNum;
                allReduceNum+=oneReduceNum;
                int pNum=oneAddNum-oneReduceNum;

                addNum+=pNum; //设置 最终产量

                farmField.setStatus(FarmConstant.FIELD_STATUS_FREE);
                farmField.getHarvestIds().clear();
                farmField.setRipeId(0);

                exp = farmSeedRule.getSeedsExp();
                //最后一次成熟农场 加经验
                farmLevelRuleContainer.addFarmExp(farmPlayer, farmSeedRule.getSeedsExp());

                /**
                 * 掉宝功能
                 */
                if (farmSeedRule.getDropItemList().size() > 0) {
                    for (DropItem dropItem : farmSeedRule.getDropItemList()) {
                        int randomNum = RandomUtil.range(0, 1000);
                        if (dropItem.getProbability() < randomNum) {
                            continue;
                        }
                        rewardVO.addGoodsVO(rsp.getGoodsVo(dropItem.getItemId(), dropItem.getNum()));
                        this.addGoodsNoSave(dropItem.getItemId(), dropItem.getNum(), farmPackage, roomPackege, fashionCupboard,farmDecoratePkg);
                    }
                }

            }


            /**
             * 添加 作物
             */
            BaseItemRule baseItemRule = getElement(farmSeedRule.getOutput());
            farmPackage.addItem(farmSeedRule.getOutput(), addNum, farmSeedRule.getOverlay(), baseItemRule.getValidDete());

            successRuleContainer.updateSuccessNum(farmPlayer.getId(),16,addNum,farmSeedRule.getOutput());

            rewardVO.setFarmExp(exp);
            rewardVO.addGoodsVO(rsp.getGoodsVo(farmSeedRule.getOutput(),addNum));
            BroadcastHolder.add(rewardVO);

//            //日志
//            String value = farmField.getId() + GameLogger.SPLIT + String.valueOf(ripId) + GameLogger.SPLIT + farmSeedRule.getOutput();
//            gameLoggerWrite.write(new GameLogger(GameLogger.LOG_10, farmPlayer.getId(), value));
        }
        if(allReduceNum>0){
            BroadcastHolder.add(new HintVO("很遗憾，这次虫害使您收获的农作物减少"+allReduceNum+"个"));
        }
        if(allAddNum>0){
            BroadcastHolder.add(new HintVO("很幸运，这次蜜蜂使您收获的农作物增加"+allAddNum+"个"));
        }


    }


    @Override
    public void eradicate(FarmPlayer farmPlayer, String fieldId) {
        FarmField farmField = farmPlayer.getOneFarmField(String.valueOf(fieldId));
        farmField.getHarvestIds().clear();
        farmField.setRipeId(0);
        farmField.setStatus(FarmConstant.FIELD_STATUS_FREE);
    }

    @Override
    public void helpFriendHarvest(FarmField farmField, FarmPackage myPackage, FarmPackage friendPackage, FarmFriendHarvest farmFriendHarvest, FarmFriendHarvest friendFarmFriendHarvest, FarmActionInfo farmActionInfo) {

        /**
         * 自己添加 帮好友收获次数
         */
        farmFriendHarvest.setNum(farmFriendHarvest.getNum() + 1);
        friendFarmFriendHarvest.setFriendNum(friendFarmFriendHarvest.getFriendNum() + 1);
        friendFarmFriendHarvest.setWeekFriendNum(friendFarmFriendHarvest.getWeekFriendNum() + 1);
        /**
         * 好友田地信息的 修改
         */
        FarmSeedRule farmSeedRule = (FarmSeedRule) this.getElement(farmField.getSeedId());
        farmField.getHarvestIds().add(farmField.getRipeId());
        Product p = farmSeedRule.getOneProduct(farmField.getRipeId());

        BaseItemRule baseItemRule = getElement(farmSeedRule.getOutput());
        /**
         * 自己添加 作物
         */
        int randomint = RandomUtil.range(0, 1000);
        if (farmSeedRule.getfProbability() > randomint) {
            myPackage.addItem(farmSeedRule.getOutput(), farmSeedRule.getCropsReward(), baseItemRule.getOverlay(), baseItemRule.getValidDete());

            RewardVO rewardVO = rsp.getRewardVO(1);
            rewardVO.setFieldId(Integer.parseInt(farmField.getId()));
            rewardVO.addGoodsVO(rsp.getGoodsVo(farmSeedRule.getOutput(), farmSeedRule.getCropsReward()));
            BroadcastHolder.add(rewardVO);
        }

        /**
         * 好友添加 作物
         */
        friendPackage.addItem(farmSeedRule.getOutput(), p.getNum(), baseItemRule.getOverlay(), baseItemRule.getValidDete());

        /**
         * 添加帮助好友 采摘日志
         */
        farmActionInfo.addAction(new FarmAction(myPackage.getId(), 3, farmSeedRule.getOutput(), p.getNum()));

        //日志
        String value = farmField.getId() + GameLogger.SPLIT + String.valueOf(farmField.getRipeId()) + GameLogger.SPLIT + farmSeedRule.getOutput() + GameLogger.SPLIT + friendPackage.getId();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_11, myPackage.getId(), value));
    }

    @Override
    public List<BaseItemRule> getAll() {
        return new ArrayList<BaseItemRule>(items.values());
    }


    @Override
    public boolean deductMoney(Player player, String itemId, int num) {
        BaseItemRule baseItemRule = this.getElement(itemId);
        if (baseItemRule == null) {
            return false;
        }
//        baseItemRule.deductMoney(player, num);
        if(baseItemRule.getGamePrice()>0){
            playerRuleContainer.decGameCoin(player,baseItemRule.getGamePrice()*num);
        }
        if(baseItemRule.getGoldPrice()>0){
            playerRuleContainer.decGoldCoin(player,baseItemRule.getGoldPrice()*num,PlayerConstant.GOLD_ACTION_18);
        }
        return true;
    }

    @Override
    public void addBuyItem(FarmPackage farmPackage, String itemId, int num) {
        BaseItemRule baseItemRule = this.getElement(itemId);
        farmPackage.addItem(itemId, num, baseItemRule.getOverlay(), baseItemRule.getValidDete());
    }

    @Override
    public BaseItemRule getItemTips(String itemId) {
        return getElement(itemId);
    }

    @Override
    public void addGoods(Player player, String itemId, int num) {
        if (itemId.equals(Constant.GAME_MONEY_ID)) { //加游戏币
//            player.incGameCoin(num);
            playerRuleContainer.incGameCoin(player,num);
            return;
        }
        BaseItemRule baseItemRule = this.getElement(itemId);
        if (baseItemRule == null) {
            return;
        }
        //农场物品
        if (baseItemRule.getType() == 1) {
            if(baseItemRule.getItemtype()==5||baseItemRule.getItemtype()==6){
               FarmDecoratePkg farmDecoratePkg=farmDecoratePkgDao.getFarmDecoratePkg(player.getId());
               farmDecoratePkg.addItem(itemId,num,baseItemRule.getOverlay(),baseItemRule.getValidDete());
                farmDecoratePkgDao.saveBean(farmDecoratePkg);
            }  else {
                FarmPackage farmPackage = farmPackageDao.getBean(player.getId());
                farmPackage.addItem(itemId, num, baseItemRule.getOverlay(), baseItemRule.getValidDete());
                farmPackageDao.saveBean(farmPackage);

                BroadcastHolder.add(rspFarmItemFactory.getFarmPackageByPid(farmPackage));
            }

        }

        //个人房间
        if (baseItemRule.getType() == 2) {
            RoomPackege roomPackege = roomPackegeDao.getRoomPackege(player.getId());
            roomPackege.addItem(itemId, num);
            roomPackegeDao.saveBean(roomPackege);

        }
        //时装道具
        if (baseItemRule.getType() == 3) {
            FashionCupboard fashionCupboard = fashionCupboardDao.getBean(player.getId());
            fashionCupboard.getFashionPkg(String.valueOf(baseItemRule.getItemtype())).addItem(itemId, num, baseItemRule.getOverlay(), baseItemRule.getValidDete());
            fashionCupboardDao.saveBean(fashionCupboard);
        }
        /**
         * 统计天线数量
         */
        if(baseItemRule.getItemsId().equals(FarmConstant.TIANXIN_ID)){
            statistics.addItemNumByFamily(player.getId(),baseItemRule.getItemsId(),num);
        }


    }

    @Override
    public int getGoodsNum(String pid, String itemId) {
        BaseItemRule baseItemRule = getElement(itemId);
        int mynum = 0;
        //农场物品
        if (baseItemRule.getType() == 1) {
            if(baseItemRule.getItemtype()==5||baseItemRule.getItemtype()==6){
                mynum = farmDecoratePkgDao.getBean(pid).getSumByNum(baseItemRule.getId());
            }  else {
                mynum = farmPackageDao.getBean(pid).getSumByNum(baseItemRule.getId());
            }

        }
        //个人房间
        if (baseItemRule.getType() == 2) {
            mynum = roomPackegeDao.getRoomPackege(pid).count(baseItemRule.getId());
        }
        //时装道具
        if (baseItemRule.getType() == 3) {
            mynum = fashionCupboardDao.getFashionPkg(pid, String.valueOf(baseItemRule.getItemtype())).getItemNumByStatus(itemId);
        }
        return mynum;
    }

    @Override
    public void useGoods(String pid, String itemId, int num) {
        BaseItemRule baseItemRule = getElement(itemId);
        //农场物品
        if (baseItemRule.getType() == 1) {

            if(baseItemRule.getItemtype()==5||baseItemRule.getItemtype()==6){
                FarmDecoratePkg farmDecoratePkg=farmDecoratePkgDao.getFarmDecoratePkg(pid);
                farmDecoratePkg.deduct(itemId,num);
                farmDecoratePkgDao.saveBean(farmDecoratePkg);
            }else {
                FarmPackage farmPackage = farmPackageDao.getBean(pid);
                farmPackage.deduct(baseItemRule.getId(), num);
                farmPackageDao.saveBean(farmPackage);
            }

        }

        //个人房间
        if (baseItemRule.getType() == 2) {
            RoomPackege roomPackege = roomPackegeDao.getRoomPackege(pid);
            for (int i = 0; i < num; i++) {
                roomPackege.useItem(baseItemRule.getId());
            }
            roomPackegeDao.saveBean(roomPackege);

        }
        //时装道具
        if (baseItemRule.getType() == 3) {
            FashionCupboard fashionCupboard = fashionCupboardDao.getBean(pid);
            fashionCupboard.getFashionPkg(String.valueOf(baseItemRule.getItemtype())).deduct(baseItemRule.getId(), num);
            fashionCupboardDao.saveBean(fashionCupboard);
        }

        /**
         * 统计天线数量
         */
        if(itemId.equals(FarmConstant.TIANXIN_ID)){
            statistics.addItemNumByFamily(pid,itemId,-num);
        }
    }

    @Override
    public void addGoodsNoSave(String itemId, int num, FarmPackage farmPackage, RoomPackege roomPackege, FashionCupboard fashionCupboard,FarmDecoratePkg farmDecoratePkg) {
        BaseItemRule baseItemRule = this.getElement(itemId);
        if (baseItemRule == null) {
            return;
        }
        //农场物品
        if (baseItemRule.getType() == 1) {
            if(baseItemRule.getItemtype()==5||baseItemRule.getItemtype()==6){ //农场内的 装饰品 背包
                farmDecoratePkg.addItem(baseItemRule.getItemsId(), num, baseItemRule.getOverlay(), baseItemRule.getValidDete());
            }else{
                farmPackage.addItem(itemId, num, baseItemRule.getOverlay(), baseItemRule.getValidDete());
                BroadcastHolder.add(rspFarmItemFactory.getFarmPackageByPid(farmPackage));
            }

        }

        //个人房间
        if (baseItemRule.getType() == 2) {
            roomPackege.addItem(itemId, num);

        }
        //时装道具
        if (baseItemRule.getType() == 3) {
            fashionCupboard.getFashionPkg(String.valueOf(baseItemRule.getItemtype())).addItem(itemId, num, baseItemRule.getOverlay(), baseItemRule.getValidDete());
        }
        /**
         * 统计天线数量
         */
        if(baseItemRule.getItemsId().equals(FarmConstant.TIANXIN_ID)){
            statistics.addItemNumByFamily(farmPackage.getPid(),baseItemRule.getItemsId(),num);
        }
    }
    @Override
    public boolean addItemAndCheckNoSaving(BaseItemRule baseItemRule, int num, FarmPackage farmPackage, RoomPackege roomPackege, FashionCupboard fashionCupboard,FarmDecoratePkg farmDecoratePkg) {

        //农场物品
        if (baseItemRule.getType() == BaseItemConstant.TYPE_FARM) {
            if(baseItemRule.getItemtype()==5||baseItemRule.getItemtype()==6){ //农场内的 装饰品 背包
                if(farmDecoratePkg==null){
                    return false;
                }
                farmDecoratePkg.addItem(baseItemRule.getItemsId(), num, baseItemRule.getOverlay(), baseItemRule.getValidDete());
            } else{
                if(null==farmPackage){
                    return false;
                }
                if(farmPackage.isMaxBySize(baseItemRule.getItemsId(),num,baseItemRule.getOverlay())){
                    return false;
                }else{
                    farmPackage.addItem(baseItemRule.getItemsId(), num, baseItemRule.getOverlay(), baseItemRule.getValidDete());
                    BroadcastHolder.add(rspFarmItemFactory.getFarmPackageByPid(farmPackage));
                }
            }

        }
        //个人房间
        else if (baseItemRule.getType() == BaseItemConstant.TYPE_ROOM) {
            if(null==roomPackege){
                return false;
            }
            roomPackege.addItem(baseItemRule.getItemsId(), num);
        }
        //时装道具
        else if (baseItemRule.getType() == BaseItemConstant.TYPE_FASHION) {
            if(null==fashionCupboard){
                return false;
            }
            FashionPkg fashionPkg = fashionCupboard.getFashionPkg(String.valueOf(baseItemRule.getItemtype()));
            if (fashionPkg.isMaxBySize(baseItemRule.getItemsId(), num, baseItemRule.getOverlay())) {
                return false;
            }else{
                fashionPkg.addItem(baseItemRule.getItemsId(), num, baseItemRule.getOverlay(), baseItemRule.getValidDete());
            }
     }
        /**
         * 统计天线数量
         */
        if(baseItemRule.getItemsId().equals(FarmConstant.TIANXIN_ID)){
            statistics.addItemNumByFamily(farmPackage.getPid(),baseItemRule.getItemsId(),num);
        }
        return true;
    }

    @Override
    public void checkBuy(List<String> itemIds, Player player) throws GameException {
        int gold = 0;
        int game = 0;
        for(String itemId:itemIds){
            BaseItemRule rule = this.get(itemId);
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
    public int getCapacity(List<ItemData> rewards) {
        int addBulk = 0;
        for (ItemData itemReward : rewards) {
            BaseItemRule baseItemRule = this.getElement(itemReward.getItemId());
            if (baseItemRule.getItemsId().equals(Constant.GAME_MONEY_ID)) {
                continue;
            }
            int num = itemReward.getNum();
            int max = baseItemRule.getOverlay();
            // 需要新增多少个格子
            int temp = num % max != 0 ? num / max + 1 : num / max;
            addBulk += temp;
        }
        return addBulk;
    }

    @Override
    public boolean checkFarmPackgeByCell(String pid, List<ItemData> rewards) {

        FarmPackage farmPackage = farmPackageDao.getBean(pid);
        int addSize = 0;
        for (ItemData itemReward : rewards) {
            BaseItemRule baseItemRule = this.getElement(itemReward.getItemId());
            //农场物品
            if (baseItemRule.getType() == 1) {
                if (baseItemRule.getItemsId().equals(Constant.GAME_MONEY_ID)) {
                    continue;
                }
                int num = itemReward.getNum();
                int max = baseItemRule.getOverlay();
                int addBulk = num % max != 0 ? num / max + 1 : num / max;
                addSize += addBulk;
            }
        }
        return (farmPackage.outlayByItemSize() + addSize) > farmPackage.getSize();
    }

    @Override
    public boolean checkFarmPackge(String pid, List<ItemData> rewards) {
        boolean max = false;
        FarmPackage farmPackage = farmPackageDao.getBean(pid);
        for (ItemData itemReward : rewards) {
            BaseItemRule baseItemRule = this.getElement(itemReward.getItemId());
            //农场物品
            if (baseItemRule.getType() == 1) {
                if (farmPackage.isMaxBySize(itemReward.getItemId(), itemReward.getNum(), baseItemRule.getOverlay())) {
                    max = true;
                    break;
                }

                farmPackage.addItem(itemReward.getItemId(), itemReward.getNum(), baseItemRule.getOverlay(), baseItemRule.getValidDete());
            }
        }
        return max;
    }

    @Override
    public boolean checkFashionPackge(String pid, List<ItemData> rewards) {
        boolean max = false;
        FashionCupboard fashionCupboard = fashionCupboardDao.getBean(pid);
        for (ItemData itemReward : rewards) {
            BaseItemRule baseItemRule = this.getElement(itemReward.getItemId());
            //时装道具
            if (baseItemRule.getType() == 3) {
                FashionPkg fashionPkg = fashionCupboard.getFashionPkg(String.valueOf(baseItemRule.getItemtype()));
                if (fashionPkg.isMaxBySize(itemReward.getItemId(), itemReward.getNum(), baseItemRule.getOverlay())) {
                    max = true;
                    break;
                }
                fashionPkg.addItem(itemReward.getItemId(), itemReward.getNum(), baseItemRule.getOverlay(), baseItemRule.getValidDete());
            }
        }
        return max;
    }

    @Override
    public int getMaxHelpFriendNum(String pid) {
        int maxNum = FarmConstant.HELP_ALL_NUM;
        //如果是VIP
        if (payMemberRuleContainer.isPayMember(pid)) {
            maxNum = payMemberRuleContainer.getHelpMaxNum(pid);
        }
        return maxNum;
    }


}
