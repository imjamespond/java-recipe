package com.pengpeng.stargame.stall.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.cmd.FarmRpc;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.stall.*;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.rsp.RspStallFactory;
import com.pengpeng.stargame.stall.container.IStallFriShelfContainer;
import com.pengpeng.stargame.stall.container.IStallGoldShelfContainer;
import com.pengpeng.stargame.stall.container.IStallPriceContainer;
import com.pengpeng.stargame.stall.dao.IPlayerStallDao;
import com.pengpeng.stargame.stall.dao.IStallAssistantDao;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.vo.farm.FarmItemVO;
import com.pengpeng.stargame.vo.stall.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-12-11
 * Time: 下午5:04
 */
@Component
public class StallRpc extends RpcHandler {
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFriendDao friendDao;

    @Autowired
    private IPlayerStallDao playerStallDao;
    @Autowired
    private IStallAssistantDao stallAssistantDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private RspStallFactory rspStallFactory;
    @Autowired
    private IStallPriceContainer stallPriceContainer;
    @Autowired
    private IStallGoldShelfContainer stallGoldShelfContainer;
    @Autowired
    private IStallFriShelfContainer stallFriShelfContainer;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private FarmRpc farmRpc;
    @Autowired
    private IExceptionFactory exceptionFactory;


    @RpcAnnotation(cmd = "stall.enable", vo = StallInfoVO.class, req = StallReq.class, name = "小摊启用")
    public StallInfoVO enable(Session session, StallReq req) throws GameException {
        String pid = req.getPid();
        Player player = playerDao.getBean(pid);
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());
        //检测启用条件
        stallPriceContainer.checkEnable(farmPlayer,player);

        PlayerStall playerStall = playerStallDao.getPlayerStall(pid);
        playerStall.setEnable(true);
        playerDao.saveBean(player);
        playerStallDao.saveBean(playerStall);

        //财富通知
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        return null;
    }

    @RpcAnnotation(cmd = "stall.info", vo = StallInfoVO.class, req = StallReq.class, name = "小摊信息")
    public StallInfoVO getInfo(Session session, StallReq req) throws GameException {
        String pid = req.getPid();
        if(pid.indexOf(Constant.QINMA_ID)>=0){
            PlayerStall playerStall = playerStallDao.getPlayerStall(session.getPid());
            playerStall.setPlayerMomShelf(stallPriceContainer.generateMomShelf(playerStall));
            playerStallDao.saveBean(playerStall);//
            return rspStallFactory.qinmaStallInfoVO(playerStall);
        } else{
            PlayerStall playerStall = playerStallDao.getPlayerStall(pid);
            StallInfoVO stallInfoVO = null;
            if(pid.indexOf(session.getPid())>=0){//是否是自己
                stallInfoVO = rspStallFactory.stallInfoVO(playerStall,playerStall);
            }else{
                PlayerStall myStall = playerStallDao.getPlayerStall(session.getPid());
                stallInfoVO = rspStallFactory.stallInfoVO(playerStall,myStall);
            }
            return stallInfoVO;
        }
    }

    @RpcAnnotation(cmd = "stall.gold.shelf", vo = StallInfoVO.class, req = StallReq.class, name = "达人币货架开启")
    public StallInfoVO goldShelf(Session session, StallReq req) throws GameException {
        String pid = session.getPid();
        Player player = playerDao.getBean(pid);
        PlayerStall playerStall = playerStallDao.getPlayerStall(pid);
        stallGoldShelfContainer.enable(playerStall,player);
        playerStallDao.saveBean(playerStall);
        playerDao.saveBean(player);

        //财富通知
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        return rspStallFactory.stallInfoVO(playerStall,playerStall);
    }

    @RpcAnnotation(cmd = "stall.friend.shelf", vo = StallInfoVO.class, req = StallReq.class, name = "好友货架开启")
    public StallInfoVO friendShelf(Session session, StallReq req) throws GameException {
        String pid = session.getPid();
        Friend friend = friendDao.getBean(pid);
        PlayerStall playerStall = playerStallDao.getPlayerStall(pid);
        stallFriShelfContainer.enable(playerStall,friend);
        playerStallDao.saveBean(playerStall);
        return rspStallFactory.stallInfoVO(playerStall,playerStall);
    }

    @RpcAnnotation(cmd = "stall.hit.shelf", vo = StallInfoVO.class, req = StallReq.class, name = "货品上架")
    public StallInfoVO hitShelf(Session session, StallReq req) throws GameException {
        String pid = session.getPid();

        FarmPackage farmPackage = farmPackageDao.getBean(pid);
        Player player = playerDao.getBean(pid);
        PlayerStall playerStall = playerStallDao.getPlayerStall(pid);
        stallPriceContainer.checkHitShelf(req.getItemId(), req.getPrice());
        PlayerShelf shelf = stallPriceContainer.hitShelf(farmPackage,playerStall,req.getItemId(),req.getItemNum(),req.getPrice(),req.getShelfOrder(),req.getShelfType());
        if(req.getAdvType()>0){   //是否广告
            stallPriceContainer.advertise(player,playerStall,req.getShelfOrder(),req.getShelfType(),req.getAdvType());
        }
        farmPackageDao.saveBean(farmPackage);
        playerDao.saveBean(player);
        playerStallDao.saveBean(playerStall);

        //添加助手资讯
        StallAssistant stallAssistant = stallAssistantDao.getStallAssistant(req.getItemId());
        stallPriceContainer.addAssistantInfo(stallAssistant, pid,req.getShelfOrder(),req.getShelfType(),shelf);
        stallAssistantDao.saveBean(stallAssistant);

        return rspStallFactory.stallInfoVO(playerStall,playerStall);
    }

    @RpcAnnotation(cmd = "stall.off.shelf", vo = StallInfoVO.class, req = StallReq.class, name = "货品下架")
    public StallInfoVO offShelf(Session session, StallReq req) throws GameException {
        String pid = session.getPid();

        Player player = playerDao.getBean(pid);
        FarmPackage farmPackage = farmPackageDao.getBean(pid);
        PlayerStall playerStall = playerStallDao.getPlayerStall(pid);
        String itemId = stallPriceContainer.offShelf(farmPackage, player, playerStall, req.getShelfOrder(), req.getShelfType());
        farmPackageDao.saveBean(farmPackage);
        playerStallDao.saveBean(playerStall);
        playerDao.saveBean(player);

        //移除助手资讯
        StallAssistant stallAssistant = stallAssistantDao.getStallAssistant(itemId);
        stallPriceContainer.removeAssistantInfo(stallAssistant, pid);
        stallAssistantDao.saveBean(stallAssistant);

        //财富通知
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        return rspStallFactory.stallInfoVO(playerStall,playerStall);
    }

    @RpcAnnotation(cmd = "stall.buy", vo = StallInfoVO.class, req = StallReq.class, name = "购买货品")
    public StallInfoVO buy(Session session, StallReq req) throws GameException {
        String pid = session.getPid();
        String sid = req.getPid();//seller's pid

        PlayerStall playerStall = null;
        if(sid.indexOf(Constant.QINMA_ID)>=0){
            playerStall = playerStallDao.getPlayerStall(session.getPid());//亲妈的小摊在自己

            Player player = playerDao.getBean(pid);
            FarmPackage farmPackage = farmPackageDao.getBean(pid);
            stallPriceContainer.buy(player,farmPackage,playerStall,req.getShelfOrder(),req.getShelfType());
            playerDao.saveBean(player);
            farmPackageDao.saveBean(farmPackage);
            playerStallDao.saveBean(playerStall);
            //财富通知
            Session[] mysessions = {session};
            frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));

            return rspStallFactory.qinmaStallInfoVO(playerStall);
        } else{
            playerStall = playerStallDao.getPlayerStall(sid);
            PlayerStall myStall = playerStallDao.getPlayerStall(pid);
            stallPriceContainer.checkBuy(myStall);

            Player player = playerDao.getBean(pid);
            FarmPackage farmPackage = farmPackageDao.getBean(pid);
            String itemId = stallPriceContainer.buy(player,farmPackage,playerStall,req.getShelfOrder(),req.getShelfType());
            stallPriceContainer.adddBuyingTime(myStall);
            playerDao.saveBean(player);
            farmPackageDao.saveBean(farmPackage);
            playerStallDao.saveBean(playerStall);
            playerStallDao.saveBean(myStall);

            //移除助手资讯
            StallAssistant stallAssistant = stallAssistantDao.getStallAssistant(itemId);
            stallPriceContainer.removeAssistantInfo(stallAssistant, sid);
            stallAssistantDao.saveBean(stallAssistant);



            //财富通知
            Session[] mysessions = {session};
            frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));

            /**
             * 通知 好友状态 改变
             */
            farmRpc.broadcastFarmState(session,sid);

            return rspStallFactory.stallInfoVO(playerStall,myStall);
        }


    }

    @RpcAnnotation(cmd = "stall.getMoney", vo = StallInfoVO.class, req = StallReq.class, name = "获取收入")
    public StallInfoVO getMoney(Session session, StallReq req) throws GameException {
        String pid = session.getPid();

        Player player = playerDao.getBean(pid);
        PlayerStall playerStall = playerStallDao.getPlayerStall(pid);
        stallPriceContainer.getMoney(player,playerStall,req.getShelfOrder(),req.getShelfType());
        playerDao.saveBean(player);
        playerStallDao.saveBean(playerStall);

        //财富通知
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        return rspStallFactory.stallInfoVO(playerStall,playerStall);
    }

    @RpcAnnotation(cmd = "stall.advertise", vo = StallInfoVO.class, req = StallReq.class, name = "登广告")
    public StallInfoVO advertise(Session session, StallReq req) throws GameException {
        String pid = session.getPid();

        Player player = playerDao.getBean(pid);
        PlayerStall playerStall = playerStallDao.getPlayerStall(pid);
        stallPriceContainer.advertise(player, playerStall, req.getShelfOrder(), req.getShelfType(), req.getAdvType());
        playerDao.saveBean(player);
        playerStallDao.saveBean(playerStall);

        //财富通知
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        return rspStallFactory.stallInfoVO(playerStall,playerStall);
    }

    @RpcAnnotation(cmd = "stall.get.advertisement", vo = StallInfoVO.class, req = StallAdvReq.class, name = "获取广告")
    public StallAdvertisementVO getAdvertisement(Session session, StallAdvReq req) throws GameException {

        PlayerStallAdvertisement[] stallPlayerAdvVOs = stallPriceContainer.getAdvertisement(req.getPage(), req.getSize());

        return rspStallFactory.stallAdvVO(stallPlayerAdvVOs);
    }

    @RpcAnnotation(cmd = "stall.assistant.enable", vo = void.class, req = StallAssistantReq.class, name = "开启助手")
    public void enableAssistant(Session session, StallAssistantReq req) throws GameException {
        String pid = session.getPid();
        Player player = playerDao.getBean(pid);
        PlayerStall playerStall = playerStallDao.getPlayerStall(pid);
        stallPriceContainer.enableAssistant(player, playerStall, req.getEnableDays());
        playerDao.saveBean(player);
        playerStallDao.saveBean(playerStall);

        //财富通知
        frontendService.broadcast(session, roleFactory.newPlayerVO(player));
    }

    @RpcAnnotation(cmd = "stall.assistant.buy", vo = StallInfoVO.class, req = StallReq.class, name = "助手购买")
    public StallInfoVO buyAssistant(Session session, StallReq req) throws GameException {
        String pid = session.getPid();
        String sid = req.getPid();//seller's pid

        PlayerStall playerStall = playerStallDao.getPlayerStall(pid);
        //助手下次服务时间
        playerStall.setAssistantNextTime(new Date(System.currentTimeMillis() + StallConstant.ASSISTANT_COOLDOWN));
        playerStallDao.saveBean(playerStall);

        //助手虚拟购买
        if(sid.indexOf(Constant.ASSISTANT_ID)>=0){
            if(req.getPrice()<=0){
                return null;
            }
            Player player = playerDao.getBean(pid);
            FarmPackage farmPackage = farmPackageDao.getBean(pid);
            stallPriceContainer.assistantBuy(player, farmPackage, req.getItemId(), req.getItemNum(), req.getPrice());
            farmPackageDao.saveBean(farmPackage);
            playerDao.saveBean(player);

            //财富通知
            Session[] mysessions = {session};
            frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
            return null;
        }

        //移除助手资讯
        StallAssistant stallAssistant = stallAssistantDao.getStallAssistant( req.getItemId());
        stallPriceContainer.removeAssistantInfo(stallAssistant, sid);
        stallAssistantDao.saveBean(stallAssistant);

        return buy(session,req);
    }

    @RpcAnnotation(cmd = "stall.assistant.items", vo = void.class, req = StallAssistantReq.class, name = "助手物品列表")
    public FarmItemVO[] browseAssistant(Session session, StallAssistantReq req) throws GameException {
         return  rspStallFactory.getAssistantItem();
    }

    @RpcAnnotation(cmd = "stall.assistant.check", vo = Boolean.class, req = StallAssistantReq.class, name = "助手是否已开启")
    public Boolean checkAssistant(Session session, StallAssistantReq req) throws GameException {
        String pid = session.getPid();
        PlayerStall playerStall = playerStallDao.getPlayerStall(pid);
        if(playerStall == null){
            return Boolean.FALSE;
        }else{
            if(playerStall.getAssistantTime() == null)
                return Boolean.FALSE;
            return  Boolean.TRUE;
        }
    }

    @RpcAnnotation(cmd = "stall.assistant.cd", vo = StallInfoVO.class, req = StallAssistantReq.class, name = "助手冷却时间")
    public StallInfoVO cdAssistant(Session session, StallAssistantReq req) throws GameException {
        String pid = session.getPid();
        PlayerStall playerStall = playerStallDao.getPlayerStall(pid);
        if(null==playerStall.getAssistantTime()){//新号要先调用此方法,避免null pointer
            playerStall.setAssistantTime(new Date(StallConstant.ASSISTANT_TRIAL + System.currentTimeMillis()));
            playerStallDao.saveBean(playerStall);
        }
        StallInfoVO stallInfoVO = rspStallFactory.stallInfoVO2(playerStall);
        return stallInfoVO;
    }

    @RpcAnnotation(cmd = "stall.assistant.info", vo = StallInfoVO.class, req = StallAssistantReq.class, name = "获取助手资讯")
    public StallInfoVO getAssistant(Session session, StallAssistantReq req) throws GameException {
        String pid = session.getPid();
        StallInfoVO stallInfoVO = new StallInfoVO();
        StallAssistant stallAssistant = stallAssistantDao.getStallAssistant(req.getItemId());

        StallPlayerShelfVO[] shelfVOs = new StallPlayerShelfVO[3];

        Iterator<Map.Entry<String,PlayerAssistant>> itMap = stallAssistant.getShelfMap().entrySet().iterator();
        //排序
        Set<Map.Entry<String,PlayerAssistant>> paSet = new TreeSet(new Comparator<Map.Entry<String,PlayerAssistant>>() {
            @Override
            public int compare(Map.Entry<String,PlayerAssistant> pa1, Map.Entry<String,PlayerAssistant> pa2) {

                return pa2.getValue().getPrice()/pa2.getValue().getNum()-pa1.getValue().getPrice()/pa1.getValue().getNum();
            }
        });
        while (itMap.hasNext()){
            Map.Entry<String,PlayerAssistant> entry = itMap.next();
            if(pid.indexOf(entry.getKey())>=0){//判断自己的
                continue;
            }
            paSet.add(entry);
        }
        //找出3个
        int counter = 3;
        Iterator<Map.Entry<String,PlayerAssistant>> it = paSet.iterator();
        while (it.hasNext()){

            if(--counter<0)
                break;
            Map.Entry<String,PlayerAssistant> entry = it.next();
            int index = 0;
            if(entry.getValue().getNum()>7){//第3个固定刷>8个
                index = 2;
            }else if(entry.getValue().getNum()>3){//第2个固定刷4~7个
                index = 1;
            }else if(entry.getValue().getNum()>0){//第3个固定刷1~3个
                index = 0;
            }

            if(shelfVOs[index] != null){
                continue;//货架已刷出
            }

            StallPlayerShelfVO vo = new StallPlayerShelfVO();
            vo.setType(entry.getValue().getShelfType());
            vo.setItemNum(entry.getValue().getNum());
            vo.setOrder(entry.getValue().getShelfOrder());
            vo.setPrice(entry.getValue().getPrice());
            vo.setBuyer(entry.getKey());
            shelfVOs[index] = vo;

        }
        //不足3个
        BaseItemRule baseItemRule = baseItemRulecontainer.getElement(req.getItemId());

        if (shelfVOs[2] == null){//第3个固定刷10个
            StallPlayerShelfVO vo = new StallPlayerShelfVO();
            vo.setItemNum(10);
            vo.setPrice(baseItemRule.getShelfPrice()*10*2);
            vo.setBuyer(Constant.ASSISTANT_ID);
            shelfVOs[2] = vo;
        }
        if (shelfVOs[1] == null){//第2个固定刷5个
            StallPlayerShelfVO vo = new StallPlayerShelfVO();
            vo.setItemNum(5);
            vo.setPrice(baseItemRule.getShelfPrice()*5*2);
            vo.setBuyer(Constant.ASSISTANT_ID);
            shelfVOs[1] = vo;
        }
        if (shelfVOs[0] == null){
            StallPlayerShelfVO vo = new StallPlayerShelfVO();
            vo.setItemNum(2);
            vo.setPrice(baseItemRule.getShelfPrice()*2*2);
            vo.setBuyer(Constant.ASSISTANT_ID);
            shelfVOs[0] = vo;
        }

        stallInfoVO.setShelfs(shelfVOs);
        return stallInfoVO;
    }

}
