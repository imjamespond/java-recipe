
package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.container.ISessionContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.rpc.FashionPkgRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.farm.FarmShopItemVO;
import com.pengpeng.stargame.vo.fashion.FashionIdReq;
import com.pengpeng.stargame.vo.fashion.FashionPkgVO;
import com.pengpeng.stargame.vo.fashion.FashionShopItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 时装衣柜
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-30 下午4:36
 */
@Component
public class FashionPkgCmd extends AbstractHandler {
	@Autowired
	private PlayerRpcRemote playerService;

	@Autowired
	private StatusRemote statusService;

	@Autowired
	private ISessionContainer container;

	@Autowired
	private IExceptionFactory exceptionFactory;

	@Autowired
	private FashionPkgRpcRemote fashionPkgRpcRemote;

	@CmdAnnotation(cmd="fashion.get.warehouse",name="取得商店所有列表",vo=FashionShopItemVO[].class,req=CommonReq.class)
	public Response getFashionShopList(Session session,CommonReq req) throws GameException {
		 return Response.newObject(fashionPkgRpcRemote.getFashionShopList(session,req));
	}

	@CmdAnnotation(cmd="fashionpkg.buy",vo=Void.class,req=FashionIdReq.class,name="购买物品")
	public void addItem(Session session,FashionIdReq req) throws GameException {
		fashionPkgRpcRemote.addItem(session,req);
	}

	@CmdAnnotation(cmd="fashionpkg.remove",vo=Object.class,req=FashionIdReq.class,name="卖出物品")
	public void saleItem(Session session , FashionIdReq req) throws GameException{
		fashionPkgRpcRemote.saleItem(session,req);
	}

	@CmdAnnotation(cmd="fashionpkg.get.fortype",vo=Void.class,req=FashionIdReq.class,name="取得玩家指定类型的仓库")
	public Response getFashionPkg(Session session,FashionIdReq req) throws GameException{
		return Response.newObject( fashionPkgRpcRemote.getFashionPkg(session,req));
	}

	@CmdAnnotation(cmd="fashionpkg.item.tip",vo=FashionShopItemVO.class,req=FashionIdReq.class,name="取得物品详细信息")
	public Response getItemTip(Session session,FashionIdReq req) throws GameException{
		return Response.newObject(fashionPkgRpcRemote.getItemTip(session,req));
	}
}