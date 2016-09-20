package com.james.test;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.james.TestDao;
import com.james.TestData;
import com.james.commons.timer.ScheduleManager;
import com.james.memcached.XmemcachedDB;

/**
 * @author cwd
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@Service
public class MyTest {

	@Autowired
	private ScheduleManager scheduleManager;
	@Autowired
	private XmemcachedDB db;
	@Autowired
	private TestDao testDao;	
    @Autowired
    private MessageSource message;

	@Test
	public void test1() throws IOException {
		// TODO Auto-generated method stub

		/*
		 * @Target( { ElementType.METHOD })
		 * 
		 * @Retention(RetentionPolicy.RUNTIME)
		 * 
		 * @Documented public @interface RpcAnnotation { String name() default
		 * ""; String cmd(); boolean lock() default true; boolean check()
		 * default false; Class<?> vo() default void.class; Class<?> req(); }
		 */
		/*
		 * @RpcAnnotation(cmd="mail.send",lock=true,req=MailReq.class,name="发邮件")
		 * public void send(Session session,MailReq req){
		 */
		// RpcAnnotation anno =
		// holder.method.getAnnotation(RpcAnnotation.class);
		// Gson gson = new
		// GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		// gson.fromJson(json, classOfT)

		 Runnable chessRoomTimer = new ChessRoomTimer(1);
//		 scheduleManager.scheduleAtFixedRate(chessRoomTimer,
//		 DateUtils.MILLIS_PER_SECOND, DateUtils.MILLIS_PER_SECOND * 2);
		 //scheduleManager.schedule(chessRoomTimer, "* 15 9-17 * * MON-FRI");
		 scheduleManager.schedule(chessRoomTimer, "* * * * * *");
		 System.out.print("should break here!");

		db.set("foo", "bar");
		db.get("foo");

		Set<String> keys = new HashSet<String>();
		keys.add("foo");
		keys.add("bar");
		db.gets(keys);

		//db.delete("foo");
		

		// start the multithread environment
//		List<Thread> list = new ArrayList<Thread>();
//		for (int i = 0; i < 10; i++) {
//			Thread thread = new ThreadTest("Thread-" + (i + 1));
//			thread.start();
//			list.add(thread);
//		}
//		for (Thread thread:list) {
//			try {
//				thread.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}		
//		}
		
		db.get("foo");
		//db.close();
		
		TestData data = testDao.getData("haha");
		data.setEnable(false);
		testDao.saveBean(data);
		
        String content = message.getMessage("foo.bar", new String[]{"!!!"}, Locale.CHINA);
		System.out.println(content);
	}

	private class ChessRoomTimer implements Runnable {

		public ChessRoomTimer(int districtId) {
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// System.out.println(districtId);
			System.out.print("hahahahha");
		}

	}

	/**
	 * Each thread runs many times
	 */
	private class ThreadTest extends Thread {

		ThreadTest(String name) throws IOException {
			super(name);
		}

		public void run() {
			int i = 0;
			while (i < 5) {
				i++;
				Long cas = db.getCas("foo");
				//might do something here ...
				//db.casSet("foo", "hehe", cas);
				db.tryCasSet("foo", "heihei", cas);
				System.out.print(this.getName()+"\n");
			}

		}
	}
}