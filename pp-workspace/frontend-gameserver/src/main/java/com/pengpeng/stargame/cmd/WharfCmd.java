package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SuccessiveRpcRemote;
import com.pengpeng.stargame.rpc.WharfRpcRemote;
import com.pengpeng.stargame.vo.successive.SuccessiveInfoVO;
import com.pengpeng.stargame.vo.successive.SuccessiveReq;
import com.pengpeng.stargame.vo.wharf.WharfGoldCoinVO;
import com.pengpeng.stargame.vo.wharf.WharfInfoVO;
import com.pengpeng.stargame.vo.wharf.WharfReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author james
 * @since 13-6-13 下午3:53
 */
@Component
public class WharfCmd extends AbstractHandler {

	@Autowired
	private WharfRpcRemote wharfRpcRemote;

	@CmdAnnotation(cmd="wharf.enable",name="码头启用",vo= WharfInfoVO.class,req=WharfReq.class)
	public Response enable(Session session,WharfReq req) throws GameException {
		return Response.newObject(wharfRpcRemote.enable(session, req));
	}

    @CmdAnnotation(cmd="wharf.getInfo",name="码头信息",vo= WharfInfoVO.class,req=WharfReq.class)
    public Response getInfo(Session session,WharfReq req) throws GameException {
        return Response.newObject(wharfRpcRemote.getInfo(session, req));
    }

    @CmdAnnotation(cmd="wharf.submit",name="提交任务",vo= WharfInfoVO.class,req=WharfReq.class)
    public Response submit(Session session,WharfReq req) throws GameException {
        return Response.newObject(wharfRpcRemote.submit(session, req));
    }

    @CmdAnnotation(cmd="wharf.shipSail",name="货船启航",vo= WharfInfoVO.class,req=WharfReq.class)
    public Response shipSail(Session session,WharfReq req) throws GameException {
        return Response.newObject(wharfRpcRemote.shipSail(session, req));
    }

    @CmdAnnotation(cmd="wharf.need.help",name="请求好友帮助",vo= WharfInfoVO.class,req=WharfReq.class)
    public Response needHelp(Session session,WharfReq req) throws GameException {
        return Response.newObject(wharfRpcRemote.needHelp(session, req));
    }


    @CmdAnnotation(cmd="wharf.rank",name="码头排行",vo= WharfInfoVO.class,req=WharfReq.class)
    public Response rank(Session session,WharfReq req) throws GameException {
        return Response.newObject(wharfRpcRemote.getRank(session, req));
    }

    @CmdAnnotation(cmd="wharf.arrive",name="码头货船到来",vo= WharfGoldCoinVO.class,req=WharfReq.class)
         public Response arrive(Session session,WharfReq req) throws GameException {
        return Response.newObject(wharfRpcRemote.arrive(session, req));
    }
    @CmdAnnotation(cmd="wharf.arrive.gold",name="码头货船到来所需达人币",vo= WharfGoldCoinVO.class,req=WharfReq.class)
    public Response arriveGold(Session session,WharfReq req) throws GameException {
        return Response.newObject(wharfRpcRemote.arriveGold(session, req));
    }
    @CmdAnnotation(cmd="wharf.submit.all",name="码头货船到来",vo= WharfGoldCoinVO.class,req=WharfReq.class)
    public Response submitAll(Session session,WharfReq req) throws GameException {
        return Response.newObject(wharfRpcRemote.submitAll(session, req));
    }

}
