package com.qianxun.test;

import org.copycat.framework.JettyServer;

public class PanelHttpServer {

	public static void main(String[] args) {
		JettyServer server = new JettyServer(8080);
		server.startup();
	}

}
