package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.container.ISessionContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.rpc.FarmGiftRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.farm.FarmShopItemVO;
import com.pengpeng.stargame.vo.role.FriendVO;
import com.pengpeng.stargame.vo.role.GiftReq;
import com.pengpeng.stargame.vo.role.GiftVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 农场 礼品
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-5 下午4:03
 */
@Component
public class FarmGiftCmd extends AbstractHandler  {

	@Autowired
	private PlayerRpcRemote playerService;

	@Autowired
	private StatusRemote statusService;

	@Autowired
	private ISessionContainer container;

	@Autowired
	private IExceptionFactory exceptionFactory;

	@Autowired
	private FarmGiftRpcRemote farmGiftRpcRemote;

	@CmdAnnotation(cmd="farm.gift.list.info",name="取得礼物列表",vo=GiftVO[].class,req=CommonReq.class)
	public Response listInfo(Session session, CommonReq req) throws GameException{
		GiftVO[] vo = farmGiftRpcRemote.listInfo(session,req);
		return Response.newObject(vo);
	}

	@CmdAnnotation(cmd="farm.gift.give",name="赠送礼物",vo=Integer.class,req=GiftReq.class)
	public Response give(Session session,GiftReq req) throws GameException{
        int value = farmGiftRpcRemote.give(session,req);
		return Response.newObject(new Integer(value));
	}

	@CmdAnnotation(cmd="farm.gift.accept",name="领取(接受)礼物",vo=Void.class,req=GiftReq.class)
	public Response accept(Session session,GiftReq req) throws GameException{
		farmGiftRpcRemote.accept(session,req);
		return Response.newOK();
	}

	@CmdAnnotation(cmd="farm.gift.reject",name="忽略(拒绝)好友的礼物",vo=Void.class,req=GiftReq.class)
	public Response reject(Session session,GiftReq req) throws GameException{
		farmGiftRpcRemote.reject(session,req);
		return Response.newOK();
	}

	@CmdAnnotation(cmd="farm.gift.get.friend",name="取得未赠送礼物的好友",vo=FriendVO[].class,req=CommonReq.class)
	public Response getFriend(Session session,CommonReq req) throws GameException{
		FriendVO[] vo = farmGiftRpcRemote.getFriend(session, req);
		return Response.newObject(vo);
	}

//	@CmdAnnotation(cmd="farm.gift.untreated",name="取得未领取的礼物总数",vo=Void.class,req=CommonReq.class)
//	public Response untreated(Session session,CommonReq req) throws GameException{
//		return Response.newObject(farmGiftRpcRemote.untreated(session,req));
//	}

	@CmdAnnotation(cmd="farm.gift.giveCount",name="礼物还可以赠送多少次",vo=Integer.class,req=CommonReq.class)
	public Response giveCount(Session session,CommonReq req) throws GameException{
		return Response.newObject(farmGiftRpcRemote.giveCount(session,req));
	}

}
