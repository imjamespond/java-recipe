package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.AntiAddictionRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.antiaddiction.AntiAddictionReq;
import com.pengpeng.stargame.vo.antiaddiction.AntiAddictionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther james
 * @since: 13-10-9下午3:11
 */
@Component
public class AntiAddictionCmd extends AbstractHandler {

    @Autowired
    private AntiAddictionRpcRemote antiAddictionRpcRemote;

    @CmdAnnotation(cmd="antiaddiction.check",name="是否已认证",vo=AntiAddictionVO.class,req=AntiAddictionReq.class)
    public Response check(Session session,AntiAddictionReq req) throws GameException {
        AntiAddictionVO vo = antiAddictionRpcRemote.check(session,req);
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd="antiaddiction.certificate",name="实名认证",vo=void.class,req=AntiAddictionReq.class)
    public Response certificate(Session session,AntiAddictionReq req) throws GameException {
        AntiAddictionVO vo =antiAddictionRpcRemote.certificate(session, req);
        return Response.newObject(vo);
    }
}
