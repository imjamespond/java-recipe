package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.IntegralRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.integral.IntegralActionVO;
import com.pengpeng.stargame.vo.integral.IntegralReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午5:36
 */
@Component
public class IntegralCmd extends AbstractHandler {
    @Autowired
    private IntegralRpcRemote integralRpcRemote;
    @CmdAnnotation(cmd = "player.integralActions", req = IntegralReq.class, name = "积分获取动态信息")
    public Response integralActions(Session session, IntegralReq req) throws GameException {
        return Response.newObject(integralRpcRemote.integralActions(session,req));
    }
}
