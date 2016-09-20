package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.QinmaSceneRpcRemote;
import com.pengpeng.stargame.rpc.RoomQinMaRpcRemote;
import com.pengpeng.stargame.rpc.RoomRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.map.MapReq;
import com.pengpeng.stargame.vo.map.ScenceVO;
import com.pengpeng.stargame.vo.room.RoomIdReq;
import com.pengpeng.stargame.vo.room.RoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-8-13
 * Time: 下午4:49
 */
@Component
public class RoomQinMaCmd extends AbstractHandler {

    @Autowired
    private RoomQinMaRpcRemote roomQinMaRpcRemote;

    @Autowired
    private QinmaSceneRpcRemote qinmaSceneRpcRemote;
    @CmdAnnotation(cmd = "room.enterQinmaRoom", req = RoomIdReq.class, name = "进入亲妈房间消息",vo= RoomVO.class)
    public Response enterFriendRoom(Session session,RoomIdReq req) throws GameException {
        RoomVO roomVO=roomQinMaRpcRemote.getRoomInfo(session, req);
        return  Response.newObject(roomVO);
    }

    @CmdAnnotation(cmd="room.qinma.comment",name="评价 亲妈的 房间",vo=RoomVO.class,req=RoomIdReq.class)
    public Response commentFarm(Session session,RoomIdReq req) throws GameException {
        RoomVO roomVO=roomQinMaRpcRemote.evaluation(session,req);
        return Response.newObject(roomVO);
    }


    @CmdAnnotation(cmd="qinma.scene.enter",name="进入亲妈指定场景(房间map_021,农场map_011)",vo=RoomVO.class,req=RoomIdReq.class)
    public Response qinmaEnterScene(Session session,MapReq req) throws GameException {
        ScenceVO vo = qinmaSceneRpcRemote.enterFarm(session,req);
        return Response.newObject(vo);
    }

}
