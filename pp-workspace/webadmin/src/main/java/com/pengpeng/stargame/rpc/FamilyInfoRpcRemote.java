package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FamilyInfoRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*修改家族信息
*/
public com.pengpeng.stargame.vo.piazza.FamilyPanelVO alter (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.FamilyPanelVO  rValue=  proxy.execute("family.alter",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.FamilyPanelVO.class);
      return rValue;
}
/**
*家族列表
*/
public com.pengpeng.stargame.vo.piazza.FamilyPageVO getFamilyList (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.FamilyPageVO  rValue=  proxy.execute("family.get.list",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.FamilyPageVO.class);
      return rValue;
}
/**
*取得家族资料
*/
public com.pengpeng.stargame.vo.piazza.FamilyInfoVO getFamilyById (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.FamilyInfoVO  rValue=  proxy.execute("family.get",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.FamilyInfoVO.class);
      return rValue;
}
/**
*获取家族面板信息
*/
public com.pengpeng.stargame.vo.piazza.FamilyPanelVO getPanel (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.FamilyPanelVO  rValue=  proxy.execute("family.get.panel",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.FamilyPanelVO.class);
      return rValue;
}

}