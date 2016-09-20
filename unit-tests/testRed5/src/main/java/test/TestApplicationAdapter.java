package test;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.scope.IScope;


public class TestApplicationAdapter extends ApplicationAdapter {
	@Override
	public synchronized boolean connect(IConnection conn, IScope scope, Object[] params) {
		System.out.println("TestApplicationAdapter connect");
		return true;
		
	}
}
