package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.FamilyAssistantRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SmallGameRpcRemote;
import com.pengpeng.stargame.vo.piazza.ApplicationVO;
import com.pengpeng.stargame.vo.piazza.FamilyAssistantReq;
import com.pengpeng.stargame.vo.piazza.FamilyAssistantVO;
import com.pengpeng.stargame.vo.smallgame.SmallGameListVO;
import com.pengpeng.stargame.vo.smallgame.SmallGameRankVO;
import com.pengpeng.stargame.vo.smallgame.SmallGameReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author james
 * @since 13-6-13 下午3:53
 */
@Component
public class FamilyAssistantCmd extends AbstractHandler {

	@Autowired
	private FamilyAssistantRpcRemote familyAssistantRpcRemote;

	@CmdAnnotation(cmd="family.assistant.info",name="助理信息",vo= FamilyAssistantVO.class,req=FamilyAssistantReq.class)
	public Response info(Session session,FamilyAssistantReq req) throws GameException {
        return Response.newObject(familyAssistantRpcRemote.getInfo(session, req));
	}

    @CmdAnnotation(cmd="family.assistant.apply",name="申请报名",vo= ApplicationVO.class,req=FamilyAssistantReq.class)
    public Response apply(Session session,FamilyAssistantReq req) throws GameException {
        return Response.newObject(familyAssistantRpcRemote.apply(session, req));
    }


}
