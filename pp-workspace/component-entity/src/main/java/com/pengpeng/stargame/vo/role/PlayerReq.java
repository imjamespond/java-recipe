package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-1-14下午3:29
 */
@Desc("玩家信息请求,可通用")
public class PlayerReq extends BaseReq {
    @Desc("玩家id,npcid,或其它约定id")
    private String id;//玩家id
    @Desc("进入游戏的令牌")
    private String tokenKey;

	public PlayerReq() {
	}

	public PlayerReq(String cmd, String id) {
		super(cmd);
		this.id = id;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }
}
