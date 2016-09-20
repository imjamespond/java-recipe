package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SuccessiveRpcRemote;
import com.pengpeng.stargame.vo.successive.SuccessiveInfoVO;
import com.pengpeng.stargame.vo.successive.SuccessiveReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author james
 * @since 13-6-13 下午3:53
 */
@Component
public class SuccessiveCmd extends AbstractHandler {

	@Autowired
	private SuccessiveRpcRemote successiveRpcRemote;

	@CmdAnnotation(cmd="successive.getPrize",name="领奖",vo= SuccessiveInfoVO.class,req=SuccessiveReq.class)
	public Response getPrize(Session session,SuccessiveReq req) throws GameException {
		return Response.newObject(successiveRpcRemote.getPrize(session, req));
	}

	@CmdAnnotation(cmd="successive.info",name="连续登陆信息",vo=SuccessiveInfoVO.class,req=SuccessiveReq.class)
	public Response successiveInfo(Session session,SuccessiveReq req) throws GameException{
		return Response.newObject(successiveRpcRemote.SuccessiveInfo(session, req));
	}

}
