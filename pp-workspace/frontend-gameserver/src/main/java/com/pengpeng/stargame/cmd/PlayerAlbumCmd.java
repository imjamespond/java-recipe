package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.PlayerAlbumRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.piazza.StarGiftReq;
import com.pengpeng.stargame.vo.role.album.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-12-3
 * Time: 下午3:02
 */
@Component
public class PlayerAlbumCmd extends AbstractHandler {
    @Autowired
    private PlayerAlbumRpcRemote albumRpcRemote;
    @CmdAnnotation(cmd = "p.getAlbumPage", req = AlbumReq.class, name = "请求 相册列表",vo=  AlbumVO[].class)
    public Response getAlbumPage(Session session, AlbumReq req) throws GameException {
        AlbumVO [] albumVOs=   albumRpcRemote.getAlbumPage(session,req);
       return   Response.newObject(albumVOs);
    }

    @CmdAnnotation(cmd = "p.getAlbumItemPage", req = AlbumReq.class, name = "请求 相册 里面的 相片列表",vo=  AlbumItemVO[].class)
    public Response getAlbumItemPage(Session session, AlbumReq req) throws GameException {
        AlbumItemVO [] albumVOs=   albumRpcRemote.getAlbumItemPage(session,req);
        return   Response.newObject(albumVOs);
    }
}
