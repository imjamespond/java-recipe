package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;


@Component
public class StatusRemote extends StatusServiceProxy {
    /**
    *取得场景内所有的用户
    */
    public com.pengpeng.stargame.rpc.Session[] getMember (com.pengpeng.stargame.rpc.Session session, java.lang.String id ){
      com.pengpeng.stargame.rpc.Session[]  rValue=  execute("channel.getMember",session, gson.toJson(id),com.pengpeng.stargame.rpc.Session[].class);
      return rValue;
     }
    /**
    *进入聊天室
    */
    public void enterChat (com.pengpeng.stargame.rpc.Session session, java.lang.String id ){
     execute("chat.enter",session, gson.toJson(id),void.class);
    
     }
    /**
    *退出聊天室
    */
    public void outerChat (com.pengpeng.stargame.rpc.Session session, java.lang.String id ){
     execute("chat.outer",session, gson.toJson(id),void.class);
    
     }
    /**
    *更新session数据
    */
    public void updateSession (com.pengpeng.stargame.rpc.Session session, java.util.Map map ){
     execute("session.update",session, gson.toJson(map),void.class);
    
     }
    /**
    *清理指定服务器的session
    */
    public void cleanSession (com.pengpeng.stargame.rpc.Session session, java.lang.String sid ){
     execute("session.clean",session, gson.toJson(sid),void.class);
    
     }
    /**
    *进入频道
    */
    public void enterChannel (com.pengpeng.stargame.rpc.Session session, java.lang.String id ){
     execute("channel.enter",session, gson.toJson(id),void.class);
    
     }
    /**
    *退出频道
    */
    public void outerChannel (com.pengpeng.stargame.rpc.Session session, java.lang.String id ){
     execute("channel.outer",session, gson.toJson(id),void.class);
    
     }
    /**
    *随机取得一个在线用户
    */
    public com.pengpeng.stargame.rpc.Session[] randomSession (com.pengpeng.stargame.rpc.Session session, java.lang.String[] ids ){
      com.pengpeng.stargame.rpc.Session[]  rValue=  execute("channel.random",session, gson.toJson(ids),com.pengpeng.stargame.rpc.Session[].class);
      return rValue;
     }
    /**
    *进入频道
    */
    public int size (com.pengpeng.stargame.rpc.Session session, java.lang.String id ){
      int  rValue=  execute("status.size",session, gson.toJson(id),int.class);
      return rValue;
     }
    /**
    *登录
    */
    public void login (com.pengpeng.stargame.rpc.Session session, java.lang.String channelId ){
     execute("status.login",session, gson.toJson(channelId),void.class);
    
     }
    /**
    *退出
    */
    public void logout (com.pengpeng.stargame.rpc.Session session, java.lang.String channelId ){
     execute("status.logout",session, gson.toJson(channelId),void.class);
    
     }
    /**
    *取得Session信息
    */
    public com.pengpeng.stargame.rpc.Session getSession (com.pengpeng.stargame.rpc.Session session, java.lang.String pid ){
      com.pengpeng.stargame.rpc.Session  rValue=  execute("status.get.session",session, gson.toJson(pid),com.pengpeng.stargame.rpc.Session.class);
      return rValue;
     }
    /**
    *取得token
    */
    public java.lang.String getToken (com.pengpeng.stargame.rpc.Session session, java.lang.String token ){
      java.lang.String  rValue=  execute("token.get",session, gson.toJson(token),java.lang.String.class);
      return rValue;
     }
    /**
    *增加一个token
    */
    public void addToken (com.pengpeng.stargame.rpc.Session session, java.lang.String token ){
     execute("token.add",session, gson.toJson(token),void.class);
    
     }
    /**
    *token验证
    */
    public boolean auth (com.pengpeng.stargame.rpc.Session session, java.lang.String token ){
      boolean  rValue=  execute("token.auth",session, gson.toJson(token),boolean.class);
      return rValue;
     }
    /**
    *删除一个token
    */
    public void removeToken (com.pengpeng.stargame.rpc.Session session, java.lang.String token ){
     execute("token.del",session, gson.toJson(token),void.class);
    
     }



}
