package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.MailRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.vo.role.MailPlusVO;
import com.pengpeng.stargame.vo.role.MailReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-28下午4:06
 */
@Component
public class MailCmd extends AbstractHandler {

    @Autowired
    private MailRpcRemote mailRpcRemote;
    @CmdAnnotation(cmd="mail.get.list",name="取得邮件列表",vo=MailPlusVO[].class,req=CommonReq.class)
    public Response getList(Session session,CommonReq req) throws GameException {
        MailPlusVO[] vo = mailRpcRemote.getMailList(session,req);
        return Response.newObject(vo);
    }
    @CmdAnnotation(cmd="mail.get.bulletin",name="取得系统邮件列表",vo=MailPlusVO[].class,req=CommonReq.class)
    public Response getBulletin(Session session,CommonReq req) throws GameException {
        MailPlusVO[] vo = mailRpcRemote.getBulletin(session,req);
        return Response.newObject(vo);
    }
    @CmdAnnotation(cmd="mail.accept",name="领取礼物",vo=MailPlusVO.class,req=MailReq.class)
    public Response accept(Session session,MailReq req) throws GameException {
        return Response.newObject(mailRpcRemote.accept(session,req));
    }
    @CmdAnnotation(cmd="mail.send",name="发邮件",vo=void.class,req=MailReq.class)
    public Response send(Session session,MailReq req) throws GameException {
        mailRpcRemote.send(session,req);
        return Response.newOK();
    }
    @CmdAnnotation(cmd="mail.remove",name="删邮件",vo=void.class,req=MailReq.class)
    public Response remove(Session session,MailReq req) throws GameException {
        mailRpcRemote.remove(session,req);
        return Response.newOK();
    }
    @CmdAnnotation(cmd="mail.player.remove",name="删玩家邮件",vo=MailPlusVO.class,req=MailReq.class)
    public Response removePlayerMail(Session session,MailReq req) throws GameException {
        return Response.newObject(mailRpcRemote.removePlayerMail(session,req));

    }
    @CmdAnnotation(cmd="mail.newmsg",name="取得新消息数量",vo=MsgVO.class,req=CommonReq.class)
    public Response newMsg(Session session,CommonReq req) throws GameException {
        MsgVO vo = mailRpcRemote.newMsg(session,req);
        return Response.newObject(vo);
    }
}
