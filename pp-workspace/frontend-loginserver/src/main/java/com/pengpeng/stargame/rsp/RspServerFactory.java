package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.vo.role.ServerVO;

/**
 * Server服务
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-25 上午10:20
 */
public class RspServerFactory {

    public Response newOkRsp(){
        return Response.newOK("");
    }

    public Response newErrorRsp(String msg){
        return Response.newError(msg);
    }
    public Response newObjectRsp(Object data){
        return new Response(data);
    }

    public Response newServerVoRsp(String ip,int port,String playerId,String token) throws Exception {
		ServerVO vo = new ServerVO();
		vo.setIp(ip);
		vo.setPort(port);
		vo.setPlayerId(playerId);
		vo.setToken(token);
		return new Response(vo);
	}
}
