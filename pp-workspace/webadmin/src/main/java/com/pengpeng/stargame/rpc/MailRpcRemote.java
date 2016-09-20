package com.pengpeng.stargame.rpc;

import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.vo.role.MailReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MailRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*删邮件
*/
public void remove (Session session, MailReq req ) throws GameException{
     proxy.execute("mail.remove",session, proxy.gson.toJson(req),void.class);

}
/**
*领取附件
*/
public void accept (Session mailInfo, MailReq session ) throws GameException{
     proxy.execute("mail.accept",mailInfo, proxy.gson.toJson(session),void.class);

}
/**
*发邮件
*/
public void send (Session mailInfo, MailReq session ) throws GameException{
     proxy.execute("mail.send",mailInfo, proxy.gson.toJson(session),void.class);

}
/**
*取得邮件列表
*/
public com.pengpeng.stargame.vo.role.MailPlusVO[] getMailList (Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.MailPlusVO[]  rValue=  proxy.execute("mail.get.list",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.MailPlusVO[].class);
      return rValue;
}
/**
*取得系统邮件
*/
public com.pengpeng.stargame.vo.role.MailPlusVO[] getBulletin (Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.MailPlusVO[]  rValue=  proxy.execute("mail.get.bulletin",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.MailPlusVO[].class);
      return rValue;
}
/**
*取得新消息数量
*/
public com.pengpeng.stargame.vo.MsgVO newMsg (Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.MsgVO  rValue=  proxy.execute("mail.newmsg",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.MsgVO.class);
      return rValue;
}

}