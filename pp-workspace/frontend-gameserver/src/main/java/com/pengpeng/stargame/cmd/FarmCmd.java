package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.container.ISessionContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.farm.FarmIdReq;
import com.pengpeng.stargame.vo.farm.FarmSpeedVO;
import com.pengpeng.stargame.vo.farm.FarmStateVO;
import com.pengpeng.stargame.vo.farm.FarmVO;
import com.pengpeng.stargame.vo.map.MoveReq;
import com.pengpeng.stargame.vo.role.PlayerReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 农场
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-25 上午10:12
 */
@Component
public class FarmCmd extends AbstractHandler {

//	@Autowired
//	private PlayerRpcRemote playerService;

//	@Autowired
//	private StatusRemote statusService;

    @Autowired
    private FarmRpcRemote farmRpcRemote;

    @Autowired
    private SceneRpcRemote sceneRpcRemote;
//	@Autowired
//	private ISessionContainer container;

	@CmdAnnotation(cmd="farm.get",name="取得农场信息",vo=FarmVO.class,req=FarmIdReq.class)
	public Response getFarm(Session session,FarmIdReq req) throws GameException {
        FarmVO vo = farmRpcRemote.getFarmInfo(session, req);
		return Response.newObject(vo);
	}

	@CmdAnnotation(cmd="farm.get.friend",name="取得好友信息",vo=FarmVO.class,req=FarmIdReq.class)
	public Response getFriendFarm(Session session,FarmIdReq req) throws GameException {
        FarmVO vo = farmRpcRemote.getFrindInfo(session,req);
        return Response.newObject(vo);
	}

	@CmdAnnotation(cmd="farm.comment",name="评价好友农场（需求待定）",vo=void.class,req=FarmIdReq.class)
	public Response commentFarm(Session session,FarmIdReq req) throws GameException {
        farmRpcRemote.evaluation(session,req);
		return Response.newOK();
	}

    @CmdAnnotation(cmd="farm.up.level",name="农场升级",vo=void.class,req=FarmIdReq.class)
    public Response levelup(Session session,FarmIdReq req) throws GameException {
        farmRpcRemote.levelUp(session,req);
        return Response.newOK();
    }

    @CmdAnnotation(cmd="farm.plant",name="种植",vo=void.class,req=FarmIdReq.class)
    public Response plant(Session session,FarmIdReq req) throws GameException {
        farmRpcRemote.plant(session,req);
        return Response.newOK();
    }

    @CmdAnnotation(cmd="farm.harvest",name="收获作物",vo=void.class,req=FarmIdReq.class)
    public Response harvest(Session session,FarmIdReq req) throws GameException {
        farmRpcRemote.harvest(session,req);
        return Response.newOK();
    }

    @CmdAnnotation(cmd="farm.harvest.friend",name="帮助好友收获",vo=void.class,req=FarmIdReq.class)
    public Response harvestFriend(Session session,FarmIdReq req) throws GameException {
        farmRpcRemote.harvestFriend(session,req);
        return Response.newOK();
    }

    @CmdAnnotation(cmd="farm.get.state",name="取得好友的农场状态",vo=String[].class,req=IdReq.class)
    public Response friendState(Session session,IdReq req) throws GameException {
        FarmStateVO[] vos =farmRpcRemote.friendState(session, req);
        return Response.newObject(  vos);
    }

    @CmdAnnotation(cmd = "farm.eradicate", req = FarmIdReq.class, name = "铲除田地",vo=void.class)
    public Response eradicate(Session session, FarmIdReq req) throws GameException {
        farmRpcRemote.eradicate(session,req);
        return Response.newOK();
    }
    @CmdAnnotation(cmd = "farm.speedup", req = FarmIdReq.class, name = "加速生长",vo=String.class)
    public Response speedup(Session session, FarmIdReq req) throws GameException {
        FarmSpeedVO s= farmRpcRemote.speedup(session,req);
        return Response.newObject(s);

    }
    @CmdAnnotation(cmd = "farm.speedup.needGold", req = FarmIdReq.class, name = "直接催熟需要的达人币", vo = Integer.class)
    public Response speedupNeedGold(Session session, FarmIdReq req) throws GameException {
       int s=farmRpcRemote.speedupNeedGold(session,req);
        return Response.newObject(s);
    }


        @CmdAnnotation(cmd="farm.harvestAll",name="一键收获所有作物",vo=void.class,req=FarmIdReq.class)
    public Response harvestAll(Session session,FarmIdReq req) throws GameException {
        farmRpcRemote.harvestAll(session,req);
        return Response.newOK();
    }

    @CmdAnnotation(cmd = "farm.friend.harvestAll", req = FarmIdReq.class, name = "帮助好友一键收获所有作物")
    public Response friendHarvestAll(Session session, FarmIdReq req) throws GameException {
        farmRpcRemote.friendHarvestAll(session,req);
        return Response.newOK();
    }

}
