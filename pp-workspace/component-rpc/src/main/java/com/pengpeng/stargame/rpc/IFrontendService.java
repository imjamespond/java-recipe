package com.pengpeng.stargame.rpc;

import java.util.Collection;
import java.util.Map;

/**
 * 前台服务,只做三件事情
 * 1.broadcast:接收来自后台的聊天广播
 * 2.onSessionEvent:接收来自后台的Session更新事件
 * 3.onActionEvent:处理特殊的事件请求
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-10下午4:34
 */
public interface IFrontendService {
    /**
     * 消息广播,只用于聊天
     * @param pids
     * @param msg
     * @return
     */
    public RpcResult broadcast(String[] pids,String msg);

    /**
     *
     * @param pids
     * @param type
     * @param json
     * @return
     */
    public RpcResult broadcast(String[] pids,String type, String json);

    /**
     * 更新session事件
     * @param json
     */
    public void onSessionEvent(String json);

    /**
     * 踢下线,或通知对方退出登录
     * @param session
     * @param pid
     */
    public void logout(Session session,String pid);

}
