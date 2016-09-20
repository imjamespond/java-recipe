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
public int give (com.pengpeng.stargame.rpc.Session s, com.pengpeng.stargame.vo.role.GiftReq id ) throws GameException{
      int  rValue=  proxy.execute("fashion.gift.give",s, proxy.gson.toJson(id),int.class);
      return rValue;
}
/**
*取得好友
*/
public com.pengpeng.stargame.vo.role.FriendVO[] getFriend2 (com.pengpeng.stargame.rpc.Session vo, com.pengpeng.stargame.vo.CommonReq item ) throws GameException{
      com.pengpeng.stargame.vo.role.FriendVO[]  rValue=  proxy.execute("fashion.get.friend",vo, proxy.gson.toJson(item),com.pengpeng.stargame.vo.role.FriendVO[].class);
      return rValue;
}

}