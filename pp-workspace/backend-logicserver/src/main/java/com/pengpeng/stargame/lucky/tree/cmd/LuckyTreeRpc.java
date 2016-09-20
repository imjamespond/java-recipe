package com.pengpeng.stargame.lucky.tree.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.integral.container.IIntegralRuleContainer;
import com.pengpeng.stargame.integral.container.impl.IIntegralRuleContainerImpl;
import com.pengpeng.stargame.lottery.container.ILotteryContainer;
import com.pengpeng.stargame.lucky.tree.container.ILuckyTreeContainer;
import com.pengpeng.stargame.lucky.tree.dao.IPlayerLuckyTreeDao;
import com.pengpeng.stargame.lucky.tree.rule.LuckyTreeRule;
import com.pengpeng.stargame.lucky.tree.rule.LuckyTreeRuleLoader;
import com.pengpeng.stargame.model.lucky.tree.LuckyTreeCall;
import com.pengpeng.stargame.model.lucky.tree.PlayerLuckyTree;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspLuckyTreeFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeCallVO;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeReq;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:04
 */
@Component
public class LuckyTreeRpc extends RpcHandler {
    @Autowired
    private ILotteryContainer lotteryContainer;
    @Autowired
    private ILuckyTreeContainer luckyTreeContainer;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private RspLuckyTreeFactory luckyTreeFactory;
    @Autowired
    private IPlayerLuckyTreeDao playerLuckyTreeDao;

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
    private IIntegralRuleContainer iIntegralRuleContainer;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;



    @RpcAnnotation(cmd = "lucky.tree.info", vo = LuckyTreeVO.class, req = LuckyTreeReq.class, name = "招财树信息")
    public LuckyTreeVO getInfo(Session session, LuckyTreeReq req) throws GameException {
        String pid = req.getPid();

        PlayerLuckyTree playerLuckyTree = playerLuckyTreeDao.getPlayerLuckyTree(pid);

        LuckyTreeVO vo = luckyTreeFactory.LuckyTreeVO(playerLuckyTree);
        vo.setRuleVOs(luckyTreeContainer.getLuckyTreeRuleVOs());

        return vo;
    }

    @RpcAnnotation(cmd = "lucky.tree.call", vo = LuckyTreeCallVO.class, req = LuckyTreeReq.class, name = "招财树招财")
    public LuckyTreeCallVO call(Session session, LuckyTreeReq req) throws GameException {
        String pid = session.getPid();
        Player player = playerDao.getBean(pid);
        PlayerLuckyTree playerLuckyTree = playerLuckyTreeDao.getPlayerLuckyTree(pid);
        LuckyTreeCall luckyTreeCall = luckyTreeContainer.call(player,playerLuckyTree);
        playerLuckyTreeDao.saveBean(playerLuckyTree);

        //赠送积分
        if(luckyTreeCall.getCredit()>0){
        try {
            iIntegralRuleContainer.addIntegralAction(pid, IIntegralRuleContainerImpl.INTEGRAL_ACTION_7,luckyTreeCall.getCredit());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        }
        //送达人币
        player.incGameCoin(luckyTreeCall.getGameCoin() * luckyTreeCall.getCritical());

        playerDao.saveBean(player);
        //财富通知
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));

        //暴击广播
        if (luckyTreeCall.getCritical()>1) {
            Session[] sessions = statusService.getMember(session, SessionUtil.KEY_CHAT_WORLD);
            String content = message.getMessage("lucky.tree.critical", new String[]{player.getNickName(), String.valueOf(luckyTreeCall.getCritical()*luckyTreeCall.getGameCoin())}, Locale.CHINA);
            ChatVO svo = rspRoleFactory.newShoutChat("", "", content);
            frontendServiceProxy.broadcast(sessions, svo);
        }


        return luckyTreeCall;
    }

    @RpcAnnotation(cmd = "lucky.tree.add", vo = LuckyTreeCallVO.class, req = LuckyTreeReq.class, name = "增加达人币次数")
    public LuckyTreeVO add(Session session, LuckyTreeReq req) throws GameException {
        String pid = session.getPid();
        Player player = playerDao.getBean(pid);

        PlayerLuckyTree playerLuckyTree = playerLuckyTreeDao.getPlayerLuckyTree(pid);
        luckyTreeContainer.add(player,playerLuckyTree);

        playerDao.saveBean(player);
        playerLuckyTreeDao.saveBean(playerLuckyTree);

        //财富通知
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));

        return luckyTreeFactory.LuckyTreeVO(playerLuckyTree);
    }

    @RpcAnnotation(cmd = "lucky.tree.water", vo = LuckyTreeVO.class, req = LuckyTreeReq.class, name = "招财树浇水")
    public LuckyTreeVO water(Session session, LuckyTreeReq req) throws GameException {
        String pid = session.getPid();
        String fid = req.getPid();//帮好友的id
        //帮好友浇水
        if(fid.indexOf(pid)<0){
            PlayerLuckyTree playerLuckyTree = playerLuckyTreeDao.getPlayerLuckyTree(fid);
            luckyTreeContainer.waterFri(playerLuckyTree);
            playerLuckyTreeDao.saveBean(playerLuckyTree);

            LuckyTreeVO vo = luckyTreeFactory.LuckyTreeVO(playerLuckyTree);
            //通知
            Session fSession = new Session(null,null);
            fSession =statusService.getSession(fSession,fid);
            frontendServiceProxy.broadcast(fSession,vo);
            return vo;
        }else{
            PlayerLuckyTree playerLuckyTree = playerLuckyTreeDao.getPlayerLuckyTree(pid);
            luckyTreeContainer.water(playerLuckyTree);
            playerLuckyTreeDao.saveBean(playerLuckyTree);
            return luckyTreeFactory.LuckyTreeVO(playerLuckyTree);
        }
    }

}
