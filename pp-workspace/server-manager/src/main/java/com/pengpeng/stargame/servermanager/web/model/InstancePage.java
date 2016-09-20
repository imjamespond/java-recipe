package com.pengpeng.stargame.servermanager.web.model;

import com.pengpeng.stargame.servermanager.ServerInstance;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-5-20 下午7:33
 */
public class InstancePage {

    public ServerInstance instance;

    public ServerInstance getInstance() {
        return instance;
    }

    public void setInstance(ServerInstance instance) {
        this.instance = instance;
    }
}
