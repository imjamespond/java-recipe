package com.metasoft.empire.service.common;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import com.metasoft.empire.service.UserService;
@Service
public class SignalService {
	private static final Logger logger = LoggerFactory.getLogger(SignalService.class);
	
	private static boolean running = true;

	@Autowired
	private UserService userService;
	/**处理系统信号HUP,并安全退出
	 * @throws InterruptedException
	 */
	@PostConstruct
	private void init() throws InterruptedException {

		SignalHandler shutdown = new SignalHandler() {
			public void handle(Signal sig) {
				if (running) {
					logger.info("Signal " + sig);
					running = false;
					//Booter.shutdown();
					userService.onPersistOffline();
					userService.onPersistOnline();
					logger.info("persistence done");
					
				} else {
					logger.info("closing interrupted!");
				}
				logger.info("System exit! " + sig);
				System.exit(0);
			}
		};
		
		SignalHandler reload = new SignalHandler() {
			public void handle(Signal sig) {
				if (running) {
					logger.info("Signal " + sig);
					userService.onPersistOffline();
					userService.onPersistOnline();
				}
			}
		};

		if(SystemUtils.IS_OS_LINUX){
			Signal.handle(new Signal("USR2"), shutdown);
			Signal.handle(new Signal("HUP"), reload);
		}else if(SystemUtils.IS_OS_WINDOWS){
			logger.debug("running on windows which does not support hup signal");
		}
		
	}
}
