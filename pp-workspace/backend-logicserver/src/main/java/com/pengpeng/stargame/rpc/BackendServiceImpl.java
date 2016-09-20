package com.pengpeng.stargame.rpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.LockContainer;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.gameevent.constiner.IEventRuleContainer;
import com.pengpeng.stargame.managed.NodeMonitor;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于rpc请求的分发
 * 此对象在spring的xml配置中配置
 * 不使用注解配置
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-12下午3:42
 */
public class BackendServiceImpl implements IBackendService {
    private final Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private ApplicationContext ctx;

    private final Map<String, CommandHandlerHolder> handlers = new HashMap<String, CommandHandlerHolder>();
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * 业务锁对象可以在RpcAnnotation属性中设置lock=true即可在入口处加锁
     * 对象锁粒度,基于玩家所在的场景加锁
     * 例如.玩家在好友的农场中,则锁定好友的农场channel知道操作完成为止
     */
    @Autowired
    private LockContainer lockContainer;

    @Autowired
    private FrontendServiceProxy frontendService;

    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private IEventRuleContainer eventRuleContainer;

    @PostConstruct
    public void init() {
        log.info("START|Init HandlerMethod Count...");
        handlers.clear();
//        hasInit.set(true);
        // 从spring上下文取出所有消息处理器
        Map<String, RpcHandler> handlerMap = ctx.getBeansOfType(RpcHandler.class);
        for (RpcHandler handler : handlerMap.values()) {
            Class<?> clazz = handler.getClass();

            // 找到所有处理方法
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                // 判断处理方法是否符合要求
                RpcAnnotation annotation = method.getAnnotation(RpcAnnotation.class);
                if (annotation == null) {
                    continue;
                }
                //Class<?>[] paramTypes = method.getParameterTypes();

                // 把处理方法加入缓存中
                CommandHandlerHolder holder = new CommandHandlerHolder(handler, method);
                handlers.put(annotation.cmd(), holder);
                log.info("START|Init HandlerMethod: " + clazz.getSimpleName() + " --> " + method.getName() + " --> " + annotation.name());
            }
        }
        log.info("START|Init HandlerMethod Count: " + handlers.size());

    }

    /**
     * 消息处理方法容器
     */
    class CommandHandlerHolder {
        public CommandHandlerHolder(RpcHandler h, Method m) {
            this.handler = h;
            this.method = m;
        }

        public RpcHandler handler;
        public Method method;
    }

    @Override
    public String testConnecting() {
        return NodeMonitor.getStatus();
    }

    @Override
    public RpcResult process(String code, Session session, String json) throws Exception {
        log.debug(" reqest>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + json);
        CommandHandlerHolder holder = handlers.get(code);
        if (holder == null) {
            log.error("not found code:" + code);
            //throw new Exception("not found rpc command:"+code);
            return new RpcResult(code, false, "not found rpc command");
        }
        RpcAnnotation anno = holder.method.getAnnotation(RpcAnnotation.class);
        if (anno == null) {
            //throw new Exception("not found rpc command:"+code);
            return new RpcResult(code, false, "not found rpc command");
        }
        Class<?> cls = anno.req();
        Object obj = gson.fromJson(json, cls);
        Object result = null;
        try {


            if (anno.lock() && session != null) {
                String channelId = session.getPid();//SessionUtil.getChannelScene(session);
                if (channelId != null) {
                    lockContainer.lock(channelId);
                }
            }
            result = holder.method.invoke(holder.handler, session, obj);
            if(session!=null&&session.getPid()!=null&&!session.getPid().equals("")){
                taskRuleContainer.autoAcceptTask(session.getPid(),code);
            }

//            log.debug("BroadcastHolder.get() " + BroadcastHolder.get());
            if (BroadcastHolder.get() != null && BroadcastHolder.get().size() > 0) {
                Session[] sessions = {session};
                for (int i = 0; i < BroadcastHolder.get().size(); i++) {
                    frontendService.broadcast(sessions, BroadcastHolder.get().get(i));
                }
            }
            BroadcastHolder.clear();


            if (result == null) {
                return new RpcResult(code);
            }
            String js = gson.toJson(result);
//            log.debug("response >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + js);


            return new RpcResult(code, js);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            log.error(e);
            return new RpcResult(code, false, e.getMessage());
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
            log.trace(e,e.getTargetException());
            return new RpcResult(code, false, e.getTargetException().getMessage());
        } catch (Exception e) {
            log.trace(e);
            e.printStackTrace();
            return new RpcResult(code, false, e.getMessage());
        } finally {
            if (anno.lock() && session != null) {
                String channelId = session.getPid();//SessionUtil.getChannelScene(session);
                if (channelId != null) {
                    lockContainer.unlock(channelId);
                }
            }
        }
    }

}
