package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class MailRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*删邮件
*/
public void remove (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.MailReq req ) throws GameException{
     proxy.execute("mail.remove",session, proxy.gson.toJson(req),void.class);
    
}
/**
*领取附件
*/
public com.pengpeng.stargame.vo.role.MailPlusVO accept (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.MailReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.MailPlusVO  rValue=  proxy.execute("mail.accept",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.MailPlusVO.class);
      return rValue;
}
/**
*发邮件
*/
public void send (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.MailReq req ) throws GameException{
     proxy.execute("mail.send",session, proxy.gson.toJson(req),void.class);
    
}
/**
*取得邮件列表
*/
public com.pengpeng.stargame.vo.role.MailPlusVO[] getMailList (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.MailPlusVO[]  rValue=  proxy.execute("mail.get.list",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.MailPlusVO[].class);
      return rValue;
}
/**
*取得系统邮件
*/
public com.pengpeng.stargame.vo.role.MailPlusVO[] getBulletin (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.MailPlusVO[]  rValue=  proxy.execute("mail.get.bulletin",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.MailPlusVO[].class);
      return rValue;
}
/**
*删玩家邮件
*/
public com.pengpeng.stargame.vo.role.MailPlusVO removePlayerMail (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.MailReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.MailPlusVO  rValue=  proxy.execute("mail.player.remove",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.MailPlusVO.class);
      return rValue;
}
/**
*取得新消息数量
*/
public com.pengpeng.stargame.vo.MsgVO newMsg (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.MsgVO  rValue=  proxy.execute("mail.newmsg",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.MsgVO.class);
      return rValue;
}

}