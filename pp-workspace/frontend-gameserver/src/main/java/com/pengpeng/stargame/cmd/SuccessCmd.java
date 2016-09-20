package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SuccessRpcRemote;
import com.pengpeng.stargame.vo.success.PlayerSuccessInfoVO;
import com.pengpeng.stargame.vo.success.SuccessReq;
import com.pengpeng.stargame.vo.successive.SuccessiveReq;
import org.aspectj.weaver.tools.ISupportsMessageContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-5-4
 * Time: 下午3:15
 */
@Component
public class SuccessCmd extends AbstractHandler {
    @Autowired
    private SuccessRpcRemote successRpcRemote;

    @CmdAnnotation(cmd="success.getSuccessInfo",vo=PlayerSuccessInfoVO.class,req=SuccessReq.class,name="获取成就信息")
    public Response getSuccessInfo(Session session,SuccessReq req) throws GameException {
        return Response.newObject(successRpcRemote.getSuccessInfo(session,req));
    }

    @CmdAnnotation(cmd="success.gerReward",vo=PlayerSuccessInfoVO.class,req=SuccessReq.class,name="获取成就奖励信息")
    public Response gerReward(Session session,SuccessReq req) throws GameException {
        return Response.newObject(successRpcRemote.gerReward(session,req));
    }
    @CmdAnnotation(cmd="success.changeTitle",vo=PlayerSuccessInfoVO.class,req=SuccessReq.class,name="改变称号")
    public Response changeTitle(Session session,SuccessReq req) throws GameException {
        return Response.newObject(successRpcRemote.changeTitle(session,req));
    }
}
