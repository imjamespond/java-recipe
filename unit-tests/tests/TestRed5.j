package test;

import org.red5.server.ContextLoader;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IServer;
import org.red5.server.scope.RoomScope;
import org.red5.server.scope.Scope;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;

public class TestRed5 {
	public static void main(String[] args) throws Exception {
		//System.setProperty("red5.root", "target/classes");
		//System.setProperty("red5.config_root", "./conf");
		
		BeanFactoryLocator loc = ContextSingletonBeanFactoryLocator.getInstance("red5.xml");
		BeanFactoryReference red5CommonRef = loc.useBeanFactory("red5.common");
		ApplicationContext red5Common = (ApplicationContext) red5CommonRef
				.getFactory();

		String defaultContex = System.getProperty("red5.root")
				+ "/webapps/red5-default.xml";System.out.println(defaultContex);
		ContextLoader contextLoader = (ContextLoader) red5Common
				.getBean("context.loader");
		contextLoader.loadContext("default.context", defaultContex);

		IServer server = (IServer) red5Common.getBean("red5.server");
		server.addMapping("", "live", "default");

		Scope testScope = new RoomScope();
		testScope.setName("live");
		testScope.setParent(server.getGlobal("default"));
		testScope.setHandler(new ApplicationAdapter());
		testScope.init();
	}
}
