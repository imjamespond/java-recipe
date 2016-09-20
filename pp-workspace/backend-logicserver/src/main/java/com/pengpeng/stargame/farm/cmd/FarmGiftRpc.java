package com.pengpeng.stargame.farm.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.constant.BaseItemConstant;
import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmGiftRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.fashion.container.IFashionGiftRuleContainer;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.model.player.FriendItem;
import com.pengpeng.stargame.model.Gift;
import com.pengpeng.stargame.model.GiftPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.FashionPkg;
import com.pengpeng.stargame.player.EventLogBuilder;
import com.pengpeng.stargame.player.dao.IEventLogDao;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.player.dao.IGiftPlayerDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspFarmGiftFactory;
import com.pengpeng.stargame.rsp.RspFashionPkgFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.vo.fashion.FashionPkgVO;
import com.pengpeng.stargame.vo.role.FriendVO;
import com.pengpeng.stargame.vo.role.GiftReq;
import com.pengpeng.stargame.vo.role.GiftVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 农场礼物模块
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-4上午11:06
 */
@Component()
public class FarmGiftRpc extends RpcHandler {

    @Autowired
    private IFriendDao friendDao;

    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IGiftPlayerDao giftPlayerDao;

    @Autowired
    private MessageSource message;

    @Autowired
    private IEventLogDao eventLogDao;

    // 农场礼品交易规则
    @Autowired
    private IFarmGiftRuleContainer farmGiftRuleContainer;

    @Autowired
    private IFarmPackageDao farmPackageDao;

    @Autowired
    private RspFarmGiftFactory rspFarmGiftFactory;

    @Autowired
    private RspRoleFactory roleFactory;

    @Autowired
    private EventLogBuilder eventLogBuilder;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private IFashionGiftRuleContainer fashionGiftRuleContainer;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private RspFashionPkgFactory factory;


    @Autowired
    private StatusRemote statusService;

    @RpcAnnotation(cmd = "farm.gift.list.info", lock = false, req = CommonReq.class, name = "取得玩家礼物列表")
    public GiftVO[] listInfo(Session session, CommonReq req) throws RuleException {
        GiftPlayer gp = giftPlayerDao.getBean(session.getPid());
        //List<Gift> list = gp.getAll();
        List<GiftVO> items = new ArrayList<GiftVO>();
        for (int i=0;i<gp.getGiftListSize();++i) {
            Gift gift = gp.getFromGiftList(i);
            if (gift.getFid().equals(Constant.QINMA_ID)) {
                items.add(rspFarmGiftFactory.newQinMaGiftVO(Constant.QINMA_ID, gift));
            } else {
                Player player = playerDao.getBean(gift.getFid());
                items.add(rspFarmGiftFactory.newGiftVO(player, gift));
            }
        }
        return items.toArray(new GiftVO[0]);
    }

    @RpcAnnotation(cmd = "farm.gift.give", lock = true, req = GiftReq.class, name = "赠送礼物")
    public int give(Session session, GiftReq req) throws RuleException {
        String itemId = req.getItemId();
        Player player = playerDao.getBean(session.getPid());
        //我的礼物对象
        GiftPlayer giftPlayer = giftPlayerDao.getBean(session.getPid());
        if (null == req.getTo() || req.getTo().length <= 0) {
            return giftPlayer.getGiveCount();
        }
        farmGiftRuleContainer.checkGive(session.getPid(), giftPlayer.getGiveCount() + req.getTo().length);//检查次数够不够
        for (String id : req.getTo()) {
            //好友的gp对象
            GiftPlayer friendGiftPlayer = giftPlayerDao.getBean(id);
            farmGiftRuleContainer.give(giftPlayer, friendGiftPlayer, itemId);
            giftPlayerDao.saveBean(friendGiftPlayer);
            Session s = statusService.getSession(session, id);
            //通知对方有新消息
            if (null != s) {
                frontendService.broadcast(new Session[]{s}, new MsgVO(EventConstant.EVENT_FARM_GIFT, 1));
            }
        }
        giftPlayerDao.saveBean(giftPlayer);
        int maxNum = farmGiftRuleContainer.getMaxSendNum(session.getPid());
        return maxNum - giftPlayer.getGiveCount();
    }

    @RpcAnnotation(cmd = "farm.gift.accept", lock = true, req = GiftReq.class, name = "领取(接受)礼物")
    public void accept(Session session, GiftReq req) throws AlertException {
        GiftPlayer giftPlayer = giftPlayerDao.getBean(session.getPid());//我的礼物
        FarmPackage farmPackage = farmPackageDao.getBean(session.getPid());
        FashionCupboard fashionCupboard = fashionCupboardDao.getBean(session.getPid());

        String[] fIds = req.getTo();
        for (String order : fIds) {
            farmGiftRuleContainer.acceptOne(giftPlayer, farmPackage, fashionCupboard, Integer.valueOf(order));
        }
        giftPlayer.cleanGift();//

        Session[] mysessions = {session};
        for (int i = 1; i < 9; i++) {
            String type = String.valueOf(i);
            FashionPkg fashionPkg = fashionCupboard.getFashionPkg(type);
            FashionPkgVO vo = factory.getFashionPkg(fashionPkg, type);
            frontendService.broadcast(mysessions, vo);
        }
        farmPackageDao.saveBean(farmPackage);
        fashionCupboardDao.saveBean(fashionCupboard);
        giftPlayerDao.saveBean(giftPlayer);
    }



    @RpcAnnotation(cmd = "farm.gift.reject", lock = true, req = GiftReq.class, name = "忽略(拒绝)好友的礼物")
    public void reject(Session session, GiftReq req) throws RuleException {
        //自己的gp对象
        String[] fIds = req.getTo();
        GiftPlayer gp = giftPlayerDao.getBean(session.getPid());
        for (String order : fIds) {
            int o = Integer.valueOf(order);
            Gift gift = gp.getFromGiftList(o);
            if(gift!=null){
                if(gift.getType()==2);// || gift.getType()==1)//不删除套装
                {
                    //continue;
                }
            }
            gp.set2GiftList(o,null);
        }
        gp.cleanGift();//
        giftPlayerDao.saveBean(gp);
    }

    @RpcAnnotation(cmd = "farm.gift.get.friend", lock = false, req = CommonReq.class, name = "取得未赠送礼物的好友")
    public FriendVO[] getFriend(Session session, CommonReq req) throws RuleException {
        Friend friend = friendDao.getBean(session.getPid());
        List<FriendVO> vos = new ArrayList<FriendVO>();
        Map<String, FriendItem> items = friend.getFriends();
        GiftPlayer gp = giftPlayerDao.getBean(session.getPid());//我的gp对象
        for (FriendItem item : items.values()) {
            //好友的gp
            //好友是否有我赠送的礼物
            boolean hasGift = gp.hasGift(item.getFid());//是否已赠送过给好友
            if (!hasGift) {
                Player p = playerDao.getBean(item.getFid());
                if (p == null) {
                    continue;
                }
                FriendVO vo = roleFactory.newFriendVO(p, item);
                vos.add(vo);
            }
        }
        return vos.toArray(new FriendVO[0]);
    }

    @RpcAnnotation(cmd = "farm.gift.untreated", lock = false, req = CommonReq.class, name = "取得未领取的礼物总数")
    public int untreated(Session session, CommonReq req) throws RuleException {
        GiftPlayer gp = giftPlayerDao.getBean(session.getPid());
        return gp.getUntreatedGift();
    }

    @RpcAnnotation(cmd = "farm.gift.giveCount", lock = false, req = CommonReq.class, name = "礼物还可以赠送多少次")
    public int giveCount(Session session, CommonReq req) throws RuleException {
        GiftPlayer gp = giftPlayerDao.getBean(session.getPid());
        int maxNum = farmGiftRuleContainer.getMaxSendNum(session.getPid());
        return maxNum - gp.getGiveCount();
    }

}
