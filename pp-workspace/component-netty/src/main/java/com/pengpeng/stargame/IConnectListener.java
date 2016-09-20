package com.pengpeng.stargame;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-7下午3:34
 */
public interface IConnectListener {

    /**
     * 用户主动断线
     * @param attach
     */
    public void disconnected(Object attach);

    /**
     * 被踢下线,或被拒绝连接
     * @param attach
     */
    public void reject(Object attach);
}
