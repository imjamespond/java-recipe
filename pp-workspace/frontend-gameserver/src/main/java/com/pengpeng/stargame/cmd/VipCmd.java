package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.PayMemberRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.task.TaskReq;
import com.pengpeng.stargame.vo.vip.VipInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-11-20
 * Time: 下午5:17
 */
@Component
public class VipCmd extends AbstractHandler {
    @Autowired
    private PayMemberRpcRemote payMemberRpcRemote;
    @CmdAnnotation(cmd = "vip.refresh", req = IdReq.class, name = "刷新VIP",vo=VipInfoVO.class)
    public Response refresh(Session session, IdReq req) throws GameException {

        return Response.newObject(payMemberRpcRemote.refresh(session,req));
    }

    @CmdAnnotation(cmd = "vip.getinfo", req = IdReq.class, name = "获取VIp信息",vo=VipInfoVO.class)
    public Response getinfo(Session session, IdReq req) throws GameException {

        return Response.newObject(payMemberRpcRemote.getinfo(session,req));
    }
}
