package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class RoomQinMaRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*评价 亲妈 房间
*/
public com.pengpeng.stargame.vo.room.RoomVO evaluation (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.room.RoomIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.room.RoomVO  rValue=  proxy.execute("room.qinma.evaluation",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.room.RoomVO.class);
      return rValue;
}
/**
*取得 亲妈 房间信息
*/
public com.pengpeng.stargame.vo.room.RoomVO getRoomInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.room.RoomIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.room.RoomVO  rValue=  proxy.execute("room.qinma.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.room.RoomVO.class);
      return rValue;
}

}