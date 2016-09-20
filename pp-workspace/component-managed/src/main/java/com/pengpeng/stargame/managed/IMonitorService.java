package com.pengpeng.stargame.managed;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-5-16 下午4:50
 */
public interface IMonitorService {

    /**
     * 到服务器管理器注册本实例。
     * @param nodeInfo
     */
    public void register(NodeInfo nodeInfo);

    /**
     * 注销本服务实例.
     * @param type
     * @param id
     */
    public void unRegister(NodeInfo nodeInfo);


    /**
     * 服务心跳汇报接口.
     * 客户端服务定时调用此接口跟管理器汇报当前状态。
     * @param nodeInfo 节点信息
     * @param status 状态
     * @see NodeStatus 状态代码
     */
    public void onState(NodeInfo nodeInfo, String status);

}
