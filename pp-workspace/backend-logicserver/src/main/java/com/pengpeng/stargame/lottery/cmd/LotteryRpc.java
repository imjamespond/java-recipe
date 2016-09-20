package com.pengpeng.stargame.lottery.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.constant.BaseItemConstant;
import com.pengpeng.stargame.constant.BaseRewardConstant;
import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.cmd.FarmRpc;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.dao.IFarmDecoratePkgDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.lottery.container.ILotteryContainer;
import com.pengpeng.stargame.lottery.container.ILotteryTypeContainer;
import com.pengpeng.stargame.lottery.dao.ILotteryInfoListDao;
import com.pengpeng.stargame.lottery.dao.IPlayerlotteryDao;
import com.pengpeng.stargame.lottery.dao.IRouletteHistListDao;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.lottery.*;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.player.container.IActivePlayerContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspLotteryFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.lottery.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:04
 */
@Component
public class LotteryRpc extends RpcHandler {
    @Autowired
    private ILotteryContainer lotteryContainer;
    @Autowired
    private ILotteryTypeContainer lotteryTypeContainer;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private RspLotteryFactory lotteryFactory;
    @Autowired
    private IPlayerlotteryDao playerlotteryDao;
    @Autowired
    private ILotteryInfoListDao lotteryInfoListDao;
    @Autowired
    private IRouletteHistListDao rouletteHistListDao;
    @Autowired
    private StatusRemote statusService;
    @Autowired
    private RspRoleFactory rspRoleFactory;
    @Autowired
    private FrontendServiceProxy frontendServiceProxy;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private MessageSource message;
    @Autowired
    private IActivePlayerContainer activePlayerContainer;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private IFarmDecoratePkgDao farmDecoratePkgDao;
    @Autowired
    private FarmRpc farmRpc;

    @RpcAnnotation(cmd = "lotter.draw", vo = LotteryVO.class, req = LotteryReq.class, name = "抽奖", lock = true)
    public LotteryVO lotteryDraw(Session session, LotteryReq req) throws GameException {
        String pid = session.getPid();
        Player player = playerDao.getBean(pid);
        int type = req.getType();

        Lottery lottery = lotteryContainer.lotteryDraw(type, player.getType());

        PlayerLottery playerLottery = playerlotteryDao.getPlayerLottery(pid);
        int freeNum = playerLottery.getNum();


        //抽奖及道具派送
        FarmPackage farmPackage = farmPackageDao.getBean(session.getPid());
        RoomPackege roomPackege = roomPackegeDao.getRoomPackege(session.getPid());
        FashionCupboard fashionCupboard = fashionCupboardDao.getBean(session.getPid());
        FarmDecoratePkg farmDecoratePkg=farmDecoratePkgDao.getFarmDecoratePkg(session.getPid());

        if (!lottery.getItemId().equals("0")) {
            BaseItemRule baseItemRule = baseItemRulecontainer.getElement(lottery.getItemId());
            if (!baseItemRulecontainer.addItemAndCheckNoSaving(baseItemRule,lottery.getNum(),farmPackage,roomPackege,fashionCupboard,farmDecoratePkg)) {
                if(baseItemRule.getItemtype() == BaseItemConstant.TYPE_FARM)
                    exceptionFactory.throwAlertException("package.farm.full");
                else if(baseItemRule.getItemtype() == BaseItemConstant.TYPE_FASHION)
                    exceptionFactory.throwAlertException("package.fashion.full");
                else
                    exceptionFactory.throwAlertException("item.add.failed");
            }
        }

        //检测金币
        lotteryTypeContainer.check(type, player, playerLottery);

        player.incGameCoin(lottery.getGameCoin());
        //持久化
        farmPackageDao.saveBean(farmPackage);
        roomPackegeDao.saveBean(roomPackege);
        fashionCupboardDao.saveBean(fashionCupboard);
        playerlotteryDao.saveBean(playerLottery);
        farmDecoratePkgDao.saveBean(farmDecoratePkg);
        playerDao.saveBean(player);
        LotteryVO vo = lotteryFactory.lotteryVO(lottery);

        //加入中奖记录
        if (lottery.getGroupId() > 0) {
            OneLotteryInfo oneLotteryInfo = new OneLotteryInfo();
            oneLotteryInfo.setPid(pid);
            oneLotteryInfo.setItemId(lottery.getItemId());
            oneLotteryInfo.setDate(new Date());
            oneLotteryInfo.setNum(lottery.getNum());
            oneLotteryInfo.setGameCoin(lottery.getGameCoin());
            lotteryInfoListDao.addLotteryInfo("", oneLotteryInfo);
            //全服广播
            BaseItemRule itemRule = baseItemRulecontainer.getElement(lottery.getItemId());
            if (itemRule != null) {
                String itemName = itemRule.getName();
                Session[] sessions = statusService.getMember(session, SessionUtil.KEY_CHAT_WORLD);
                String content = message.getMessage("lottery.win", new String[]{player.getNickName(), Integer.toString(lottery.getNum()), itemName}, Locale.CHINA);
                ChatVO svo = rspRoleFactory.newShoutChat("", "", content);
                frontendServiceProxy.broadcast(sessions, svo);
            }
        }

        //财富通知
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        //数字图标推送
        if (freeNum > 0) {
            frontendServiceProxy.broadcast(mysessions, new MsgVO(EventConstant.EVENT_LOTTERY, playerLottery.getNum()));
        }
        //任务通知
        if ("items_12023items_12015".indexOf(lottery.getItemId()) >= 0 && lottery.getGameCoin() == 0) {
            frontendServiceProxy.broadcast(mysessions, new MsgVO(EventConstant.EVENT_TASK_ITEM, lottery.getNum()));
        }


        //抽奖记录
        List<OneLotteryInfo> list = lotteryInfoListDao.getLotteryList("");
        LotteryInfoVO ivo = lotteryFactory.lotteryInfoVO(playerLottery.getNum(), list);
        vo.setLotteryInfoVO(ivo);

        //增加活跃度
        activePlayerContainer.finish(session, pid, PlayerConstant.ACTIVE_TYPE_6, 1);

        //日志
        String value = lottery.getItemId() + GameLogger.SPLIT + lottery.getNum() + GameLogger.SPLIT + lottery.getGameCoin();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_6, session.getPid(), value));

        return vo;
    }

    @RpcAnnotation(cmd = "lotter.info", vo = LotteryInfoVO.class, req = LotteryReq.class, name = "抽奖信息")
    public LotteryInfoVO lotteryInfo(Session session, LotteryReq req) throws GameException {
        String pid = session.getPid();

        List<OneLotteryInfo> list = lotteryInfoListDao.getLotteryList("");
        PlayerLottery playerLottery = playerlotteryDao.getPlayerLottery(pid);

        LotteryInfoVO vo = lotteryFactory.lotteryInfoVO(playerLottery.getNum(), list);
        return vo;
    }

    @RpcAnnotation(cmd = "roulette.draw", vo = LotteryInfoVO.class, req = LotteryReq.class, name = "轮盘抽奖")
    public RouletteVO rouletteDraw(Session session, LotteryReq req) throws GameException {
        String pid = session.getPid();
        Player player = playerDao.getBean(pid);
        PlayerLottery playerLottery = playerlotteryDao.getPlayerLottery(pid);
        RouletteVO vo = lotteryContainer.rouletteDraw(player,playerLottery);
        playerlotteryDao.saveBean(playerLottery);
        playerDao.saveBean(player);

        List<RouletteHist> list = rouletteHistListDao.getLotteryList("");
        lotteryFactory.rouletteInfoVO(list,vo);
        vo.setNum(RouletteConstant.FREE - playerLottery.getRouletteNum());
        vo.setExp(playerLottery.getRouletteExp());

        //数字图标推送
        if (vo.getNum() > 0) {
            Session[] mysessions = {session};
            frontendServiceProxy.broadcast(mysessions, new MsgVO(EventConstant.EVENT_ROULETTE, vo.getNum()));
        }
       return vo;
    }

    @RpcAnnotation(cmd = "roulette.deduct", vo = LotteryInfoVO.class, req = LotteryReq.class, name = "轮盘减次数")
    public RouletteVO rouletteDeduct(Session session, LotteryReq req) throws GameException {
        String pid = session.getPid();
        Player player = playerDao.getBean(pid);
        PlayerLottery playerLottery = playerlotteryDao.getPlayerLottery(pid);
        lotteryContainer.deduct(player,playerLottery);
        playerlotteryDao.saveBean(playerLottery);
        playerDao.saveBean(player);


        if(playerLottery.getRouletteSpeaker()>0){
            RouletteHistVO vo = new RouletteHistVO();
            vo.setItem(playerLottery.getrReward());
            vo.setName(player.getNickName());
            RouletteHist hist = new RouletteHist();
            hist.setVo(vo);
            hist.setPid(pid);
            rouletteHistListDao.addLotteryInfo("",hist);
        }

        //财富通知
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));

        //数字图标推送
        int num = RouletteConstant.FREE - playerLottery.getRouletteNum();
        if (num > 0) {
            frontendServiceProxy.broadcast(mysessions, new MsgVO(EventConstant.EVENT_ROULETTE, num));
        }
        return null;
    }

    @RpcAnnotation(cmd = "roulette.accept", vo = LotteryInfoVO.class, req = LotteryReq.class, name = "轮盘领奖")
    public RouletteVO rouletteAccept(Session session, LotteryReq req) throws GameException {
        String pid = session.getPid();
        Player player = playerDao.getBean(pid);
        PlayerLottery playerLottery = playerlotteryDao.getPlayerLottery(pid);

        int notify = lotteryContainer.rouletteAccept(player, playerLottery);
        //推送
        Session[] mysessions = {session};
        if ((notify & (BaseRewardConstant.NOTIFY_GAMECOIN | BaseRewardConstant.NOTIFY_GOLDCOIN)) > 0) {
            //财富通知
            frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        }
        if ((notify & BaseRewardConstant.NOTIFY_FARMEXP) > 0) {
            //经验通知
            frontendService.broadcast(mysessions, farmRpc.getFarmVoByPlayerId(pid));
        }

        return null;
    }



}
