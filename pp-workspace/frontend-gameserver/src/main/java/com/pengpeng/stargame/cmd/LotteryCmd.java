package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.LotteryRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.lottery.LotteryInfoVO;
import com.pengpeng.stargame.vo.lottery.LotteryReq;
import com.pengpeng.stargame.vo.lottery.LotteryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author james
 * @since 13-6-13 下午3:53
 */
@Component
public class LotteryCmd extends AbstractHandler {

	@Autowired
	private LotteryRpcRemote lotteryRpcRemote;

	@CmdAnnotation(cmd="lotter.draw",name="抽奖",vo=LotteryVO.class,req=LotteryReq.class)
	public Response lotteryDraw(Session session,LotteryReq req) throws GameException {
		return Response.newObject(lotteryRpcRemote.lotteryDraw(session, req));
	}

	@CmdAnnotation(cmd="lotter.info",name="抽奖信息",vo=LotteryInfoVO.class,req=LotteryReq.class)
	public Response lotteryInfo(Session session,LotteryReq req) throws GameException{
		return Response.newObject(lotteryRpcRemote.lotteryInfo(session, req));
	}

}
