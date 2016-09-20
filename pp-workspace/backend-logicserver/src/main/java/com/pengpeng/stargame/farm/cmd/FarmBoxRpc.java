package com.pengpeng.stargame.farm.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.farm.container.IFarmBoxRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmBoxDao;
import com.pengpeng.stargame.model.farm.box.PlayerFarmBox;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspFarmFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.event.EventReq;
import com.pengpeng.stargame.vo.farm.box.FarmBoxReq;
import com.pengpeng.stargame.vo.farm.box.FarmBoxVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午2:25
 */
@Component()
public class FarmBoxRpc extends RpcHandler {
    @Autowired
    private IFarmBoxDao farmBoxDao;
    @Autowired
    private IFarmBoxRuleContainer farmBoxRuleContainer;
    @Autowired
    private RspFarmFactory rspFarmFactory;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private RspRoleFactory roleFactory;
    @RpcAnnotation(cmd = "farm.box.open", lock = false, req = FarmBoxReq.class, name = "打开宝箱")
    public RewardVO openbox(Session session, FarmBoxReq req) throws GameException {
        Player player=playerDao.getBean(session.getPid());
        PlayerFarmBox playerFarmBox=farmBoxDao.getPlayerFarmBox(session.getPid());
        if(playerFarmBox.getBoxstatu()==0){
            return null;
        }
        RewardVO rewardVO=farmBoxRuleContainer.openBox(playerFarmBox,player);
        playerDao.saveBean(player);
        farmBoxDao.saveBean(playerFarmBox);
        frontendService.broadcast(session,roleFactory.newPlayerVO(player));
        return  rewardVO;
    }
    @RpcAnnotation(cmd = "farm.box.refresh", lock = false, req = FarmBoxReq.class, name = "刷新宝箱，进入农场调用")
    public FarmBoxVO refresh(Session session, FarmBoxReq req) throws GameException {
        PlayerFarmBox playerFarmBox=farmBoxDao.getPlayerFarmBox(session.getPid());
        farmBoxRuleContainer.refreshFarmBox(playerFarmBox);
        farmBoxDao.saveBean(playerFarmBox);
        return rspFarmFactory.getFarmBoxVo(playerFarmBox);
    }
    @RpcAnnotation(cmd = "farm.box.cancel", lock = false, req = FarmBoxReq.class, name = "取消打开宝箱")
    public FarmBoxVO cancel(Session session, FarmBoxReq req) throws GameException {
        PlayerFarmBox playerFarmBox=farmBoxDao.getPlayerFarmBox(session.getPid());
        playerFarmBox.setBoxstatu(0);
        playerFarmBox.setAllboxnum(playerFarmBox.getAllboxnum()+1);
        farmBoxDao.saveBean(playerFarmBox);
        return rspFarmFactory.getFarmBoxVo(playerFarmBox);
    }
}
