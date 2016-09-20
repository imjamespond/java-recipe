package test;

import org.red5.server.api.scope.IBasicScope;
import org.red5.server.scope.GlobalScope;

public class Red5GlobalScope extends GlobalScope {
	
	@Override
	public void removeChildScope(IBasicScope scope) {
		System.out.println("removeChildScope trick###");
	}
}
