package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.container.ISessionContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.SceneRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.map.MapReq;
import com.pengpeng.stargame.vo.map.MoveReq;
import com.pengpeng.stargame.vo.map.MoveVO;
import com.pengpeng.stargame.vo.map.ScenceVO;
import com.pengpeng.stargame.vo.role.PlayerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 场景
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-23 下午4:41
 */
@Component
public class MapCmd extends AbstractHandler {

    @Autowired
	private ISessionContainer container;

    @Autowired
    private SceneRpcRemote sceneService;

    @Autowired
    private StatusRemote statusRemote;

    @CmdAnnotation(cmd="scene.enter",vo=ScenceVO.class,req=MapReq.class,name="进入当前地图")
    public Response enterScene(Session session,MapReq req) throws GameException {

        ScenceVO vo = sceneService.enterScene(session,req);

        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd="scene.to",vo=ScenceVO.class,req=MapReq.class,name="指定地图和坐标点")
    public Response enterTo(Session session,MapReq req) throws GameException {

        sceneService.enterTo(session,req);
        session.addParam("x",String.valueOf(req.getX()));
        session.addParam("y", String.valueOf(req.getY()));
        return Response.newOK();
    }

    @CmdAnnotation(cmd="scene.get.info",vo=ScenceVO.class,req=IdReq.class,name="取得地图信息")
    public Response getSceneInfo(Session session,IdReq req) throws GameException {
        ScenceVO vo = sceneService.getScene(session,req.getId());
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd="scene.move",vo=void.class,req= MoveReq.class,name="移动")
    public Response move(Session session,MoveReq req) throws GameException {
        MoveVO vo = sceneService.move(session, req);
//        session.addParam("x",String.valueOf(req.getX()));
//        session.addParam("y", String.valueOf(req.getY()));
        return Response.newObject(vo);
    }
}
