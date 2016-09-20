package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class PlayerInfoRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*取得人物信息
*/
public com.pengpeng.stargame.vo.role.PlayerInfoVO getPlayerInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.IdReq idReq ) throws GameException{
      com.pengpeng.stargame.vo.role.PlayerInfoVO  rValue=  proxy.execute("player.info",session, proxy.gson.toJson(idReq),com.pengpeng.stargame.vo.role.PlayerInfoVO.class);
      return rValue;
}
/**
*更新区域信息
*/
public com.pengpeng.stargame.vo.role.PlayerInfoVO updatePlayerDistrict (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.PlayerDistrictReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.PlayerInfoVO  rValue=  proxy.execute("player.district.update",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.PlayerInfoVO.class);
      return rValue;
}
/**
*更新称号信息
*/
public com.pengpeng.stargame.vo.role.PlayerInfoVO updatePlayerTitle (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.PlayerTitleReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.PlayerInfoVO  rValue=  proxy.execute("player.title.update",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.PlayerInfoVO.class);
      return rValue;
}
/**
*更新生日信息
*/
public com.pengpeng.stargame.vo.role.PlayerInfoVO updatePlayerBirth (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.PlayerBirthReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.PlayerInfoVO  rValue=  proxy.execute("player.birth.update",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.PlayerInfoVO.class);
      return rValue;
}
/**
*更新签名信息
*/
public com.pengpeng.stargame.vo.role.PlayerInfoVO updatePlayerSignature (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.PlayerSignatureReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.PlayerInfoVO  rValue=  proxy.execute("player.signature.update",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.PlayerInfoVO.class);
      return rValue;
}

}