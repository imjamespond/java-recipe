package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.FarmQinMaRpcRemote;
import com.pengpeng.stargame.rpc.FarmRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.farm.FarmIdReq;
import com.pengpeng.stargame.vo.farm.FarmVO;
import com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-8-13
 * Time: 下午4:47
 */
@Component
public class FarmQinMaCmd extends AbstractHandler {

    @Autowired
    private FarmQinMaRpcRemote farmQinMaRpcRemote;
    @CmdAnnotation(cmd="farm.qinma.info",name="取得 亲妈 的 农场信息",vo=FarmVO.class,req=FarmIdReq.class)
    public Response getFarm(Session session,FarmIdReq req) throws GameException {
        FarmVO vo = farmQinMaRpcRemote.getFarmInfo(session,req);
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd="farm.qinma.comment",name="评价亲妈的 农场",vo=void.class,req=FarmIdReq.class)
    public Response commentFarm(Session session,FarmIdReq req) throws GameException {
        farmQinMaRpcRemote.evaluation(session,req);
        return Response.newOK();
    }

    @CmdAnnotation(cmd = "farm.qinma.decorate.info", req = FarmIdReq.class, name = "取得亲妈农场装饰信息",vo= FarmDecorateVO.class)
    public Response getFarmDecorateInfo(Session session, FarmIdReq req) throws GameException {
      FarmDecorateVO farmDecorateVO=  farmQinMaRpcRemote.getFarmDecorateInfo(session,req);
        return Response.newObject(farmDecorateVO);
    }


}
