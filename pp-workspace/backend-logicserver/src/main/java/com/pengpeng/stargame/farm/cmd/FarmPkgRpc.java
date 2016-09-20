package com.pengpeng.stargame.farm.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmWareHouseContainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.farm.rule.FarmWareHouseRule;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.manager.IFinder;
import com.pengpeng.stargame.model.farm.FarmItem;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.cmd.PlayerRpc;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.req.BaseReq;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspFarmItemFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.BaseShopItemVO;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.farm.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 包裹 , 仓库
 *
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-25 下午2:59
 */
@Component()
public class FarmPkgRpc extends RpcHandler {
    private static final Logger logger = Logger.getLogger(FarmPkgRpc.class);

    @Autowired
    private IPlayerDao playerDao;

    @Autowired
    private FrontendServiceProxy frontendService;

    @Autowired
    private IFarmPackageDao farmPackageDao;

    @Autowired
    private IFarmPlayerDao farmPlayerDao;

    // 农场规则
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;

    // 仓库规则
    @Autowired
    private IFarmWareHouseContainer farmWareHouseContainer;

    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private RspFarmItemFactory rspFarmItemFactory;

    @Autowired
    private PlayerRpc playerRpc;

    @Autowired
    private RspRoleFactory roleFactory;

    @Autowired
    private GameLoggerWrite gameLoggerWrite;

    @RpcAnnotation(cmd = "farm.goodsNum", vo =Integer.class, req = FarmIdReq.class, name = "查询物品的数量")
    public GoodsVO goodsNum(Session session,FarmIdReq req){

        GoodsVO goodsVO=new GoodsVO();
        BaseItemRule baseItemRule=baseItemRulecontainer.getElement(req.getItemId());
        goodsVO.setMyNum(baseItemRulecontainer.getGoodsNum(session.getPid(),req.getItemId()));
        goodsVO.setItemId(req.getItemId());
        goodsVO.setIcon(baseItemRule.getIcon());
        return goodsVO;
    }

    /**
    * 取得商店列表
    *
    * @param session
    * @return
    */
    @RpcAnnotation(cmd = "farm.get.warehouse", vo = FarmShopItemVO[].class, req = FarmIdReq.class, name = "取得商店列表",lock = false)
    public FarmShopItemVO[] getPackageInfo(Session session, FarmIdReq req) throws GameException {
        // 1.获取商店列表
        List<BaseItemRule> list = baseItemRulecontainer.getAll();
        List<BaseItemRule> list1 = new ArrayList<BaseItemRule>();
        for (BaseItemRule baseItemRule : list) {
            if (baseItemRule.getType() == 1) {
                list1.add(baseItemRule);
            }
        }
        return rspFarmItemFactory.getFarmShopItemVOArr(list1);
    }

    /**
     * 已废弃
     *
     * @param session
     * @param req
     * @return
     */
    @Deprecated
    @RpcAnnotation(cmd = "farmpkg.isAddItem", req = FarmIdReq.class, name = "是否可增加物品")
    public boolean isAddItem(Session session, FarmIdReq req) {
        // 1.物品是否可以买卖
        return false;
    }

    /**
     * 购买
     *
     * @param session
     * @param req     itemid,num
     */
    @RpcAnnotation(cmd = "farmpkg.buy", vo = FarmPkgVO.class, req = FarmItemReq.class, name = "购买物品", lock = true)
    public FarmPkgVO addItem(Session session, FarmItemReq req) throws GameException, AlertException {
        // 1.物品是否可以购买
        // 2.物品等级是否可以购买
        // 3.用户购买金额是否足够
        // 4.购买物品
        // 5.更新数据
        String pid = req.getPid();
        Integer num = req.getNum();
        String itemId = req.getItemId();
        if (num == null || num<0) {
            num = 1;
        }

        Player player = playerDao.getBean(pid);
        FarmPackage farmPackage = this.farmPackageDao.getBean(pid);
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());
        baseItemRulecontainer.checkPackegeSize(farmPackage, itemId, num);

        baseItemRulecontainer.checkBuy(itemId);

        baseItemRulecontainer.checkLevel(itemId, farmPlayer.getLevel());

        baseItemRulecontainer.checkMoney(player, itemId, num);

        baseItemRulecontainer.deductMoney(player, itemId, num);
        baseItemRulecontainer.addBuyItem(farmPackage, itemId, num);

        playerDao.saveBean(player);
        this.farmPackageDao.saveBean(farmPackage);

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));


        /**
         * 添加屏幕 右下角 提示信息
         */
        BaseItemRule baseItemRule = baseItemRulecontainer.getElement(itemId);
        RewardVO rewardVO = new RewardVO(6);
        rewardVO.setGold(baseItemRule.getGamePrice() * num);
        rewardVO.setRmb(baseItemRule.getGoldPrice() * num);
        rewardVO.addGoodsVO(new GoodsVO(itemId, baseItemRule.getName(), num, 0, ""));
        frontendService.broadcast(mysessions, rewardVO);

        //日志
        String value = itemId + GameLogger.SPLIT + String.valueOf(num)+ GameLogger.SPLIT + baseItemRule.getType()+GameLogger.SPLIT + baseItemRule.getItemtype();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_15, player.getId(), value));

        return getItemAll(session, null);
    }

    /**
     * 卖出物品
     *
     * @param session
     * @param req     itemid ,num
     */
    @RpcAnnotation(cmd = "farmpkg.removeSome", vo = FarmPkgVO.class, req = FarmItemReq.class, name = "卖出一些物品", lock = true)
    public FarmPkgVO saleItemsome(Session session, FarmItemReq req) throws GameException, AlertException {
        // 1.物品是否可以卖出
        // 2.卖出物品
        // 3.卖出后,更新数据

        String pid = session.getPid();

        Player player = playerDao.getBean(pid);
        FarmPackage farmPackage = this.farmPackageDao.getBean(pid);
        Map<String,FarmItem> tempMap=new HashMap<String, FarmItem>();
        for (String tid : req.getIds()) {
            FarmItem farmItem = farmPackage.getFarmItem(tid);
            tempMap.put(tid,farmItem);
        }
        for (String tid : req.getIds()) {
            FarmItem farmItem = farmPackage.getFarmItem(tid);
            baseItemRulecontainer.checkSales(farmPackage, tid, farmItem.getItemId(), farmItem.getNum());
        }

        for (String tid : req.getIds()) {
            FarmItem farmItem = farmPackage.getFarmItem(tid);
            baseItemRulecontainer.salesFram(player, farmPackage, farmItem.getItemId(), tid, farmItem.getNum());
        }

        playerDao.saveBean( player);
        this.farmPackageDao.saveBean(farmPackage);

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));

        int sum=0;
        for (String tid : req.getIds()) {
            FarmItem farmItem = tempMap.get(tid);
            /**
             * 添加屏幕 右下角 提示信息
             */
            BaseItemRule baseItemRule = baseItemRulecontainer.getElement(farmItem.getItemId());
            sum+= baseItemRule.getRecyclingPrice() * farmItem.getNum() ;

            //日志
            String value = farmItem.getItemId() + GameLogger.SPLIT + String.valueOf(farmItem.getNum())+ GameLogger.SPLIT + baseItemRule.getType()+GameLogger.SPLIT + baseItemRule.getItemtype();
            gameLoggerWrite.write(new GameLogger(GameLogger.LOG_16, player.getId(), value));
        }

            /**
             * 添加屏幕 右下角 提示信息
             */
            RewardVO rewardVO = new RewardVO(10);
            rewardVO.setGold(sum);
            frontendService.broadcast(mysessions, rewardVO);



        return getItemAll(session, null);
    }

    /**
     * 卖出物品
     *
     * @param session
     * @param req     itemid ,num
     */
    @RpcAnnotation(cmd = "farmpkg.remove", vo = FarmPkgVO.class, req = FarmItemReq.class, name = "卖出物品", lock = true)
    public FarmPkgVO saleItem(Session session, FarmItemReq req) throws GameException, AlertException {
        // 1.物品是否可以卖出
        // 2.卖出物品
        // 3.卖出后,更新数据

        String pid = session.getPid();
        Integer num = req.getNum();
        String itemId = req.getItemId();
        String id = req.getId();

        if (num == null) {
            num = 1;
        }

        Player player = playerDao.getBean(pid);
        FarmPackage farmPackage = this.farmPackageDao.getBean(pid);

        baseItemRulecontainer.checkSales(farmPackage, id, itemId, num);

        baseItemRulecontainer.salesFram(player, farmPackage, itemId, id, num);

        playerDao.saveBean(player);
        this.farmPackageDao.saveBean(farmPackage);

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));


        /**
         * 添加屏幕 右下角 提示信息
         */
        BaseItemRule baseItemRule = baseItemRulecontainer.getElement(itemId);
        RewardVO rewardVO = new RewardVO(7);
        rewardVO.setGold(baseItemRule.getRecyclingPrice() * num);
        rewardVO.addGoodsVO(new GoodsVO(itemId, baseItemRule.getName(), num, 0, ""));
        frontendService.broadcast(mysessions, rewardVO);


        //日志
        String value = itemId + GameLogger.SPLIT + String.valueOf(num)+ GameLogger.SPLIT + baseItemRule.getType()+GameLogger.SPLIT + baseItemRule.getItemtype();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_16, player.getId(), value));

        return getItemAll(session, null);
    }

    /**
     * 取得物品
     *
     * @param session
     * @param req
     */
    @RpcAnnotation(cmd = "farmpkg.get", vo = FarmItemVO.class, req = FarmItemReq.class, name = "取得玩家一个物品",lock = false)
    public FarmItemVO getItem(Session session, FarmItemReq req) throws GameException {
        String pid = session.getPid();
        String itemId = req.getItemId();
        FarmPackage farmPackage = this.farmPackageDao.getBean(pid);
        return rspFarmItemFactory.getFarmItemByItemId(farmPackage, itemId);
    }


    @RpcAnnotation(cmd = "farmpkg.get.all", vo = FarmPkgVO.class, req = FarmItemReq.class, name = "取得玩家仓库信息",lock = false)
    public FarmPkgVO getItemAll(Session session, FarmItemReq req) {
        // 1.获取用户的仓库内容
        // 2.统一放到一个LIST里面，虚拟成仓库
        String pid = session.getPid();
        return getItemByPid(pid);
    }

    @RpcAnnotation(cmd = "farmpkg.clearup", vo = FarmPkgVO.class, req = FarmItemReq.class, name = "整理背包",lock = false)
    public FarmPkgVO clearup(Session session, FarmItemReq req) {
        FarmPackage farmPackage = farmPackageDao.getBean(session.getPid());
        Set<String> strings=new HashSet<String>();
        for(FarmItem farmItem:farmPackage.getItemAll()){
            BaseItemRule baseItemRule = baseItemRulecontainer.getElement(farmItem.getItemId());
            if(farmItem.getNum()<baseItemRule.getOverlay()){
                strings.add(farmItem.getItemId());
            }
        }
        for(String itemId:strings){
            BaseItemRule baseItemRule = baseItemRulecontainer.getElement(itemId);
            farmPackage.arrangeItem(itemId,baseItemRule.getOverlay());
        }
        farmPackageDao.saveBean(farmPackage);
        return getItemByPid(session.getPid());
    }


    public FarmPkgVO getItemByPid(String pid) {
        FarmPackage farmPackage = farmPackageDao.getBean(pid);
        return rspFarmItemFactory.getFarmPackageByPid(farmPackage);
    }

    @RpcAnnotation(cmd = "farmpkg.get.fortype", vo = FarmItemVO[].class, req = FarmItemReq.class, name = "取得玩家指定的仓库物品集合")
    public FarmItemVO[] getForType(Session session, FarmItemReq req) {
        String pid = session.getPid();
        final int subType = req.getSubType().intValue();

        FarmPackage farmPackage = this.farmPackageDao.getBean(pid);

        List<FarmItem> items = farmPackage.getItemAll();

        List<BaseItemRule> rules = baseItemRulecontainer.find(new IFinder<BaseItemRule>() {
            @Override
            public boolean match(int index, BaseItemRule item) {
                return item.getItemtype() == subType;
            }
        });

        List<FarmItem> list = new ArrayList<FarmItem>();

        if (items != null && !items.isEmpty() && rules != null && !rules.isEmpty()) {
            for (FarmItem farmItem : items) {
                for (BaseItemRule baseItemRule : rules) {
                    if (farmItem.getItemId().equals(baseItemRule.getItemsId())) {
                        list.add(farmItem);
                        break;
                    }
                }
            }
        }
        return rspFarmItemFactory.getFarmItem(list);
    }

    @RpcAnnotation(cmd = "farmpkg.up.level", vo = FarmPkgVO.class, req = FarmIdReq.class, name = "升级仓库", lock = true)
    public FarmPkgVO levelUp(Session session, FarmIdReq req) throws GameException {
        // 1.仓库升级条件是否达到
        // 2.升级仓库
        String pid = session.getPid();

        FarmPackage farmPackage = this.farmPackageDao.getBean(pid);

        final int level = farmPackage.getLevel();
        if (this.farmWareHouseContainer.checkMaxLevel(level)) {
            exceptionFactory.throwRuleException("farmpkg.maxlevel");
        }

        if (!this.farmWareHouseContainer.checkNextLevel(farmPackage)) {
            exceptionFactory.throwRuleException("farmpkg.uplevel.condition");
        }

        this.farmWareHouseContainer.deduct(farmPackage);
        this.farmWareHouseContainer.upgrade(farmPackage);

        this.farmPackageDao.saveBean(farmPackage);
        FarmPkgVO vo = rspFarmItemFactory.getFarmPackageByPid(farmPackage);
        return vo;
    }

    @RpcAnnotation(cmd = "farmpkg.item.tip", vo = FarmShopItemVO.class, req = FarmItemReq.class, name = "取得物品详细信息")
    public FarmShopItemVO getFarmItemTip(Session session, FarmItemReq req) throws GameException {
        String itemId = req.getItemId();
        BaseItemRule baseItemRule = this.baseItemRulecontainer.getItemTips(itemId);
        if (baseItemRule == null) {
            exceptionFactory.throwRuleException("farm.item.notfound");
        }
        return rspFarmItemFactory.getFarmShopItemVO(baseItemRule);
    }

    @RpcAnnotation(cmd = "goods.item.tip", vo = FarmShopItemVO.class, req = IdReq.class, name = "取得物品详细信息")
    public BaseShopItemVO getGoodsTip(Session session, IdReq req) throws GameException {
        String itemId = req.getId();
        if(itemId==null){
            return null;
        }
        BaseItemRule baseItemRule = this.baseItemRulecontainer.getItemTips(itemId);
        if (baseItemRule == null) {
            exceptionFactory.throwRuleException("farm.item.notfound");
        }
        return rspFarmItemFactory.getGoodsVO(baseItemRule);
    }

    @RpcAnnotation(cmd = "farmpkg.level.item", vo = FarmPkgLevelVO[].class, req = FarmItemReq.class, name = "升级仓库下一级物品")
    public FarmPkgLevelVO[] levelItem(Session session, FarmItemReq req) throws GameException {
        String pid = session.getPid();
        FarmPackage farmPackage = this.farmPackageDao.getBean(pid);

        if (this.farmWareHouseContainer.checkMaxLevel(farmPackage.getLevel())) {
            exceptionFactory.throwRuleException("farmpkg.maxlevel");
        }

        List<FarmWareHouseRule.Items> list = this.farmWareHouseContainer.getNextLevelByItems(farmPackage);
        if (list == null || list.isEmpty()) {
            exceptionFactory.throwRuleException("farmpkg.uplevel.notfound");
        }

        return rspFarmItemFactory.getLevelItem(farmPackage, list);
    }

}
