package com.pengpeng.stargame.farm.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmDecorateRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmDecorateDao;
import com.pengpeng.stargame.farm.dao.IFarmDecoratePkgDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.farm.rule.FarmDecorateRule;
import com.pengpeng.stargame.model.farm.FarmItem;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.farm.decorate.FarmDecorate;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.farm.decorate.OneDecorate;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspFarmFactory;
import com.pengpeng.stargame.rsp.RspFarmItemFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.farm.FarmItemReq;
import com.pengpeng.stargame.vo.farm.FarmPkgVO;
import com.pengpeng.stargame.vo.farm.GoodsVO;
import com.pengpeng.stargame.vo.farm.decorate.FarmDecoratePkgVO;
import com.pengpeng.stargame.vo.farm.decorate.FarmDecorateReq;
import com.pengpeng.stargame.vo.farm.decorate.FarmDecorateShopItemVO;
import com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO;
import com.pengpeng.stargame.vo.room.DecorateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * User: mql
 * Date: 14-3-13
 * Time: 下午4:01
 * 农场装扮 功能
 */
@Component
public class FarmDecorateRpc extends RpcHandler {
    @Autowired
    private IFarmDecorateDao farmDecorateDao;
    @Autowired
    private RspFarmFactory rspFarmFactory;
    @Autowired
    private IFarmDecorateRuleContainer farmDerocateRuleContainer;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFarmDecoratePkgDao farmDecoratePkgDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private FarmRpc farmRpc;
    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private RspFarmItemFactory rspFarmItemFactory;
    @Autowired
    private MessageSource message;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;

    @RpcAnnotation(cmd = "farm.decorate.itemNum", req = FarmDecorateReq.class, name = "取得农场内的装饰品装饰品的数量")
    public GoodsVO getDecorateNum(Session session, FarmDecorateReq req) {
        GoodsVO goodsVO=new GoodsVO();
        FarmDecorate farmDecorate = farmDecorateDao.getFarmDecorate(session.getPid());
        goodsVO.setMyNum(farmDecorate.getNumByItemId(req.getItemId()));
        goodsVO.setItemId(req.getItemId());
        return goodsVO;
    }
    @RpcAnnotation(cmd = "farm.decorate.list", req = FarmDecorateReq.class, name = "取得农场内的装饰品 列表")
    public FarmDecorateShopItemVO[] getShopList(Session session, FarmDecorateReq req) {
        List<FarmDecorateRule> farmDecorateRuleList = new ArrayList<FarmDecorateRule>(farmDerocateRuleContainer.values());
        return rspFarmFactory.geFarmDecorateShopItemVOArray(farmDecorateRuleList);
    }

    @RpcAnnotation(cmd = "farm.decorate.pkg", vo = FarmPkgVO.class, req = FarmDecorateReq.class, name = "取得农场装饰 背包",lock = false)
    public FarmDecoratePkgVO getItemAll(Session session, FarmDecorateReq req) {
        FarmDecoratePkg farmDecoratePkg=farmDecoratePkgDao.getFarmDecoratePkg(session.getPid());
        return rspFarmItemFactory.getFarmDecoratePkg(farmDecoratePkg);
    }

    @RpcAnnotation(cmd = "farm.decorate.info", req = FarmDecorateReq.class, name = "农场装饰信息", vo = FarmDecorateVO.class)
    public FarmDecorateVO getFarmDecorateInfo(Session session, FarmDecorateReq req) throws GameException {
        FarmDecorate farmDecorate = farmDecorateDao.getFarmDecorate(req.getFarmId());
        if (session.getPid().equals(req.getFarmId())) { //如果是自己访问，看有没有提示信息 返回
           List<String> list=farmDerocateRuleContainer.getHints(farmDecorate);
           farmDecorateDao.saveBean(farmDecorate);
            return rspFarmFactory.getFarDecorateVo(farmDecorate, list.toArray(new String[0]));
        }
        return rspFarmFactory.getFarDecorateVo(farmDecorate, null);
    }
    @RpcAnnotation(cmd = "farm.decorate.add", req = FarmDecorateReq.class, name = "添加装饰信息", vo = FarmDecorateVO.class)
    public FarmDecorateVO add(Session session, FarmDecorateReq req) throws GameException {
        FarmDecorate farmDecorate = farmDecorateDao.getFarmDecorate(req.getFarmId());
        List<String> list=new ArrayList<String>();
        list.add("items_15013");
        if(!list.contains(req.getItemId())){
            return null;
        }
        if(farmDecorate.getNumByItemId(req.getItemId())>=1){
            return null;
        }
        farmDecorate.add( new OneDecorate(null,req.getItemId(), req.getPosition(),null));
        farmDecorateDao.saveBean(farmDecorate);
        return rspFarmFactory.getFarDecorateVo(farmDecorate, null);
    }
    @RpcAnnotation(cmd = "farm.decorate.clear", req = FarmDecorateReq.class, name = "清除农场物件", vo = FarmDecorateVO.class)
    public FarmDecorateVO clear(Session session, FarmDecorateReq req) throws AlertException, GameException {
        FarmDecorate farmDecorate = farmDecorateDao.getFarmDecorate(session.getPid());
        Player player=playerDao.getBean(session.getPid());
        OneDecorate oneDecorate=farmDecorate.getOneDecorate(req.getId());
        if(oneDecorate==null){
            exceptionFactory.throwAlertException("farm.decorate.no");
        }
        FarmDecorateRule farmDecorateRule = farmDerocateRuleContainer.getElement(oneDecorate.getiId());
        if (farmDecorateRule.getRemove() != 1) {
            exceptionFactory.throwAlertException("farm.decorate.noclear");
        }
        List<String> removeList = new ArrayList<String>();//本次 清除的 物品列表
        removeList.add(oneDecorate.getiId());
        farmDerocateRuleContainer.checkAndBuy(null, new ArrayList<String>(), removeList, player, farmDecorate);
        playerDao.saveBean(player);
        farmDecorate.remove(oneDecorate.getId());
        farmDecorateDao.saveBean(farmDecorate);
        //广播钱的变化
        frontendService.broadcast(session,roleFactory.newPlayerVO(player));

        return rspFarmFactory.getFarDecorateVo(farmDecorate,null);
    }
        @RpcAnnotation(cmd = "farm.decorate.save", req = FarmDecorateReq.class, name = "编辑农场 保存", vo = FarmDecorateVO.class)
    public FarmDecorateVO save(Session session, FarmDecorateReq req) throws AlertException, GameException {

        DecorateVO[] decorateVOs = req.getDecorateVOs();
        if (decorateVOs == null || decorateVOs.length == 0) {
            exceptionFactory.throwAlertException("请求参数不对");
        }
        FarmDecorate farmDecorate = farmDecorateDao.getFarmDecorate(session.getPid());
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(session.getPid(), System.currentTimeMillis());
        //农场装饰 品包括小动物的背包
        FarmDecoratePkg chekfarmPackage = farmDecoratePkgDao.getBean(session.getPid());

        Player player = playerDao.getBean(session.getPid());
        List<String> addList=new ArrayList<String>();//本次编辑所有新加的Id
        List<String> buyList = new ArrayList<String>();//本次购买 的 物品列表
        List<String> removeList = new ArrayList<String>();//本次 清除的 物品列表
        for (DecorateVO decorateVO : decorateVOs) {
            // 新加  优先使用背包
            if (decorateVO.getChangeType() == 1) {
//                addList.add(decorateVO.getItemId());
//                if (!chekfarmPackage.existByItemId(decorateVO.getItemId())) {
//                    buyList.add(decorateVO.getItemId());
//                } else {
//                    chekfarmPackage.deduct(decorateVO.getItemId(), 1);
//                }
                addList.add(decorateVO.getItemId());
                if(decorateVO.getAddType()==1){
                    buyList.add(decorateVO.getItemId());
                } else if(decorateVO.getAddType()==2){
                    if(!chekfarmPackage.existById(decorateVO.getItemGid())){
                        exceptionFactory.throwAlertException("闲置仓库内，不存在要装饰的物品！");
                    }
//                    FarmItem farmItem=chekfarmPackage.getFarmItem(decorateVO.getId());
//                    BaseItemRule baseItemRule=baseItemRulecontainer.getElement(farmItem.getItemId());
//                    if(baseItemRule.getValidDete()!=null){//有时效性的 物品
//                       chekfarmPackage.clearItemByKey(decorateVO.getId());
//                    }else {
//                        chekfarmPackage.deduct(farmItem.getItemId(),1);
//                    }
                    chekfarmPackage.deductByGid(decorateVO.getItemGid(),1);
                } else{
                    exceptionFactory.throwAlertException("请求参数不对");
                }
            }
            // 修改   需要判断 客户端传过来的Id 是否是已经有的了
            if (decorateVO.getChangeType() == 2) {
                if(decorateVO.getItemId().equals(FarmConstant.FIELD_ID)){  //农田特殊处理
                    if(farmPlayer.getOneFarmField(decorateVO.getId())==null){
                        exceptionFactory.throwAlertException("请求参数不对， 田地" + decorateVO.getId() + "不存在！");
                    }
                }  else{
                    if (!farmDecorate.getItems().containsKey(decorateVO.getId())) {
                        exceptionFactory.throwAlertException("请求参数不对，修改类型是 2，但是" + decorateVO.getId() + "不存在！");
                    }
                }

            }
            // 回收
            if (decorateVO.getChangeType() == 3) {
                if(decorateVO.getItemId().equals(FarmConstant.FIELD_ID)){
                    if(farmPlayer.getOneFarmField(decorateVO.getId())==null){
                        exceptionFactory.throwAlertException("请求参数不对， 田地" + decorateVO.getId() + "不存在！");
                    }
                } else {
                    if (!farmDecorate.getItems().containsKey(decorateVO.getId())) {
                        exceptionFactory.throwAlertException("请求参数不对，修改类型是 3，但是" + decorateVO.getId() + "不存在！");
                    }
                }

            }
            // 卖出
            if (decorateVO.getChangeType() == 4) {
                farmDerocateRuleContainer.getElement(decorateVO.getItemId()).checkSales();
            }
            // 清除
            if (decorateVO.getChangeType() == 5) {
                FarmDecorateRule farmDecorateRule = farmDerocateRuleContainer.getElement(decorateVO.getItemId());
                if (farmDecorateRule.getRemove() != 1) {
                    exceptionFactory.throwAlertException("物品不能清除！");
                }
                removeList.add(decorateVO.getItemId());
            }

        }

        FarmDecoratePkg farmDecoratePkg = farmDecoratePkgDao.getFarmDecoratePkg(session.getPid());
        /**
         * 本次操作 达人币、游戏币的、土地的 判断
         */
        farmDerocateRuleContainer.checkAndBuy(farmPlayer, buyList, removeList, player, farmDecorate);

        for (DecorateVO decorateVO : decorateVOs) {
            BaseItemRule baseItemRule = baseItemRulecontainer.getElement(decorateVO.getItemId());
            // 新加
            if (decorateVO.getChangeType() == 1) {
                farmDerocateRuleContainer.addDecorate(farmDecorate, decorateVO, farmPlayer,farmDecoratePkg);
            }
            // 修改
            if (decorateVO.getChangeType() == 2) {
                farmDerocateRuleContainer.updateDecorate(farmDecorate,decorateVO,farmPlayer);
            }
            // 回收
            if (decorateVO.getChangeType() == 3) {
                farmDerocateRuleContainer.recycleDecorate(farmDecorate, farmDecoratePkg, decorateVO, farmPlayer);
            }
            // 卖出
            if (decorateVO.getChangeType() == 4) {
                if (decorateVO.getId().equals("0")) {   //从闲置仓库内托进去 又卖出
                    farmDecoratePkg.deduct(decorateVO.getItemId(), 1);
//                    player.incGameCoin(baseItemRule.getRecyclingPrice());
                    playerRuleContainer.incGameCoin(player,baseItemRule.getRecyclingPrice());
                } else {
                    OneDecorate dp = farmDecorate.getOneDecorate(decorateVO.getId());
                    if (dp == null) {
                        exceptionFactory.throwAlertException("没有此物品");
                    }
                    farmDecorate.remove(dp.getId());
//                    player.incGameCoin(baseItemRule.getRecyclingPrice());
                    playerRuleContainer.incGameCoin(player,baseItemRule.getRecyclingPrice());
                }
            }
            // 清除
            if (decorateVO.getChangeType() == 5) {
                OneDecorate dp = farmDecorate.getOneDecorate(decorateVO.getId());
                if (dp == null) {
                    exceptionFactory.throwAlertException("没有此物品");
                }
                farmDecorate.remove(dp.getId());
            }

        }
        /**
         * 小动物的处理   如果 放入小鸟，农场有瓢虫，那么删除所有瓢虫
         * 如果是小鸡   立刻减少一个瓢虫
         */
        List<String> hints=new ArrayList<String>();
         if(addList.contains(FarmConstant.X_N)){
             int pNum=farmDecorate.getNumByItemId(FarmConstant.P_C);
             if(pNum>0){
//                 hints.add("小鸟在你的农场成功吃光了所有的瓢虫!");
                 hints.add(message.getMessage("farm.decorate.animal1",null, Locale.CHINA));
                 farmDecorate.deleteByItemId(FarmConstant.P_C, pNum);
             }
         }
        if(addList.contains(FarmConstant.X_J)){
            int pNum=farmDecorate.getNumByItemId(FarmConstant.P_C);
            if(pNum>0){
//                hints.add("母鸡在您农场吃掉了 "+1+" 只瓢虫!");
                hints.add(message.getMessage("farm.decorate.animal2",new String[]{"1"}, Locale.CHINA));
                farmDecorate.deleteByItemId(FarmConstant.P_C,1);
            }
        }
        /**
         * 保存数据
         */
        playerDao.saveBean(player);
        farmDecoratePkgDao.saveBean(farmDecoratePkg);
        farmDecorateDao.saveBean(farmDecorate);
        farmPlayerDao.saveBean(farmPlayer);


        //广播农田的 变化
        frontendService.broadcast(session, farmRpc.getFarmVoByPlayerId(session.getPid()));
        //广播钱的变化
        frontendService.broadcast(session,roleFactory.newPlayerVO(player));
        return rspFarmFactory.getFarDecorateVo(farmDecorate, hints.toArray(new String[0]));
    }

    @RpcAnnotation(cmd = "farm.decorate.tip", vo = FarmDecorateShopItemVO.class, req = FarmDecorateReq.class, name = "取得物品详细信息 Tip编辑的时候 用")
    public FarmDecorateShopItemVO getItemTip(Session session, FarmDecorateReq req) throws GameException {
        if(req.getItemId()==null){
            return null;
        }
        FarmDecorateRule farmDecorateRule = farmDerocateRuleContainer.getElement(req.getItemId());
        if(farmDecorateRule==null){
            return null;
        }
        return rspFarmFactory.getFarmShopItemVO(farmDecorateRule);
    }


    @RpcAnnotation(cmd = "farm.animal.operation", vo = FarmDecorateVO.class, req = FarmDecorateReq.class, name = "操作 打 鼹鼠（地鼠）")
    public FarmDecorateVO animalOperation(Session session, FarmDecorateReq req) throws GameException {
        FarmDecorate farmDecorate = farmDecorateDao.getFarmDecorate(req.getFarmId());
        Player player = playerDao.getBean(session.getPid());
        farmDerocateRuleContainer.animalOperation(farmDecorate, req.getId());
        farmDecorateDao.saveBean(farmDecorate);
        List<String> hints = new ArrayList<String>();
        if (session.getPid().equals(req.getFarmId())) {
//            hints.add("地鼠被驱赶出农场，避免了一些损失!");
            hints.add(message.getMessage("farm.decorate.animal3",null, Locale.CHINA));
        }
        FarmDecorateVO farmDecorateVO = rspFarmFactory.getFarDecorateVo(farmDecorate, hints.toArray(new String[0]));
        //给好友广播 农场装饰信息的 变化
        if (!session.getPid().equals(req.getFarmId())) {
            Session sessionF=statusRemote.getSession(session,req.getFarmId());
            if(session!=null){
//                hints.add(player.getNickName() + " 在农场帮你驱赶了1只地鼠，避免了一些损失");
                hints.add(message.getMessage("farm.decorate.animal4",new String[]{player.getNickName()}, Locale.CHINA));
                FarmDecorateVO farmDecorateVO1 = rspFarmFactory.getFarDecorateVo(farmDecorate, hints.toArray(new String[0]));
                frontendService.broadcast(sessionF, farmDecorateVO1);
            }
        }
        return farmDecorateVO;
    }

}
