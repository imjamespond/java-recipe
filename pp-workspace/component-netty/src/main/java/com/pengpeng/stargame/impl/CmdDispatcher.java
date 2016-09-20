package com.pengpeng.stargame.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.AbstractHandler;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.req.BaseReq;

/**
 * 命令分发器
 *
 */
@Component
public class CmdDispatcher {

    private final Logger log = Logger.getLogger(this.getClass());
    private static final Logger logger = Logger.getLogger("action");

    @Autowired
    private ApplicationContext springContext;

    // 初始化标记
//    private final AtomicBoolean hasInit = new AtomicBoolean(false);

    // 缓存消息处理器
    private final Map<String, CommandHandlerHolder> handlers = new HashMap<String, CommandHandlerHolder>();

    @PostConstruct
    private void init() {
    	log.info("START|Init HandlerMethod Count...");
//        hasInit.set(true);
        // 从spring上下文取出所有消息处理器
        Map<String,AbstractHandler> handlerMap = springContext.getBeansOfType(AbstractHandler.class);
        for (Object obj : handlerMap.values()) {
        	AbstractHandler handler = (AbstractHandler) obj;
            Class<?> clazz = handler.getClass();

            // 找到所有处理方法
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                // 判断处理方法是否符合要求
                CmdAnnotation annotation = method.getAnnotation(CmdAnnotation.class);
                if (annotation==null){
                	continue;
                }
                //Class<?>[] paramTypes = method.getParameterTypes();
                
                // 把处理方法加入缓存中
                CommandHandlerHolder holder = new CommandHandlerHolder(handler, method);
                handlers.put(annotation.cmd(), holder);
                log.info("START|Init HandlerMethod: " + clazz.getSimpleName() + " --> " + method.getName()+" --> "+annotation.name());
            }
        }
        log.info("START|Init HandlerMethod Count: " + handlers.size());
    }

    public void dispatch(BaseReq command, ChannelHandlerContext context) {
        // 如果处理器未缓存,先初始化。在第一次请求时触发
//        if (!hasInit.get()) {
//            init();
//        }
        if (null == context||!context.getChannel().isConnected()){
            log.warn("REQUEST|ctx|disconnect|" + command);
            return ;
        }
        // 取出处理方法,调用
        CommandHandlerHolder holder = handlers.get(command.getCmd());
        if (null == holder || null == holder.method || null == holder.handler) {
            log.warn("RECIVE|No handler or method for head:" + command.getCmd());
            return;
        }
        long time = System.currentTimeMillis();
        CmdAnnotation anno = holder.method.getAnnotation(CmdAnnotation.class);
        try {
            //Object[] args = this.prepareArguments(holder.method, command, context);
//            if (null != args) {
        	//TODO检查,指令有效性,数据有没有被篡改
			if (!anno.cmd().equalsIgnoreCase(command.getCmd())) {
				//如果参数的指令编号,和方法的编号不符,则是非法参数
				log.warn("RECIVE|cmd|error|" + command.getCmd());
				return;
			}
            Object returnValue = null;
//            if (anno.cmd()==10001){//注册
//                returnValue = holder.method.invoke(holder.handler, context, command);
//
//            }else
            if (anno.channel()){//登录
                returnValue = holder.method.invoke(holder.handler, context, command);
            }else {
                returnValue = holder.method.invoke(holder.handler,context.getAttachment(),command);
            }
			if (null == returnValue){
				return ;
			}
			if ((returnValue instanceof Response)==false) {
				log.warn("RECIVE|rsp|error|" + returnValue);
				return ;
			}

			if (null == context||!context.getChannel().isConnected()){
				log.warn("RECIVE|ctx|disconnect|" + returnValue);
				return ;
			}
			
//			if (returnValue.getClass().isAssignableFrom(anno.rsp() )) {//无用的代码
//				log.error("SEND|rsp|check|" + command.getCmd() + "|"
//						+ returnValue);
//				return;
//			}
			Response rsp = (Response) returnValue;
            rsp.setCode(anno.cmd());
			if (rsp.getData() == null) {
				//log.info("SEND|" + command.getCmd() + "|" + returnValue);
				context.getChannel().write(returnValue);
				return;
			}
			if (anno.check()) {
				// 如果检查返回数据类型
				if (anno.vo() != rsp.getData().getClass()) {
					//log.error("SEND|vo|check|" + command.getCmd() + "|"+ returnValue);
					return;
				}
			}
			//log.info("SEND|" + command.getCmd() + "|" + returnValue);
			context.getChannel().write(returnValue);
        		
        } catch (IllegalArgumentException e) {
            log.error("INVOKE|Error IllegalArgumentException", e);
        } catch (IllegalAccessException e) {
            log.error("INVOKE|Error IllegalAccessException", e);
        } catch (InvocationTargetException e) {
            log.trace("INVOKE|Error InvocationTargetException", e.getTargetException());
            Response rsp = Response.newError(e.getTargetException().getMessage());
            rsp.setCode(anno.cmd());
            context.getChannel().write(rsp);
        }finally{
            time = System.currentTimeMillis() - time;
            logger.info(command);
            //log.debug("cmd time:"+time+"/ms");
        }
    }

    /**
     * 参数包装
     */
//    private Object[] prepareArguments(Method method, AbstractCmd command, ChannelHandlerContext context) {
//        Class<?>[] parameterTypes = method.getParameterTypes();
//        Object[] args = new Object[parameterTypes.length];
//        args[0] = command;
//        String[] bodies = command.getBodies();
//        // 如果参数长度不对应,抛出错误信息
//        if (bodies.length < parameterTypes.length - 1) {
//            log.warn("ARGUMENT|Not match. expect:" + (parameterTypes.length - 1) + " actual:" + bodies.length);
//            return null;
//        }
//        int index = 0;
//        for (int i = 1; i < args.length; i++) {
//            if (parameterTypes[i].equals(int.class) || parameterTypes[i].equals(Integer.class)) {
//                args[i] = Integer.valueOf(bodies[index++]);
//            } else if (parameterTypes[i].equals(String.class)) {
//                args[i] = bodies[index++];
//            } else if (parameterTypes[i].equals(long.class) || parameterTypes[i].equals(Long.class)) {
//                args[i] = Long.valueOf(bodies[index++]);
//            } else if (parameterTypes[i].equals(float.class) || parameterTypes[i].equals(Float.class)) {
//                args[i] = Float.valueOf(bodies[index++]);
//            } else if (parameterTypes[i].equals(double.class) || parameterTypes[i].equals(Double.class)) {
//                args[i] = Double.valueOf(bodies[index++]);
//            } else if (parameterTypes[i].equals(byte.class) || parameterTypes[i].equals(Byte.class)) {
//                args[i] = Byte.valueOf(bodies[index++]);
//            } else if (parameterTypes[i].equals(short.class) || parameterTypes[i].equals(Short.class)) {
//                args[i] = Short.valueOf(bodies[index++]);
//            } else if (parameterTypes[i].equals(ChannelHandlerContext.class)) {
//                args[i] = context;
//            }
//        }
//        return args;
//    }

    /**
     * 消息处理方法容器
     */
    class CommandHandlerHolder {
        public CommandHandlerHolder(AbstractHandler h, Method m) {
            this.handler = h;
            this.method = m;
        }

        public AbstractHandler handler;
        public Method method;
    }

}

