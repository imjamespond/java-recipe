package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.LotteryRpcRemote;
import com.pengpeng.stargame.rpc.PlayerInfoRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.lottery.LotteryInfoVO;
import com.pengpeng.stargame.vo.lottery.LotteryReq;
import com.pengpeng.stargame.vo.lottery.LotteryVO;
import com.pengpeng.stargame.vo.role.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author james
 * @since 13-6-13 下午3:53
 */
@Component
public class PlayerInfoCmd extends AbstractHandler {

	@Autowired
	private PlayerInfoRpcRemote playerInfoRpcRemote;

	@CmdAnnotation(cmd="player.info",name="个人信息",vo=PlayerInfoVO.class,req=IdReq.class)
	public Response getPlayerInfo(Session session,IdReq req) throws GameException {
		return Response.newObject(playerInfoRpcRemote.getPlayerInfo(session, req));
	}

    @CmdAnnotation(cmd="player.district.update",name="更新地区信息",vo=PlayerInfoVO.class,req=PlayerDistrictReq.class)
    public Response updatePlayerDistrict(Session session,PlayerDistrictReq req) throws GameException {
        return Response.newObject(playerInfoRpcRemote.updatePlayerDistrict(session, req));

    }

    @CmdAnnotation(cmd="player.birth.update",name="更新生日信息",vo=PlayerInfoVO.class,req=PlayerBirthReq.class)
    public Response updatePlayerBirth(Session session,PlayerBirthReq req) throws GameException {
        return Response.newObject(playerInfoRpcRemote.updatePlayerBirth(session, req));
    }

    @CmdAnnotation(cmd="player.title.update",name="更新称号信息",vo=PlayerInfoVO.class,req=PlayerTitleReq.class)
    public Response updatePlayerTitle(Session session,PlayerTitleReq req) throws GameException {
        return Response.newObject(playerInfoRpcRemote.updatePlayerTitle(session, req));
    }

    @CmdAnnotation(cmd="player.signature.update",name="更新签名信息",vo=PlayerInfoVO.class,req=PlayerSignatureReq.class)
    public Response updatePlayerSignature(Session session,PlayerSignatureReq req) throws GameException {
        return Response.newObject(playerInfoRpcRemote.updatePlayerSignature(session, req));
    }
}
