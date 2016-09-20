package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class GiftBagRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*打开礼包
*/
public com.pengpeng.stargame.vo.RewardVO getGiftBag (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.giftBag.GiftBagReq req ) throws GameException{
      com.pengpeng.stargame.vo.RewardVO  rValue=  proxy.execute("giftbag.open",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.RewardVO.class);
      return rValue;
}

}