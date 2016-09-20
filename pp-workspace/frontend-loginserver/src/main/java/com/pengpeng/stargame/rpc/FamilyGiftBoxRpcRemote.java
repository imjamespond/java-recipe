package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FamilyGiftBoxRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*送礼时候 礼物列表
*/
public com.pengpeng.stargame.vo.piazza.StarSendPageVO giftList (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.StarGiftReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.StarSendPageVO  rValue=  proxy.execute("family.gift.giftList",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.StarSendPageVO.class);
      return rValue;
}
/**
*送礼
*/
public void sendGift (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.StarGiftReq req ) throws GameException{
     proxy.execute("family.gift.sendGift",session, proxy.gson.toJson(req),void.class);
    
}
/**
*礼物盒界面
*/
public com.pengpeng.stargame.vo.piazza.StarGiftPageVO getInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.StarGiftReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.StarGiftPageVO  rValue=  proxy.execute("family.gift.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.StarGiftPageVO.class);
      return rValue;
}

}