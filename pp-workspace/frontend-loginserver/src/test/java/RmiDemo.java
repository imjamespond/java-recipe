import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.annotation.EventAnnotation;
import com.pengpeng.stargame.vo.farm.FarmStateVO;
import com.pengpeng.stargame.vo.role.TokenReq;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-6下午12:18
 */
public class RmiDemo {
    private static class JsonBean{
        private Date dd;
        private String str;
    }
    public static void main(String[] args) throws Exception {
        FarmStateVO[] vos = new FarmStateVO[0];
        EventAnnotation ev = vos.getClass().getSuperclass().getAnnotation(EventAnnotation.class);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        JsonBean jj = new JsonBean();
        jj.dd = new Date();
        jj.str = "a";
        String json = gson.toJson(jj);
        TokenReq tk = gson.fromJson("{\"tokenKey\":\"107319_16D8DB9DB25FF662ABDCC94DCDDE7CA9\",\"cmd\":\"p.login\"}\n", TokenReq.class);
        System.out.println(json);
        JsonBean kk = gson.fromJson("{\"dd\":\"2013-5-9 17:35:30\",\"str\":\"a\"}",JsonBean.class);
        System.out.println(kk);

//		RmiProxyFactoryBean proxy1 = new RmiProxyFactoryBean();
//		proxy1.setServiceUrl("rmi://192.168.10.22:2299/status");
//		proxy1.setServiceInterface(IStatusService.class);
//		proxy1.afterPropertiesSet();
//        IStatusService statusService = (IStatusService)proxy1.getObject();
//        System.out.println(statusService);
//
//        RmiProxyFactoryBean proxy2 = new RmiProxyFactoryBean();
//        proxy2.setServiceUrl("rmi://192.168.10.124:1199/logic");
//        proxy2.setServiceInterface(IBackendService.class);
//        proxy2.afterPropertiesSet();
//        IBackendService logicService = (IBackendService)proxy2.getObject();
//        System.out.println(logicService);
//
//        RmiProxyFactoryBean proxy3 = new RmiProxyFactoryBean();
//        proxy3.setServiceUrl("rmi://192.168.10.124:1199/NodeClient");
//        proxy3.setServiceInterface(INodeClient.class);
//        proxy3.afterPropertiesSet();
//        INodeClient nodeService = (INodeClient)proxy3.getObject();
//        System.out.println(nodeService);

    }
    }
