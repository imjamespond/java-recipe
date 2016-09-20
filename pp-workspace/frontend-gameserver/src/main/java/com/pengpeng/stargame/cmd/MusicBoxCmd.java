package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.MusicBoxRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.piazza.MusicDetailVO;
import com.pengpeng.stargame.vo.piazza.MusicItemVO;
import com.pengpeng.stargame.vo.piazza.MusicZanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-12-5下午3:58
 */
@Component
public class MusicBoxCmd extends AbstractHandler {

    @Autowired
    private MusicBoxRpcRemote musicBoxRpcRemote;

    @CmdAnnotation(cmd="music.box.list",name="音乐盒列表",vo=MusicItemVO.class,req=CommonReq.class)
    public Response list(Session session,CommonReq req) throws GameException {
        return Response.newObject(musicBoxRpcRemote.list(session,req));
    }
    @CmdAnnotation(cmd="music.box.zaninfo",name="赞-信息",vo=void.class,req=CommonReq.class)
    public Response zaninfo(Session session,CommonReq req) throws GameException {
          MusicZanVO vo = musicBoxRpcRemote.zaninfo(session,req);
        return Response.newObject(vo);
    }
    @CmdAnnotation(cmd="music.box.zan",name="音乐盒-赞",vo=void.class,req=IdReq.class)
    public Response zan(Session session,IdReq req) throws GameException {
        MusicZanVO vo = musicBoxRpcRemote.zan(session, req);
        return Response.newObject(vo);
    }
    @CmdAnnotation(cmd="music.box.detail",name="音乐盒详细",vo=MusicDetailVO.class,req=IdReq.class)
    public Response detail(Session session,IdReq req) throws GameException {
        MusicDetailVO vo = musicBoxRpcRemote.detail(session, req);
        return Response.newObject(vo);
    }
}
