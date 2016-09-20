package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class RoomRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*评价好友房间
*/
public com.pengpeng.stargame.vo.room.RoomVO evaluation (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.room.RoomIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.room.RoomVO  rValue=  proxy.execute("room.evaluation",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.room.RoomVO.class);
      return rValue;
}
/**
*取得物品详细信息
*/
public com.pengpeng.stargame.vo.room.RoomShopItemVO getItemTip (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.room.RoomIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.room.RoomShopItemVO  rValue=  proxy.execute("room.item.tip",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.room.RoomShopItemVO.class);
      return rValue;
}
/**
*取得房间信息
*/
public com.pengpeng.stargame.vo.room.RoomVO getRoomInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.room.RoomIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.room.RoomVO  rValue=  proxy.execute("room.get.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.room.RoomVO.class);
      return rValue;
}
/**
*取得房间内商品 列表
*/
public com.pengpeng.stargame.vo.room.RoomShopItemVO[] getRoomshop (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.room.RoomIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.room.RoomShopItemVO[]  rValue=  proxy.execute("room.shopList",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.room.RoomShopItemVO[].class);
      return rValue;
}
/**
*取得房间内闲置的物品列表
*/
public com.pengpeng.stargame.vo.room.RoomPkgVO getRoomItemList (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.room.RoomIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.room.RoomPkgVO  rValue=  proxy.execute("room.myList",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.room.RoomPkgVO.class);
      return rValue;
}
/**
*购买
*/
public void buy (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.room.RoomIdReq req ) throws GameException{
     proxy.execute("room.buy",session, proxy.gson.toJson(req),void.class);
    
}
/**
*卖出
*/
public void sale (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.room.RoomIdReq req ) throws GameException{
     proxy.execute("room.sale",session, proxy.gson.toJson(req),void.class);
    
}
/**
*进入好友房间
*/
public com.pengpeng.stargame.vo.room.RoomVO enterFriendRoom (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.room.RoomIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.room.RoomVO  rValue=  proxy.execute("room.enterFriendRoom",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.room.RoomVO.class);
      return rValue;
}
/**
*编辑房间 保存
*/
public com.pengpeng.stargame.vo.room.RoomVO save (com.pengpeng.stargame.rpc.Session arr$, com.pengpeng.stargame.vo.room.RoomIdReq len$ ) throws GameException{
      com.pengpeng.stargame.vo.room.RoomVO  rValue=  proxy.execute("room.save",arr$, proxy.gson.toJson(len$),com.pengpeng.stargame.vo.room.RoomVO.class);
      return rValue;
}

}