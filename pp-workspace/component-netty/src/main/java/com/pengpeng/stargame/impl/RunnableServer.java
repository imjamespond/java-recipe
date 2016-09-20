package com.pengpeng.stargame.impl;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.pengpeng.stargame.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.pengpeng.stargame.req.BaseReq;

@Component()
public class RunnableServer extends SimpleChannelHandler implements IServerServer ,Runnable{
	private static Logger logger = Logger.getLogger(RunnableServer.class);

	@Autowired
	private CmdDispatcher dispatcher;
	
	@Autowired
	private IpCache ipCache;

    private IConnectListener listener =null;
//	private ExecutorService service = Executors.newSingleThreadExecutor();//.newFixedThreadPool(50);
    private ExecutorService service = Executors.newFixedThreadPool(200);
    private LinkedBlockingQueue<DispatchThread> queue = new LinkedBlockingQueue<DispatchThread>();
	public RunnableServer(){

	}
	public String toString(){
		return "RUNNING";
	}

	public void shutdown() {

	}

	public void start() {
		logger.info("服务器已经启动.");
//        service.execute(this);
	}

//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
//        super.exceptionCaught(ctx, e);
//        if (listener!=null){
//            listener.disconnected(ctx.getAttachment());
//        }
//    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        if (listener!=null){
            listener.disconnected(ctx.getAttachment());
        }
        super.channelDisconnected(ctx, e);
    }

//    @Override
//    public void disconnectRequested(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//        if (listener!=null){//aa
//            listener.disconnected(ctx.getAttachment());
//        }
//        super.disconnectRequested(ctx, e);
//    }

//    @Override
//    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//        if (listener!=null){//aa
//            listener.disconnected(ctx.getAttachment());
//        }
//        super.channelClosed(ctx, e);
//    }

    @Override
    public void closeRequested(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        if (listener!=null){
            listener.disconnected(ctx.getAttachment());
        }
        super.closeRequested(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		logger.info(">>>DEBUG|msg|" + e.getMessage());
		BaseReq message = (BaseReq) e.getMessage();

        //IP过滤
        String ip = ((InetSocketAddress) ctx.getChannel().getRemoteAddress()).getAddress().getHostAddress();
        if (ipCache.get(ip)) {
        	logger.info("DISABLE|" + ip + "|" + message);
            ctx.getChannel().close();
            return;
        }

        BaseReq command = message;//cmdBuilder.decoder(message);
        if (null == command || !command.validate()) {
            logger.warn("WARN|REQEST|" + message);
            return;
        }
//        queue.add(new DispatchThread(command, ctx));
        service.execute(new DispatchThread(command, ctx));
//        dispatcher.dispatch(command,ctx);
    }

    public void setListener(IConnectListener bean) {
        this.listener = bean;
    }

    @Override
    public void run() {
        DispatchThread thread = null;
        while(true){
        try {
            thread = queue.take();
            thread.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        }
    }

    class DispatchThread implements Runnable {
        private BaseReq command;
        private ChannelHandlerContext ctx;

        DispatchThread(BaseReq command, ChannelHandlerContext ctx) {
            this.command = command;
            this.ctx = ctx;
        }

        @Override
        public void run() {
            dispatcher.dispatch(command, ctx);
        }
    }	
	




}
