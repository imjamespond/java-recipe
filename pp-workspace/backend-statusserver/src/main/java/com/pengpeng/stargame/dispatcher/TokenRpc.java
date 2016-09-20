package com.pengpeng.stargame.dispatcher;

import com.pengpeng.stargame.IChannelContainer;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.container.ITokenContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-12下午4:29
 */
@Component()
public class TokenRpc extends StatusHandler {

    @Autowired
    private IChannelContainer container;

    @Autowired
    private ITokenContainer tokenContainer;

    public TokenRpc(){
    }

    @RpcAnnotation(cmd="token.add",req= String.class,name="增加一个token")
    public void addToken(Session session,String token){
        tokenContainer.addToken(token);
    }

    @RpcAnnotation(cmd="token.auth",req= String.class,name="token验证")
    public boolean auth(Session session,String token){
        boolean exists = tokenContainer.contains(token);
        if (exists){
            tokenContainer.removeToken(token);
            return true;
        }
        return false;
    }

    @RpcAnnotation(cmd="token.get",req= String.class,name="取得token")
    public String getToken(Session session,String token){
        boolean exists = tokenContainer.contains(token);
        if (exists){
            return token;
        }else{
            return null;
        }
    }

    @RpcAnnotation(cmd="token.del",req= String.class,name="删除一个token")
    public void removeToken(Session session,String token){
        tokenContainer.removeToken(token);
    }
}
