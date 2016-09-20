package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class PlayerAlbumRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*取得相册列表
*/
public com.pengpeng.stargame.vo.role.album.AlbumVO[] getAlbumPage (com.pengpeng.stargame.rpc.Session albumProfile, com.pengpeng.stargame.vo.role.album.AlbumReq session ) throws GameException{
      com.pengpeng.stargame.vo.role.album.AlbumVO[]  rValue=  proxy.execute("p.getAlbumPage",albumProfile, proxy.gson.toJson(session),com.pengpeng.stargame.vo.role.album.AlbumVO[].class);
      return rValue;
}
/**
*取得相册相片列表
*/
public com.pengpeng.stargame.vo.role.album.AlbumItemVO[] getAlbumItemPage (com.pengpeng.stargame.rpc.Session albumProfile, com.pengpeng.stargame.vo.role.album.AlbumReq session ) throws GameException{
      com.pengpeng.stargame.vo.role.album.AlbumItemVO[]  rValue=  proxy.execute("p.getAlbumItemPage",albumProfile, proxy.gson.toJson(session),com.pengpeng.stargame.vo.role.album.AlbumItemVO[].class);
      return rValue;
}

}