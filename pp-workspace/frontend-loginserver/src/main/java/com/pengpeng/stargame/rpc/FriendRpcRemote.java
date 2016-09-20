package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FriendRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*取得游戏内好友信息
*/
public com.pengpeng.stargame.vo.role.PlayerVO getFriendInfo (com.pengpeng.stargame.rpc.Session session, java.lang.String friendId ) throws GameException{
      com.pengpeng.stargame.vo.role.PlayerVO  rValue=  proxy.execute("friend.get.info",session, proxy.gson.toJson(friendId),com.pengpeng.stargame.vo.role.PlayerVO.class);
      return rValue;
}
/**
*取得游戏内好友列表
*/
public com.pengpeng.stargame.vo.role.FriendPage getListFriend (com.pengpeng.stargame.rpc.Session p, com.pengpeng.stargame.vo.role.PageReq vo ) throws GameException{
      com.pengpeng.stargame.vo.role.FriendPage  rValue=  proxy.execute("friend.get.list",p, proxy.gson.toJson(vo),com.pengpeng.stargame.vo.role.FriendPage.class);
      return rValue;
}
/**
*取得网站好友
*/
public com.pengpeng.stargame.vo.role.FriendVO[] getPPListFriend (com.pengpeng.stargame.rpc.Session id, java.lang.String item ) throws GameException{
      com.pengpeng.stargame.vo.role.FriendVO[]  rValue=  proxy.execute("friend.get.site",id, proxy.gson.toJson(item),com.pengpeng.stargame.vo.role.FriendVO[].class);
      return rValue;
}
/**
*导入好友
*/
public void addBatchFriend (com.pengpeng.stargame.rpc.Session friend, com.pengpeng.stargame.vo.IdReq s ) throws GameException{
     proxy.execute("friend.import",friend, proxy.gson.toJson(s),void.class);
    
}
/**
*审核好友
*/
public void auditFriend (com.pengpeng.stargame.rpc.Session friendMsg, com.pengpeng.stargame.vo.role.AuditReq vo ) throws GameException{
     proxy.execute("friend.audit",friendMsg, proxy.gson.toJson(vo),void.class);
    
}
/**
*批量审核好友
*/
public void auditBatchFriend (com.pengpeng.stargame.rpc.Session friendPlayer, com.pengpeng.stargame.vo.role.AuditReq msg ) throws GameException{
     proxy.execute("friend.audit.batch",friendPlayer, proxy.gson.toJson(msg),void.class);
    
}
/**
*删除好友
*/
public void deleteFriend (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.AuditReq auditReq ) throws GameException{
     proxy.execute("friend.del",session, proxy.gson.toJson(auditReq),void.class);
    
}
/**
*是否有好友需要审核
*/
public boolean applyFriend (com.pengpeng.stargame.rpc.Session session, java.lang.String pid ) throws GameException{
      boolean  rValue=  proxy.execute("friend.apply",session, proxy.gson.toJson(pid),boolean.class);
      return rValue;
}
/**
*取得需要审核的申请列表
*/
public com.pengpeng.stargame.vo.role.FriendAuditVO[] getAuditFriend (com.pengpeng.stargame.rpc.Session i, java.lang.String session ) throws GameException{
      com.pengpeng.stargame.vo.role.FriendAuditVO[]  rValue=  proxy.execute("friend.get.audit",i, proxy.gson.toJson(session),com.pengpeng.stargame.vo.role.FriendAuditVO[].class);
      return rValue;
}
/**
*添加好友
*/
public void addFriend (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.IdReq req ) throws GameException{
     proxy.execute("friend.add",session, proxy.gson.toJson(req),void.class);
    
}

}