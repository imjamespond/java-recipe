package com.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.LockRedis;
import com.pengpeng.stargame.dao.RedisDB;
import com.pengpeng.stargame.dao.RedisKeyValueDB;
import com.pengpeng.stargame.farm.dao.IFarmDecorateDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmProcessDao;
import com.pengpeng.stargame.farm.dao.impl.FarmDecorateDaoImpl;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.process.FarmProcessQueue;
import com.pengpeng.stargame.model.piazza.FamilyBank;
import com.pengpeng.stargame.piazza.container.IMoneyTreeRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyBankDao;
import com.pengpeng.stargame.piazza.dao.IMoneyTreeDao;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.rpc.IBackendService;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vo.api.FamilyRankVO;
import com.pengpeng.stargame.vo.farm.FarmIdReq;
import com.pengpeng.stargame.vo.fashion.FashionIdReq;
import com.pengpeng.stargame.vo.piazza.MoneyTreeReq;
import com.pengpeng.stargame.vo.room.RoomIdReq;
import com.pengpeng.user.LiveVideoInfo;
import org.junit.Ignore;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * User: mql
 * Date: 13-4-28
 * Time: 下午2:54
 */
@Ignore
public class Test {
    private  static Gson gson  = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public static void getRedisData(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context-logicserver.xml", "context-jredis.xml", "beanRefRuleDataAccess.xml"});
        Map<String, IRuleLoader> loaders = ctx.getBeansOfType(IRuleLoader.class);
        for (IRuleLoader loader : loaders.values()) {
            loader.load();
        }
//        IFarmPlayerDao farmPlayerDao= (IFarmPlayerDao) ctx.getBean("farmPlayDao");
//        FarmPlayer farmPlayer= farmPlayerDao.getFarmPlayer("maqile", System.currentTimeMillis());
//        System.out.println(gson.toJson(farmPlayerDao.getFarmPlayer("maqile",System.currentTimeMillis())));

        RedisTemplate<String, String> redisTemplate=ctx.getBean(RedisTemplate.class);

        redisTemplate.boundZSetOps("pza.member.family_1").incrementScore("4ccb46d0cb6d4d56bf056dfd1d9a9914",-2);


        ctx.close();

    }
    public static void addGoods(ClassPathXmlApplicationContext ctx){
        IFarmPackageDao farmPackageDao=(IFarmPackageDao)ctx.getBean("farmPackagedao");
        FarmPackage farmPackage=farmPackageDao.getBean("maqile");
//        farmPackage.addItem("items_11000",1000,30);
        farmPackageDao.saveBean(farmPackage);

    }
    public static void test() throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context-logicserver.xml", "context-jredis.xml", "beanRefRuleDataAccess.xml"});
        Map<String, IRuleLoader> loaders = ctx.getBeansOfType(IRuleLoader.class);
        for (IRuleLoader loader : loaders.values()) {
            loader.load();
        }
        IBackendService backendService= (IBackendService) ctx.getBean("logicService");

        Session session=  new Session("ad7e05026e94474a950040390dd91a08","");
        session.setScene("map_01");
        FarmIdReq req=new FarmIdReq();
//        req.setFid("b777408f4bc94b8a86020c0430f36ac7");
//        req.setPid("b777408f4bc94b8a86020c0430f36ac7");
//        req.setSeedId("items_11000");
//        req.setItemId("items_11000");
        req.setFieldId("1");
        req.setOid("02fcf5ea8182496f88cb0f3a46982576");
        String josn=gson.toJson(req);

        //购买
//        backendService.process("farmpkg.buy",session,josn);
//        获取农场信息
//        backendService.process("farm.get.info",session,josn);
//        种植
//        backendService.process("farm.plant",session,josn);

        //收获
//        backendService.process("farm.harvest",session,josn);

        // 获取订单列表
//        backendService.process("farm.get.orderlist",session,josn);

        // 完成订单
//        backendService.process("farm.finish.order",session,josn);


        ctx.close();
    }


      public  static void   testFashion() throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context-logicserver.xml", "context-jredis.xml", "beanRefRuleDataAccess.xml"});
        Map<String, IRuleLoader> loaders = ctx.getBeansOfType(IRuleLoader.class);
        for (IRuleLoader loader : loaders.values()) {
            loader.load();
        }

        IBackendService backendService= (IBackendService) ctx.getBean("logicService");

        Session session=  new Session("0e3de1be52cf49c1bedb696182834813","");
        session.setScene("map_01");
        FashionIdReq req=new FashionIdReq();
        req.setType("1");
        req.setFashionId("cb26925de4734213a825da69d6383bce");
        req.setItemId("items_30001");

        String josn=gson.toJson(req);

        //取得玩家指定类型的仓库
//        backendService.process("fashionpkg.get.fortype",session,josn);

        backendService.process("fashionpkg.remove",session,josn);

//        backendService.process("fashionpkg.buy",session,josn);

//        backendService.process("fashion.change",session,josn);

    }

    public  static void   testRoom() throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context-logicserver.xml", "context-jredis.xml", "beanRefRuleDataAccess.xml"});
        Map<String, IRuleLoader> loaders = ctx.getBeansOfType(IRuleLoader.class);
        for (IRuleLoader loader : loaders.values()) {
            loader.load();
        }

        IBackendService backendService= (IBackendService) ctx.getBean("logicService");

        Session session=  new Session("0e3de1be52cf49c1bedb696182834813","");
        session.setScene("map_01");
        RoomIdReq req=new RoomIdReq();
        req.setItemId("items_25000");
        req.setNum(1);

        String josn=gson.toJson(req);


        backendService.process("room.sale",session,josn);


    }

    public  static void   testMoneyTree() throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context-logicserver.xml", "context-jredis.xml", "beanRefRuleDataAccess.xml"});
        Map<String, IRuleLoader> loaders = ctx.getBeansOfType(IRuleLoader.class);
        for (IRuleLoader loader : loaders.values()) {
            loader.load();
        }

        IBackendService backendService= (IBackendService) ctx.getBean("logicService");

        IMoneyTreeDao moneyTreeDao= (IMoneyTreeDao) ctx.getBean("moneytreeDao");

        IMoneyTreeRuleContainer moneyTreeRuleContainer = (IMoneyTreeRuleContainer) ctx.getBean("moneyTreeRuleContainer");

        Session session=  new Session("0e3de1be52cf49c1bedb696182834813","");
        session.setScene("map_01");
        MoneyTreeReq req=new MoneyTreeReq();


        String josn=gson.toJson(req);



//        backendService.process("",session,josn);
//        moneyTreeDao.getMoneyTree("family_1",new Date());?
//        moneyTreeRuleContainer.getRipeTime("family_1",new Date());

        System.out.println(DateUtil.getDateFormat(moneyTreeRuleContainer.getRipeTime("family_1",new Date()),null));


    }

    public  static void   testTask() throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context-logicserver.xml", "context-jredis.xml", "beanRefRuleDataAccess.xml"});
        Map<String, IRuleLoader> loaders = ctx.getBeansOfType(IRuleLoader.class);
        for (IRuleLoader loader : loaders.values()) {
            loader.load();
        }

        IBackendService backendService= (IBackendService) ctx.getBean("logicService");

        ITaskRuleContainer taskRuleContainer= (ITaskRuleContainer) ctx.getBean("taskRuleContainerImpl");

        taskRuleContainer.autoAcceptTask("ad7e05026e94474a950040390dd91a08","");


    }

    private static void testRedisDb(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context-logicserver.xml", "context-jredis.xml", "beanRefRuleDataAccess.xml"});

        RedisDB redisDB= (RedisDB) ctx.getBean("redisdb");


        System.out.println(redisDB.getRedisTemplate("eventLog.307f79db804c48b381687b603b82ac01").boundHashOps("eventLog.307f79db804c48b381687b603b82ac01").entries());
        System.out.println(redisDB.getRedisTemplate("4").boundValueOps("4").get());

    }

    public  static void   testLock() throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context-logicserver.xml", "context-jredis.xml", "beanRefRuleDataAccess.xml"});
        Map<String, IRuleLoader> loaders = ctx.getBeansOfType(IRuleLoader.class);
        for (IRuleLoader loader : loaders.values()) {
            loader.load();
        }
        LockRedis lockRedis= (LockRedis) ctx.getBean("lockRedis");

        System.out.println( lockRedis.lock("maqile"));
        lockRedis.unlock("maqile");
    }

    public  static void   testValue() throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context-logicserver.xml", "context-jredis.xml", "beanRefRuleDataAccess.xml"});

    }

    public static void main(String[] args) {
        try {

            int a=10;
            int b=a+=Math.abs(-100000000000l);
            System.out.println(b);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
