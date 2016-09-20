package com.pengpeng.stargame.servermanager.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pengpeng.stargame.servermanager.CommandLineTools;
import com.pengpeng.stargame.servermanager.ServerInstance;
import com.pengpeng.stargame.servermanager.ServerInstanceManager;
import com.pengpeng.stargame.servermanager.web.model.InstancePage;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-5-20 下午7:30
 */
public class InstanceAction extends ActionSupport implements ModelDriven {

    private static final long serialVersionUID = -1374140649668134763L;

    public InstancePage page = new InstancePage();

    private static Logger logger = Logger.getLogger(InstanceAction.class);

    public String id;

    public String cmd;

    public String detail() throws Exception{
        final ServerInstance serverInstance = ServerInstanceManager.getServerInstance(id);
        if(serverInstance == null){
            return "dashboard";
        }
        page.setInstance(serverInstance);
        return SUCCESS;
    }

    public String manage() throws Exception{
        if("pause".equals(cmd)){
            ServerInstanceManager.pause(id);
        }
        if("stop".equals(cmd)){
            ServerInstanceManager.stop(id);
        }
        if("resume".equals(cmd)){
            ServerInstanceManager.resume(id);
        }
        if("kill".equals(cmd)){
            ServerInstanceManager.resume(id);
        }
        if("restart".equals(cmd)){
            ServerInstanceManager.resume(id);
        }

        page.setInstance(ServerInstanceManager.getServerInstance(id));
        return SUCCESS;
    }

    @Override
    public Object getModel() {
        return page;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPage(InstancePage page) {
        this.page = page;
    }

    public String getId() {
        return id;
    }

    public InstancePage getPage() {
        return page;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
