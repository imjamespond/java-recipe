package com.pengpeng.stargame.rpc;

/**
 * 服务器组件需要实现的接口
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-10下午4:47
 */
public interface IServerService {

    /**
     * 启动
     * @param time 指定启动的绝对时间
     */
    public void start(long time);

    /**
     * 暂停/恢复
     * @param time 指定暂停的绝对时间
     */
    public void pause(long time);

    /**
     * 停止服务器,但不会停止服务器的心跳
     * @param time 指定停止服务器的绝对时间
     */
    public void stop(long time);

    /**
     * 完全关闭服务器,进程退出,并在管理服务器中注销
     * @param time 绝对时间
     */
    public void shutdown(long time);

    /**
     * 刷新服务端列表
     * @param json
     */
    public void refresh(String json);
}
