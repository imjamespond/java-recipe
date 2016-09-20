package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.BalloonRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.event.BalloonPanelInfoVO;
import com.pengpeng.stargame.vo.event.BalloonVO;
import com.pengpeng.stargame.vo.event.EventReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-1-20
 * Time: 下午2:43
 */
@Component
public class BalloonCmd extends AbstractHandler {

    @Autowired
    private BalloonRpcRemote balloonRpcRemote;

    @CmdAnnotation(cmd = "gameevent.balloon.info", req = EventReq.class, name = "取得放气球面板信息",vo = BalloonPanelInfoVO.class)
    public Response getBalloonInfo(Session session, EventReq req) throws GameException {
        return Response.newObject(balloonRpcRemote.getBalloonInfo(session,req));

    }
    @CmdAnnotation(cmd = "gameevent.balloon.put", req = EventReq.class, name = "放气球")
    public Response putBalloon(Session session, EventReq req) throws GameException {
        balloonRpcRemote.putBalloon(session,req);
        return Response.newOK();
    }

}
