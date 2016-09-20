package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.container.ISessionContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.rpc.GiftRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.gift.GiftTipVO;
import com.pengpeng.stargame.vo.gift.ShopGiftReq;
import com.pengpeng.stargame.vo.gift.ShopGiftVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-13 下午3:53
 */
@Component
public class GiftCmd extends AbstractHandler {

	@Autowired
	private GiftRpcRemote giftRpcRemote;

	@CmdAnnotation(cmd="gift.shop.list",name="取得指定类型下的礼物列表",vo=ShopGiftVO[].class,req=ShopGiftReq.class)
	public Response listGiftShop(Session session,ShopGiftReq req) throws GameException {
		return Response.newObject(giftRpcRemote.listGiftShop(session,req));
	}

	@CmdAnnotation(cmd="gift.shop.untreated",name="各个模块下未收取礼物数",vo=GiftTipVO[].class,req=CommonReq.class)
	public Response untreated(Session session,CommonReq req) throws GameException{
		return Response.newObject(giftRpcRemote.untreated(session,req));
	}
    @CmdAnnotation(cmd="gift.online.give",name="音乐榜期间领取礼包",vo=RewardVO.class,req=CommonReq.class)
    public Response onlineGive(Session session,CommonReq req) throws GameException{
        RewardVO vo = giftRpcRemote.onlineGive(session,req);
        return Response.newObject(vo);
    }
}
