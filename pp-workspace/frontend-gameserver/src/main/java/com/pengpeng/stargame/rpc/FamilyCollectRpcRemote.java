package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FamilyCollectRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*收集面板信息
*/
public com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectInfoVO collectInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectInfoVO  rValue=  proxy.execute("family.collect.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectInfoVO.class);
      return rValue;
}
/**
*捐献物品
*/
public com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectInfoVO donate (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectInfoVO  rValue=  proxy.execute("family.collect.donate",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectInfoVO.class);
      return rValue;
}
/**
*本族排行
*/
public com.pengpeng.stargame.vo.piazza.collectcrop.MemberColletPageVO memberrank (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.collectcrop.MemberColletPageVO  rValue=  proxy.execute("family.collect.memberrank",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.collectcrop.MemberColletPageVO.class);
      return rValue;
}
/**
*家族排行
*/
public com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectRankVO[] familyrank (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectRankVO[]  rValue=  proxy.execute("family.collect.familyrank",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectRankVO[].class);
      return rValue;
}

}