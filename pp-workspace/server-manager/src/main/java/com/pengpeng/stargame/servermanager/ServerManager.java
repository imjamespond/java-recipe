package com.pengpeng.stargame.servermanager;

import com.pengpeng.stargame.managed.NodeInfo;
import com.pengpeng.stargame.servermanager.servlets.ManageServlet;
import com.pengpeng.stargame.servermanager.servlets.ServletStatusServlet;
import com.pengpeng.stargame.util.SpringContextUtil;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务器管理启动程序.
 * @author ChenHonghong@gmail.com
 * @since 13-4-7 下午12:28
 */
public class ServerManager {

    private static Logger logger = Logger.getLogger(ServerManager.class);

    public static void main(String[] args) throws Exception{
        logger.info("----------------------------------------------------");
        logger.info("Starting Stargame Server Manager");
        logger.info("----------------------------------------------------");
        logger.info("server.properties:" + System.getProperty("server.properties"));

        SpringContextUtil.init();

//        System.out.println(html);


        //Configure & start Jetty server
        JettyWebServer webServer = new JettyWebServer();
        webServer.start();
/*        int httpServerPort = 9090;
        logger.info("Starting web container, listen on " + httpServerPort);

        final Server server = new Server(httpServerPort);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new ServletStatusServlet()),"/");
        context.addServlet(new ServletHolder(new ManageServlet()),"/manage");

        server.start();
        server.join();*/

    }

}
