package com.pengpeng.stargame.wharf.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.cmd.FarmRpc;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.container.IFarmOrderRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.integral.container.IIntegralRuleContainer;
import com.pengpeng.stargame.integral.container.impl.IIntegralRuleContainerImpl;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.wharf.*;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.rsp.RspWharfFactory;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.vo.wharf.WharfGoldCoinVO;
import com.pengpeng.stargame.vo.wharf.WharfInfoVO;
import com.pengpeng.stargame.vo.wharf.WharfReq;
import com.pengpeng.stargame.wharf.container.IWharfInvoiceContainer;
import com.pengpeng.stargame.wharf.dao.IPlayerWharfDao;
import com.pengpeng.stargame.wharf.dao.IWharfRankSetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-12-11
 * Time: 下午5:04
 */
@Component
public class WharfRpc extends RpcHandler {
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IPlayerWharfDao playerWharfDao;
    @Autowired
    private IWharfInvoiceContainer wharfInvoiceContainer;
    @Autowired
    private StatusRemote statusService;
    @Autowired
    private IWharfRankSetDao wharfRankSetDao;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private RspWharfFactory rspWharfFactory;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private FarmRpc farmRpc;
    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private IIntegralRuleContainer iIntegralRuleContainer;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;

    @RpcAnnotation(cmd = "wharf.enable", vo = WharfInfoVO.class, req = WharfReq.class, name = "码头启用", lock = true)
    public WharfInfoVO enable(Session session, WharfReq req) throws GameException {
        String pid = session.getPid();

        Player player = playerDao.getBean(pid);
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());
        //检测条件
        wharfInvoiceContainer.checkEnable(farmPlayer, player);

        PlayerWharf playerWharf = playerWharfDao.getPlayerWharf(pid);
        playerWharf.setEnable(true);
        playerWharf.setEnableDate(new Date());
        playerWharfDao.saveBean(playerWharf);
        playerDao.saveBean(player);
        //财富通知
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        return null;
    }

    @RpcAnnotation(cmd = "wharf.getInfo", vo = WharfInfoVO.class, req = WharfReq.class, name = "码头信息", lock = true)
    public WharfInfoVO getInfo(Session session, WharfReq req) throws GameException {
        String pid = req.getPid();//session.getPid();
        String myPid = session.getPid();

        PlayerWharf playerWharf = playerWharfDao.getPlayerWharf(pid);
        if (!playerWharf.isEnable()) {
            return rspWharfFactory.wharfInfoVO3();
        }
        FarmPackage farmPackage = farmPackageDao.getBean(myPid);
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());

        WharfInfo wharfInfo = null;
        if(pid.indexOf(myPid)>=0){//访问自己
            wharfInfo = wharfInvoiceContainer.checkShip(playerWharf, farmPlayer);//检测货船
        }else{
            wharfInfo = rspWharfFactory.wharfInfoVO1(playerWharf);//只做判断,不更新好友数据
            if(!wharfInfo.getShipArrived()){
                if(playerWharf.getInvoices()!=null){//清空货单防止好友列表显示帮助状态
                    playerWharf.setInvoices(null);
                    playerWharfDao.saveBean(playerWharf);
                }
                return wharfInfo;
            }
        }

        //检测货单
        WharfInvoiceInfo[] wharfInvoiceInfo = wharfInvoiceContainer.checkInvoice(farmPackage, playerWharf);
        wharfInfo.setInvoices(wharfInvoiceInfo);
        wharfInfo.setCredit(playerWharf.getCredit());
        wharfInfo.setContribution(playerWharf.getContribution());
        playerWharfDao.saveBean(playerWharf);
        return rspWharfFactory.wharfInfoVO(wharfInfo);
    }

    @RpcAnnotation(cmd = "wharf.submit", vo = WharfInfoVO.class, req = WharfReq.class, name = "提交任务", lock = true)
    public WharfInfoVO submit(Session session, WharfReq req) throws GameException {
        String pid = req.getPid();
        String mid = session.getPid();

        PlayerWharf playerWharf = playerWharfDao.getPlayerWharf(pid);
        FarmPackage farmPackage = farmPackageDao.getBean(mid);
        WharfInvoiceInfo wii = wharfInvoiceContainer.submit(farmPackage, playerWharf, req.getInvoiceOrder());
        if(null == wii){
            return null;
        }
        Session[] mysessions = {session};
        if (wii.getGameCoin() > 0) {
            Player player = playerDao.getBean(mid);
            player.incGameCoin(wii.getGameCoin());
            playerDao.saveBean(player);
            //财富通知
            frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));

        }
        if (wii.getFarmExp() > 0) {
            FarmPlayer farmPlayer=farmPlayerDao.getFarmPlayer(mid,System.currentTimeMillis());
            farmLevelRuleContainer.addFarmExp(farmPlayer,wii.getFarmExp());
            farmPlayerDao.saveBean(farmPlayer);
            //经验通知
            frontendService.broadcast(mysessions,farmRpc.getFarmVoByPlayerId(mid));
        }
        playerWharfDao.saveBean(playerWharf);
        farmPackageDao.saveBean(farmPackage);

        if(!pid.equals(mid)){
            /**
             * 成就统计  为其它玩家装箱次数
             */
            successRuleContainer.updateSuccessNum(session.getPid(),9,1,"");
            /**
             * 任务的
             */
            taskRuleContainer.updateTaskNum(session.getPid(), TaskConstants.CONDI_TYPE_24,"",1);
            /**
             * 通知 好友状态 改变
             */
            farmRpc.broadcastFarmState(session,pid);

        }

        //推送完成事件

        return null;
    }

    @RpcAnnotation(cmd = "wharf.ship.sail", vo = WharfInfoVO.class, req = WharfReq.class, name = "货船启航", lock = true)
    public WharfInfoVO shipSail(Session session, WharfReq req) throws GameException {
        String pid = session.getPid();

        PlayerWharf playerWharf = playerWharfDao.getPlayerWharf(pid);
        FarmPackage farmPackage = farmPackageDao.getBean(pid);
        //检测货单
        WharfInvoiceInfo[] wharfInvoiceInfo = wharfInvoiceContainer.checkInvoice(farmPackage, playerWharf);

        wharfInvoiceContainer.sail(playerWharf, wharfInvoiceInfo);

        //奖励
        if (playerWharf.getContribution() > 0) {
            FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
            familyMemberInfo.incDevote(playerWharf.getContribution(),new Date());
            //familyRuleContainer.addFamilyDevote(familyMemberInfo,playerWharf.getContribution());
            familyMemberInfoDao.saveBean(familyMemberInfo);

            //记录
            playerWharf.addContri();
            //本周最高
            String weekKey = wharfInvoiceContainer.getWeekKey() ;
            //本周贡献排行
            Double score = wharfRankSetDao.getScore(weekKey,pid);
            if(null!=score){
                //wharfRankSetDao.removeBean(weekKey,pid);
                wharfRankSetDao.addBeanExpire(weekKey,pid,playerWharf.getContribution()+score.intValue(),7);
            } else{
                wharfRankSetDao.addBeanExpire(weekKey,pid,playerWharf.getContribution(),7);
            }

            //家族本周贡献排行
            String fkey = familyMemberInfo.getFamilyId()+weekKey;
            score = null;
            score = wharfRankSetDao.getScore(fkey,pid);
            if(null!=score) {
                //wharfRankSetDao.removeBean(fkey,pid);
                wharfRankSetDao.addBeanExpire(fkey,pid,playerWharf.getContribution()+score.intValue(),7);
            }else{
                wharfRankSetDao.addBeanExpire(fkey,pid,playerWharf.getContribution(),7);
            }
        }

        playerWharfDao.saveBean(playerWharf);

        if (playerWharf.getCredit() > 0) {
            Player player = playerDao.getBean(pid);
            try {
                /**
                 * 记录积分获取动作
                 */
                iIntegralRuleContainer.addIntegralAction(session.getPid(), IIntegralRuleContainerImpl.INTEGRAL_ACTION_3,playerWharf.getCredit());

            } catch (Exception e) {
                exceptionFactory.throwRuleException("active.gamecoin");
            }
        }

        /**
         * 成就统计  起航次数
         */
        successRuleContainer.updateSuccessNum(session.getPid(),3,1,"");
        /**
         * 任务的
         */
        taskRuleContainer.updateTaskNum(session.getPid(), TaskConstants.CONDI_TYPE_23,"",1);

        //场景更新通知
        WharfInfoVO wharfInfoVO = rspWharfFactory.wharfInfoSailVO(playerWharf);
        Session[] sessions = statusRemote.getMember(session, SessionUtil.getChannelScene(session));
        frontendService.broadcast(sessions, wharfInfoVO);
        return wharfInfoVO;
    }

    @RpcAnnotation(cmd = "wharf.need.help", vo = WharfInfoVO.class, req = WharfReq.class, name = "请求好友帮助", lock = true)
    public WharfInfoVO needHelp(Session session, WharfReq req) throws GameException {
        String pid = session.getPid();
        PlayerWharf playerWharf = playerWharfDao.getPlayerWharf(pid);
        wharfInvoiceContainer.needHelp(playerWharf, req.getInvoiceOrder());
        playerWharfDao.saveBean(playerWharf);
        return null;
    }

    @RpcAnnotation(cmd = "wharf.rank", vo = WharfInfoVO.class, req = WharfReq.class, name = "码头排行", lock = true)
    public WharfInfoVO getRank(Session session, WharfReq req) throws GameException {
        String pid = session.getPid();
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
        //本周
        String weekKey = wharfInvoiceContainer.getWeekKey() ;
        return rspWharfFactory.wharfRankVO2(weekKey, familyMemberInfo.getFamilyId());
    }

    @RpcAnnotation(cmd = "wharf.ship.arrive", vo = WharfInfoVO.class, req = WharfReq.class, name = "码头货船到来", lock = true)
    public WharfGoldCoinVO arrive(Session session, WharfReq req) throws GameException {
        String pid = session.getPid();
        Player player = playerDao.getBean(pid);

        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());
        PlayerWharf playerWharf = playerWharfDao.getPlayerWharf(pid);
        WharfGoldCoinVO wg = new WharfGoldCoinVO();
        wg.setGoldCoin(wharfInvoiceContainer.arrive(playerWharf,player,farmPlayer));

        playerDao.saveBean(player);
        playerWharfDao.saveBean(playerWharf);
        return wg;
    }


    @RpcAnnotation(cmd = "wharf.arrive.gold", vo = WharfInfoVO.class, req = WharfReq.class, name = "码头货船到来所需达人币", lock = true)
    public WharfGoldCoinVO arriveGold(Session session, WharfReq req) throws GameException {
        String pid = session.getPid();
        PlayerWharf playerWharf = playerWharfDao.getPlayerWharf(pid);

        WharfGoldCoinVO wg = new WharfGoldCoinVO();
        wg.setGoldCoin(wharfInvoiceContainer.arriveGold(playerWharf));
        return wg;
    }

    @RpcAnnotation(cmd = "wharf.ship.submit.all", vo = WharfInfoVO.class, req = WharfReq.class, name = "全部提交", lock = true)
    public WharfGoldCoinVO submitAll(Session session, WharfReq req) throws GameException {
        String pid = session.getPid();
        PlayerWharf playerWharf = playerWharfDao.getPlayerWharf(pid);
        List<PlayerWharfInvoice> list = playerWharf.getInvoices();
        if(list != null){
        for(PlayerWharfInvoice pwi:list){
            pwi.setCompleted(true);
        }
        }
        playerWharfDao.saveBean(playerWharf);
        return null;
    }

}
