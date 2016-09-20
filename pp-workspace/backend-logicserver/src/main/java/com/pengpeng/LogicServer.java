package com.pengpeng;


import com.pengpeng.stargame.manager.IRuleLoader;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-15下午3:59
 */
public class LogicServer {

    public static void main(String[] args) throws Exception {
//        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
//        ctx.scan("com.pengpeng.stargame", "org.springframework");
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context-logicserver.xml","context-jredis.xml","beanRefRuleDataAccess.xml"});
        Map<String,IRuleLoader> loaders = ctx.getBeansOfType(IRuleLoader.class);
        for(IRuleLoader loader:loaders.values()){
            loader.load();
        }
        System.out.println("服务器加载完成.");
//        Map<String,Object> map = ctx.getBeansWithAnnotation(EventAnnotation.class);
//        System.out.println(map);
//        RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
//        proxy.setServiceUrl("rmi://192.168.10.124:1199/logic");
//        proxy.setServiceInterface(IBackendService.class);
//        proxy.afterPropertiesSet();
//        IBackendService service = (IBackendService)proxy.getObject();
//        String json = new Gson().toJson("farm.1");
//        System.out.println(json);
//        RpcResult result = null;//service.process("channel.enter",new Session("11111","fid"),json);
//        result = service.process("p.enter",new Session("11111","fid"),json);
//        System.out.println(result.getJson());
    }
}
