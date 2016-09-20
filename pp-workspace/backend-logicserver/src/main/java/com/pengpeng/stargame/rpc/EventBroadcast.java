package com.pengpeng.stargame.rpc;

import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.vo.MsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-18下午3:51
 */
@Component
public class EventBroadcast {
    @Autowired
    private FrontendServiceProxy frontendServiceProxy;

    public void broadcast(Session to,int type){
        frontendServiceProxy.broadcast(new Session[]{to}, new MsgVO(type,0));
    }

    public void broadcast(Session[] tos,int type){
        frontendServiceProxy.broadcast(tos, new MsgVO(type,0));
    }
}
