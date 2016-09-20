package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.FashionGiftRpcRemote;
import com.pengpeng.stargame.rpc.GiftRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.gift.GiftTipVO;
import com.pengpeng.stargame.vo.gift.ShopGiftReq;
import com.pengpeng.stargame.vo.gift.ShopGiftVO;
import com.pengpeng.stargame.vo.role.FriendVO;
import com.pengpeng.stargame.vo.role.GiftReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-13 下午3:53
 */
@Component
public class FashionGiftCmd extends AbstractHandler {

	@Autowired
	private FashionGiftRpcRemote fashionGiftRpcRemote;

	@CmdAnnotation(cmd="fashion.get.friend",name="取得好友",vo=FriendVO[].class,req=CommonReq.class)
	public Response getFriend(Session session,CommonReq req) throws GameException {
		return Response.newObject(fashionGiftRpcRemote.getFriend2(session, req));
	}

    @CmdAnnotation(cmd="fashion.gift.give",name="赠送礼物",vo=Integer.class,req=GiftReq.class)
    public Response give(Session session,GiftReq req) throws GameException {
        return Response.newObject(fashionGiftRpcRemote.give(session, req));
    }

}
