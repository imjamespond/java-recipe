package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class MusicBoxRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*音乐盒歌曲列表
*/
public com.pengpeng.stargame.vo.piazza.MusicItemVO[] list (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.MusicItemVO[]  rValue=  proxy.execute("music.box.list",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.MusicItemVO[].class);
      return rValue;
}
/**
*赞-信息
*/
public com.pengpeng.stargame.vo.piazza.MusicZanVO zaninfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.MusicZanVO  rValue=  proxy.execute("music.box.zaninfo",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.MusicZanVO.class);
      return rValue;
}
/**
*音乐盒-赞
*/
public com.pengpeng.stargame.vo.piazza.MusicZanVO zan (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.IdReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.MusicZanVO  rValue=  proxy.execute("music.box.zan",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.MusicZanVO.class);
      return rValue;
}
/**
*音乐盒-详细
*/
public com.pengpeng.stargame.vo.piazza.MusicDetailVO detail (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.IdReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.MusicDetailVO  rValue=  proxy.execute("music.box.detail",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.MusicDetailVO.class);
      return rValue;
}

}