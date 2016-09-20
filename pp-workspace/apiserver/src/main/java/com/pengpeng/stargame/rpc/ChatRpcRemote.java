package com.pengpeng.stargame.rpc;

import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ChatRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*发送聊天信息
*/
public void talk (Session sessions, com.pengpeng.stargame.vo.chat.ChatReq vo ) throws GameException{
     proxy.execute("chat.talk",sessions, proxy.gson.toJson(vo),void.class);

}
/**
*千里传音
*/
public void shout (Session session, com.pengpeng.stargame.vo.chat.ChatReq req ) throws GameException{
     proxy.execute("chat.shout",session, proxy.gson.toJson(req),void.class);
    
}

}