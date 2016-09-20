package com.pengpeng.stargame.fashion.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.fashion.container.IFashionItemRuleContainer;
import com.pengpeng.stargame.fashion.rule.FashionItemRule;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.FashionItem;
import com.pengpeng.stargame.model.room.FashionPkg;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.cmd.PlayerRpc;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspFashionPkgFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.fashion.FashionIdReq;
import com.pengpeng.stargame.vo.fashion.FashionItemVO;
import com.pengpeng.stargame.vo.fashion.FashionPkgVO;
import com.pengpeng.stargame.vo.fashion.FashionShopItemVO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * 时装仓库
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-28 下午7:22
 */
@Component()
public class FashionPkgRpc extends RpcHandler {
	private static final Logger logger = Logger.getLogger(FashionPkgRpc.class);

	@Autowired
	private IPlayerDao playerDao;

	@Autowired
	private FrontendServiceProxy frontendService;

	@Autowired
	private IExceptionFactory exceptionFactory;

	@Autowired
	private PlayerRpc playerRpc;

	// 时装规则
	@Autowired
	private IFashionItemRuleContainer fashionItemRuleContainer;

	// 时装衣柜(包含所有仓库)
	@Autowired
	private IFashionCupboardDao fashionCupboardDao;

	@Autowired
	private RspFashionPkgFactory factory;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer ;
	/**
	 * 取得商店所有列表
	 * @param session
	 * @return
	 */
	@RpcAnnotation(cmd="fashion.get.warehouse",lock = false,vo=FashionShopItemVO [].class,req=CommonReq.class,name="取得商店列表")
	public FashionShopItemVO[] getFashionShopList(Session session,CommonReq req) throws GameException {
		// 1.获取商店列表
		List<FashionItemRule> list = fashionItemRuleContainer.getAll();
		return factory.getFashionShopList(list);
	}

	/**
	 * 购买
	 * @param session
	 * @param req itemid,num
	 */
	@RpcAnnotation(cmd="fashionpkg.buy",vo=Void.class,req=FashionIdReq.class,name="购买物品")
	public void addItem(Session session,FashionIdReq req) throws GameException {
		// 1.物品是否可以购买
		// 2.物品等级是否可以购买
		// 3.用户购买金额是否足够
		// 4.购买物品
		// 5.更新数据
		String pid = session.getPid();
		String [] itemIds = req.getItemIds();
        if(itemIds==null||itemIds.length==0){
            return;
        }
		Player player = playerDao.getBean(pid);
		FashionCupboard fashionCupboard = fashionCupboardDao.getBean(pid);

		fashionItemRuleContainer.checkBuy(fashionCupboard,player,itemIds);

		fashionItemRuleContainer.buy(fashionCupboard,player,itemIds);

		playerDao.saveBean(player);
		fashionCupboardDao.saveBean(fashionCupboard);

		HashSet<String> set = new HashSet<String>();
		for(String itemId : itemIds){
			FashionItemRule rule = fashionItemRuleContainer.getElement(itemId);
			set.add(String.valueOf(rule.getItemtype()));
		}

		Session[] mysessions={session};
		frontendService.broadcast(mysessions,roleFactory.newPlayerVO(player));

		Iterator itr = set.iterator();
		while (itr.hasNext()){
			String type = (String) itr.next();
            FashionPkg fashionPkg =fashionCupboard.getFashionPkg(type);
			FashionPkgVO vo = factory.getFashionPkg(fashionPkg,type);
			frontendService.broadcast(mysessions,vo);
		}


	}

	/**
	 * 卖出物品
	 * @param session
	 * @param req grid ,num
	 */
	@RpcAnnotation(cmd="fashionpkg.remove",vo=Void.class,req=FashionIdReq.class,name="卖出物品")
	public void saleItem(Session session , FashionIdReq req) throws GameException{
		String pid = session.getPid();
		String itemId = req.getItemId();
		String fashionId = req.getFashionId();

		Player player = playerDao.getBean(pid);
		FashionCupboard fashionCupboard = fashionCupboardDao.getBean(pid);

		fashionItemRuleContainer.checkSale(fashionCupboard,itemId,1,fashionId);

		fashionItemRuleContainer.sale(fashionCupboard,player,itemId,1,fashionId);


		playerDao.saveBean(player);
		fashionCupboardDao.saveBean(fashionCupboard);

		Session[] mysessions={session};
		frontendService.broadcast(mysessions,roleFactory.newPlayerVO(player));
        FashionItemRule rule = fashionItemRuleContainer.getElement(itemId);
        String type = String.valueOf(rule.getItemtype());
		FashionPkg fashionPkg =fashionCupboard.getFashionPkg(type);
		FashionPkgVO vo = factory.getFashionPkg(fashionPkg,type);
		frontendService.broadcast(mysessions,vo);

        BaseItemRule baseItemRule = baseItemRulecontainer.getElement(itemId);
        //日志
        String value=itemId+ GameLogger.SPLIT+String.valueOf(1)+ GameLogger.SPLIT + baseItemRule.getType()+GameLogger.SPLIT + baseItemRule.getItemtype();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_16, player.getId(), value));
	}

	/**
	 * 取得玩家指定类型的仓库
	 */
	@RpcAnnotation(cmd="fashionpkg.get.fortype",lock = false,vo=Object.class,req=FashionIdReq.class,name="取得玩家指定类型的仓库")
	public FashionPkgVO getFashionPkg(Session session,FashionIdReq req) throws GameException{
		String pid = session.getPid();
		String type = req.getType();
		FashionPkg fashionPkg = this.fashionCupboardDao.getFashionPkg(pid,type);
		return factory.getFashionPkg(fashionPkg,type);
	}

	/**
	 * 取得物品详细信息
	 */
	@RpcAnnotation(cmd="fashionpkg.item.tip",lock = false,vo=FashionShopItemVO.class,req=FashionIdReq.class,name="取得物品详细信息")
	public FashionShopItemVO getItemTip(Session session,FashionIdReq req) throws GameException{
		String itemId = req.getItemId();
		FashionItemRule fashionItemRule = fashionItemRuleContainer.getElement(itemId);
		return factory.getFashionShopItem(fashionItemRule);
	}

	/**
	 * 从背包中随机一件物品
	 */
	@RpcAnnotation(cmd="fashionpkg.item.random",vo=FashionItemVO.class,req=FashionIdReq.class,name="从背包中随机一件物品")
	public FashionItemVO randomItem(Session session , FashionIdReq req) throws GameException{
		String pid  = session.getPid();
		String type = req.getType();

		FashionPkg fashionPkg = this.fashionCupboardDao.getFashionPkg(pid,type);
		FashionItem fashionItem = fashionPkg.getRandomItem();
		return factory.buildFashionItemVO(fashionItem,type);
	}
}
