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
public String createPlayer (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.CreatePlayerReq req ) throws GameException{
      String  rValue=  proxy.execute("p.create",session, proxy.gson.toJson(req),String.class);
      return rValue;
}
/**
*取得playerid
*/
public String getPid (com.pengpeng.stargame.rpc.Session session, Integer id ) throws GameException{
      String  rValue=  proxy.execute("p.get.id",session, proxy.gson.toJson(id),String.class);
      return rValue;
}
/**
*退出登录
*/
public void logout (com.pengpeng.stargame.rpc.Session session, String pid ) throws GameException{
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
*取得玩家信息
*/
public com.pengpeng.stargame.vo.role.PlayerVO getPlayerInfo (com.pengpeng.stargame.rpc.Session session, String pid ) throws GameException{
      com.pengpeng.stargame.vo.role.PlayerVO  rValue=  proxy.execute("p.get.info",session, proxy.gson.toJson(pid),com.pengpeng.stargame.vo.role.PlayerVO.class);
      return rValue;
}
/**
*充值,加达人币
*/
public void charge (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.ChargeReq req ) throws GameException{
     proxy.execute("p.charge",session, proxy.gson.toJson(req),void.class);
    
}
/**
*角色改名
*/
public void rename (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.RoleReq req ) throws GameException{
     proxy.execute("p.rename",session, proxy.gson.toJson(req),void.class);
    
}

}