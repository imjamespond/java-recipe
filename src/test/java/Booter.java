

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.metasoft.empire.net.websocket.WebSocketServer;
import com.metasoft.empire.service.HandlerService;

/**
 * A HTTP server which serves Web Socket requests at:
 *
 * http://localhost:8080/websocket
 *
 * Open your browser at http://localhost:8080/, then the demo page will be loaded and a Web Socket connection will be
 * made automatically.
 *
 * This server illustrates support for the different web socket specification versions and will work with:
 *
 * <ul>
 * <li>Safari 5+ (draft-ietf-hybi-thewebsocketprotocol-00)
 * <li>Chrome 6-13 (draft-ietf-hybi-thewebsocketprotocol-00)
 * <li>Chrome 14+ (draft-ietf-hybi-thewebsocketprotocol-10)
 * <li>Chrome 16+ (RFC 6455 aka draft-ietf-hybi-thewebsocketprotocol-17)
 * <li>Firefox 7+ (draft-ietf-hybi-thewebsocketprotocol-10)
 * <li>Firefox 11+ (RFC 6455 aka draft-ietf-hybi-thewebsocketprotocol-17)
 * </ul>
 */
public class Booter {
	private static Logger logger = LoggerFactory.getLogger(Booter.class);

	public static void main(String[] args) throws Exception {
		System.setProperty("spring.profiles.active", "home-dev" );//   "development"
		WebSocketServer.context = new ClassPathXmlApplicationContext("applicationContext.xml");
		//Properties properties = (Properties) context.getBean("properties");
		//int serverPort = Integer.valueOf(properties.getProperty("server.port"));
		//do some initialization
		HandlerService hs = WebSocketServer.context.getBean(HandlerService.class);
		hs.init();

		WebSocketServer.start();
		logger.debug("launching done");
	}
}