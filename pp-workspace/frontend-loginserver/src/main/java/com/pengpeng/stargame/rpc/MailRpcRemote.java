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
*赠送礼物
*/
public com.pengpeng.stargame.vo.role.MailVO[] give (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.MailVO[]  rValue=  proxy.execute("mail.give.gift",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.MailVO[].class);
      return rValue;
}
/**
*取得邮件列表
*/
public com.pengpeng.stargame.vo.role.MailVO[] getMailList (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.MailVO[]  rValue=  proxy.execute("mail.get.list",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.MailVO[].class);
      return rValue;
}
/**
*索要礼物
*/
public com.pengpeng.stargame.vo.role.MailVO[] claim (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.MailVO[]  rValue=  proxy.execute("mail.claim.gift",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.MailVO[].class);
      return rValue;
}
/**
*取得新消息数量
*/
public com.pengpeng.stargame.vo.MsgVO newMsg (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.MsgVO  rValue=  proxy.execute("mail.newmsg",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.MsgVO.class);
      return rValue;
}
/**
*领取礼物
*/
public com.pengpeng.stargame.vo.role.MailVO[] accept (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.MailVO[]  rValue=  proxy.execute("mail.accept.gift",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.MailVO[].class);
      return rValue;
}

}