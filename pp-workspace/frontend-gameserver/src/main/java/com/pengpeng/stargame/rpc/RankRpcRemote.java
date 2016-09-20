package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class RankRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*排行榜信息
*/
public com.pengpeng.stargame.vo.rank.RankTypeVO rankInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.rank.RankReq req ) throws GameException{
      com.pengpeng.stargame.vo.rank.RankTypeVO  rValue=  proxy.execute("rank.rankInfo",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.rank.RankTypeVO.class);
      return rValue;
}

}