package com.pengpeng.stargame.player.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.gameevent.constiner.IEventRuleContainer;
import com.pengpeng.stargame.gameevent.dao.IEventDropDao;
import com.pengpeng.stargame.gameevent.dao.IPlayerEventDao;
import com.pengpeng.stargame.gameevent.rule.EventRule;
import com.pengpeng.stargame.giftbag.GiftBagConstant;
import com.pengpeng.stargame.giftbag.dao.IGiftBagDao;
import com.pengpeng.stargame.gm.dao.IAddGoldDao;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.lottery.dao.IPlayerlotteryDao;
import com.pengpeng.stargame.model.gameEvent.FamilyBankEvent;
import com.pengpeng.stargame.model.gameEvent.PlayerEvent;
import com.pengpeng.stargame.model.giftBag.PlayerGiftGag;
import com.pengpeng.stargame.model.gm.AddGoldInfo;
import com.pengpeng.stargame.model.lottery.PlayerLottery;
import com.pengpeng.stargame.model.lottery.RouletteConstant;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBank;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.*;
import com.pengpeng.stargame.model.stall.PlayerStall;
import com.pengpeng.stargame.model.successive.PlayerSuccessive;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyCollectRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.player.container.*;
import com.pengpeng.stargame.player.dao.*;
import com.pengpeng.stargame.player.dao.impl.PlayerDaoImpl;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.rank.container.IRankRuleContainer;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.stall.container.IStallPriceContainer;
import com.pengpeng.stargame.stall.dao.IPlayerStallDao;
import com.pengpeng.stargame.statistics.IStatistics;
import com.pengpeng.stargame.statistics.Statistics;
import com.pengpeng.stargame.successive.container.ISuccessiveContainer;
import com.pengpeng.stargame.successive.dao.IPlayerSuccessiveDao;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.util.SceneConstant;
import com.pengpeng.stargame.util.StringTrimUtils;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.gm.AddGoldVO;
import com.pengpeng.stargame.vo.map.MoveVO;
import com.pengpeng.stargame.vo.role.*;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.util.Uid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;

/**
 * 地图,场景,相关指令
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-15上午11:57
 */
@Component()
public class PlayerRpc extends RpcHandler {
    private static final Logger logger = Logger.getLogger(PlayerRpc.class);
    @Autowired
    private PlayerDaoImpl playerDao;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private FrontendServiceProxy frontendService;

    @Autowired
    private RspRoleFactory roleFactory;

    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Autowired
    private MessageSource message;

    @Autowired
    private IOtherPlayerDao otherPlayerDao;

    @Autowired
    private IActivePlayerContainer activePlayerContainer;

    @Autowired
    private IPlayerlotteryDao playerlotteryDao;
    @Autowired
    private IPlayerStallDao playerStallDao;
    @Autowired
    private IStallPriceContainer stallPriceContainer;
    @Autowired
    private IPlayerSuccessiveDao playerSuccessiveDao;
    @Autowired
    private ISuccessiveContainer successiveContainer;

    @Autowired
    private IActivePlayerDao activePlayerDao;

    @Autowired
    private IActiveRuleContainer activeRuleContainer;
    @Autowired
    private StatusRemote statusService;
    @Autowired
    private IPlayerInfoDao playerInfoDao;

    @Autowired
    private IAddGoldDao addGoldDao;
    @Autowired
    private IGiftBagDao giftBagDao;
    @Autowired
    private IEventDropDao eventDropDao;
    @Autowired
    private IEventRuleContainer eventRuleContainer;
    @Autowired
    private IFamilyCollectRuleContainer familyCollectRuleContainer;
    @Autowired
    private IRankRuleContainer rankRuleContainer;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Autowired
    private IPlayerEventDao playerEventDao;
    @Autowired
    private IStatistics statistics;
    @RpcAnnotation(cmd="p.enter",lock = false,req=PlayerReq.class,name="玩家进入游戏")
    public PlayerVO enterGame(Session session,PlayerReq req)throws AlertException{
        boolean state = statusRemote.auth(session,req.getTokenKey());
        if (!state){
            exceptionFactory.throwAlertException("p.notlogin");
        }
        Player player = playerDao.getBean(req.getId());
        if (player == null) {
            exceptionFactory.throwAlertException("p.notplayer");
        }
        Session s = statusRemote.getSession(session,player.getId());
        if (s!=null){
            statusRemote.outerChannel(session,SessionUtil.getChannelScene(s));
            //退出的同时就会退出场景
            frontendService.logout(s,null);
        }
        boolean  ischange=false;
        /**
         * 游戏币 上限的 判断
         */
        int  maxGold=farmLevelRuleContainer.getElement(farmPlayerDao.getFarmLevel(player.getId())).getMaxGold();
        if(player.getGameCoin()>maxGold){
            player.setGameCoin(maxGold);
            ischange=true;
        }
        //赠送达人币
        AddGoldInfo addGoldInfo=addGoldDao.getAddGoldInfo();
        if(addGoldInfo.getuIds().contains(String.valueOf(player.getUserId()))){
            if(!addGoldInfo.getDayIds().contains(String.valueOf(player.getUserId()))){
                player.incGoldCoin(addGoldInfo.getGold());
                addGoldInfo.getDayIds().add(String.valueOf(player.getUserId()));
                addGoldDao.saveBean(addGoldInfo);
                ischange=true;
            }
        }
        if(ischange){
            playerDao.saveBean(player);
        }



        return roleFactory.newPlayerVO(player);
    }
    @RpcAnnotation(cmd="p.enter.after",lock = false,req=CommonReq.class,name="玩家进入游戏之后")
    public void afterEnter(Session session,CommonReq req){
        OtherPlayer op = otherPlayerDao.getBean(session.getPid());
        Player player = playerDao.getBean(session.getPid());
        //日志  记录一次 登录
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_1,session.getPid(),""));
        /**
         * 记录一次登录    数据统计
         */
        statistics.recordLogin(player.getId(),player,op);


        op.doLogin(new Date());
        otherPlayerDao.saveBean(op);
        activePlayerContainer.finish(session,session.getPid(), PlayerConstant.ACTIVE_TYPE_1,1);
//        /**
//         * 收集 奖励邮件的处理
//         */
//        try {
//            familyCollectRuleContainer.checkRewardMail(session.getPid());
//        } catch (AlertException e) {
//            logger.error(e.getMessage());
//        }
//        if (!op.isLoginGive()){
//            //音乐先锋榜消息推送
//            frontendService.broadcast(session,new MsgVO(GameEventConstant.EVENT_MUSIC_XIANFENG,1));
//        }
        /**
         * 排行帮的处理
         */
        rankRuleContainer.initRankValue(session.getPid());
        /**
         * 礼包活动相关的  圣诞活动 元旦活动  春节
         */
        PlayerGiftGag playerGiftGag=giftBagDao.getPlayerGiftBag(session.getPid());
//        if(!playerGiftGag.getHistoryGet().containsKey(GiftBagConstant.M_C_GIFTID)){
//            frontendService.broadcast(session,new MsgVO(EventConstant.EVENT_S_D,1));
//        }
//        if(!playerGiftGag.getHistoryGet().containsKey(GiftBagConstant.Y_D_GIFTID)){
//            frontendService.broadcast(session,new MsgVO(EventConstant.EVENT_Y_D,1));
//        }
//        if(!playerGiftGag.getHistoryGet().containsKey(GiftBagConstant.C_J_GIFTID)){
//            frontendService.broadcast(session,new MsgVO(EventConstant.EVENT_C_J,1));
//        }
//        if(!playerGiftGag.getHistoryGet().containsKey(GiftBagConstant.MUSIC)){
//            frontendService.broadcast(session,new MsgVO(EventConstant.EVENT_MUSIC,1));
//        }
        //五一活动礼包
        if(!playerGiftGag.getHistoryGet().containsKey(GiftBagConstant.W_1)){
            frontendService.broadcast(session,new MsgVO(17,1));
        }
        /**
         * 幸运翻牌
         */
        PlayerLottery playerLottery=playerlotteryDao.getPlayerLottery(session.getPid());
        if(playerLottery.getNum()>0){
            //幸运翻牌的推送
            frontendService.broadcast(session, new MsgVO(EventConstant.EVENT_LOTTERY,playerLottery.getNum()));
        }
        if(playerLottery.getRouletteNum()< RouletteConstant.FREE){
            //轮盘的推送
            frontendService.broadcast(session, new MsgVO(EventConstant.EVENT_ROULETTE,RouletteConstant.FREE - playerLottery.getRouletteNum()));
        }

        //摆摊相关
        PlayerStall playerStall = playerStallDao.getPlayerStall(session.getPid());
        if(playerStall.isEnable()){
            stallPriceContainer.sysBuy(playerStall);
            playerStallDao.saveBean(playerStall);
        }

        //连续登陆检测
        PlayerSuccessive playerSuccessive = playerSuccessiveDao.getPlayerSuccessive(session.getPid());
        successiveContainer.detect(playerSuccessive);
        playerSuccessiveDao.saveBean(playerSuccessive);
        //数字图标推送
        int num = successiveContainer.getNum(playerSuccessive.getDay(),playerSuccessive.getGetPrize());
        if(num>0){
            frontendService.broadcast(session, new MsgVO(EventConstant.EVENT_SUCCESSIVE,num));
        }

        //明星助理广播
        Family family = familyDao.getBeanByStarId(player.getStarId());
        if(family!=null){
            FamilyMemberInfo memberInfo = familyDao.getMemberInfo(family.getId(), session.getPid());
            if(memberInfo.getIdentity() == FamilyConstant.TYPE_ZL){
                String expired = message.getMessage("family.assistant.login", new String[]{family.getName(),player.getNickName()}, Locale.CHINA);
                ChatVO evo = roleFactory.newShoutChat("", "", expired);
                Session[] sessions = statusService.getMember(session, SessionUtil.KEY_CHAT_WORLD);
                frontendService.broadcast(sessions, evo);
            }
        }
        /**
         * 活跃度
         */
        ActivePlayer ap = activePlayerDao.getBean(session.getPid());
        num = activeRuleContainer.getFinishNum(ap);
        if (num>0){
            //活跃度数据推送
            frontendService.broadcast(session, new MsgVO(EventConstant.EVENT_DAY_ACTIVE,num));
        }
        /**
         * 家族银行活动
         */
//        PlayerEvent playerEvent=playerEventDao.getPlayerEvent(session.getPid());
//        EventRule eventRule=eventRuleContainer.getFamilyBankEventRule();
//        FamilyBankEvent familyBankEvent=playerEvent.getFamilyBankEvent(eventRule.getId());
//        if(familyBankEvent.getCanReward().size()>0){
//            frontendService.broadcast(session, new MsgVO(EventConstant.EVENT_F_B,familyBankEvent.getCanReward().size()));
//        }


    }

    @RpcAnnotation(cmd="p.logout",req=String.class,name="退出登录")
    public void logout(Session session,String pid){
        String channelId = SessionUtil.getChannelScene(session);
        //退出的同时就会退出场景
        //statusRemote.outerScene(session,channelId);
        statusRemote.logout(session,channelId);
        //取得当前频道的所有用户,发广播
        Session[] sessions = statusRemote.getMember(session,channelId);
        int x = 0;
        int y = 0;
        MoveVO move = new MoveVO(session.getPid(),x,y, SceneConstant.MOVE_TYPE_OUTER_2);
        frontendService.broadcast(sessions,move);
        SessionUtil.setChannelScene(session, null);
        OtherPlayer op = otherPlayerDao.getBean(session.getPid());
        op.doLogout(new Date());
        otherPlayerDao.saveBean(op);

        //日志
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_2,session.getPid(),""));
    }
    @RpcAnnotation(cmd="p.rename",req=RoleReq.class,name="角色改名")
    public void rename(Session session,RoleReq req){
        Player player = playerDao.getBean(session.getPid());
        if (player!=null){
            player.setNickName(req.getName());
            playerDao.saveBean(player);
        }
    }

    @RpcAnnotation(cmd="p.get.info",lock = false,req=String.class,name="取得玩家信息")
    public PlayerVO getPlayerInfo(Session session,String pid) throws AlertException {
        Player player = playerDao.getBean(pid);
        if (null==player){
            exceptionFactory.throwAlertException("p.notfound");
        }
        return roleFactory.newPlayerVO(player);
    }



    @RpcAnnotation(cmd="p.create",lock=false,req=CreatePlayerReq.class,name="创建角色")
    public String createPlayer(Session session,CreatePlayerReq req) {
        Player player = new Player();
        player.setId(Uid.uuid());
        player.setCreateTime(new Date());
        player.setSex(req.getSex());
        player.setType(req.getType());
        String name = StringTrimUtils.trim(req.getNickName(),12);
        player.setNickName(name);
        player.setUserId(req.getUserId());
//        player.incGameCoin(0);//TODO:上线前需要处理
//        player.incGoldCoin(0);
        player.setStarId(req.getStarId());
        player.setPayMember(req.getPayMember());

        PlayerInfo playerInfo = playerInfoDao.getPlayerInfo(player.getId());
        playerInfo.setSignature(req.getSignature());
        playerInfo.setBirth(new Date(req.getBirthday()));
        if(req.getProvince()!=null){
            playerInfo.setProvince(req.getProvince());
        }
        if(req.getCity()!=null){
            playerInfo.setCity(req.getCity());
        }

        playerInfoDao.saveBean(playerInfo);

        playerDao.insertBean(player);

         //日志
         gameLoggerWrite.write(new GameLogger(GameLogger.LOG_24,player.getId(),""));
        /**
         * 记录玩家注册
         */
        statistics.recordRegister(player.getId(),player);

        return player.getId();
    }
    @RpcAnnotation(cmd="p.update",lock = true,req=CreatePlayerReq.class,name="更新玩家超级粉丝信息")
    public void updatePlayerPay(Session session,CreatePlayerReq req) {
        String pid=playerDao.getPid(req.getUserId());
        Player player=playerDao.getBean(pid);
        player.setPayMember(req.getPayMember());
        playerDao.saveBean(player);
    }
    @RpcAnnotation(cmd="p.get.id",lock = false,req=Integer.class,name="取得playerid")
    public String getPid(Session session,Integer id) {
        String pid = playerDao.getPid(id);
        return pid;
    }

    //公测后要去掉的代码
    @RpcAnnotation(cmd="p.claim",lock = true,req=CommonReq.class,name="公测期间每日领取达人币")
    public RewardVO claim(Session session,CommonReq req) throws AlertException {
        Player player = playerDao.getBean(session.getPid());
        if (player==null){
            exceptionFactory.throwAlertException("p.notplayer");
        }
        OtherPlayer op = otherPlayerDao.getBean(player.getId());
        if (op.isClaim()){
            exceptionFactory.throwAlertException("p.claim.already");
        }
//        player.incGoldCoin(50);
        op.setClaimDate(new Date());
        playerDao.saveBean(player);
        otherPlayerDao.saveBean(op);
        RewardVO vo = new RewardVO();
        vo.setRmb(50);
        return vo;
    }

}
