package com.pengpeng.stargame.stall.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.BaseRewardConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.cmd.FarmRpc;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.integral.container.IIntegralRuleContainer;
import com.pengpeng.stargame.integral.container.impl.IIntegralRuleContainerImpl;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.stall.PlayerStallPassenger;
import com.pengpeng.stargame.model.stall.PlayerStallPassengerInfo;
import com.pengpeng.stargame.model.stall.StallConstant;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.rsp.RspStallPassengerFactory;
import com.pengpeng.stargame.stall.container.IStallGoldShelfContainer;
import com.pengpeng.stargame.stall.container.IStallPassengerContainer;
import com.pengpeng.stargame.stall.container.IStallPriceContainer;
import com.pengpeng.stargame.stall.dao.IPlayerStallPassengerDao;
import com.pengpeng.stargame.stall.rule.StallPassengerPurchaseRule;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.vo.stall.StallInfoVO;
import com.pengpeng.stargame.vo.stall.StallPassengerInfoVO;
import com.pengpeng.stargame.vo.stall.StallPassengerReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-12-11
 * Time: 下午5:04
 */
@Component
public class StallPassengerRpc extends RpcHandler {
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IPlayerStallPassengerDao playerStallPassengerDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private RspStallPassengerFactory rspStallFactory;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IStallPassengerContainer stallPassengerContainer;
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private FarmRpc farmRpc;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;

    @Autowired
    private IIntegralRuleContainer iIntegralRuleContainer;
    @RpcAnnotation(cmd = "stall.passenger.info", vo = StallInfoVO.class, req = StallPassengerReq.class, name = "路人信息", lock = true)
    public StallPassengerInfoVO getInfo(Session session, StallPassengerReq req) throws GameException {
        String pid = session.getPid();
        PlayerStallPassengerInfo info = playerStallPassengerDao.getPlayerStallPassenger(pid);
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());
        FarmPackage farmPackage = farmPackageDao.getBean(pid);
        boolean needUpdate = stallPassengerContainer.check(info, farmPlayer);
        StallPassengerInfoVO vo = rspStallFactory.stallPassengerInfoVO(info,farmPackage);
        if(needUpdate) {
            playerStallPassengerDao.saveBean(info);
        }
        return vo;
    }

    @RpcAnnotation(cmd = "stall.passenger.sell", vo = StallInfoVO.class, req = StallPassengerReq.class, name = "向路人出售", lock = true)
    public StallPassengerInfoVO sell(Session session, StallPassengerReq req) throws GameException {
        String pid = session.getPid();
        Player player = playerDao.getBean(pid);
        FarmPackage farmPackage = farmPackageDao.getBean(pid);
        PlayerStallPassengerInfo info = playerStallPassengerDao.getPlayerStallPassenger(pid);
        PlayerStallPassenger passenger =stallPassengerContainer.getPlayerStallPassenger(info,req.getPassengerId());
        StallPassengerPurchaseRule rule = stallPassengerContainer.getPurchaseRules(passenger.getPurchaseId());
        //扣物品
        int num = farmPackage.getSumByNum(rule.getItemId());
        if(num>=rule.getItemNum()){
            farmPackage.deduct(rule.getItemId(), rule.getItemNum());//扣减仓库物品
            farmPackageDao.saveBean(farmPackage);
        }
        if(rule.getGameCoin()>0){
            player.incGameCoin(rule.getGameCoin());
            playerDao.saveBean(player);
            //财富通知
            Session[] mysessions = {session};
            frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        }
        //路人离开
        info.getPassengers()[req.getPassengerId()] = null;
        info.setRefreshDate(new Date());
        //赠送积分
        if(passenger.getCredit()>0){
            try {
                /**
                 * 记录积分获取动作
                 */
                iIntegralRuleContainer.addIntegralAction(session.getPid(), IIntegralRuleContainerImpl.INTEGRAL_ACTION_4,passenger.getCredit());
            } catch (Exception e) {
                exceptionFactory.throwAlertException("active.gamecoin");
            }
        }
        playerStallPassengerDao.saveBean(info);
        //成就统计  像路人出售商品 次数
        successRuleContainer.updateSuccessNum(session.getPid(),8,1,"");
        return null;
    }

    @RpcAnnotation(cmd = "stall.passenger.reject", vo = StallInfoVO.class, req = StallPassengerReq.class, name = "拒绝出售", lock = true)
    public StallPassengerInfoVO reject(Session session, StallPassengerReq req) throws GameException {
        String pid = session.getPid();
        PlayerStallPassengerInfo info = playerStallPassengerDao.getPlayerStallPassenger(pid);
        PlayerStallPassenger passenger =stallPassengerContainer.getPlayerStallPassenger(info,req.getPassengerId());
        //路人离开
        info.getPassengers()[req.getPassengerId()] = null;
        info.setRefreshDate(new Date());
        playerStallPassengerDao.saveBean(info);
        StallPassengerInfoVO vo = new StallPassengerInfoVO();
        vo.setRefreshTime(System.currentTimeMillis()+StallConstant.PASSENGER_REFRESH);
        return null;
    }
}
