package com.pengpeng.stargame.managed;

import java.util.List;

/**
 * 服务器管理器功能接口，比如：客户端可以通过此接口取得可用的服务节口等等。
 * <p>由 server-manager 实现功能。</p>
 *
 * @auther foquan.lin@pengpeng.com,honghong.chen@pengpeng.com
 * @since: 13-4-10下午4:45
 */
public interface IManageService {

    /**
     * 取得当前所有可用（onservice）的服务器节点。
     * @return 当前可用的节点
     */
    public List<NodeInfo> getServerNodes();

    /**
     * 取得某一类型的服务节点信息。
     * @param serverType 服务类型
     * @return 相应的节点信息
     */
    public NodeInfo getServerNode(String serverType);

    /**
     * 根据服务实例ID取得节点信息。
     * @param serverId　服务实例ID
     * @return 对应的节点信息
     */
    public NodeInfo getServerNodeById(String serverId);

    /**
     * 取得某一类型的所有服务节点信息。
     * @param serverType　服务类型
     * @return 多个节点信息
     */
    public List<NodeInfo> getServerNodes(String serverType);
}
