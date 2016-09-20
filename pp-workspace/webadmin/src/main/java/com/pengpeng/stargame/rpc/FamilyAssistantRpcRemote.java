package com.pengpeng.stargame.rpc;

import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class FamilyAssistantRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*申请列表
*/
public com.pengpeng.stargame.vo.piazza.ApplicationListVO list (Session fa, com.pengpeng.stargame.vo.piazza.FamilyAssistantReq i ) throws GameException{
      com.pengpeng.stargame.vo.piazza.ApplicationListVO  rValue=  proxy.execute("family.assistant.list",fa, proxy.gson.toJson(i),com.pengpeng.stargame.vo.piazza.ApplicationListVO.class);
      return rValue;
}
/**
*清除申请
*/
public com.pengpeng.stargame.vo.piazza.ApplicationVO clean (Session session, com.pengpeng.stargame.vo.piazza.FamilyAssistantReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.ApplicationVO  rValue=  proxy.execute("family.assistant.clean",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.ApplicationVO.class);
      return rValue;
}
/**
*助理信息
*/
public com.pengpeng.stargame.vo.piazza.FamilyAssistantVO getInfo (Session assistant, com.pengpeng.stargame.vo.piazza.FamilyAssistantReq id ) throws GameException{
      com.pengpeng.stargame.vo.piazza.FamilyAssistantVO  rValue=  proxy.execute("family.assistant.info",assistant, proxy.gson.toJson(id),com.pengpeng.stargame.vo.piazza.FamilyAssistantVO.class);
      return rValue;
}
/**
*申请助理
*/
public com.pengpeng.stargame.vo.piazza.ApplicationVO apply (Session session, com.pengpeng.stargame.vo.piazza.FamilyAssistantReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.ApplicationVO  rValue=  proxy.execute("family.assistant.apply",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.ApplicationVO.class);
      return rValue;
}
/**
*批准助理
*/
public com.pengpeng.stargame.vo.piazza.ApplicationVO approve (Session session, com.pengpeng.stargame.vo.piazza.FamilyAssistantReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.ApplicationVO  rValue=  proxy.execute("family.assistant.approve",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.ApplicationVO.class);
      return rValue;
}
/**
*指定助理
*/
public com.pengpeng.stargame.vo.piazza.ApplicationVO designate (Session playerSession, com.pengpeng.stargame.vo.piazza.FamilyAssistantReq pid ) throws GameException{
      com.pengpeng.stargame.vo.piazza.ApplicationVO  rValue=  proxy.execute("family.assistant.designate",playerSession, proxy.gson.toJson(pid),com.pengpeng.stargame.vo.piazza.ApplicationVO.class);
      return rValue;
}

}