package com.pengpeng.stargame.fashion.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.fashion.container.IFashionGiftRuleContainer;
import com.pengpeng.stargame.model.GiftPlayer;
import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.model.player.FriendItem;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.player.dao.IGiftPlayerDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.vo.role.FriendVO;
import com.pengpeng.stargame.vo.role.GiftReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 空间礼物模块
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-4上午11:06
 */
@Component()
public class FashionGiftRpc extends RpcHandler {

    @Autowired
    private IFriendDao friendDao;

    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IGiftPlayerDao giftPlayerDao;


    @Autowired
    private RspRoleFactory roleFactory;


    // 礼品交易规则
    @Autowired
    private IFashionGiftRuleContainer fashionGiftRuleContainer;

    @Autowired
    private StatusRemote statusService;
    @Autowired
    private FrontendServiceProxy frontendService;


    @RpcAnnotation(cmd = "fashion.gift.give", lock = true, req = GiftReq.class, name = "赠送礼物", vo = Integer.class)
    public int give(Session session, GiftReq req) throws AlertException {
        String itemId = req.getItemId();
        Player player = playerDao.getBean(session.getPid());
        GiftPlayer giftPlayer = giftPlayerDao.getBean(session.getPid());
        for (String id : req.getTo()) {
            GiftPlayer gp = giftPlayerDao.getBean(id);
            fashionGiftRuleContainer.give2(player, gp, itemId,req.getNum(), req.getMessage());
            giftPlayerDao.saveBean(gp);
            Session s = statusService.getSession(session, id);
            //通知对方有新消息
            if (null != s) {
                frontendService.broadcast(new Session[]{s}, new MsgVO(EventConstant.EVENT_FARM_GIFT, 1));
            }
        }
        //财富通知
        frontendService.broadcast(session, roleFactory.newPlayerVO(player));
        playerDao.saveBean(player);
        return 0;

    }


    @RpcAnnotation(cmd = "fashion.get.friend", lock = false, req = CommonReq.class, name = "取得好友")
    public FriendVO[] getFriend2(Session session, CommonReq req) throws RuleException {
        Friend friend = friendDao.getBean(session.getPid());
        List<FriendVO> vos = new ArrayList<FriendVO>();
        Map<String, FriendItem> items = friend.getFriends();
        for (FriendItem item : items.values()) {
                Player p = playerDao.getBean(item.getFid());
                FriendVO vo = roleFactory.newFriendVO(p, item);
            vos.add(vo);
        }
        return vos.toArray(new FriendVO[0]);
    }
}
