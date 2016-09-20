package com.pengpeng.stargame.servermanager;

import com.pengpeng.stargame.servermanager.servlets.ServletStatusServlet;
import org.apache.jasper.servlet.JspServlet;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.URL;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-5-17 下午2:45
 */
public class JettyWebServer {

    private static Logger logger = Logger.getLogger(JettyWebServer.class);

    public void start(){
        String webapp = "webapp";
        final URL warUrl = this.getClass().getClassLoader().getResource(webapp);
        final String warUrlString = warUrl.toExternalForm();
        System.out.println(warUrlString);
        WebAppContext context = new WebAppContext(warUrlString,"/");
        context.setWelcomeFiles(new String[]{"/index.jsp"});
        context.addServlet(DefaultServlet.class, "/");
//        final ServletHolder jsp = context.addServlet(JspServlet.class, "*.jsp");
//        jsp.setInitParameter("classpath", context.getClassPath());
        // add your own additional servlets like this:
        // context.addServlet(JSONServlet.class, "/json");

        final Server server = new Server(9090);
        server.setHandler(context);

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
        JettyWebServer webServer = new JettyWebServer();
        webServer.start();;
    }
}
