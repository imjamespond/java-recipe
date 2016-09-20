package com.pengpeng.stargame;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.IPAccessHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.URL;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-5-17 下午2:45
 */
public class ApiServer {
    private static Logger logger = Logger.getLogger(ApiServer.class);

    public void start(){
        String webapp = "webapp";
        final URL warUrl = this.getClass().getClassLoader().getResource(webapp);
        final String warUrlString = warUrl.toExternalForm();
        System.out.println(warUrlString);
        WebAppContext context = new WebAppContext(warUrlString,"/");
//        context.setWelcomeFiles(new String[]{"/index.jsp"});
//        context.setDescriptor("WEB-INF/web.xml");
//        context.addServlet(DefaultServlet.class, "/");
//        context.addServlet(DispatcherServlet.class,"/");
        Connector connector=new SelectChannelConnector();
        connector.setPort(9091);
        final Server server = new Server();
        server.setHandler(context);
        server.addConnector(connector);
        //公网发布指定的白名单
//        IPAccessHandler handler = new IPAccessHandler();
//        handler.setWhite(new String[]{"172.18.1.32","172.18.1.40"});
//        server.setHandler(handler);

        System.out.println("Starting server...");
        try {
            server.start();
        } catch (Exception e) {
            System.out.println("Failed to start server!");
            return;
        }

        System.out.println("Server running...");
        while (true) {
            try {
                server.join();
            } catch (InterruptedException e) {
                System.out.println("Server interrupted!");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ApiServer webServer = new ApiServer();
        webServer.start();;
    }
}
