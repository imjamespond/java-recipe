package com.metasoft;
import org.apache.log4j.BasicConfigurator;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.metasoft.service.JobQueue;
import com.metasoft.service.MsgServiceHandler;
import com.metasoft.thrift.MsgService;

public class MsgServer {
	public static MsgService.Processor<MsgServiceHandler> processor;
	public static ApplicationContext context;
	static Logger log = LoggerFactory.getLogger(MsgServer.class);
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		System.setProperty("spring.profiles.active", "production");// production
		context = new ClassPathXmlApplicationContext("application-core.xml");
		
		blockingServer();
	}
	
	static void blockingServer(){
		MsgServiceHandler handler = context.getBean(MsgServiceHandler.class);
		JobQueue queue = context.getBean(JobQueue.class);
		queue.initSingleThread();
		processor = new MsgService.Processor<MsgServiceHandler>(handler);
		TServerTransport serverTransport;
		try {
			serverTransport = new TServerSocket(9090);
			TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).minWorkerThreads(2).maxWorkerThreads(8)
					.requestTimeout(60).processor(processor));
			log.debug("Starting the simple server...");
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		
		queue.joinSingleThread();
	}

	static void nonBlockingServer(){
		MsgServiceHandler handler = context.getBean(MsgServiceHandler.class);
		processor = new MsgService.Processor<MsgServiceHandler>(handler);
		TNonblockingServerTransport serverTransport;
		try {
			serverTransport = new TNonblockingServerSocket(9090);
			TThreadedSelectorServer server = new TThreadedSelectorServer(new TThreadedSelectorServer.Args(serverTransport).processor(processor));
			log.debug("Starting the Nonblocking Server...");
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		
	}
}