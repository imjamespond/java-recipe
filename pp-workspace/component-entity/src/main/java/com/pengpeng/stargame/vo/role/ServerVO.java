package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 通知客户端该连接哪个服务器
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-15 下午2:40
 */
@Desc("服务器信息")
public class ServerVO {
	@Desc("服务器IP")
	private String ip;

	@Desc("服务器端口")
	private int port;

	@Desc("角色Id")
	private String playerId;

	@Desc("token")
	private String token;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String toString(){
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE, true, true);
	}
}
