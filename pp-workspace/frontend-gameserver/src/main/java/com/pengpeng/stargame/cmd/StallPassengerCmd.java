package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StallPassengerRpcRemote;
import com.pengpeng.stargame.vo.stall.StallPassengerInfoVO;
import com.pengpeng.stargame.vo.stall.StallPassengerReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author james
 * @since 13-6-13 下午3:53
 */
@Component
public class StallPassengerCmd extends AbstractHandler {

	@Autowired
	private StallPassengerRpcRemote stallRpcRemote;

    @CmdAnnotation(cmd="stall.passenger.info",name="路人信息",vo= StallPassengerInfoVO.class,req=StallPassengerReq.class)
    public Response getInfo(Session session,StallPassengerReq req) throws GameException {
        return Response.newObject(stallRpcRemote.getInfo(session, req));
    }

	@CmdAnnotation(cmd="stall.passenger.sell",name="向路人出售",vo= StallPassengerInfoVO.class,req=StallPassengerReq.class)
	public Response sell(Session session,StallPassengerReq req) throws GameException {
		return Response.newObject(stallRpcRemote.sell(session, req));
	}

    @CmdAnnotation(cmd="stall.passenger.reject",name="拒绝出售",vo= StallPassengerInfoVO.class,req=StallPassengerReq.class)
    public Response reject(Session session,StallPassengerReq req) throws GameException {
        return Response.newObject(stallRpcRemote.reject(session, req));
    }
}
