package com.metasoft.flying.node.net;


public class RpcUtils {
	private static final ThreadLocal<RpcChannel> CURRENT_LOCAL_SESSION = new ThreadLocal<RpcChannel>();

	public static void set(RpcChannel conn) {
		CURRENT_LOCAL_SESSION.set(conn);
	}
	
	public static RpcChannel getRpcChannel() {
		return CURRENT_LOCAL_SESSION.get();
	}

	public static void clear() {
		CURRENT_LOCAL_SESSION.set(null);
	}
}
