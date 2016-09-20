package com.pengpeng.stargame.servermanager.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pengpeng.stargame.managed.ServerType;
import com.pengpeng.stargame.servermanager.ServerInstance;
import com.pengpeng.stargame.servermanager.ServerInstanceManager;
import com.pengpeng.stargame.servermanager.web.model.DashboardPage;

import java.util.List;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-5-19 下午8:12
 */
public class DashboardAction extends ActionSupport implements ModelDriven{

    public DashboardPage page = new DashboardPage();

    public String type;

    public String instanceId;

    public String index() throws Exception{
        final List<ServerInstance> instances = ServerInstanceManager.getInstances();
        page.setServers(instances);

        page.setGameServers(ServerInstanceManager.getByType(ServerType.GAMESERVER));
        page.setLogicServers(ServerInstanceManager.getByType(ServerType.LOGIC));
        page.setLoginServers(ServerInstanceManager.getByType(ServerType.LOGIN));
        page.setStatusServers(ServerInstanceManager.getByType(ServerType.STATUS));
        page.setServerManagers(ServerInstanceManager.getByType(ServerType.MANAGER));

        return SUCCESS;
    }


    public String servers() throws Exception{
        page.setServers(ServerInstanceManager.getByType(type));
        return SUCCESS;
    }

    public String detail() throws Exception{
        return SUCCESS;
    }

    @Override
    public Object getModel() {
        return page;
    }

    public void setPage(DashboardPage page) {
        this.page = page;
    }

    public void setType(String type) {
        this.type = type;
    }
}
