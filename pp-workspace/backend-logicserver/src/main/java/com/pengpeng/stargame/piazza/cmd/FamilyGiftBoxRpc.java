package com.pengpeng.stargame.piazza.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.farm.rule.DropItem;
import com.pengpeng.stargame.gameevent.GameEventConstant;
import com.pengpeng.stargame.gameevent.dao.IEventDropDao;
import com.pengpeng.stargame.gameevent.dao.IFamilyEventValueDao;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.gameEvent.EventDrop;
import com.pengpeng.stargame.model.gameEvent.FamilyEventValue;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.container.IGiftBoxRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.dao.IStarGiftDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.player.cmd.PlayerRpc;
import com.pengpeng.stargame.player.container.IActivePlayerContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspGameEventFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.rsp.RspStarGiftFactory;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.piazza.StarGiftPageVO;
import com.pengpeng.stargame.vo.piazza.StarGiftReq;
import com.pengpeng.stargame.vo.piazza.StarSendPageVO;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;

/**
 * 礼物盒相关功能
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-25下午5:37
 */
@Component()
public class FamilyGiftBoxRpc extends RpcHandler {

    @Autowired
    private RspStarGiftFactory starGiftFactory;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IGiftBoxRuleContainer giftBoxRuleContainer;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private FrontendServiceProxy frontendService;

    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private MessageSource message;
    @Autowired
    private RspRoleFactory rspRoleFactory;

    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    IActivePlayerContainer activePlayerContainer;
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IEventDropDao eventDropDao;
    @Autowired
    private RspGameEventFactory rspGameEventFactory;
    @Autowired

    private IFamilyEventValueDao familyEventValueDao;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;
    @RpcAnnotation(cmd = "family.gift.info", req = StarGiftReq.class, name = "礼物盒界面", vo = StarGiftPageVO.class)
    public StarGiftPageVO getInfo(Session session, StarGiftReq req) throws AlertException, RuleException {

        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        familyRuleContainer.checkMember(family, familyMemberInfo);


        return starGiftFactory.getStarGiftPageVO(familyMemberInfo.getFamilyId(), req.getPageNo(), req.getType());
    }

    @RpcAnnotation(cmd = "family.gift.giftList", req = StarGiftReq.class, name = "送礼时候 礼物列表", vo = StarSendPageVO.class)
    public StarSendPageVO giftList(Session session, StarGiftReq req) throws AlertException, RuleException {


        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        familyRuleContainer.checkMember(family, familyMemberInfo);


        return starGiftFactory.getStarSendPageVO(session.getPid(), req.getType());
    }



    @RpcAnnotation(cmd = "family.gift.sendGift", req = StarGiftReq.class, name = "送礼", vo = void.class)
    public void sendGift(Session session, StarGiftReq req) throws AlertException, RuleException {
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        familyRuleContainer.checkMember(family, familyMemberInfo);
        FamilyRule rule = familyRuleContainer.getElement(family.getId());

        if (req.getNum() <= 0) {
            exceptionFactory.throwAlertException("非法请求");
        }
        /**
         * 非VIP 玩家 不能送多个
         */
        if(!payMemberRuleContainer.isPayMember(session.getPid())){
            req.setNum(1);
        }


        Player player = playerDao.getBean(session.getPid());
        FarmPlayer farmPlayer=farmPlayerDao.getFarmPlayer(session.getPid(),System.currentTimeMillis());
        giftBoxRuleContainer.checkSend(player, req.getType(), req.getId(), req.getNum(),farmPlayer,familyMemberInfo);

        DropItem dropItem=giftBoxRuleContainer.send(session,player, req.getType(), req.getId(), req.getNum(), req.getWords(),familyMemberInfo);
        BaseItemRule baseItemRule = baseItemRulecontainer.getElement(req.getId());


        //任务
        taskRuleContainer.updateTaskNum(session.getPid(), TaskConstants.CONDI_TYPE_7, req.getId(), req.getNum());

        //判断礼物为达人币型
        if (baseItemRule.getGoldPrice() > 0) {

            String msg = message.getMessage("family.giftbox", new String[]{player.getNickName(), rule.getStarName(), baseItemRule.getName()}, Locale.CHINA);
            Session[] sessions = statusRemote.getMember(session, SessionUtil.KEY_CHAT_WORLD);
            frontendService.broadcast(sessions, rspRoleFactory.newShoutChat(null, null, msg));
        }
        /**
         * 添加活跃度
         */
        activePlayerContainer.finish(session, session.getPid(), PlayerConstant.ACTIVE_TYPE_8, 1);
        /**
         * 如果获得了明星 回馈的礼物 ，给家族频道广播
         */
        if(dropItem!=null){
            BaseItemRule baseItemRuleTemp=baseItemRulecontainer.getElement(dropItem.getItemId());
            ChatVO vo = rspRoleFactory.newFamilyChat("", player.getNickName(), message.getMessage("family.giftbox.getreward", new String[]{player.getNickName(),rule.getStarName(),rule.getStarName(),baseItemRuleTemp.getName(),String.valueOf(dropItem.getNum())}, Locale.CHINA));
            Session[] sessions = statusRemote.getMember(session, familyMemberInfo.getFamilyId());
            frontendService.broadcast(sessions, vo);
        }

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        /**
         * 场景内的掉落  送礼物 掉落活动
         */
        String channelId = SessionUtil.getChannelScene(session);
        Session[] scenesessions = statusRemote.getMember(session, channelId);
        EventDrop eventDropInfo =eventDropDao.getEventDrop(channelId);
        frontendService.broadcast(scenesessions, rspGameEventFactory.getEventDropInfoVO(eventDropInfo,channelId));

        /**
         * 春节活动相关
         */
//        if(req.getId().equals("items_13013")||req.getId().equals("items_59999")){
//            FamilyEventValue familyEventValue=familyEventValueDao.getFamilyEventValue(family.getId());
//            int addValue=0;
//            if(req.getId().equals("items_13013")){
//                addValue=2*req.getNum();
//            }
//            if(req.getId().equals("items_59999")){
//                addValue=25*req.getNum();
//            }
//            familyEventValue.addFValue(addValue);
//
//            if(familyEventValue.getFirecrackerValue()< GameEventConstant.SPRING_EVENT_VALUE){
//                if(familyEventValue.getFirecrackerValue()%10==0){
//                    /**
//                     * 家族 广播
//                     */
//                    ChatVO vo = rspRoleFactory.newFamilyChat("","", message.getMessage("gameevent.springevent1", new String[]{String.valueOf(familyEventValue.getFirecrackerValue())}, Locale.CHINA));
//                    Session[] fsessions = statusRemote.getMember(session, familyMemberInfo.getFamilyId());
//                    frontendService.broadcast(fsessions, vo);
//
//                }
//            }else {
//                if(familyEventValue.getStatus()==1){ //放完之后 才进行 下一次的放
//                    familyEventValue.setStatus(2);
//                    familyEventValue.subFValue(GameEventConstant.SPRING_EVENT_VALUE);
//                    familyEventValue.setTime(DateUtil.addMinute(new Date(),GameEventConstant.SPRING_EVENT_STIME));
//
//                    /**
//                     * 全服广播
//                     */
//                    Session[] sessions = statusRemote.getMember(session, SessionUtil.KEY_CHAT_WORLD);
//                    ChatVO vo = rspRoleFactory.newShoutChat("", "",  message.getMessage("gameevent.springevent3",new String[]{family.getName(),String.valueOf(GameEventConstant.SPRING_EVENT_STIME)},Locale.CHINA));
//                    frontendService.broadcast(sessions, vo);
//                    /**
//                     * 家族明星广场广播  放鞭炮 状态
//                     */
//                    Session[] fsessions = statusRemote.getMember(session,"map_10."+familyMemberInfo.getFamilyId());
//                    frontendService.broadcast(fsessions, rspGameEventFactory.getSpringEventVO(family.getId(),familyEventValue,0));
//                }
//
//            }
//            familyEventValueDao.saveBean(familyEventValue);
//        }

        /**
         * 成就统计 送明星礼物次数
         */
        successRuleContainer.updateSuccessNum(session.getPid(),17,req.getNum(),"");
        //日志
        String value = baseItemRule.getItemsId() + GameLogger.SPLIT + String.valueOf(1) + GameLogger.SPLIT + baseItemRule.getType() + GameLogger.SPLIT + baseItemRule.getItemtype();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_20, session.getPid(), value));



    }

}
