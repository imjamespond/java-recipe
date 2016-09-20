package com.pengpeng.stargame.player.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.BaseRewardConstant;
import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.cmd.FarmRpc;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.model.GiftPlayer;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.player.*;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.piazza.container.IFamilyCollectRuleContainer;
import com.pengpeng.stargame.player.container.IMailRuleContainer;
import com.pengpeng.stargame.player.dao.*;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspMailFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.vo.role.MailPlusVO;
import com.pengpeng.stargame.vo.role.MailReq;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-28下午4:53
 */
@Component()
public class MailRpc extends RpcHandler {

    @Autowired
    private IMailDao mailDao;
    @Autowired
    private IMailBulletinDao mailBulletinDao;
    @Autowired
    private IMailRuleContainer mailRuleContainer;
    @Autowired
    private RspMailFactory rspFactory;

    @Autowired
    private IFriendDao friendDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IGiftPlayerDao giftPlayerDao;
    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private FrontendServiceProxy frontendService;

    @Autowired
    private FarmRpc farmRpc;
    @Autowired
    private IFamilyCollectRuleContainer familyCollectRuleContainer;
    private static final Logger logger = Logger.getLogger(MailRpc.class);

    @RpcAnnotation(cmd = "mail.get.list", lock = false, req = CommonReq.class, name = "取得邮件列表", vo = MailPlusVO[].class)
    public MailPlusVO[] getMailList(Session session, CommonReq req) {
        /**
         * 收集 奖励邮件的处理
         */
        try {
            familyCollectRuleContainer.checkRewardMail(session.getPid());
        } catch (AlertException e) {
            logger.error(e.getMessage());
        }
        Mail mailBulletin = mailBulletinDao.getMail();
        Mail mail = mailDao.getMail(session.getPid());
        List<MailPlusVO> list = mailRuleContainer.getMailList(mailBulletin, mail);
        MailPlusVO[] vos = rspFactory.mailList(list);
        return vos;
    }


    @RpcAnnotation(cmd = "mail.get.bulletin", lock = false, req = CommonReq.class, name = "取得系统邮件", vo = MailPlusVO[].class)
    public MailPlusVO[] getBulletin(Session session, CommonReq req) {
        Mail bulletin = mailBulletinDao.getMail();
        List<MailPlusVO> list = new ArrayList<MailPlusVO>();
        //系统邮件
        mailRuleContainer.getMailPlus(bulletin, list);
        MailPlusVO[] vos = rspFactory.mailList(list);
        return vos;
    }

    @RpcAnnotation(cmd = "mail.send", lock = true, req = MailReq.class, name = "发邮件")
    public void send(Session session, MailReq req) throws AlertException {
        Mail mail = null;
        if (req.getType() == MailConstant.TYPE_BULLETIN) {
            mail = mailBulletinDao.getMail();
        } else if (req.getType() == MailConstant.TYPE_ORDINARY) {
            mail = mailDao.getMail(session.getPid());
        }

        MailInfo mailInfo = new MailInfo();
        mailInfo.setTitle(req.getTitle());
        mailInfo.setContent(req.getContent());
        mailInfo.setIntegralType(req.getIntegralType());
        mailRuleContainer.parseAttachmentsString(mailInfo, req.getAttachments());
        mail.setNotify(1);
        mail.addMailInfo(mailInfo);

        if (req.getType() == MailConstant.TYPE_BULLETIN) {
            mailBulletinDao.saveBean(mail);
        } else if (req.getType() == MailConstant.TYPE_ORDINARY) {
            mailDao.saveBean(mail);
        }
    }

    @RpcAnnotation(cmd = "mail.remove", lock = true, req = MailReq.class, name = "删邮件")
    public void remove(Session session, MailReq req) throws AlertException {
        Mail mail = null;
        if (req.getType() == MailConstant.TYPE_BULLETIN) {
            mail = mailBulletinDao.getMail();
            mail.removeMail(req.getMailId());
            mailBulletinDao.saveBean(mail);
        } else if (req.getType() == MailConstant.TYPE_ORDINARY) {
            mail = mailDao.getMail(session.getPid());
            mail.removeMail(req.getMailId());
            mailDao.saveBean(mail);
        }
    }

    @RpcAnnotation(cmd = "mail.player.remove", lock = true, req = MailReq.class, name = "删玩家邮件")
    public MailPlusVO removePlayerMail(Session session, MailReq req) throws AlertException {
        Mail mail = mailDao.getMail(session.getPid());
        if (req.getType() == MailConstant.TYPE_BULLETIN) {
            Mail bulletin = mailBulletinDao.getMail();
            mail.removeBulletin(bulletin, req.getMailId());
        } else if (req.getType() == MailConstant.TYPE_ORDINARY) {
            mail.removeMail(req.getMailId());
        }
        mailDao.saveBean(mail);

        MailPlusVO vo = new MailPlusVO();
        vo.setId(req.getMailId());
        return vo;
    }

    @RpcAnnotation(cmd = "mail.accept", lock = true, req = MailReq.class, name = "领取附件")
    public MailPlusVO accept(Session session, MailReq req) throws AlertException {
        String pid = session.getPid();

        Player player = playerDao.getBean(pid);
        Mail mail = mailDao.getMail(session.getPid());
        MailInfo mailInfo = null;
        if (req.getType() == MailConstant.TYPE_BULLETIN) {
            Mail bulletin = mailBulletinDao.getMail();
            mailInfo = bulletin.getMailInfo(req.getMailId());
            if (null != mailInfo) {
                if (mail.accepted(req.getMailId())) {
                    exceptionFactory.throwAlertException("attachment.accepted");
                }
                mail.setAccepted(req.getMailId()); //设置已领取
                mailDao.saveBean(mail);
            }
        } else if (req.getType() == MailConstant.TYPE_ORDINARY) {

            mailInfo = mail.getMailInfo(req.getMailId());
            if (null != mailInfo) {
                if (mailInfo.accepted()) {
                    exceptionFactory.throwAlertException("attachment.accepted");
                }
                mailInfo.setAccepted(1); //设置已领取
                mailDao.saveBean(mail);
            }
        }

        if (null != mailInfo) {
            //领取:网站积分应做为单独类型附件赠送
            int notify = mailRuleContainer.accept(mailInfo, player);

            //推送
            Session[] mysessions = {session};
            if ((notify & BaseRewardConstant.NOTIFY_GAMECOIN) > 0) {
                //财富通知
                frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
            }
            if ((notify & BaseRewardConstant.NOTIFY_FARMEXP) > 0) {
                //经验通知
                frontendService.broadcast(mysessions, farmRpc.getFarmVoByPlayerId(pid));
            }

            //VO
            MailPlusVO vo = new MailPlusVO();
            vo.setId(req.getMailId());
            mailRuleContainer.getAttachment(mailInfo, vo);
            return vo;
        }
        return null;
    }

    @RpcAnnotation(cmd = "mail.newmsg", lock = false, req = CommonReq.class, name = "取得新消息数量")
    public MsgVO newMsg(Session session, CommonReq req) {
        Friend friend = friendDao.getBean(session.getPid());
        int count = friend.getUnknownsFriends().size();
        if (count > 0) {
            return new MsgVO(EventConstant.EVENT_EMAIL, 1);
        }
        GiftPlayer gp = giftPlayerDao.getBean(session.getPid());
        count = gp.size();
        if (count > 0) {
            return new MsgVO(EventConstant.EVENT_FARM_GIFT, 1);
        }

        Mail mail = mailDao.getMail(session.getPid());
        if (mail.getNotify() > 0) {
            return new MsgVO(EventConstant.EVENT_NEW_MAIL, 1);
        }

        return new MsgVO(EventConstant.EVENT_EMAIL, 0);
    }
}
