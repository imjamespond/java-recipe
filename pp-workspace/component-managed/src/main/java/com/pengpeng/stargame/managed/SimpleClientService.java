package com.pengpeng.stargame.managed;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-5-8 下午4:22
 */
public class SimpleClientService implements IClientService {

    private NodeMonitor nodeMonitor;

    @Override
    public void resume() {
        nodeMonitor.setStatus(NodeStatus.ONSERVICE.getCode());
        System.out.println("Receive refresh command, Node status chanaged to : " + NodeMonitor.getStatus());
    }

    @Override
    public void stop() {
        nodeMonitor.setStatus(NodeStatus.STOPPED.getCode());
        System.out.println("Receive stop command, Node status chanaged to : " + NodeMonitor.getStatus());
    }

    @Override
    public void pause() {
        nodeMonitor.setStatus(NodeStatus.PAUSED.getCode());
        System.out.println("Receive pause command, Node status chanaged to : " + NodeMonitor.getStatus());
    }

    @Override
    public void notifyNodeStop(NodeInfo stoppedServer) {
        System.out.println("Node is stopped:" + stoppedServer.getId());
    }

    @Override
    public NodeInfo getInfo() {
        return NodeConfig.getInfo();
    }

    public void setNodeMonitor(NodeMonitor nodeMonitor) {
        this.nodeMonitor = nodeMonitor;
    }
}
