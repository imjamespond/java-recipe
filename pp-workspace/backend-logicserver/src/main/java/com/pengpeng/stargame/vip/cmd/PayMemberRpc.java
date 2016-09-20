package com.pengpeng.stargame.vip.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.vip.PlayerVip;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.rsp.RspVipFactiory;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vip.dao.IVipDao;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.room.RoomIdReq;
import com.pengpeng.stargame.vo.room.RoomVO;
import com.pengpeng.stargame.vo.vip.VipInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-11-19
 * Time: 下午12:16
 */
@Component()
public class PayMemberRpc extends RpcHandler {
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    @Autowired
    private IVipDao vipDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private RspVipFactiory rspVipFactiory;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private RspRoleFactory rspRoleFactory;
    @RpcAnnotation(cmd = "vip.refresh", req = IdReq.class, name = "刷新VIP",vo=VipInfoVO.class)
    public VipInfoVO refresh(Session session, IdReq req) throws AlertException {
        PlayerVip vipInfoVO=vipDao.getPlayerVip(session.getPid());
        PlayerVip vipInfo=vipDao.getPlayerVip(session.getPid());
        Player player=playerDao.getBean(session.getPid());
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, rspRoleFactory.newPlayerVO(player));
        return rspVipFactiory.getVipInfoVO(session,vipInfo,player);
    }
    @RpcAnnotation(cmd = "vip.getinfo", req = IdReq.class, name = "获取VIp信息",vo=VipInfoVO.class)
    public VipInfoVO getinfo(Session session, IdReq req) throws AlertException {
        PlayerVip vipInfo=vipDao.getPlayerVip(session.getPid());
        Player player=playerDao.getBean(session.getPid());
        return rspVipFactiory.getVipInfoVO(session,vipInfo,player);
    }

}
