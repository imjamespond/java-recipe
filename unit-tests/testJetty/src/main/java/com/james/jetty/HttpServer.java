package com.james.jetty;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.annotation.PreDestroy;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HttpServer {

	@Value("${webapp.port}")
	private int port;

	@Value("${webapp.path}")
	private String path;
	private Server server;
	
	HttpServer(){
		port = 9999;
		path = "C:/project/jetty/webapp";//"/webapp";
		server = new Server(port);  		
	}

	public void start() {
		try {
			 
			WebAppContext servletContext = new WebAppContext();
			servletContext.setContextPath("/");
			String realPath = this.path;
			URL url = getClass().getResource(this.path);
			if (url != null) {
				File dir = new File(url.toURI());
				if (dir.exists())
					realPath = dir.getAbsolutePath();
			}
			servletContext.setResourceBase(realPath);
			this.server.setHandler(servletContext);
			this.server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@PreDestroy
	public void stop() {
		try {
			this.server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		HttpServer server = new HttpServer();
		server.start();
	}
	
	
	
	
	
	
	

	private class HelloWorld extends AbstractHandler {
		public void handle(String target, Request baseRequest,
				HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
			response.getWriter().println("<h1>Hello World</h1>");
		}
	}
}