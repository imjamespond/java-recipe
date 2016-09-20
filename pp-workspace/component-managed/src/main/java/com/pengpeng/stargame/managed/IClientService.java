package com.pengpeng.stargame.managed;

/**
 * 客户端节点管理程序，由服务器管理器进行调用。
 * <p>所有的服务节点必须提供管理接口供管理器调用，管理器有时需要通知客户端节点，比如：当有节点终止时通知所有节点。</p>
 * <p>RMI 上下文：/NodeClient </p>
 * @author ChenHonghong@gmail.com
 * @since 13-4-15 下午2:28
 */
public interface IClientService {

    /**
     * 恢复当前节点为正常状态。
     * <p>将节点的状态改为：正常。</p>
     */
    public void resume();

    /**
     * 停止当前节点。
     * <p>分为三个步骤：</p>
     * <ol>
     *     <li>将节点状态改为：停止</li>
     *     <li>等待请求处理完成，并释放资源</li>
     *     <li>安全退出程序</li>
     * </ol>
     */
    public void stop();

    /**
     * 暂停当前节点。
     * <p>将节点的状态改为：暂停。</p>
     */
    public void pause();

    public void notifyNodeStop(NodeInfo stoppedServer);

    /**
     * 取得当前节点信息。
     * @return
     */
    public NodeInfo getInfo();
}
