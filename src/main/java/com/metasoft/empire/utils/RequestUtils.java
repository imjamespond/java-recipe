package com.metasoft.empire.utils;

import java.util.ArrayDeque;
import java.util.Deque;

import com.metasoft.empire.net.BaseConnection;
import com.metasoft.empire.service.common.IJob;


public class RequestUtils {
	private static final ThreadLocal<BaseConnection<?>> CURRENT_LOCAL_SESSION = new ThreadLocal<BaseConnection<?>>();
	private static final ThreadLocal<Deque<IJob>> PENDING_JOBS = new ThreadLocal<Deque<IJob>>();

	public static void setCurrentConn(BaseConnection<?> conn) {
		CURRENT_LOCAL_SESSION.set(conn);
	}
	
	public static BaseConnection<?> getCurrentConn() {
		return CURRENT_LOCAL_SESSION.get();
	}

	public static void putJob(IJob j) {
		Deque<IJob> deq = PENDING_JOBS.get();
		if(null == deq){
			deq = new ArrayDeque<IJob>();
			PENDING_JOBS.set(deq);
		}
		deq.add(j);
	}
	
	public static void doPenddingJobs() {
		Deque<IJob> deq = PENDING_JOBS.get();
		if(null == deq){
			return;
		}
		for(IJob j:deq){
			j.doJob();
		}
		deq.clear();
	}

}
