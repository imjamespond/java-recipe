package com.pengpeng.stargame.player.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.cmd.FarmGiftRpc;
import com.pengpeng.stargame.farm.container.IFarmGiftItemRuleContainer;
import com.pengpeng.stargame.gift.container.IGiftItemRuleContainer;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.container.IPlayerGiftContainer;
import com.pengpeng.stargame.player.dao.IOtherPlayerDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.rule.BaseGiftRule;
import com.pengpeng.stargame.rpc.EventBroadcast;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspGiftFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.gift.GiftTipVO;
import com.pengpeng.stargame.vo.gift.ShopGiftReq;
import com.pengpeng.stargame.vo.gift.ShopGiftVO;

import com.pengpeng.stargame.vo.role.PlayerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.List;

/**
 * Common 礼物信息
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-4上午11:06
 */
@Component()
public class GiftRpc extends RpcHandler {

	// 礼品规则
	@Autowired
	private IGiftItemRuleContainer giftItemRuleContainer;

	@Autowired
	private RspGiftFactory rspGiftFactory;

	@Autowired
	private FarmGiftRpc farmGiftRpc;

    @Autowired
    private IPlayerGiftContainer playerGiftContainer;

    @Autowired
    private IPlayerDao playerDao;

    @Autowired
    private IOtherPlayerDao otherPlayerDao;

    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private FrontendServiceProxy frontendServiceProxy;

    @Autowired
    private RspRoleFactory rspRoleFactory;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
	// 取得商店礼物列表
	@RpcAnnotation(cmd="gift.shop.list",lock = false,vo=ShopGiftVO[].class,req=ShopGiftReq.class,name="取得指定类型下的礼物列表")
	public ShopGiftVO[] listGiftShop(Session session,ShopGiftReq req) throws RuleException {
		int presentType = req.getPresentType();
		List<BaseGiftRule> list = null;
		if(presentType == 1){
			list = giftItemRuleContainer.getByPresentType(presentType);
		}
		return rspGiftFactory.buildShopGiftVO(list,presentType);
	}

	@RpcAnnotation(cmd="gift.shop.untreated",lock = false,vo=GiftTipVO[].class,req=CommonReq.class,name="各个模块下未收取礼物数")
	public GiftTipVO[] untreated(Session session , CommonReq req ) throws RuleException{
		List<GiftTipVO> list = new ArrayList<GiftTipVO>();

		// 农场未收取的礼物数
		int farm_num = farmGiftRpc.untreated(session,req);
		if(farm_num > 0){
			list.add(new GiftTipVO(1,farm_num));
		}

		// 房间未收取礼物数
		return list.toArray(new GiftTipVO[0]);
	}

    @RpcAnnotation(cmd="gift.online.give",lock = true,vo=RewardVO.class,req=CommonReq.class,name="音乐榜期间,领取礼包")
    public RewardVO onlineGive(Session session , CommonReq req) throws RuleException, AlertException {
        OtherPlayer op = otherPlayerDao.getBean(session.getPid());
        if (op.isLoginGive()){
            exceptionFactory.throwAlertException("gift.isgive");
        }
        Player player = playerDao.getBean(session.getPid());
        RewardVO vo = playerGiftContainer.give(player);
        op.setLoginGive(true);
        otherPlayerDao.saveBean(op);
        playerDao.saveBean(player);
        PlayerVO playerVO = rspRoleFactory.newPlayerVO(player);
        frontendServiceProxy.broadcast(session,playerVO);

        //日志
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_7, session.getPid(), ""));

        return vo;
    }
}
