package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.container.ISessionContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.rpc.FriendRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.role.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FriendCmd extends AbstractHandler{

	private static final Logger logger = Logger.getLogger(FriendCmd.class);

	@Autowired
	private PlayerRpcRemote playerService;

	@Autowired
	private FriendRpcRemote friendService;

	@Autowired
	private StatusRemote statusService;
	@Autowired
	private ISessionContainer container;

    @Autowired
    private IExceptionFactory exceptionFactory;

	@CmdAnnotation(cmd="friend.get.info",name="取得游戏内好友信息",vo= PlayerVO.class,req=IdReq.class)
	public Response getFriendInfo(Session session,IdReq req) throws GameException {
		PlayerVO player = playerService.getPlayerInfo(null,req.getId());
		return Response.newObject(player);
	}

	@CmdAnnotation(cmd="friend.get.list",name="取得游戏内好友列表",vo= FriendPage.class,req=PageReq.class)
	public Response getListFriend(Session session,PageReq pageReq) throws GameException {
		FriendPage page = friendService.getListFriend(session, pageReq);
        return Response.newObject(page);
	}

	@CmdAnnotation(cmd="friend.get.site",name="取得网站好友",vo= FriendVO.class,req=CommonReq.class)
	public Response getPPListFriend(Session session,CommonReq commonReq) throws GameException {
		FriendVO[] list = friendService.getPPListFriend(session,session.getPid());
		return Response.newObject(list);
	}

	@CmdAnnotation(cmd="friend.import",name="导入好友",req=IdReq.class)
	public Response addBatchFriend(Session session,IdReq idReq) throws GameException {
	    friendService.addBatchFriend(session,idReq);
		return Response.newOK();
	}

	@CmdAnnotation(cmd="friend.audit",name="审核好友",req=AuditReq.class)
	public Response auditFriend(Session session, AuditReq auditReq) throws GameException {
	    friendService.auditFriend(session, auditReq);
		return Response.newOK();
	}

    @CmdAnnotation(cmd="friend.add",name="添加好友",req=IdReq.class)
    public Response addFriend(Session session, IdReq req) throws GameException {
        friendService.addFriend(session, req);
        return Response.newOK();
    }

    @CmdAnnotation(cmd="friend.audit.batch",name="批量审核好友",req=AuditReq.class)
	public Response auditBatchFriend(Session session, AuditReq auditReq) throws GameException {
		friendService.auditBatchFriend(session, auditReq);
		return Response.newOK();
	}

	@CmdAnnotation(cmd="friend.del",name="删除好友",req=AuditReq.class)
	public Response deleteFriend(Session session,AuditReq auditReq) throws GameException {
		friendService.deleteFriend(session, auditReq);
		return Response.newOK();
	}

	@CmdAnnotation(cmd="friend.apply",name="是否有好友需要审核",vo= Boolean.class,req=CommonReq.class)
	public Response applyFriend(Session session,CommonReq commonReq) throws GameException {
		boolean  flag = friendService.applyFriend(session,session.getPid());
		return Response.newObject(flag);
	}

    @CmdAnnotation(cmd="friend.get.audit",name="取得需要审核的好友别表",vo= FriendAuditVO[].class,req=CommonReq.class)
    public Response getAuditFriend(Session session,CommonReq commonReq) throws GameException {
        FriendAuditVO[]  list = friendService.getAuditFriend(session,session.getPid());
        return Response.newObject(list);
    }

    @CmdAnnotation(cmd="friend.top",name="好友顶置",req=IdReq.class)
    public Response topFriend(Session session,IdReq req) throws GameException {
        friendService.topFriend(session, req);
        return Response.newOK();
    }
    @CmdAnnotation(cmd="friend.cancel.top",name="取消好友顶置",req=IdReq.class)
    public Response cancelTopFriend(Session session,IdReq req) throws GameException {
        friendService.cancelTopFriend(session, req);
        return Response.newOK();
    }
}
