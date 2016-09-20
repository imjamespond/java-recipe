package com.pengpeng.stargame.servermanager;

import com.pengpeng.stargame.managed.IClientService;
import com.pengpeng.stargame.managed.NodeInfo;

import java.util.Date;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-4-16 下午4:37
 */
public class ServerInstance {

    public String instanceId;

    public NodeInfo nodeInfo;

    public IClientService nodeClient;

    public String status;

    public Date beatTime;

    public Date startTime;

    public long upTime;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public NodeInfo getNodeInfo() {
        return nodeInfo;
    }

    public void setNodeInfo(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getBeatTime() {
        return beatTime;
    }

    public void setBeatTime(Date beatTime) {
        this.beatTime = beatTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public long getUpTime() {
        return upTime;
    }

    public void setUpTime(long upTime) {
        this.upTime = upTime;
    }

    public IClientService getNodeClient() {
        return nodeClient;
    }

    public void setNodeClient(IClientService nodeClient) {
        this.nodeClient = nodeClient;
    }
}
