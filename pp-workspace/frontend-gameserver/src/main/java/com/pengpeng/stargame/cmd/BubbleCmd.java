package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.BubbleRpcRemote;
import com.pengpeng.stargame.rpc.LuckyTreeRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeCallVO;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeReq;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO;
import com.pengpeng.stargame.vo.map.BubbleReq;
import com.pengpeng.stargame.vo.map.BubbleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author james
 * @since 13-6-13 下午3:53
 */
@Component
public class BubbleCmd extends AbstractHandler {

	@Autowired
	private BubbleRpcRemote bubbleRpcRemote;

	@CmdAnnotation(cmd="get.bubble",name="取得泡泡",vo= BubbleVO.class,req=BubbleReq.class)
	public Response get(Session session,BubbleReq req) throws GameException {
		return Response.newObject(bubbleRpcRemote.get(session, req));
	}
    @CmdAnnotation(cmd="get.info.bubble",name="取得自己信息",vo= BubbleVO.class,req=BubbleReq.class)
    public Response getInfo(Session session,BubbleReq req) throws GameException {
        return Response.newObject(bubbleRpcRemote.getInfo(session, req));
    }
    @CmdAnnotation(cmd="activate.bubble",name="激活泡泡",vo= BubbleVO.class,req=BubbleReq.class)
    public Response activate(Session session,BubbleReq req) throws GameException {
        return Response.newObject(bubbleRpcRemote.activate(session, req));
    }

    @CmdAnnotation(cmd="deactivate.bubble",name="不激活泡泡",vo= BubbleVO.class,req=BubbleReq.class)
    public Response deactivate(Session session,BubbleReq req) throws GameException {
        return Response.newObject(bubbleRpcRemote.deactivate(session, req));
    }

    @CmdAnnotation(cmd="accept.bubble",name="领取泡泡",vo= BubbleVO.class,req=BubbleReq.class)
    public Response accept(Session session,BubbleReq req) throws GameException {
        return Response.newObject(bubbleRpcRemote.accept(session, req));
    }
}
