package com.pengpeng.stargame.room.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.DecoratePosition;
import com.pengpeng.stargame.model.room.RoomEvaluate;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.player.cmd.PlayerRpc;
import com.pengpeng.stargame.player.container.IFriendRuleContainer;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.container.IRoomExpansionRuleContainer;
import com.pengpeng.stargame.room.container.IRoomItemRuleContainer;
import com.pengpeng.stargame.room.container.IRoomRuleContainer;
import com.pengpeng.stargame.room.dao.IRoomEvaluateDao;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.room.dao.IRoomPlayerDao;
import com.pengpeng.stargame.room.dao.IRoomRankDao;
import com.pengpeng.stargame.room.rule.RoomExpansionRule;
import com.pengpeng.stargame.room.rule.RoomItemRule;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.rsp.RspRoomFactory;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.farm.GoodsVO;
import com.pengpeng.stargame.vo.room.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-5-20
 * Time: 下午7:17
 */
@Component()
public class RoomRpc extends RpcHandler {
    private static final Logger logger = Logger.getLogger(RpcHandler.class);
    @Autowired
    private IRoomItemRuleContainer roomItemRuleContainer;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IRoomPlayerDao roomPlayerDao;
    @Autowired
    private RspRoomFactory rspRoomFactory;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private FrontendServiceProxy frontendService;

    @Autowired
    private IFriendDao friendDao;

    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private IRoomEvaluateDao roomEvaluateDao;
    @Autowired
    private IRoomRuleContainer roomRuleContainer;
    @Autowired
    private IFriendRuleContainer friendRuleContainer;
    @Autowired
    private IRoomRankDao roomRankDao;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private IRoomExpansionRuleContainer expansionRuleContainer;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;

    @RpcAnnotation(cmd = "room.get.info", req = RoomIdReq.class, name = "取得房间信息")
    public RoomVO getRoomInfo(Session session, RoomIdReq req) throws AlertException {
        if (req.getRoomId() != null && !req.getRoomId().equals(session.getPid())) {
            friendRuleContainer.checkFriend(friendDao.getBean(session.getPid()), playerDao.getBean(session.getPid()), playerDao.getBean(req.getRoomId()));

        }
        RoomPlayer roomPlayer = roomPlayerDao.getRoomPlayer(req.getRoomId());
        return rspRoomFactory.getRoomVO(roomPlayer);
    }

    @RpcAnnotation(cmd = "room.enterFriendRoom", req = RoomIdReq.class, name = "进入好友房间")
    public RoomVO enterFriendRoom(Session session, RoomIdReq req) throws AlertException {
        friendRuleContainer.checkFriend(friendDao.getBean(session.getPid()), playerDao.getBean(session.getPid()), playerDao.getBean(req.getRoomId()));
        RoomPlayer roomPlayer = roomPlayerDao.getRoomPlayer(req.getRoomId());
        return rspRoomFactory.getRoomVO(roomPlayer);
    }

    @RpcAnnotation(cmd = "room.shopList", req = RoomIdReq.class, name = "取得房间内商品 列表")
    public RoomShopItemVO[] getRoomshop(Session session, RoomIdReq req) {
        List<RoomItemRule> roomItemRules = new ArrayList<RoomItemRule>(roomItemRuleContainer.values());
        return rspRoomFactory.getRoomShopVOArray(roomItemRules);
    }

    @RpcAnnotation(cmd = "room.myList", req = RoomIdReq.class, name = "取得房间内闲置的物品列表")
    public RoomPkgVO getRoomItemList(Session session, RoomIdReq req) {
        RoomPackege roomPackege = roomPackegeDao.getRoomPackege(session.getPid());
        return rspRoomFactory.buildRoomPkgVO(roomPackege);
    }

    /**
     * 取得物品详细信息
     */
    @RpcAnnotation(cmd = "room.item.tip", vo = RoomShopItemVO.class, req = RoomIdReq.class, name = "取得物品详细信息")
    public RoomShopItemVO getItemTip(Session session, RoomIdReq req) throws GameException {
        String itemId = req.getItemId();
        RoomItemRule roomItemRule = roomItemRuleContainer.getElement(itemId);
        return rspRoomFactory.getRoomShopItemVO(roomItemRule);
    }

    @RpcAnnotation(cmd = "room.buy", vo = RoomShopItemVO.class, req = RoomIdReq.class, name = "购买")
    public void buy(Session session, RoomIdReq req) throws GameException {
        String itemId = req.getItemId();
        int num = req.getNum();
        Player player = playerDao.getBean(session.getPid());
        RoomPackege roomPackege = roomPackegeDao.getRoomPackege(session.getPid());
        RoomPlayer roomPlayer = roomPlayerDao.getRoomPlayer(session.getPid());
        roomItemRuleContainer.checkBuy(player, itemId, num);

        roomItemRuleContainer.buy(player, roomPlayer, roomPackege, itemId, num);
        playerDao.saveBean(player);
        roomPackegeDao.saveBean(roomPackege);

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        frontendService.broadcast(mysessions, rspRoomFactory.buildRoomPkgVO(roomPackege));

        BaseItemRule baseItemRule=  baseItemRulecontainer.getElement(itemId);
        //日志
        String value = itemId + GameLogger.SPLIT + String.valueOf(num)+ GameLogger.SPLIT + baseItemRule.getType()+GameLogger.SPLIT + baseItemRule.getItemtype();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_15, player.getId(), value));
    }

    @RpcAnnotation(cmd = "room.sale", vo = RoomShopItemVO.class, req = RoomIdReq.class, name = "卖出")
    public void sale(Session session, RoomIdReq req) throws GameException, RuleException {
        String itemId = req.getItemId();
        int num = req.getNum();
        roomItemRuleContainer.checkSale(itemId);
        Player player = playerDao.getBean(session.getPid());
        RoomPackege roomPackege = roomPackegeDao.getRoomPackege(session.getPid());
        RoomPlayer roomPlayer = roomPlayerDao.getRoomPlayer(session.getPid());
        roomItemRuleContainer.sale(player, roomPlayer, roomPackege, itemId, num);

        playerDao.saveBean(player);
        roomPackegeDao.saveBean(roomPackege);

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));

        frontendService.broadcast(mysessions, rspRoomFactory.buildRoomPkgVO(roomPackege));


        /**
         * 添加屏幕 右下角 提示信息
         */
        BaseItemRule baseItemRule = baseItemRulecontainer.getElement(itemId);
        RewardVO rewardVO = new RewardVO(7);
        rewardVO.setGold(baseItemRule.getRecyclingPrice() * num);
        rewardVO.addGoodsVO(new GoodsVO(itemId, baseItemRule.getName(), num, 0, ""));
        frontendService.broadcast(mysessions, rewardVO);

        //日志
        String value = itemId + GameLogger.SPLIT + String.valueOf(num)+ GameLogger.SPLIT + baseItemRule.getType()+GameLogger.SPLIT + baseItemRule.getItemtype();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_16, player.getId(), value));

    }

    @RpcAnnotation(cmd = "room.save", req = RoomIdReq.class, name = "编辑房间 保存", vo = RoomVO.class)
    public RoomVO save(Session session, RoomIdReq req) throws AlertException, RuleException {
        DecorateVO[] decorateVOs = req.getDecorateVOs();
        if (decorateVOs == null || decorateVOs.length == 0) {
            exceptionFactory.throwAlertException("请求参数不对");
        }
        RoomPlayer roomPlayer = roomPlayerDao.getRoomPlayer(session.getPid());
        RoomPackege checkRoomPackege = roomPackegeDao.getRoomPackege(session.getPid());

        Player player = playerDao.getBean(session.getPid());
        List<String> buyList = new ArrayList<String>();
        for (DecorateVO decorateVO : decorateVOs) {
            // 新加
            if (decorateVO.getChangeType() == 1) {
                if (!checkRoomPackege.hasItem(decorateVO.getItemId())) {
                    buyList.add(decorateVO.getItemId());
                } else {
                    checkRoomPackege.useItem(decorateVO.getItemId());
                }
            }
            // 修改
            if (decorateVO.getChangeType() == 2) {
                if (!roomPlayer.hasDecorate(decorateVO.getId())) {
                    exceptionFactory.throwAlertException("请求参数不对");
                }
            }
            // 回收
            if (decorateVO.getChangeType() == 3) {
                if (!roomPlayer.hasDecorate(decorateVO.getId())) {
                    exceptionFactory.throwAlertException("请求参数不对");
                }
            }
            // 卖出
            if (decorateVO.getChangeType() == 4) {
//                if (!roomPlayer.hasDecorate(decorateVO.getId())) {
//                    exceptionFactory.throwAlertException("请求参数不对");
//                }
                roomItemRuleContainer.checkSale(decorateVO.getItemId());
            }
        }
        RoomPackege roomPackege = roomPackegeDao.getRoomPackege(session.getPid());
        roomItemRuleContainer.checkBuy(buyList, player);
        roomItemRuleContainer.consumerBuy(buyList, player, roomPackege);

        for (DecorateVO decorateVO : decorateVOs) {
            BaseItemRule baseItemRule = baseItemRulecontainer.getElement(decorateVO.getItemId());
            // 新加
            if (decorateVO.getChangeType() == 1) {
                roomItemRuleContainer.decorate(roomPlayer, roomPackege, decorateVO.getItemId(), decorateVO.getPosition());
            }
            // 修改
            if (decorateVO.getChangeType() == 2) {
                if (roomPlayer.hasDecorate(decorateVO.getId())) {
                    DecoratePosition decoratePosition = new DecoratePosition(decorateVO.getId(), decorateVO.getItemId(), decorateVO.getPosition());
                    roomPlayer.updateDecorate(decorateVO.getId(), decoratePosition);
                }
            }
            // 回收
            if (decorateVO.getChangeType() == 3) {
                if (roomPlayer.hasDecorate(decorateVO.getId())) {
                    roomItemRuleContainer.removeDecorate(roomPlayer, roomPackege, decorateVO.getId());
                }
            }
            // 卖出
            if (decorateVO.getChangeType() == 4) {
                if (decorateVO.getId().equals("0")) {   //从闲置仓库内托进去 又卖出
                    roomPackege.useItem(decorateVO.getItemId());
                    RoomItemRule rule = roomItemRuleContainer.getElement(decorateVO.getItemId());
//                    rule.effectSale(player,1);
                    playerRuleContainer.incGameCoin(player,rule.getRecyclingPrice());

                    //日志
                    String value = decorateVO.getItemId() + GameLogger.SPLIT + String.valueOf(1)+ GameLogger.SPLIT + baseItemRule.getType()+GameLogger.SPLIT + baseItemRule.getItemtype();
                    gameLoggerWrite.write(new GameLogger(GameLogger.LOG_16, player.getId(), value));
                } else {
                    roomItemRuleContainer.saleByRoom(player, roomPlayer, decorateVO.getId());
                }
            }
            String value = decorateVO.getItemId() + GameLogger.SPLIT + String.valueOf(1)+ GameLogger.SPLIT + baseItemRule.getType()+GameLogger.SPLIT + baseItemRule.getItemtype();
            gameLoggerWrite.write(new GameLogger(GameLogger.LOG_18, player.getId(), value));
        }
        playerDao.saveBean(player);
        roomPackegeDao.saveBean(roomPackege);
        roomPlayerDao.saveBean(roomPlayer);


        /**
         * 排行榜
         */

        if (roomRankDao.contains("", session.getPid())) {
            roomRankDao.removeBean("", session.getPid());
        }
        roomRankDao.addBean("", session.getPid(), roomItemRuleContainer.getLuxuryValue(roomPlayer));


        /**
         * 推送事件
         */
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        frontendService.broadcast(mysessions, rspRoomFactory.buildRoomPkgVO(roomPackege));

        Session[] sessions = statusRemote.getMember(session, SessionUtil.getChannelScene(session));
        List<Session> sessionList=new ArrayList<Session>();
        for(Session session1:sessions){
            if(session1.getPid().equals(session.getPid())){
                continue;
            }
            sessionList.add(session1);
        }
        frontendService.broadcast(sessionList.toArray(new Session[0]),new RoomSaveVO());


        return rspRoomFactory.getRoomVO(roomPlayer);
    }


    @RpcAnnotation(cmd = "room.evaluation", req = RoomIdReq.class, name = "评价好友房间", vo = RoomVO.class)
    public RoomVO evaluation(Session session, RoomIdReq req) throws AlertException {
        friendRuleContainer.checkFriend(friendDao.getBean(session.getPid()), playerDao.getBean(session.getPid()), playerDao.getBean(req.getRoomId()));

        /**
         * 可否评价
         */
        RoomEvaluate roomEvaluate = roomEvaluateDao.getRoomEvaluate(session.getPid());
        RoomEvaluate friendRoomEvaluate = roomEvaluateDao.getRoomEvaluate(req.getRoomId());


        roomRuleContainer.checkEvaluate(roomEvaluate, req.getRoomId());

        /**
         * 评价
         */
        roomRuleContainer.evaluate(roomEvaluate, friendRoomEvaluate);

        roomEvaluateDao.saveBean(roomEvaluate);
        roomEvaluateDao.saveBean(friendRoomEvaluate);


        RoomPlayer roomPlayer = roomPlayerDao.getRoomPlayer(req.getRoomId());

        return rspRoomFactory.getRoomVO(roomPlayer);

    }

    @RpcAnnotation(cmd = "room.extension.before", req = RoomIdReq.class, name = "扩建之前请求", vo = ExtensionVO.class)
    public ExtensionVO extensionBefore(Session session, RoomIdReq req) throws AlertException {
        RoomPlayer roomPlayer = roomPlayerDao.getRoomPlayer(session.getPid());
        RoomExpansionRule roomExpansionRule=expansionRuleContainer.getElement(roomPlayer.getcExpansionId()+1);
        if(roomExpansionRule==null){
            exceptionFactory.throwAlertException("扩建达到了最大！");

        }        /**
         * 农场升级更新玩家VO
         */
        Player player=playerDao.getBean(session.getPid());
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));

        return rspRoomFactory.getExtensionVO(roomExpansionRule);
    }
    @RpcAnnotation(cmd = "room.extension.start", req = RoomIdReq.class, name = "扩建", vo = ExtensionVO.class)
    public RoomVO extension(Session session, RoomIdReq req) throws AlertException {
        RoomPlayer roomPlayer = roomPlayerDao.getRoomPlayer(session.getPid());
        Player player=playerDao.getBean(session.getPid());
        expansionRuleContainer.extensionStart(roomPlayer,player);
        playerDao.saveBean(player);
        roomPlayerDao.saveBean(roomPlayer);
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));

        return  rspRoomFactory.getRoomVO(roomPlayer);
    }
    @RpcAnnotation(cmd = "room.extension.finish", req = RoomIdReq.class, name = "直接完成扩建", vo = ExtensionVO.class)
    public RoomVO extensionFinish(Session session, RoomIdReq req) throws AlertException {
        RoomPlayer roomPlayer = roomPlayerDao.getRoomPlayer(session.getPid());
        Player player=playerDao.getBean(session.getPid());
        expansionRuleContainer.extensionFinish(roomPlayer,player);
        roomPlayerDao.saveBean(roomPlayer);
        playerDao.saveBean(player);
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        return  rspRoomFactory.getRoomVO(roomPlayer);
    }
    @RpcAnnotation(cmd = "room.extension.finish.needGold", req = RoomIdReq.class, name = "直接完成扩建需要的达人币", vo = Integer.class)
    public int extensionFinishNeedGold(Session session, RoomIdReq req) throws AlertException {
        RoomPlayer roomPlayer = roomPlayerDao.getRoomPlayer(session.getPid());
        return  expansionRuleContainer.extensionNeedGold(roomPlayer);
    }

}
