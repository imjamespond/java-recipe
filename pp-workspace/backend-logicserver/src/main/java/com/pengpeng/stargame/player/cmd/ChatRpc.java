package com.pengpeng.stargame.player.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.model.player.PlayerChat;
import com.pengpeng.stargame.player.GameManager;
import com.pengpeng.stargame.player.container.IChatContainer;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IOtherPlayerDao;
import com.pengpeng.stargame.player.dao.IPlayerChatDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.chat.ChatReq;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.chat.ShoutVO;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 聊天
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-20上午10:57
 */
@Component()
public class ChatRpc extends RpcHandler {
    private static final Logger logger = Logger.getLogger("infolog");


    @Autowired
    private StatusRemote statusService;

    @Autowired
    private FrontendServiceProxy frontendService;

    @Autowired
    private IChatContainer chatContainer;

    @Autowired
    private IPlayerDao playerDao;

    @Autowired
    private IOtherPlayerDao otherPlayerDao;

    @Autowired
    private RspRoleFactory rspRoleFactory;

    @Autowired
    private GameManager gm;
    @Autowired

    private IPlayerChatDao playerChatDao;
    @Autowired
    private IExceptionFactory exceptionFactory;
    private int environment;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;

    @RpcAnnotation(cmd = "chat.talk", lock = false, req = ChatReq.class, name = "发送聊天信息")
    public void talk(Session session, ChatReq req) throws RuleException, AlertException {

        String msg = req.getMsg();
        int idx = msg.indexOf("##");
        if (environment == 0) {
            if (idx >= 0) {
                gm.execute(session, msg);
                return;
            }
        }

        //1.检查并过滤聊天内容
        //2.检查频道是否可发音
        //3.取得频道内Session,并广播消息

        //如果是私聊,检查是否好友关系
        //如果是某一频道,检查是否在某一频道内可发音
        boolean sensitive = chatContainer.hasSensitive(req.getMsg());
        if (sensitive) {
            return;
        }

        Player player = playerDao.getBean(session.getPid());
        OtherPlayer op = otherPlayerDao.getBean(player.getId());
        chatContainer.checkTalk(op);
        if (StringUtils.isBlank(req.getType())) {
            String channelId = SessionUtil.getChannelScene(session);
            Session[] sessions = statusService.getMember(session, channelId);

            ChatVO vo = rspRoleFactory.newTalkChat(session.getPid(), player.getNickName(), req.getMsg());
            frontendService.broadcast(sessions, vo);
        } else if ("fans".equalsIgnoreCase(req.getType())) {
            //粉丝团聊天
            String channelId = session.getParam(SessionUtil.KEY_CHANNEL_FAMILY);
            Session[] sessions = statusService.getMember(session, channelId);

            ChatVO vo = rspRoleFactory.newFamilyChat(session.getPid(), player.getNickName(), req.getMsg());
            frontendService.broadcast(sessions, vo);
        } else if("world".equalsIgnoreCase(req.getType())) {
            if(player.getGameCoin()<300){
                exceptionFactory.throwAlertException("gamecoin.notenough");
            }
//            player.decGameCoin(300);
            playerRuleContainer.decGameCoin(player,300);
            playerDao.saveBean(player);
            frontendService.broadcast(session, rspRoleFactory.newPlayerVO(player));
            Session[] sessions = statusService.getMember(session, SessionUtil.KEY_CHAT_WORLD);
            ChatVO vo = rspRoleFactory.newWorldChat(session.getPid(), player.getNickName(), req.getMsg());
            frontendService.broadcast(sessions, vo);
        }

    }

    @RpcAnnotation(cmd = "chat.shout", lock = false, req = ChatReq.class, name = "千里传音")
    public void shout(Session session, ChatReq req) throws GameException {
        //1.检查并过滤聊天内容
        //2.检查频道是否可发音
        //3.取得频道内Session,并广播消息

        //如果是私聊,检查是否好友关系
        //如果是某一频道,检查是否在某一频道内可发音
        Player player = playerDao.getBean(session.getPid());
        OtherPlayer op = otherPlayerDao.getBean(player.getId());
        chatContainer.checkTalk(op);

        PlayerChat playerChat = playerChatDao.getPlayerChat(session.getPid());
        if (req.getSendType() != 1 && req.getSendType() != 2) {
            return;
        }
        if (req.getSendType() == 1) {
            chatContainer.checkCoin(player, playerChat);
            playerDao.saveBean(player);
        }
        if (req.getSendType() == 2) {
            chatContainer.checFreeNum(player, playerChat);
            playerChatDao.saveBean(playerChat);
        }


        Session[] sessions = statusService.getMember(session, SessionUtil.KEY_CHAT_WORLD);
        ChatVO vo = rspRoleFactory.newShoutChat(session.getPid(), player.getNickName(), req.getMsg());
        frontendService.broadcast(sessions, vo);

        if (req.getSendType() == 1) {
            //财富通知
            Session[] mysessions = {session};
            frontendService.broadcast(mysessions, rspRoleFactory.newPlayerVO(player));
        }
    }

    @RpcAnnotation(cmd = "chat.shoutInfo", lock = false, req = ChatReq.class, name = "千里传音信息")
    public ShoutVO shoutInfo(Session session, ChatReq req) throws GameException {
        PlayerChat playerChat = playerChatDao.getPlayerChat(session.getPid());
        ShoutVO shoutVO = new ShoutVO();
        shoutVO.setNum(chatContainer.getFreeNum(session.getPid()) - playerChat.getNum());
        return shoutVO;
    }

    public int getEnvironment() {
        return environment;
    }

    public void setEnvironment(int environment) {
        this.environment = environment;
    }
}
