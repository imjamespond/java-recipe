package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-19下午3:22
 */
@Desc("token请求")
public class TokenReq extends BaseReq {
    @Desc("登录游戏的时候是网站的token,进入游戏的时候是游戏服务器产生的token")
    private String tokenKey;

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String token) {
        this.tokenKey = token;
    }
}
