package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SmallGameRpcRemote;
import com.pengpeng.stargame.rpc.SuccessiveRpcRemote;
import com.pengpeng.stargame.vo.smallgame.SmallGameListVO;
import com.pengpeng.stargame.vo.smallgame.SmallGameRankVO;
import com.pengpeng.stargame.vo.smallgame.SmallGameReq;
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
public class SmallGameCmd extends AbstractHandler {

	@Autowired
	private SmallGameRpcRemote smallGameRpcRemote;

	@CmdAnnotation(cmd="small.game.updateRank",name="更新排名",vo= SmallGameRankVO.class,req=SmallGameReq.class)
	public Response updateRank(Session session,SmallGameReq req) throws GameException {
		return Response.newObject(smallGameRpcRemote.updateRank(session, req));
	}

    @CmdAnnotation(cmd="small.game.getRank",name="获取排名",vo= SmallGameRankVO.class,req=SmallGameReq.class)
    public Response getRank(Session session,SmallGameReq req) throws GameException {
        return Response.newObject(smallGameRpcRemote.getRank(session, req));
    }

    @CmdAnnotation(cmd="small.game.list",name="取得小游戏列表",vo= SmallGameListVO.class,req=SmallGameReq.class)
    public Response getList(Session session,SmallGameReq req) throws GameException {
        return Response.newObject(smallGameRpcRemote.getlist(session, req));
    }

    @CmdAnnotation(cmd="small.game.goldBuy",name="购买星星",vo= SmallGameRankVO.class,req=SmallGameReq.class)
    public Response goldBuy(Session session,SmallGameReq req) throws GameException {
        return Response.newObject(smallGameRpcRemote.goldBuy(session, req));
    }

    @CmdAnnotation(cmd="small.game.deduct",name="扣减次数",vo= SmallGameRankVO.class,req=SmallGameReq.class)
    public Response deduct(Session session,SmallGameReq req) throws GameException {
        return Response.newObject(smallGameRpcRemote.deduct(session, req));
    }
}
