package com.pengpeng.stargame.managed;

import java.io.Serializable;

/**
 * 服务节点信息。
 * @author ChenHonghong@gmail.com
 * @since 13-4-15 下午3:46
 */
public class NodeInfo implements Serializable {

    private static final long serialVersionUID = 3562456262219665000L;

    private int port;

    private int tcpPort;

    private String type;

    private String host;

    private String id;

    private String buildVersion;

    private String buildTime;

    private NodeRuntime runtime = new NodeRuntime();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public NodeRuntime getRuntime() {
        return runtime;
    }

    public void setRuntime(NodeRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "port=" + port +
                ", tcpPort=" + tcpPort +
                ", type='" + type + '\'' +
                ", host='" + host + '\'' +
                ", id='" + id + '\'' +
                ", buildVersion='" + buildVersion + '\'' +
                ", buildTime='" + buildTime + '\'' +
                '}';
    }
}
