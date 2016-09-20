package com.pengpeng.stargame.gameevent.cmd;

import com.pengpeng.stargame.LockRedis;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.farm.dao.IFarmDecoratePkgDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.gameevent.constiner.IEventRuleContainer;
import com.pengpeng.stargame.gameevent.dao.IEventDropDao;
import com.pengpeng.stargame.gameevent.dao.IFamilyEventValueDao;
import com.pengpeng.stargame.gameevent.dao.IPlayerEventDao;
import com.pengpeng.stargame.gameevent.rule.EventRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.gameEvent.*;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBank;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyBankDao;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.container.ISceneRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspGameEventFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.event.EventDropInfoVO;
import com.pengpeng.stargame.vo.event.EventReq;
import com.pengpeng.stargame.vo.event.FamilyBankEventVO;
import com.pengpeng.stargame.vo.event.SpringEventVO;
import net.sf.cglib.core.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;

/**
 * User: mql
 * Date: 13-12-6
 * Time: 下午5:21
 */
@Component()
public class EventRpc extends RpcHandler {
    @Autowired
    private IEventDropDao eventDropDao;
    @Autowired
    private IEventRuleContainer eventRuleContainer;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private StatusRemote statusService;
    @Autowired
    private RspGameEventFactory rspGameEventFactory;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IPlayerEventDao playerEventDao;
    @Autowired
    private IFamilyBankDao familyBankDao;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private RspRoleFactory rspRoleFactory;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private IFamilyEventValueDao familyEventValueDao;
    @Autowired
    private MessageSource message;
    @Autowired
    private LockRedis lockRedis;
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private ISceneRuleContainer sceneRuleContainer;
    @Autowired
    private IFarmDecoratePkgDao farmDecoratePkgDao;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @RpcAnnotation(cmd = "gameevent.dropgift.list", lock = false, req = EventReq.class, name = "取得场景内掉落")
    public EventDropInfoVO getDropGift(Session session, EventReq req) throws GameException {
        String channelId = SessionUtil.getChannelScene(session);
        if(!session.getScene().equals("map_10")&&!session.getScene().equals("map_11")&&!session.getScene().equals("map_12")){
            return null;
        }
        EventDrop eventDropInfo = eventDropDao.getEventDrop(channelId);
        return rspGameEventFactory.getEventDropInfoVO(eventDropInfo,channelId);
    }

    @RpcAnnotation(cmd = "gameevent.pick.gift", lock = false, req = EventReq.class, name = "捡取礼物")
    public RewardVO pickGift(Session session, EventReq req) throws GameException {
        Player player = playerDao.getBean(session.getPid());
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(session.getPid(), System.currentTimeMillis());
        String channelId = SessionUtil.getChannelScene(session);
        EventDrop eventDropInfo = eventDropDao.getEventDrop(channelId);
        OneDrop oneDrop = eventDropInfo.getOneDropById(req.getId());

        FarmPackage farmPackage = farmPackageDao.getBean(session.getPid());
        FashionCupboard fashionCupboard = fashionCupboardDao.getBean(session.getPid());
        RoomPackege roomPackege = roomPackegeDao.getRoomPackege(session.getPid());
        PlayerEvent playerEvent = playerEventDao.getPlayerEvent(session.getPid());
        FarmDecoratePkg farmDecoratePkg=farmDecoratePkgDao.getFarmDecoratePkg(session.getPid());
        RewardVO rewardVO = eventRuleContainer.pickDropGift(playerEvent, player, farmPlayer, farmPackage, fashionCupboard, roomPackege, oneDrop, eventDropInfo,farmDecoratePkg);

        Session[] sessions = statusService.getMember(session, channelId);
        frontendService.broadcast(sessions, rspGameEventFactory.getEventDropInfoVO(eventDropInfo,channelId));

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        return rewardVO;
    }

    @RpcAnnotation(cmd = "gameevent.familyBank.info", lock = false, req = EventReq.class, name = "获取家族银行活动信息")
    public FamilyBankEventVO getFamilyBankEventInfo(Session session, EventReq req) throws GameException {
        Player player = playerDao.getBean(session.getPid());
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        FamilyBank familyBank = familyBankDao.getFamilyBank(familyMemberInfo.getFamilyId());
        EventRule eventRule = eventRuleContainer.getFamilyBankEventRule();
        PlayerEvent playerEvent = playerEventDao.getPlayerEvent(session.getPid());
        FamilyBankEvent familyBankEvent = playerEvent.getFamilyBankEvent(eventRule.getId());

        boolean isChange = false;
        int gameMoney = familyBank.getMoenyByPid(session.getPid());
        for (String reward : eventRule.getFamilyBankRewards()) {
            String[] oneReward = reward.split(",");
            if (Integer.parseInt(oneReward[0]) <= gameMoney) {
                if (!familyBankEvent.getCanReward().contains(oneReward[0]) && !familyBankEvent.getReewarded().contains(oneReward[0])) {
                    familyBankEvent.getCanReward().add(oneReward[0]);
                    isChange = true;
                }
            }
        }
        if (isChange) {
            playerEvent.updateFamilyBankEvent(eventRule.getId(), familyBankEvent);
            playerEventDao.saveBean(playerEvent);
        }

        return rspGameEventFactory.getFamilybankEventVO(familyBank.getMoenyByPid(session.getPid()), familyBankEvent, 0);
    }


    @RpcAnnotation(cmd = "gameevent.familyBank.get", lock = false, req = EventReq.class, name = "领取存款奖励")
    public FamilyBankEventVO getFamilyBankEventReward(Session session, EventReq req) throws GameException {
        Player player = playerDao.getBean(session.getPid());
        PlayerEvent playerEvent = playerEventDao.getPlayerEvent(session.getPid());
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        FamilyBank familyBank = familyBankDao.getFamilyBank(familyMemberInfo.getFamilyId());
        EventRule eventRule = eventRuleContainer.getFamilyBankEventRule();
        FamilyBankEvent familyBankEvent = playerEvent.getFamilyBankEvent(eventRule.getId());

        int gold = 0;

        for (String reward : eventRule.getFamilyBankRewards()) {
            String[] oneReward = reward.split(",");
            if (oneReward[0].equals(req.getGameMoney())) {
                gold = Integer.parseInt(oneReward[1]);
                break;
            }
        }
        if (gold == 0) {
            return null;
        }
        if (familyBankEvent.getReewarded().contains(req.getGameMoney())) {
            return null;
        }
        if (!familyBankEvent.getCanReward().contains(req.getGameMoney())) {
            return null;
        }
        familyBankEvent.getReewarded().add(req.getGameMoney());
        familyBankEvent.getCanReward().remove(req.getGameMoney());

        playerEvent.updateFamilyBankEvent(eventRule.getId(), familyBankEvent);
//        player.incGoldCoin(gold);
         playerRuleContainer.incGoldCoin(player,gold,0);
        playerDao.saveBean(player);
        playerEventDao.saveBean(playerEvent);


        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        frontendService.broadcast(session, new MsgVO(EventConstant.EVENT_F_B, familyBankEvent.getCanReward().size()));

        return rspGameEventFactory.getFamilybankEventVO(familyBank.getMoenyByPid(session.getPid()), familyBankEvent, gold);
    }

    @RpcAnnotation(cmd = "gameevent.springevent.info", lock = false, req = EventReq.class, name = "获取春节活动信息")
    public SpringEventVO springeventInfo(Session session, EventReq req) throws GameException {
        String channelId = SessionUtil.getChannelScene(session);
        if(req.getFamilyId()==null||req.getFamilyId().equals("")){
            return null;
        }
        FamilyEventValue familyEventValue = familyEventValueDao.getFamilyEventValue(req.getFamilyId());
        return rspGameEventFactory.getSpringEventVO(req.getFamilyId(), familyEventValue,0);
    }

    @RpcAnnotation(cmd = "gameevent.springevent.start", lock = false, req = EventReq.class, name = "放鞭炮")
    public SpringEventVO springeventStart(Session session, EventReq req) throws GameException {
        if(req.getFamilyId()==null||req.getFamilyId().equals("")){
            return null;
        }
        String lock = "gameevent.springevent.start." + req.getFamilyId();
        if (!lockRedis.lock(lock)) {
             return null;
        }
        try {
            String channelId = sceneRuleContainer.getChannelId("map_10",null,req.getFamilyId());
            Family family = familyDao.getBean(req.getFamilyId());
            FamilyEventValue familyEventValue = familyEventValueDao.getFamilyEventValue(req.getFamilyId());
            if (familyEventValue.getStatus() != 2) {
                return null;
            }
            Date now = new Date();
            if (familyEventValue.getTime().getTime() > now.getTime()) {
                return null;
            }
            familyEventValue.setStatus(1);
            familyEventValueDao.saveBean(familyEventValue);
            /**
             * 掉落礼物
             */
            eventRuleContainer.springEventDrop(channelId);
            /**
             * 全服广播 消息
             */

            Session[] sessions = statusService.getMember(session, SessionUtil.KEY_CHAT_WORLD);
            ChatVO vo = rspRoleFactory.newShoutChat("", "", message.getMessage("gameevent.springevent2", new String[]{family.getName()}, Locale.CHINA));
            frontendService.broadcast(sessions, vo);
            /**
             * 广播放鞭炮 消息     ,如果下一次也达到了 那么继续倒计时 ，这里从新查询下 信息
             */
            FamilyEventValue familyEventValueN = familyEventValueDao.getFamilyEventValue(req.getFamilyId());
            frontendService.broadcast(sessions, rspGameEventFactory.getSpringEventVO(req.getFamilyId(), familyEventValueN,1));
            /**
             * 像同一场景的玩家 广播 礼物掉落
             */
            EventDrop eventDropInfo = eventDropDao.getEventDrop(channelId);
            Session[] csessions = statusService.getMember(session, channelId);
            frontendService.broadcast(csessions, rspGameEventFactory.getEventDropInfoVO(eventDropInfo,channelId));


            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockRedis.unlock(lock);
        }
        return null;
    }

    @RpcAnnotation(cmd = "gameevent.plazaDrop.execute", lock = false, req = EventReq.class, name = "GM调用，广场定时掉落 广播")
    public void plazaDrop(Session session, EventReq req) throws GameException {
        if(eventRuleContainer.openStarPlazaEvent()){
            /**
             * 调用掉落
             */
            eventRuleContainer.starPlazaDrop();
            /**
             * 明星广场广播
             */
            for(FamilyRule familyRule:familyRuleContainer.getAll()){
                String channelId = sceneRuleContainer.getChannelId("map_10",null,familyRule.getId());
                EventDrop eventDropInfo =eventDropDao.getEventDrop(channelId);
                Session[] scenesessions = statusService.getMember(session, channelId);
                frontendService.broadcast(scenesessions, rspGameEventFactory.getEventDropInfoVO(eventDropInfo,channelId));
            }
        }
    }
}
