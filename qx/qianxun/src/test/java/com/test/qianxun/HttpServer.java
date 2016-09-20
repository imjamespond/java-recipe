package com.test.qianxun;

import org.copycat.framework.JettyServer;

public class HttpServer {

	public static void main(String[] args) throws Exception {
		JettyServer server = new JettyServer(8080);
		server.startup();
	}

}
