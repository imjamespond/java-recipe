package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FashionGiftRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*赠送礼物
*/
public int give (com.pengpeng.stargame.rpc.Session id, com.pengpeng.stargame.vo.role.GiftReq arr$ ) throws GameException{
      int  rValue=  proxy.execute("fashion.gift.give",id, proxy.gson.toJson(arr$),int.class);
      return rValue;
}
/**
*取得礼物列表
*/
public com.pengpeng.stargame.vo.role.GiftVO[] listInfo (com.pengpeng.stargame.rpc.Session gift, com.pengpeng.stargame.vo.CommonReq session ) throws GameException{
      com.pengpeng.stargame.vo.role.GiftVO[]  rValue=  proxy.execute("fashion.gift.list.info",gift, proxy.gson.toJson(session),com.pengpeng.stargame.vo.role.GiftVO[].class);
      return rValue;
}
/**
*忽略(拒绝)好友的礼物
*/
public void reject (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.GiftReq req ) throws GameException{
     proxy.execute("fashion.gift.reject",session, proxy.gson.toJson(req),void.class);
    
}
/**
*取得未赠送礼物的好友
*/
public com.pengpeng.stargame.vo.role.FriendVO[] getFriend (com.pengpeng.stargame.rpc.Session vo, com.pengpeng.stargame.vo.CommonReq gp ) throws GameException{
      com.pengpeng.stargame.vo.role.FriendVO[]  rValue=  proxy.execute("fashion.gift.get.friend",vo, proxy.gson.toJson(gp),com.pengpeng.stargame.vo.role.FriendVO[].class);
      return rValue;
}
/**
*取得未领取的礼物总数
*/
public int untreated (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      int  rValue=  proxy.execute("fashion.gift.untreated",session, proxy.gson.toJson(req),int.class);
      return rValue;
}
/**
*礼物还可以赠送多少次
*/
public int giveCount (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      int  rValue=  proxy.execute("fashion.gift.giveCount",session, proxy.gson.toJson(req),int.class);
      return rValue;
}
/**
*领取(接受)礼物
*/
public void accept (com.pengpeng.stargame.rpc.Session fid, com.pengpeng.stargame.vo.role.GiftReq arr$ ) throws GameException{
     proxy.execute("fashion.gift.accept",fid, proxy.gson.toJson(arr$),void.class);
    
}

}