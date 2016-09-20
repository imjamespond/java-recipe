package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FamilyManagerRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*欢迎家族成员
*/
public void welcome (com.pengpeng.stargame.rpc.Session family, com.pengpeng.stargame.vo.piazza.FamilyReq channelId ) throws GameException{
     proxy.execute("family.welcome",family, proxy.gson.toJson(channelId),void.class);
    
}
/**
*取得家族成员信息
*/
public com.pengpeng.stargame.vo.piazza.FamilyMemberVO getMembers (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.FamilyMemberVO  rValue=  proxy.execute("family.get.member",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.FamilyMemberVO.class);
      return rValue;
}
/**
*退出家族
*/
public void removeMember (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
     proxy.execute("family.remove.member",session, proxy.gson.toJson(req),void.class);
    
}
/**
*取得家族成员列表
*/
public com.pengpeng.stargame.vo.piazza.FamilyMemberPageVO listMembers (com.pengpeng.stargame.rpc.Session player, com.pengpeng.stargame.vo.piazza.FamilyReq bi ) throws GameException{
      com.pengpeng.stargame.vo.piazza.FamilyMemberPageVO  rValue=  proxy.execute("family.list.member",player, proxy.gson.toJson(bi),com.pengpeng.stargame.vo.piazza.FamilyMemberPageVO.class);
      return rValue;
}
/**
*变更家族
*/
public com.pengpeng.stargame.vo.piazza.FamilyInfoVO changeFamily (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.FamilyInfoVO  rValue=  proxy.execute("family.change",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.FamilyInfoVO.class);
      return rValue;
}
/**
*捐献经费
*/
public com.pengpeng.stargame.vo.RewardVO endow (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      com.pengpeng.stargame.vo.RewardVO  rValue=  proxy.execute("family.endow",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.RewardVO.class);
      return rValue;
}
/**
*取得家族贡献比例
*/
public com.pengpeng.stargame.vo.piazza.FamilyDevoteVO getDevoteInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.FamilyDevoteVO  rValue=  proxy.execute("family.get.devote",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.FamilyDevoteVO.class);
      return rValue;
}
/**
*领取家族福利
*/
public int getBoon (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      int  rValue=  proxy.execute("family.receive.boon",session, proxy.gson.toJson(req),int.class);
      return rValue;
}
/**
*变更家族成员身份
*/
public com.pengpeng.stargame.vo.piazza.FamilyMemberVO changeIdentity (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.FamilyMemberVO  rValue=  proxy.execute("family.change.identity",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.FamilyMemberVO.class);
      return rValue;
}
/**
*加入家族
*/
public com.pengpeng.stargame.vo.piazza.FamilyInfoVO join (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.FamilyInfoVO  rValue=  proxy.execute("family.join",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.FamilyInfoVO.class);
      return rValue;
}

}