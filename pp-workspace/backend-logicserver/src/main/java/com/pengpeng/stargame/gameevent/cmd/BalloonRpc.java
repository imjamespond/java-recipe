package com.pengpeng.stargame.gameevent.cmd;

import com.pengpeng.framework.utils.DateUtil;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.gameevent.GameEventConstant;
import com.pengpeng.stargame.gameevent.constiner.IDropGfitRuleContainer;
import com.pengpeng.stargame.gameevent.constiner.IEventRuleContainer;
import com.pengpeng.stargame.gameevent.dao.IEventDropDao;
import com.pengpeng.stargame.gameevent.rule.DropGiftRule;
import com.pengpeng.stargame.gameevent.rule.EventRule;
import com.pengpeng.stargame.model.gameEvent.EventDrop;
import com.pengpeng.stargame.model.gameEvent.OneDrop;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspGameEventFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.util.RandomUtil;
import com.pengpeng.stargame.util.Uid;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.event.BalloonPanelInfoVO;
import com.pengpeng.stargame.vo.event.EventReq;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.Locale;

/**
 * User: mql
 * Date: 14-1-20
 * Time: 下午2:38
 */
@Component()
public class BalloonRpc extends RpcHandler {
    @Autowired
    private IEventDropDao eventDropDao;
    @Autowired
    private RspGameEventFactory rspGameEventFactory;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private RspRoleFactory rspRoleFactory;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private MessageSource message;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private IDropGfitRuleContainer dropGfitRuleContainer;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private IEventRuleContainer eventRuleContainer;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;

    @RpcAnnotation(cmd = "gameevent.balloon.info", lock = false, req = EventReq.class, name = "取得放气球面板信息")
    public BalloonPanelInfoVO getBalloonInfo(Session session, EventReq req){
        return rspGameEventFactory.getBalloonPanelInfoVO();
    }
    @RpcAnnotation(cmd = "gameevent.balloon.put", lock = false, req = EventReq.class, name = "放气球")
    public void putBalloon(Session session, EventReq req) throws AlertException {


        String channelId = SessionUtil.getChannelScene(session);
        if(!session.getScene().equals("map_12")){
            return;
        }
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        if(familyMemberInfo.getFamilyId()==null){
            return ;
        }
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        Player player=playerDao.getBean(session.getPid());
        /**
         * 游戏币的判断
         */
        int gameMoney= GameEventConstant.BALLOON.get(req.getMinutes());
        if(gameMoney==0){
            return;
        }
        if(player.getGameCoin()<gameMoney){
            exceptionFactory.throwAlertException("gamecoin.notenough");
        }
//        player.decGameCoin(gameMoney);
        playerRuleContainer.decGameCoin(player,gameMoney);
        playerDao.saveBean(player);
        EventDrop eventDropInfo = eventDropDao.getEventDrop(channelId);
        /**
         * 次数的判断
         */
        if(eventDropInfo.getBalloonNun(session.getPid())>=3){
            exceptionFactory.throwAlertException("gameevent.balloon2");
        }

        /**
         * 放气球
         */
        OneDrop oneDrop=new OneDrop();
        oneDrop.setUid(Uid.uuid());
        oneDrop.setGiftId(req.getId());
        oneDrop.setExpiration(DateUtil.addMinute(new Date(), req.getMinutes() * 60));
        oneDrop.setPid(session.getPid());
        oneDrop.setWord(req.getWord());
        oneDrop.setfName(family.getName());
        oneDrop.setPosition(req.getPosition());
        eventDropInfo.addOneDrop(oneDrop);
        eventDropDao.saveBean(eventDropInfo);


        /**
         * 家族 广播
         */
        DropGiftRule dropGiftRule=dropGfitRuleContainer.getElement(req.getId());
        ChatVO vo = rspRoleFactory.newFamilyChat("","", message.getMessage("gameevent.balloon1", new String[]{player.getNickName(),dropGiftRule.getName()}, Locale.CHINA));
        Session[] fsessions = statusRemote.getMember(session, familyMemberInfo.getFamilyId());
        frontendService.broadcast(fsessions, vo);

        /**
         * 场景 广播气球
         */
        Session[] csessions = statusRemote.getMember(session, channelId);
        frontendService.broadcast(csessions, rspGameEventFactory.getEventDropInfoVO(eventDropInfo,channelId));

        Session[] mysessions={session};
        frontendService.broadcast(mysessions,roleFactory.newPlayerVO(player));
    }

}
