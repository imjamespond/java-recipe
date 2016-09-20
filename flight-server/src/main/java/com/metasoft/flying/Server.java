package com.metasoft.flying;

public class Server {
	private static FlyServer server;

	public static void main(String[] args) throws Exception {
		System.setProperty("spring.profiles.active", "production");// production
																	// development
		server = new FlyServer();
		server.startup();
	}

	public static void shutdown() {
		if (null != server) {
			server.shutDown();
		}

	}
}
