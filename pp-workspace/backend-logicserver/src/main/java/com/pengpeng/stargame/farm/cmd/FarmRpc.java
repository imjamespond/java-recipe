package com.pengpeng.stargame.farm.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.container.IFarmRuleContainer;
import com.pengpeng.stargame.farm.dao.*;
import com.pengpeng.stargame.farm.rule.FarmSeedRule;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.farm.*;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.model.player.FriendItem;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.model.stall.PlayerStall;
import com.pengpeng.stargame.model.wharf.PlayerWharf;
import com.pengpeng.stargame.player.container.IFriendRuleContainer;
import com.pengpeng.stargame.player.container.ISceneRuleContainer;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.rsp.RspFarmFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.stall.container.IStallPriceContainer;
import com.pengpeng.stargame.stall.dao.IPlayerStallDao;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.farm.FarmIdReq;
import com.pengpeng.stargame.vo.farm.FarmSpeedVO;
import com.pengpeng.stargame.vo.farm.FarmStateVO;
import com.pengpeng.stargame.vo.farm.FarmVO;
import com.pengpeng.stargame.wharf.container.IWharfInvoiceContainer;
import com.pengpeng.stargame.wharf.dao.IPlayerWharfDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 玩家农场 和 农田
 *
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-26 下午2:27
 */
@Component
public class FarmRpc extends RpcHandler {
    private static final Logger logger = Logger.getLogger(FarmRpc.class);

    @Autowired
    private IPlayerDao playerDao;

    //    @Autowired
//    private StatusService statusService;
    @Autowired
    private StatusRemote statusRemote;

    @Autowired
    private FrontendServiceProxy frontendService;


    @Autowired
    private IFarmPlayerDao farmPlayerDao;

    @Autowired
    private RspFarmFactory rsp;

    @Autowired
    private IFarmFriendHarvestDao farmFriendHarvestDao;

    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;

    @Autowired
    private IFarmRuleContainer farmRuleContainer;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IFarmEvaluateDao farmEvaluateDao;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IFriendDao friendDao;
    @Autowired
    private FarmPkgRpc farmPkgRpc;
    @Autowired
    private IFarmOrderDao farmOrderDao;

    @Autowired
    private ISceneRuleContainer sceneRuleContainer;
    @Autowired
    private IFriendRuleContainer friendRuleContainer;

    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private IFarmActionDao farmActionDao;
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    @Autowired
    private IWharfInvoiceContainer wharfInvoiceContainer;
    @Autowired
    private IPlayerWharfDao playerWharfDao;
    @Autowired
    private IStallPriceContainer stallPriceContainer;
    @Autowired
    private IPlayerStallDao playerStallDao;
    @Autowired
    private IFarmDecoratePkgDao farmDecoratePkgDao;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;

    public FarmVO getFarmVoByPlayerId(String pId) {
        Player player = playerDao.getBean(pId);
        FarmPlayer fpObj = farmPlayerDao.getFarmPlayer(pId, System.currentTimeMillis());
        FarmEvaluate farmEvaluate = farmEvaluateDao.getFarmEvaluate(pId);
        FarmFriendHarvest farmFriendHarvest = farmFriendHarvestDao.getFarmFriendHarvest(pId);
        FarmOrder farmOrder = farmOrderDao.getFarmOrder(pId);
        return rsp.transitionVO(player, fpObj, farmEvaluate, farmFriendHarvest, farmOrder);
    }

    @RpcAnnotation(cmd = "farm.get.info", req = FarmIdReq.class, name = "取得农场信息",lock = false)
    public FarmVO getFarmInfo(Session session, FarmIdReq req) {
        logger.debug(session.getPid());
        return getFarmVoByPlayerId(req.getPid());
    }

    /**
     * @param session
     * @param req     fid
     * @return
     */
    @RpcAnnotation(cmd = "farm.get.friend", req = FarmIdReq.class, name = "取得好友农场信息",lock = false)
    public FarmVO getFrindInfo(Session session, FarmIdReq req) throws AlertException {
        friendRuleContainer.checkFriend(friendDao.getBean(session.getPid()), playerDao.getBean(session.getPid()), playerDao.getBean(req.getFid()));


        // demo li
        return getFarmVoByPlayerId(req.getFid());
    }


    /**
     * 种植作物
     * 有推送事件
     *
     * @param session
     * @param req     farmId,fieldId
     */
    @RpcAnnotation(cmd = "farm.plant", req = FarmIdReq.class, name = "种植作物", lock = true)
    public void plant(Session session, FarmIdReq req) throws AlertException {
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(req.getPid(), System.currentTimeMillis());
        /**
         * 作物 等级的 判断
         */
        baseItemRulecontainer.checkLevel(req.getSeedId(), farmPlayer.getLevel());
        FarmField farmField = farmPlayer.getOneFarmField(req.getFieldId());
        /**
         * 田地可否种植的 检测
         */
        baseItemRulecontainer.checkField(farmField);
        /**
         * 种子数量的判断 可否种植
         */
        FarmPackage farmPackage = farmPackageDao.getBean(req.getPid());
        baseItemRulecontainer.chekPlantSeedNum(req.getSeedId(), farmPackage);

        /**
         * 种植
         */
        baseItemRulecontainer.plant(req.getSeedId(), farmField, farmPackage);

        /**
         * 更改背包  和 农场 信息
         */
        farmPlayerDao.saveBean(farmPlayer);
        farmPackageDao.saveBean(farmPackage);

        /**
         * 推送事件
         */
        Session[] sessions = statusRemote.getMember(session, SessionUtil.getChannelScene(session));
        frontendService.broadcast(sessions, getFarmVoByPlayerId(req.getPid()));


        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, farmPkgRpc.getItemByPid(req.getPid()));

//        broadcastFarmState(session, session.getPid());
        /**
         * 成就统计 种植收获 作物
         */

        //日志
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_9, session.getPid(), req.getSeedId() + GameLogger.SPLIT + req.getFieldId()));
    }


    @RpcAnnotation(cmd = "farm.harvestAll", req = FarmIdReq.class, name = "一键收获所有作物")
    public void harvestAll(Session session, FarmIdReq req) throws GameException {

        /**
         * 1、是否是超级粉丝
         * 2、判断 背包是否 满足
         * 3、修改所有田地状态
         */
        req.setPid(session.getPid());
        Player player=playerDao.getBean(session.getPid());

        if(!payMemberRuleContainer.isPayMember(session.getPid())){
            exceptionFactory.throwAlertException("farm.harAll");
        }

        FarmPackage farmPackageT = farmPackageDao.getBean(session.getPid());
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(req.getPid(), System.currentTimeMillis());


        /**
         * 检测
         */
        baseItemRulecontainer.checkMyHarvestAll(farmPlayer, farmPackageT);

        /**
         * 收获
         */
        RoomPackege roomPackege = roomPackegeDao.getRoomPackege(session.getPid());
        FashionCupboard fashionCupboard = fashionCupboardDao.getBean(session.getPid());
        FarmPackage farmPackage = farmPackageDao.getBean(req.getPid());
        FarmDecoratePkg farmDecoratePkg=farmDecoratePkgDao.getFarmDecoratePkg(session.getPid());
        baseItemRulecontainer.harvestSeedAll(farmPackage, farmPlayer, roomPackege, fashionCupboard,farmDecoratePkg);


        /**
         * 保存数据
         */
        farmPlayerDao.saveBean(farmPlayer);
        farmPackageDao.saveBean(farmPackage);
        roomPackegeDao.saveBean(roomPackege);
        fashionCupboardDao.saveBean(fashionCupboard);
        /**
         * 推送 事件
         */
        Session[] sessions = statusRemote.getMember(session, SessionUtil.getChannelScene(session));
        frontendService.broadcast(sessions, getFarmVoByPlayerId(session.getPid()));

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, farmPkgRpc.getItemByPid(session.getPid()));

//        broadcastFarmState(session, session.getPid());
    }





    /**
     * @param session
     * @param req     farmId,fieldId
     */
    @RpcAnnotation(cmd = "farm.harvest", req = FarmIdReq.class, name = "收获作物", lock = true)
    public void harvest(Session session, FarmIdReq req) throws AlertException {
        /**
         *能否收获的 判断
         */
        FarmPackage farmPackage = farmPackageDao.getBean(req.getPid());

        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(req.getPid(), System.currentTimeMillis());
        FarmField farmField = farmPlayer.getOneFarmField(req.getFieldId());
        baseItemRulecontainer.checkMyHarvest(farmField, farmPackage);

        /**
         * 收获
         */
        RoomPackege roomPackege = roomPackegeDao.getRoomPackege(session.getPid());
        FashionCupboard fashionCupboard = fashionCupboardDao.getBean(session.getPid());
        FarmDecoratePkg farmDecoratePkg=farmDecoratePkgDao.getFarmDecoratePkg(session.getPid());
        baseItemRulecontainer.harvestSeed(farmField, farmPackage, farmPlayer, roomPackege, fashionCupboard,farmDecoratePkg);


        farmPlayerDao.saveBean(farmPlayer);
        farmPackageDao.saveBean(farmPackage);
        roomPackegeDao.saveBean(roomPackege);
        fashionCupboardDao.saveBean(fashionCupboard);
        /**
         * 推送 事件
         */
        Session[] sessions = statusRemote.getMember(session, SessionUtil.getChannelScene(session));

        frontendService.broadcast(sessions, getFarmVoByPlayerId(req.getPid()));


        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, farmPkgRpc.getItemByPid(req.getPid()));

//        broadcastFarmState(session, session.getPid());
    }

    /**
     *
     * @param session
     * @param cPid   变化的人的 Id
     */
    public void broadcastFarmState(Session session, String cPid) {
        //农田状态改变,通知好友
        Friend friend = friendDao.getBean(cPid);
        FarmStateVO helps = new FarmStateVO(cPid, "");
        /**
         * 农场是否有作物收获的状态
         */
        FarmPlayer fp = farmPlayerDao.getFarmPlayer(cPid, System.currentTimeMillis());
        boolean canHelp = farmRuleContainer.canHelp(fp);
        if (canHelp) {
            helps.setState("1");
        } else {
            helps.setState("");
        }
        /**
         * 码头是否需要帮忙
         */
        PlayerWharf pw=playerWharfDao.getPlayerWharf(cPid);
        boolean w=wharfInvoiceContainer.getHelpState(pw);
        if (w) {
            helps.setWharfState("1");
        } else {
            helps.setWharfState("");
        }
        /**
         * 摆摊是否有 货物出售
         */
        PlayerStall playerStall=playerStallDao.getPlayerStall(cPid);
        boolean s=stallPriceContainer.isAvailable(playerStall);
        if (s) {
            helps.setStallState("1");
        } else {
            helps.setStallState("");
        }
        List<String> fids=new ArrayList<String>();
        Map<String, FriendItem> map = friend.getFriends();
        for (Map.Entry<String, FriendItem> entry : map.entrySet()) {
            fids.add(entry.getValue().getFid());
        }
        List<Session> sessions=new ArrayList<Session>();
        Session [] fsessions = statusRemote.getSomeSession(session,fids.toArray(new String[0]));
        if(fsessions!=null&&fsessions.length!=0){
           for(Session session1:fsessions){
               sessions.add(session1);
           }
        }
        sessions.add(session);
        frontendService.broadcast(sessions.toArray(new Session[0]), helps);
    }

    @RpcAnnotation(cmd = "farm.friend.harvestAll", req = FarmIdReq.class, name = "帮助好友一键收获所有作物")
    public void friendHarvestAll(Session session, FarmIdReq req) throws GameException {

        //亲妈直接返回
        if (req.getFid().equals(Constant.QINMA_ID)) {
            return;
        }
        //1.检查是否好友身份
        Friend friend = friendDao.getBean(session.getPid());
        if (!friend.isFriend(req.getFid())) {
            exceptionFactory.throwAlertException("farm.friend.notharvest");
        }
        //是否是VIP
        req.setPid(session.getPid());
        if(!payMemberRuleContainer.isPayMember(session.getPid())){
            exceptionFactory.throwAlertException("farm.harAll");
        }
        FarmActionInfo farmActionInfo=farmActionDao.getFarmActionInfo(req.getFid());
        FarmFriendHarvest farmFriendHarvest = farmFriendHarvestDao.getFarmFriendHarvest(session.getPid());
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(req.getFid(), System.currentTimeMillis());
        FarmPackage myPackage = farmPackageDao.getBean(session.getPid());
        FarmPackage friendPackage = farmPackageDao.getBean(req.getFid());

        baseItemRulecontainer.helpFriendHarvestAll(farmFriendHarvest,farmPlayer,myPackage,friendPackage,farmActionInfo);



        farmActionDao.saveBean(farmActionInfo);
        farmPlayerDao.saveBean(farmPlayer);
        farmPackageDao.saveBean(myPackage);
        farmPackageDao.saveBean(friendPackage);
        farmFriendHarvestDao.saveBean(farmFriendHarvest);


        /**
         * 推送事件
         */

        Session[] sessions = statusRemote.getMember(session, SessionUtil.getChannelScene(session));
        frontendService.broadcast(sessions, getFarmVoByPlayerId(req.getFid()));


        broadcastFarmState(session, req.getFid());
    }

    /**
     * @param session
     * @param req     fid好友,farmid农田id, fieldid
     */
    @RpcAnnotation(cmd = "farm.harvest.friend", req = FarmIdReq.class, name = "帮助好友收获", lock = true)
    public void harvestFriend(Session session, FarmIdReq req) throws AlertException {

        if (req.getFid().equals(Constant.QINMA_ID)) {
            return;
        }
        //1.检查是否好友身份
        Friend friend = friendDao.getBean(session.getPid());
        if (!friend.isFriend(req.getFid())) {
            exceptionFactory.throwAlertException("farm.friend.notharvest");
        }

        FarmActionInfo farmActionInfo=farmActionDao.getFarmActionInfo(req.getFid());
        FarmFriendHarvest farmFriendHarvest = farmFriendHarvestDao.getFarmFriendHarvest(session.getPid());
        FarmFriendHarvest friendFarmFriendHarvest = farmFriendHarvestDao.getFarmFriendHarvest(req.getFid());
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(req.getFid(), System.currentTimeMillis());
        FarmField farmField = farmPlayer.getOneFarmField(req.getFieldId());

        /**
         * 能否帮忙收获
         */
        FarmPackage myPackage = farmPackageDao.getBean(session.getPid());
        FarmPackage friendPackage = farmPackageDao.getBean(req.getFid());

        baseItemRulecontainer.checkFriendHarvest(farmFriendHarvest, farmField, myPackage, friendPackage);


        /**
         *帮好友收获
         */

        baseItemRulecontainer.helpFriendHarvest(farmField, myPackage, friendPackage, farmFriendHarvest, friendFarmFriendHarvest,farmActionInfo);

        farmActionDao.saveBean(farmActionInfo);
        farmPlayerDao.saveBean(farmPlayer);
        farmPackageDao.saveBean(myPackage);
        farmPackageDao.saveBean(friendPackage);
        farmFriendHarvestDao.saveBean(farmFriendHarvest);
        farmFriendHarvestDao.saveBean(friendFarmFriendHarvest);


        //任务
        taskRuleContainer.updateTaskNum(session.getPid(), TaskConstants.CONDI_TYPE_9, "", 1);

        /**
         * 推送事件
         */

        Session[] sessions = statusRemote.getMember(session, SessionUtil.getChannelScene(session));
        ;
        frontendService.broadcast(sessions, getFarmVoByPlayerId(req.getFid()));


        broadcastFarmState(session, req.getFid());

    }

    /**
     * 进入好友农场
     *
     * @param session
     * @param req     fid好友id
     */
    @Deprecated
    @RpcAnnotation(cmd = "farm.enter.friend", req = FarmIdReq.class, name = "进入好友农场")
    public void enterFriendFarm(Session session, FarmIdReq req) throws AlertException {

        //1.检查是否好友身份,非好友身份不能进入
        Friend friend = friendDao.getBean(session.getPid());
        if (!friend.isFriend(req.getFid())) {
            exceptionFactory.throwAlertException("farm.friend.notenter");
        }
        //2.statusService.enterChannel(session,sceneid农场的id,pid好友的pid);
        Player friendPlayer = playerDao.getBean(req.getFid());
        String channelId = sceneRuleContainer.getChannelId("map_01", friendPlayer,"");
        statusRemote.enterChannel(session, channelId);

        //3.推好友农场信息


        //像玩家 推好友农场信息
        Session[] sessions = {session};
        frontendService.broadcast(sessions, getFarmVoByPlayerId(req.getFid()));
    }

    /**
     * 评价好友农场
     *
     * @param session
     * @param req     fid,farmid
     */
    @RpcAnnotation(cmd = "farm.evaluation", req = FarmIdReq.class, name = "评价好友农场（每日一评）", lock = true)
    public void evaluation(Session session, FarmIdReq req) throws AlertException {
        Friend friend = friendDao.getBean(session.getPid());
        if (!friend.isFriend(req.getFid())) {
            exceptionFactory.throwAlertException("farm.friend.notevaluation");
        }
        /**
         * 可否评价
         */
        FarmEvaluate farmEvaluate = farmEvaluateDao.getFarmEvaluate(session.getPid());
        FarmEvaluate friendFarmEvaluate = farmEvaluateDao.getFarmEvaluate(req.getFid());
        farmRuleContainer.checkEvaluate(farmEvaluate, req.getFid());

        /**
         * 评价
         */
        farmRuleContainer.evaluate(farmEvaluate, friendFarmEvaluate);

        farmEvaluateDao.saveBean(farmEvaluate);
        farmEvaluateDao.saveBean(friendFarmEvaluate);


        //任务
        taskRuleContainer.updateTaskNum(session.getPid(), TaskConstants.CONDI_TYPE_8, "", 1);
        //成就
        successRuleContainer.updateSuccessNum(req.getFid(),6,1,"");
        FarmActionInfo farmActionInfo=farmActionDao.getFarmActionInfo(req.getFid());
        farmActionInfo.addAction(new FarmAction(session.getPid(),2,"",0));
        farmActionDao.saveBean(farmActionInfo);

        /**
         * 推送事件
         */
        Session[] sessions = statusRemote.getMember(session, SessionUtil.getChannelScene(session));
        ;
        frontendService.broadcast(sessions, getFarmVoByPlayerId(req.getFid()));

        //日志
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_12, session.getPid(), req.getFid()));

    }

    /**
     * 农场升级
     *
     * @param session
     * @param req     farmid
     */
    @RpcAnnotation(cmd = "farm.up.level", req = FarmIdReq.class, name = "升级农场（农场等级）")
    public void levelUp(Session session, FarmIdReq req) throws AlertException {
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(req.getPid(), System.currentTimeMillis());

        farmLevelRuleContainer.check(farmPlayer);
        farmLevelRuleContainer.upgrade(farmPlayer);

        farmPlayerDao.saveBean(farmPlayer);

    }

    @RpcAnnotation(cmd = "farm.state.friends", req = IdReq.class, name = "取得农场状态")
    public FarmStateVO[] friendState(Session session, IdReq req) throws AlertException {
        if (req.getIds() == null) {
            //请选择好友id
            exceptionFactory.throwAlertException("farm.select.friendid");
        }
        Friend friend = friendDao.getBean(session.getPid());
        String[] ids = req.getIds();
        FarmStateVO[] helps = new FarmStateVO[ids.length];
        for (int i = 0; i < ids.length; i++) {
            helps[i] = new FarmStateVO(ids[i], "");
//            boolean isFriend = friend.isFriend(ids[i]);
//            if (!isFriend) {
//                helps[i].setState("");
//                continue;
//            }
            /**
             * 农场是否可以帮忙收获
             */
            FarmPlayer fp = farmPlayerDao.getFarmPlayer(ids[i], System.currentTimeMillis());
            boolean canHelp = farmRuleContainer.canHelp(fp);
            if (canHelp) {
                helps[i].setState("1");
            } else {
                helps[i].setState("");
            }
            /**
             * 码头是否需要帮忙
             */
            PlayerWharf pw=playerWharfDao.getPlayerWharf(ids[i]);
            boolean w=wharfInvoiceContainer.getHelpState(pw);
            if (w) {
                helps[i].setWharfState("1");
            } else {
                helps[i].setWharfState("");
            }
            /**
             * 摆摊是否有 货物出售
             */
            PlayerStall playerStall=playerStallDao.getPlayerStall(ids[i]);
            boolean s=stallPriceContainer.isAvailable(playerStall);
            if (s) {
                helps[i].setStallState("1");
            } else {
                helps[i].setStallState("");
            }
        }
        return helps;
    }

    @RpcAnnotation(cmd = "farm.eradicate", req = FarmIdReq.class, name = "铲除田地")
    public void eradicate(Session session, FarmIdReq req) throws GameException {

        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(session.getPid(), System.currentTimeMillis());
        baseItemRulecontainer.eradicate(farmPlayer, req.getFieldId());
        farmPlayerDao.saveBean(farmPlayer);
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_26, farmPlayer.getId(), req.getFieldId()));
        Session[] sessions = statusRemote.getMember(session, SessionUtil.getChannelScene(session));
        frontendService.broadcast(sessions, getFarmVoByPlayerId(session.getPid()));

//        broadcastFarmState(session, session.getPid());
    }

    @RpcAnnotation(cmd = "farm.speedup", req = FarmIdReq.class, name = "加速生长", vo = FarmSpeedVO.class)
    public FarmSpeedVO speedup(Session session, FarmIdReq req) throws GameException {

        FarmPackage farmPackage = farmPackageDao.getBean(session.getPid());
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(session.getPid(), System.currentTimeMillis());
        Player player = playerDao.getBean(session.getPid());
        FarmField farmField = farmPlayer.getOneFarmField(String.valueOf(req.getFieldId()));
        baseItemRulecontainer.checkSpeedUP(player, farmField, farmPackage);
        FarmSpeedVO farmSpeedVO=baseItemRulecontainer.speedUp(player, farmField, farmPackage);
        farmRuleContainer.runFarmPlayer(farmPlayer,System.currentTimeMillis());
        farmPlayerDao.saveBean(farmPlayer);
        playerDao.saveBean(player);
        farmPackageDao.saveBean(farmPackage);



        Session[] sessions = statusRemote.getMember(session, SessionUtil.getChannelScene(session));
        frontendService.broadcast(sessions, getFarmVoByPlayerId(session.getPid()));

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));

        frontendService.broadcast(mysessions, farmPkgRpc.getItemByPid(session.getPid()));

        FarmSeedRule farmSeedRule = (FarmSeedRule) baseItemRulecontainer.getElement(farmField.getSeedId());

        farmSpeedVO.setName(baseItemRulecontainer.getElement(farmSeedRule.getOutput()).getName());

//        broadcastFarmState(session, session.getPid());

        return farmSpeedVO;

    }

    @RpcAnnotation(cmd = "farm.speedup.needGold", req = FarmIdReq.class, name = "直接催熟需要的达人币", vo = Integer.class)
    public int speedupNeedGold(Session session, FarmIdReq req) throws GameException {

        FarmPackage farmPackage = farmPackageDao.getBean(session.getPid());
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(session.getPid(), System.currentTimeMillis());
        Player player = playerDao.getBean(session.getPid());
        FarmField farmField = farmPlayer.getOneFarmField(String.valueOf(req.getFieldId()));
        baseItemRulecontainer.checkSpeedUP(player, farmField, farmPackage);

        return baseItemRulecontainer.getSpeedupNeedGold(player,farmField);

    }

}
