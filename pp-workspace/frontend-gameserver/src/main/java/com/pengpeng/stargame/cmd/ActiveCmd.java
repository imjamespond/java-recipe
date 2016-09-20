package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.ActiveRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.role.ActiveReq;
import com.pengpeng.stargame.vo.role.ActiveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-12下午3:11
 */
@Component
public class ActiveCmd extends AbstractHandler {

    @Autowired
    private ActiveRpcRemote activeRpcRemote;

    @CmdAnnotation(cmd="active.list",name="活跃度面板信息",vo=ActiveVO.class,req=ActiveReq.class)
    public Response listActive(Session session,ActiveReq req) throws GameException {
        ActiveVO vo = activeRpcRemote.getActiveList(session, req);
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd="active.finish",name="领取积分",vo=void.class,req=ActiveReq.class)
    public Response finish(Session session,ActiveReq req) throws GameException {
        int score = activeRpcRemote.reward(session, req);
        return Response.newObject(score);
    }
}
