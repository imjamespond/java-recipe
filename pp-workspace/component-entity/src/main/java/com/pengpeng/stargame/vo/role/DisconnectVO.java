package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-29下午5:13
 */
@Desc("拒绝连接,或踢下线的事件")
@EventAnnotation(name="event.disconnect",desc = "踢下线事件")
public class DisconnectVO {
    private String msg;

    public DisconnectVO(){

    }

    public DisconnectVO(String msg){
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
