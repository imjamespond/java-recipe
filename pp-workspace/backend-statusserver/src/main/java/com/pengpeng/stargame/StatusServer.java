package com.pengpeng.stargame;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-12下午5:33
 */

public class StatusServer {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context-statusserver.xml"});

//        ChannelRpc cmd = ctx.getBean(ChannelRpc.class);
//        StatusServiceImpl impl = (StatusServiceImpl)ctx.getBean("statusService");
//        //impl.init();
//        cmd.enterScene(new Session("12345","fid"),"farm.1");
//        cmd.enterScene(new Session("98765","fid"),"farm.1");
//        cmd.enterScene(new Session("00000","fid"),"farm.1");
//        Collection<Session> sets= cmd.getMember(new Session("uid", "fid"), "farm.1");
//        System.out.println(sets);
//        RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
//        proxy.setServiceUrl("rmi://192.168.10.124:2299/status");
//        proxy.setServiceInterface(IBackendService.class);
//        proxy.afterPropertiesSet();
//        IBackendService service = (IBackendService)proxy.getObject();
//        String json = new Gson().toJson("farm.1");
//        System.out.println(json);
//        RpcResult result = null;//service.process("channel.enter",new Session("11111","fid"),json);
//        result = service.process("channel.getMember",new Session("11111","fid"),json);
//        System.out.println(result.getJson());
    }
}
