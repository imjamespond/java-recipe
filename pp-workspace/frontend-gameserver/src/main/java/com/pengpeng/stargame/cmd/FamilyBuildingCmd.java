package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.FamilyBuildingRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.piazza.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 家族建筑
 * User: mql
 * Date: 13-6-27
 * Time: 下午2:48
 */
@Component
public class FamilyBuildingCmd extends AbstractHandler {

    @Autowired
    private FamilyBuildingRpcRemote familyBuildingRpcRemote;
    @CmdAnnotation(cmd = "familyinfo.getBuilds", req = FamilyReq.class, name = "获取建筑列表",vo=  BuildVO [].class)
    public Response getBuilds(Session session, FamilyReq req) throws GameException {
        BuildVO[] vos = familyBuildingRpcRemote.getBuilds(session,req);
        return  Response.newObject(vos);
    }

    @CmdAnnotation(cmd = "familyinfo.upgradeBuild", req = FamilyReq.class, name = "升级建筑",vo=  BuildVO[].class)
    public Response upgradeBuild(Session session, FamilyReq req) throws GameException {
        BuildVO[] vos = familyBuildingRpcRemote.upgradeBuild(session,req);
        return  Response.newObject(vos);
    }

}
