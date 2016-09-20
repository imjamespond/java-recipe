package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.container.ISessionContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.rpc.FarmOrderRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.vo.farm.FarmIdReq;
import com.pengpeng.stargame.vo.farm.FarmOrderInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-13 上午10:31
 */
@Component
public class FarmOrderCmd extends AbstractHandler  {
	@Autowired
	private PlayerRpcRemote playerService;

	@Autowired
	private StatusRemote statusService;

	@Autowired
	private ISessionContainer container;

	@Autowired
	private IExceptionFactory exceptionFactory;

	@Autowired
	private FarmOrderRpcRemote farmOrderRpcRemote;

	@CmdAnnotation(cmd="farm.get.orderlist",vo=FarmOrderInfoVO.class,req=FarmIdReq.class,name="获取当前订单列表")
	public Response getOrderList(Session session,FarmIdReq farmIdReq) throws GameException {
		FarmOrderInfoVO vo= farmOrderRpcRemote.getOrderList(session,farmIdReq);
		return Response.newObject(vo);
	}

	@CmdAnnotation(cmd="farm.refresh.order",vo=FarmOrderInfoVO.class,req=FarmIdReq.class,name="刷新订单或取得订单信息")
	public Response refreshOrder(Session session,FarmIdReq farmIdReq) throws GameException {
		FarmOrderInfoVO vo = farmOrderRpcRemote.refreshOrder(session,farmIdReq);
		return Response.newObject(vo);
	}

	@CmdAnnotation(cmd="farm.finish.order",vo=FarmOrderInfoVO.class,req=FarmIdReq.class,name="完成订单")
	public Response finishOrder(Session session,FarmIdReq farmIdReq) throws GameException{
		FarmOrderInfoVO vo = farmOrderRpcRemote.finishOrder(session,farmIdReq);
		return Response.newObject(vo);
	}
}
