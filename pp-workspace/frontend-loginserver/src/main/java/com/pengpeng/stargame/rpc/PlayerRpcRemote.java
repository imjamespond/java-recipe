package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class PlayerRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*创建角色
*/
public java.lang.String createPlayer (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.CreatePlayerReq req ) throws GameException{
      java.lang.String  rValue=  proxy.execute("p.create",session, proxy.gson.toJson(req),java.lang.String.class);
      return rValue;
}
/**
*取得playerid
*/
public java.lang.String getPid (com.pengpeng.stargame.rpc.Session session, java.lang.Integer id ) throws GameException{
      java.lang.String  rValue=  proxy.execute("p.get.id",session, proxy.gson.toJson(id),java.lang.String.class);
      return rValue;
}
/**
*更新玩家超级粉丝信息
*/
public void updatePlayerPay (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.CreatePlayerReq req ) throws GameException{
     proxy.execute("p.update",session, proxy.gson.toJson(req),void.class);
    
}
/**
*取得玩家信息
*/
public com.pengpeng.stargame.vo.role.PlayerVO getPlayerInfo (com.pengpeng.stargame.rpc.Session session, java.lang.String pid ) throws GameException{
      com.pengpeng.stargame.vo.role.PlayerVO  rValue=  proxy.execute("p.get.info",session, proxy.gson.toJson(pid),com.pengpeng.stargame.vo.role.PlayerVO.class);
      return rValue;
}
/**
*公测期间每日领取达人币
*/
public com.pengpeng.stargame.vo.RewardVO claim (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.RewardVO  rValue=  proxy.execute("p.claim",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.RewardVO.class);
      return rValue;
}
/**
*退出登录
*/
public void logout (com.pengpeng.stargame.rpc.Session session, java.lang.String pid ) throws GameException{
     proxy.execute("p.logout",session, proxy.gson.toJson(pid),void.class);
    
}
/**
*玩家进入游戏
*/
public com.pengpeng.stargame.vo.role.PlayerVO enterGame (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.PlayerReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.PlayerVO  rValue=  proxy.execute("p.enter",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.PlayerVO.class);
      return rValue;
}
/**
*玩家进入游戏之后
*/
public void afterEnter (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
     proxy.execute("p.enter.after",session, proxy.gson.toJson(req),void.class);
    
}
/**
*角色改名
*/
public void rename (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.RoleReq req ) throws GameException{
     proxy.execute("p.rename",session, proxy.gson.toJson(req),void.class);
    
}

}