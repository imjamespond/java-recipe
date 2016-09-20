package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.cmd.response.Response;
import org.apache.log4j.Logger;

/**
 * 通用内容
 */
public abstract class RspFactory {

	protected static final Logger logger = Logger.getLogger(RspFactory.class);

	/**
	 * 获取用户头像
	 * @param userId
	 * @return http://uud.com.pengpeng.com/S%d.jpeg
	 */
	public static String getUserPortrait(int userId){
		return String.format("http://uud.pengpeng.com/S%d.jpeg",userId);
	}

	public Response newOkRsp(){
		return Response.newOK("");
	}
	
	public Response newErrorRsp(String msg){
		return Response.newError(msg);
	}
	public Response newObjectRsp(Object data){
		return new Response(data);
	}

}
