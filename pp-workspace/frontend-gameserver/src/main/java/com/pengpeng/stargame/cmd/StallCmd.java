package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StallRpcRemote;
import com.pengpeng.stargame.vo.farm.FarmItemVO;
import com.pengpeng.stargame.vo.stall.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author james
 * @since 13-6-13 下午3:53
 */
@Component
public class StallCmd extends AbstractHandler {

	@Autowired
	private StallRpcRemote stallRpcRemote;

    @CmdAnnotation(cmd="stall.enable",name="小摊启用",vo= StallInfoVO.class,req=StallReq.class)
    public Response enable(Session session,StallReq req) throws GameException {
        return Response.newObject(stallRpcRemote.enable(session, req));
    }

	@CmdAnnotation(cmd="stall.info",name="小摊信息",vo= StallInfoVO.class,req=StallReq.class)
	public Response getInfo(Session session,StallReq req) throws GameException {
		return Response.newObject(stallRpcRemote.getInfo(session, req));
	}

    @CmdAnnotation(cmd="stall.gold.shelf",name="达人币货架开启",vo= StallInfoVO.class,req=StallReq.class)
    public Response goldShelf(Session session,StallReq req) throws GameException {
        return Response.newObject(stallRpcRemote.goldShelf(session, req));
    }

    @CmdAnnotation(cmd="stall.friend.shelf",name="好友货架开启",vo= StallInfoVO.class,req=StallReq.class)
    public Response friendShelf(Session session,StallReq req) throws GameException {
        return Response.newObject(stallRpcRemote.friendShelf(session, req));
    }

    @CmdAnnotation(cmd="stall.buy",name="购买货品",vo= StallInfoVO.class,req=StallReq.class)
    public Response buy(Session session,StallReq req) throws GameException {
        return Response.newObject(stallRpcRemote.buy(session, req));
    }
    @CmdAnnotation(cmd="stall.get.money",name="获取收入",vo= StallInfoVO.class,req=StallReq.class)
    public Response getMoney(Session session,StallReq req) throws GameException {
        return Response.newObject(stallRpcRemote.getMoney(session, req));
    }

    @CmdAnnotation(cmd="stall.hit.shelf",name="货品上架",vo= StallInfoVO.class,req=StallReq.class)
    public Response hitShelf(Session session,StallReq req) throws GameException {
        return Response.newObject(stallRpcRemote.hitShelf(session, req));
    }

    @CmdAnnotation(cmd="stall.off.shelf",name="货品下架",vo= StallInfoVO.class,req=StallReq.class)
    public Response offShelf(Session session,StallReq req) throws GameException {
        return Response.newObject(stallRpcRemote.offShelf(session, req));
    }

    @CmdAnnotation(cmd="stall.advertise",name="登广告",vo= StallInfoVO.class,req=StallReq.class)
    public Response advertise(Session session,StallReq req) throws GameException {
        return Response.newObject(stallRpcRemote.advertise(session, req));
    }

    @CmdAnnotation(cmd="stall.getAdvertisement",name="获取广告",vo= StallAdvertisementVO.class,req=StallAdvReq.class)
    public Response getAdvertisement(Session session,StallAdvReq req) throws GameException {
        return Response.newObject(stallRpcRemote.getAdvertisement(session, req));
    }

    @CmdAnnotation(cmd="stall.assistant.item",name="助手物品列表",vo= FarmItemVO[].class,req=StallAssistantReq.class)
    public Response browseAssistant(Session session,StallAssistantReq req) throws GameException {
        return Response.newObject(stallRpcRemote.browseAssistant(session, req));
    }

    @CmdAnnotation(cmd="stall.assistant.enable",name="开启助手",vo= void.class,req=StallAssistantReq.class)
    public Response enableAssistant(Session session,StallAssistantReq req) throws GameException {
        stallRpcRemote.enableAssistant(session, req);
        return Response.newOK();
    }

    @CmdAnnotation(cmd="stall.assistant.check",name="助手是否已开启",vo= Boolean.class,req=StallAssistantReq.class)
    public Response checkAssistant(Session session,StallAssistantReq req) throws GameException {
        return Response.newObject(stallRpcRemote.checkAssistant(session, req));
    }


    @CmdAnnotation(cmd="stall.assistant.buy",name="助手购买",vo= StallInfoVO.class,req=StallReq.class)
    public Response buyAssistant(Session session,StallReq req) throws GameException {
        return Response.newObject(stallRpcRemote.buyAssistant(session, req));
    }

    @CmdAnnotation(cmd="stall.assistant.info",name="获取助手资讯",vo= StallInfoVO.class,req=StallAssistantReq.class)
    public Response getAssistant(Session session,StallAssistantReq req) throws GameException {
        return Response.newObject(stallRpcRemote.getAssistant(session, req));
    }

    @CmdAnnotation(cmd="stall.assistant.cd",name="助手冷却时间",vo= StallInfoVO.class,req=StallAssistantReq.class)
    public Response cdAssistant(Session session,StallAssistantReq req) throws GameException {
        return Response.newObject(stallRpcRemote.cdAssistant(session, req));
    }
}
