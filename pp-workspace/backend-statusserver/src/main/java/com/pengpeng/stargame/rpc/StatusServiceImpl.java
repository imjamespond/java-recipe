package com.pengpeng.stargame.rpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.StatusHandler;
import com.pengpeng.stargame.rpc.IStatusService;
import com.pengpeng.stargame.rpc.RpcResult;
import com.pengpeng.stargame.rpc.Session;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-17上午10:49
 */
public class StatusServiceImpl implements IStatusService {
    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ApplicationContext ctx;

    private final Map<String, CommandHandlerHolder> handlers = new HashMap<String, CommandHandlerHolder>();
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    @PostConstruct
    public void init(){
        logger.info("START|Init HandlerMethod Count...");
        handlers.clear();
//        hasInit.set(true);
        // 从spring上下文取出所有消息处理器
        Map<String,StatusHandler> handlerMap = ctx.getBeansOfType(StatusHandler.class);
        for (StatusHandler handler : handlerMap.values()) {
            Class<?> clazz = handler.getClass();

            // 找到所有处理方法
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                // 判断处理方法是否符合要求
                RpcAnnotation annotation = method.getAnnotation(RpcAnnotation.class);
                if (annotation==null){
                    continue;
                }
                //Class<?>[] paramTypes = method.getParameterTypes();

                // 把处理方法加入缓存中
                CommandHandlerHolder holder = new CommandHandlerHolder(handler, method);
                handlers.put(annotation.cmd(), holder);
                logger.info("START|Init HandlerMethod: " + clazz.getSimpleName() + " --> " + method.getName()+" --> "+annotation.name());
            }
        }
        logger.info("START|Init HandlerMethod Count: " + handlers.size());

    }
    /**
     * 消息处理方法容器
     */
    class CommandHandlerHolder {
        public CommandHandlerHolder(StatusHandler h, Method m) {
            this.handler = h;
            this.method = m;
        }

        public StatusHandler handler;
        public Method method;
    }
    @Override
    public RpcResult process(String code, Session session, String json) throws Exception {
        CommandHandlerHolder holder = handlers.get(code);
        if (holder==null){
            logger.error("not found code:"+code);
            return new RpcResult(code,false,"not found rpc command");
            //throw new Exception("not found rpc command:"+code);
        }
        RpcAnnotation anno = holder.method.getAnnotation(RpcAnnotation.class);
        if (anno==null){
            //throw new Exception("not found rpc command:"+code);
            return new RpcResult(code,false,"not found rpc command");
        }
        Class<?> cls = anno.req();
        Object obj = null;
        if (cls == Map.class){
            obj = gson.fromJson(json,new TypeToken<Map<String,String>>(){}.getType());
        }else{
            obj = gson.fromJson(json,cls);
        }
        Object result = null;
        try {
            long t = System.currentTimeMillis();
            result = holder.method.invoke(holder.handler,session,obj);
            t = System.currentTimeMillis() - t;
            logger.debug(String.format("cmd[code=%s,time=%s,session=%s,data=%s,result=%s]",code,t,session,obj,result));
            if (result==null){
                return new RpcResult(code);
            }
            String js = gson.toJson(result);
            return new RpcResult(code,js);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new RpcResult(code,false,e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return new RpcResult(code,false,e.getTargetException().getMessage());
        }
    }

}
