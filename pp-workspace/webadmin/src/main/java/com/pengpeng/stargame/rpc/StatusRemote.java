package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;


@Component
public class StatusRemote extends StatusServiceProxy {
    /**
    *进入频道
    */
    public int size (Session session, String id ){
      int  rValue=  execute("status.size",session, gson.toJson(id),int.class);
      return rValue;
     }
    /**
    *取得场景内所有的用户
    */
    public Session[] getMember (Session session, String id ){
      Session[]  rValue=  execute("channel.getMember",session, gson.toJson(id),Session[].class);
      return rValue;
     }
    /**
    *进入聊天室
    */
    public void enterChat (Session session, String id ){
     execute("chat.enter",session, gson.toJson(id),void.class);

     }
    /**
    *退出聊天室
    */
    public void outerChat (Session session, String id ){
     execute("chat.outer",session, gson.toJson(id),void.class);

     }
    /**
    *更新session数据
    */
    public void updateSession (Session session, java.util.Map map ){
     execute("session.update",session, gson.toJson(map),void.class);

     }
    /**
    *清理指定服务器的session
    */
    public void cleanSession (Session session, String sid ){
     execute("session.clean",session, gson.toJson(sid),void.class);

     }
    /**
    *进入频道
    */
    public void enterChannel (Session session, String id ){
     execute("channel.enter",session, gson.toJson(id),void.class);

     }
    /**
    *退出频道
    */
    public void outerChannel (Session session, String id ){
     execute("channel.outer",session, gson.toJson(id),void.class);

     }
    /**
    *随机取得一个在线用户
    */
    public Session[] randomSession (Session session, String[] ids ){
      Session[]  rValue=  execute("channel.random",session, gson.toJson(ids),Session[].class);
      return rValue;
     }
    /**
    *登录
    */
    public void login (Session session, String channelId ){
     execute("status.login",session, gson.toJson(channelId),void.class);

     }
    /**
    *退出
    */
    public void logout (Session session, String channelId ){
     execute("status.logout",session, gson.toJson(channelId),void.class);

     }
    /**
    *取得Session信息
    */
    public Session getSession (Session session, String pid ){
      Session  rValue=  execute("status.get.session",session, gson.toJson(pid),Session.class);
      return rValue;
     }
    /**
    *取得token
    */
    public String getToken (Session session, String token ){
      String  rValue=  execute("token.get",session, gson.toJson(token),String.class);
      return rValue;
     }
    /**
    *增加一个token
    */
    public void addToken (Session session, String token ){
     execute("token.add",session, gson.toJson(token),void.class);

     }
    /**
    *token验证
    */
    public boolean auth (Session session, String token ){
      boolean  rValue=  execute("token.auth",session, gson.toJson(token),boolean.class);
      return rValue;
     }
    /**
    *删除一个token
    */
    public void removeToken (Session session, String token ){
     execute("token.del",session, gson.toJson(token),void.class);
    
     }



}
