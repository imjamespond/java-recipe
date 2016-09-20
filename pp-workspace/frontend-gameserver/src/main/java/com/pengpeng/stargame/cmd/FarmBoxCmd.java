package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.FarmBoxRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.farm.box.FarmBoxReq;
import com.pengpeng.stargame.vo.farm.box.FarmBoxVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午2:46
 */
@Component
public class FarmBoxCmd extends AbstractHandler {
    @Autowired
    private FarmBoxRpcRemote farmBoxRpcRemote;
    @CmdAnnotation(cmd = "farm.box.open", req = FarmBoxReq.class, vo=RewardVO.class,name = "打开宝箱")
    public Response openbox(Session session, FarmBoxReq req) throws GameException {
        return Response.newObject(farmBoxRpcRemote.openbox(session,req));
    }
    @CmdAnnotation(cmd = "farm.box.refresh", req = FarmBoxReq.class, vo=FarmBoxVO.class,name = "刷新宝箱，进入农场调用")
    public Response refresh(Session session, FarmBoxReq req) throws GameException {
        return Response.newObject(farmBoxRpcRemote.refresh(session,req));
    }
    @CmdAnnotation(cmd = "farm.box.cancel",req = FarmBoxReq.class,  vo=FarmBoxVO.class,name = "取消打开宝箱")
    public Response cancel(Session session, FarmBoxReq req) throws GameException {
        return Response.newObject(farmBoxRpcRemote.cancel(session,req));
    }
}
