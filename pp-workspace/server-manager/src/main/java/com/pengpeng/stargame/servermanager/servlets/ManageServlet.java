package com.pengpeng.stargame.servermanager.servlets;

import com.pengpeng.stargame.servermanager.CommandLineTools;
import com.pengpeng.stargame.servermanager.ServerInstanceManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-4-7 下午2:49
 */
public class ManageServlet extends HttpServlet{

    private static Logger logger = Logger.getLogger(ManageServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        doGet(req,response);
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        String cmd = req.getParameter("cmd");
        String instanceId = req.getParameter("instanceId");

//        ServerInstance instance = ServerInstanceManager.getServerInstance(instanceId);
        if("pause".equals(cmd)){
            ServerInstanceManager.pause(instanceId);
        }
        if("stop".equals(cmd)){
            ServerInstanceManager.stop(instanceId);
        }
        if("onservice".equals(cmd)){
            ServerInstanceManager.resume(instanceId);
        }
        if("newinstance".equals(cmd)){
            String type = req.getParameter("type");
            String serverip = req.getParameter("serverip");
            String port = req.getParameter("port");
            String shell = "sg-master '"+serverip+"' '--type="+type+" --port="+port+"'";
            System.out.println(shell);

            String msg = "SUCCESSFUL.";
            try {
                CommandLineTools.Exec(shell);
            } catch (Exception e) {
                logger.error(e);
                msg = e.getMessage();
            }

            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(msg);
            return;

        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/");
    }
}
