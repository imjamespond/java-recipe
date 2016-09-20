package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class ChatRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*发送聊天信息
*/
public void talk (com.pengpeng.stargame.rpc.Session sessions, com.pengpeng.stargame.vo.chat.ChatReq vo ) throws GameException{
     proxy.execute("chat.talk",sessions, proxy.gson.toJson(vo),void.class);
    
}
/**
*千里传音
*/
public void shout (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.chat.ChatReq req ) throws GameException{
     proxy.execute("chat.shout",session, proxy.gson.toJson(req),void.class);
    
}
/**
*千里传音信息
*/
public com.pengpeng.stargame.vo.chat.ShoutVO shoutInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.chat.ChatReq req ) throws GameException{
      com.pengpeng.stargame.vo.chat.ShoutVO  rValue=  proxy.execute("chat.shoutInfo",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.chat.ShoutVO.class);
      return rValue;
}

}