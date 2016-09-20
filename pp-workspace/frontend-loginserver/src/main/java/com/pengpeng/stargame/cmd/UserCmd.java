package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.dao.UserSecurityDao;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.managed.IManageService;
import com.pengpeng.stargame.managed.NodeInfo;
import com.pengpeng.stargame.managed.ServerType;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.rsp.RspServerFactory;
import com.pengpeng.stargame.vo.role.UserInfo;
import com.pengpeng.stargame.vo.role.CreatePlayerReq;
import com.pengpeng.stargame.vo.role.RoleReq;
import com.pengpeng.stargame.vo.role.ServerVO;
import com.pengpeng.stargame.vo.role.TokenReq;
import com.pengpeng.user.exception.AuthFailException;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserCmd extends AbstractHandler{

	private static final Logger logger = Logger.getLogger(UserCmd.class);

	// 逻辑服务器
    @Autowired
    private StatusRemote statusService;

	//状态服务器
	@Autowired
	private PlayerRpcRemote logicService;

    @Autowired
    private UserSecurityDao dao;

	private RspServerFactory rsp = new RspServerFactory();

	@Autowired
	private IManageService manageService;

	public UserCmd() {
	}

	@CmdAnnotation(cmd="p.create",channel=true,name="创建角色",req = RoleReq.class,vo=ServerVO.class)
	public Response createRole(ChannelHandlerContext context,RoleReq user){
		// 是否在主站已登录
        Object token = context.getAttachment();
        if(token == null){
            return Response.newError("未登录!");
        }

		// 创建角色
		String pid=null;
        if (token instanceof UserInfo){
            UserInfo userInfo = (UserInfo)token;
            try {
				// 开始创建
				CreatePlayerReq createPlayerReq = new CreatePlayerReq();
				createPlayerReq.setUserId(userInfo.getId());
				createPlayerReq.setSex(userInfo.getSex());
				createPlayerReq.setType(user.getRoleType());
				createPlayerReq.setNickName(user.getName());
                createPlayerReq.setStarId(userInfo.getStarId());
                createPlayerReq.setCity(userInfo.getCity());
                createPlayerReq.setProvince(userInfo.getProvince());
                createPlayerReq.setSignature(userInfo.getSignature());
                createPlayerReq.setBirthCity(userInfo.getBirthCity());
                createPlayerReq.setBirthProvince(userInfo.getBirthProvince());
                createPlayerReq.setBirthday(userInfo.getBirthday());
                if(userInfo.getPayMember()==null){
                    createPlayerReq.setPayMember(0);
                } else {
                    createPlayerReq.setPayMember(userInfo.getPayMember());
                }
				pid = logicService.createPlayer(null,createPlayerReq);
            } catch (Exception e) {
                logger.error("创建角色失败.",e);
				return Response.newError("创建角色失败");
            }
        }

		if(pid == null){
			return Response.newError("创建角色失败");
		}

		// server manager 返回服务IP 和端口, 以及playerId
		try {
			NodeInfo nodeInfo = manageService.getServerNode(ServerType.GAMESERVER);
            String loginToken= UUID.randomUUID().toString();
            statusService.addToken(null,loginToken);
			return rsp.newServerVoRsp(nodeInfo.getHost(),nodeInfo.getTcpPort(),pid,loginToken);
		} catch (Exception e) {
			logger.error(e);
		}
        return Response.newError("创建角色失败");
	}

	@CmdAnnotation(cmd="p.login",channel=true,name="登录",req =TokenReq.class,vo= ServerVO.class)
	public Response login(ChannelHandlerContext context ,TokenReq token) throws GameException {
		/*
			1.验证用户是否登录
			2.创建角色
			3.把已登录的连接踢下线
			4.返回服务IP 和端口, token , playerId
		*/
		// 是否在主站已登录
        UserInfo userInfo = null;
        try {
            userInfo = dao.getUserInfo(token.getTokenKey());
			if(userInfo == null){
				return Response.newError("没有找到主站用户!");
			}
        } catch (AuthFailException e) {
            logger.error(e);
            return Response.newError("验证失败!");
        }
		// 是否有角色
		String pid  = logicService.getPid(null,userInfo.getId());
		// 没有角色,返回创建角色
		if(pid == null){
			context.setAttachment(userInfo);
            return new Response(userInfo);
		}

        /**
         * 同步一下 玩家 网站 超级粉丝数据 (暂时不需要)
         */
//        CreatePlayerReq createPlayerReq = new CreatePlayerReq();
//        createPlayerReq.setUserId(userInfo.getId());
//        if(userInfo.getPayMember()==null){
//            createPlayerReq.setPayMember(0);
//        } else {
//            createPlayerReq.setPayMember(userInfo.getPayMember());
//        }
//
//        logicService.updatePlayerPay(null,createPlayerReq);


		// 把已登录的连接踢下线  --调用statusServer确定是否在线 ,在线需要剔除用户
//		Session session = statusService.getSession(null,pid);
//			if(session !=null){//在PlayerRpc中进入游戏的时候踢下线
//				statusService.logout(session,pid);
//			}

		// 增加一个token
		String loginToken= UUID.randomUUID().toString();
		statusService.addToken(null,loginToken);

		// server manager 返回服务IP 和端口, 以及playerId
		try {
			NodeInfo nodeInfo = manageService.getServerNode(ServerType.GAMESERVER);
			if(nodeInfo !=null){
				return rsp.newServerVoRsp(nodeInfo.getHost(),nodeInfo.getTcpPort(),pid,loginToken);
			}
		} catch (Exception e) {
			logger.error("没有获取到服务实例.",e);
		}

		return Response.newError("登录失败");
	}


}
