package com.pengpeng.stargame.servermanager.web.model;

import com.pengpeng.stargame.servermanager.ServerInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-5-19 下午11:16
 */
public class DashboardPage {

    public List<ServerInstance> servers = new ArrayList<ServerInstance>();

    public List<ServerInstance> gameServers = new ArrayList<ServerInstance>();
    public List<ServerInstance> loginServers = new ArrayList<ServerInstance>();
    public List<ServerInstance> logicServers = new ArrayList<ServerInstance>();
    public List<ServerInstance> statusServers = new ArrayList<ServerInstance>();
    public List<ServerInstance> serverManagers = new ArrayList<ServerInstance>();


    public List<ServerInstance> getServers() {
        return servers;
    }

    public void setServers(List<ServerInstance> servers) {
        this.servers = servers;
    }

    public List<ServerInstance> getGameServers() {
        return gameServers;
    }

    public void setGameServers(List<ServerInstance> gameServers) {
        this.gameServers = gameServers;
    }

    public List<ServerInstance> getLoginServers() {
        return loginServers;
    }

    public void setLoginServers(List<ServerInstance> loginServers) {
        this.loginServers = loginServers;
    }

    public List<ServerInstance> getLogicServers() {
        return logicServers;
    }

    public void setLogicServers(List<ServerInstance> logicServers) {
        this.logicServers = logicServers;
    }

    public List<ServerInstance> getStatusServers() {
        return statusServers;
    }

    public void setStatusServers(List<ServerInstance> statusServers) {
        this.statusServers = statusServers;
    }

    public List<ServerInstance> getServerManagers() {
        return serverManagers;
    }

    public void setServerManagers(List<ServerInstance> serverManagers) {
        this.serverManagers = serverManagers;
    }
}
