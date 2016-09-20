package com.pengpeng.stargame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.annotation.EventAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.container.ISessionContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.role.DisconnectVO;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-28下午5:09
 */
@Component
public class Broadcast implements  Runnable,IConnectListener{
    private static final Logger logger = Logger.getLogger(Broadcast.class);

    private BlockingQueue<RspItem> queue;

    private ScheduledExecutorService executor;
    private ExecutorService putExecutor;

    private Map<String,Class> classMap;

    @Autowired
    private ApplicationContext ctx;

    private Gson gson =new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Autowired
    private StatusRemote statusRemote;

    @Autowired
    private SceneRpcRemote sceneRemote;

    @Autowired
    private PlayerRpcRemote playerRemote;
    @Autowired
    private ISessionContainer container;

    @Autowired
    private MessageSource message;

    private Map<String,ChannelHandlerContext> channelHandlers = new HashMap<String,ChannelHandlerContext>();
    //停机标志
    private volatile boolean shutdown;

    private AtomicLong count = new AtomicLong();
    private long oldCount = 0;
    private long maxCount =0 ;
    public Broadcast(){
        shutdown = false;
        queue = new LinkedBlockingQueue<RspItem>(500000);
        putExecutor = Executors.newCachedThreadPool();
        executor = Executors.newScheduledThreadPool(51);
    }
    public void start(){
        for(int i=0;i<50;i++){
            executor.execute(this);
        }
        final long time = System.currentTimeMillis();
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long all = count.get();
                long t = System.currentTimeMillis() -time;
                t = t/1000;
                long curr = all - oldCount;
                if (curr>maxCount){
                    maxCount = curr;
                }
                oldCount =all;
                int remain = queue.size();
                if (all>0){
                    logger.info(String.format("广播数[平均:%d,累计:%d,高峰:%d,当前:%d,剩余数据包:%d]/每秒" , (all/t),oldCount,maxCount,curr,remain));
                }
            }
        },1,1, TimeUnit.SECONDS);
    }


    public void shutdown(){
        shutdown = true;
        executor.shutdown();
    }
    @Override
    public void run() {
        while(!shutdown){
            try {
                RspItem obj = queue.take();
                if (null == obj) {
                    continue;
                }
                count.incrementAndGet();
                if (obj.type==1){//如果是断线类型
                    disconnect(obj.pid,obj.rsp);
                }else {
                    ChannelHandlerContext ctx = channelHandlers.get(obj.pid);
                    if (ctx != null && ctx.getChannel().isConnected()&&ctx.getChannel().isWritable()) {
                        ctx.getChannel().write(obj.rsp);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    @PostConstruct
    private void init(){
        classMap = new HashMap<String,Class>();
        Map<String,Object> map = ctx.getBeansWithAnnotation(EventAnnotation.class);
        for(Object obj:map.values()){
            EventAnnotation anno = obj.getClass().getAnnotation(EventAnnotation.class);
            if (anno==null){
                continue;
            }
            logger.info(String.format("注册事件[%s,%s]",anno.name(),obj.getClass()));
            classMap.put(anno.name(), obj.getClass());
        }
        start();
    }

    public void broadcast(final String[] pids, final String type, final String json) {
        final Class cls = classMap.get(type);
        if (cls==null){
            logger.error("not found event;"+type);
            return ;
        }
        //logger.info("broadcast:"+type);

        putExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final Object obj = gson.fromJson(json, cls);
                final Response rsp = Response.newObject(obj);
                rsp.setCode(type);
                for (String pid : pids) {
                    try { //保证先到的数据先发送出去
                        queue.put(new RspItem(pid, rsp));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void broadcast(final String[] pids, String json) {
        ChatVO vo = gson.fromJson(json, ChatVO.class);
        final Response rsp = Response.newObject(vo);
        rsp.setCode("event.chat");
        putExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for(String pid:pids){
                    try {
                        queue.put(new RspItem(pid,rsp));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    /**
     * 客户端主动断开连接,触发的事件调用此方法
     */
    @Override
    public void disconnected(Object attach) {
        logger.debug("客户端断开链接,清理session:"+attach);
        if (attach instanceof Session){
            final Session s = (Session) attach;
            putExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        queue.put(new RspItem(s.getPid(), 1, Response.newOK()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    private void disconnect(String pid,Response rsp){
        logger.debug("断开链接:"+rsp);
        ChannelHandlerContext ctx = channelHandlers.get(pid);
        Session session = container.getElement(pid);
        if (null!=session){
            container.removeElement(pid);
            channelHandlers.remove(pid);
            try {
                sceneRemote.outerScene(session, session.getScene());
                playerRemote.logout(session, session.getPid());
            } catch (GameException e) {
                logger.error(e);
            }
        }
        if (null!=ctx&&null!=rsp.getData()){
            rsp.setCode("event.disconnect");
            if (ctx.getChannel().isConnected()&&ctx.getChannel().isWritable()) {
                ctx.getChannel().write(rsp);
                ctx.getChannel().close().awaitUninterruptibly();
            }
        }
    }

    @Override
    public void reject(Object attach) {
        logger.debug("踢下线,清理session:"+attach);
        if (attach instanceof Session){
            final String msg = message.getMessage("reject.connect",null, Locale.CHINA);
            final Session s = (Session) attach;
            //踢下线时必须实时,不能用线程队列
            disconnect(s.getPid(), Response.newObject(new DisconnectVO(msg)));
        };
    }

    public void addConnection(ChannelHandlerContext ctx,Session session){
        channelHandlers.put(session.getPid(), ctx);
        //container.addElement(session);
        //SessionContainer.createSession的时候就已经自动加入容器
    }

    //用于家族摇钱树活动推送
    public void broadcast(final List<Session> sessions, String content) {
        //以千里传音的方式发送
        final Response rsp = Response.newObject( new ChatVO("shout",null,null,content));
        rsp.setCode("event.chat.msg");
        for (Session s : sessions) {
            try {
                queue.put(new RspItem(s.getPid(), rsp));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class RspItem{
        private String pid;
        private int type;
        private Response rsp;
        RspItem(String pid,Response rsp){
            this.pid = pid;
            this.type = 0;
            this.rsp = rsp;
        }

        RspItem(String pid,int type,Response rsp){
            this.pid = pid;
            this.type = type;
            this.rsp = rsp;
        }
    }
}
