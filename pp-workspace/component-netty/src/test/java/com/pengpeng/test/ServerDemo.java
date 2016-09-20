package com.pengpeng.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.ServerConfig;
import org.jboss.netty.channel.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: 林佛权
 * Date: 12-12-26
 * Time: 下午12:14
 * To change this template use File | Settings | File Templates.
 */
public class ServerDemo {

    private static ApplicationContext ctx;
    private static void init(){
        if (ctx==null){
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServerConfig.class);
            context.scan("com.com.pengpeng.stargame.*", "org.springframework");
            ctx = context;
        }
    }

    public static void testPlayerDao(){
        init();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//        RedisTemplate<String,String> dao = (RedisTemplate)ctx.getBean("redisTemplate");
//        Player p = new Player();
//        p.setId("7f3343f64f5d4ad9a8277ffb2da7997b");
////        dao.boundValueOps("7f3343f64f5d4ad9a8277ffb2da7997b").set(gson.toJson(p));
////        Object obj = dao.boundValueOps("7f3343f64f5d4ad9a8277ffb2da7997b").get();
////        p = gson.fromJson((String)obj,Player.class);
//        //Player player = dao.getBean("7f3343f64f5d4ad9a8277ffb2da7997b");
////        System.out.println(obj);
//        ListOperations<String,String> ops = dao.opsForList();
//        ops.leftPush("bbb", gson.toJson(p));
//        ops.leftPush("bbb",gson.toJson(p));
//        ops.leftPush("bbb",gson.toJson(p));
//        List<String> list = ops.range("bbb",0,ops.size("bbb"));
//        for(String item:list){
//            System.out.println(gson.fromJson(item,Player.class));
//        }
    }
//    public static void testPlayerDao(){
//        init();
//        PlayerDaoImpl dao = ctx.getBean(PlayerDaoImpl.class);
//        Player player = dao.getBean("f65441bb-0f5c-451c-8fdc-5212033be6f3");
//        final  RedisTemplate template =  dao.getTemplate();
//        Object objs = template.boundValueOps("uid.f65441bb-0f5c-451c-8fdc-5212033be6f3").get();
//        Object obj = template.execute(new RedisCallback() {
//            @Override
//            public Object doInRedis(RedisConnection connection)
//                    throws DataAccessException {
//                RedisSerializer ks = template.getKeySerializer();
//                byte[] keys = ks.serialize("uid.f65441bb-0f5c-451c-8fdc-5212033be6f3");
//                if (connection.exists(keys)) {
//                    byte[] value = connection.get(keys);
//                    return template.getValueSerializer().deserialize(value);
//                }
//                return null;
//            }
//        });
//
//        //Player player = dao.getBean("7f3343f64f5d4ad9a8277ffb2da7997b");
//        System.out.println(obj);
//
//    }
//    public static void testFarmDao(){
//        init();
//        FarmItemDaoImpl dao = ctx.getBean(FarmItemDaoImpl.class);
//        FarmItem farm = new FarmItem();
//        farm.setCode("aaabb");
//        dao.insertBean("farm.pkg.xxx",farm);
//        dao.insertBean("farm.pkg.xxx",new FarmItem());
//        List<FarmItem> list = null;//dao.getFarmItem("");
//        System.out.println(list);
//    }
    public static void testCreateRole(){
        init();
//        IModuleContainer module = ctx.getBean(IModuleContainer.class);
//        UserCmd cmd = ctx.getBean(UserCmd.class);
//        RoleReq req = new RoleReq();
//        req.setName("fangyaoxia");
//        req.setRoleType(1);
//        Response rsp = cmd.createRole(channel, req);
//        System.out.println(rsp);
    }
    public static void testLogin(){
        init();
//        IModuleContainer module = ctx.getBean(IModuleContainer.class);
//        UserCmd cmd = ctx.getBean(UserCmd.class);
//        IUserSecurityService dao = (IUserSecurityService)ctx.getBean("remotingUserSecurityService");
//        String token = dao.buildUserToken(1016);
//        TokenReq req = new TokenReq();
//        req.setToken("1888154_06A43D8D1FAADC71EF24A333717F7EED");
//        Response rsp = cmd.login(channel, req);
    }
    public static void testRename(){
        init();
//        IModuleContainer module = ctx.getBean(IModuleContainer.class);
//        FriendModuleImpl fm = module.getManager("c764a14e-92c8-49e8-be9b-5a49b6800cc0", FriendModuleImpl.class);
//        Response rsp = fm.getFriendRoleInfo("c764a14e-92c8-49e8-be9b-5a49b6800cc0");
    }
    private static ChannelHandlerContext channel = new Channels();
    public static void testModule(){
        init();
//        IModuleContainer module = ctx.getBean(IModuleContainer.class);
//        Player p = new Player();
//        p.setId(UUID.randomUUID().toString());
//        IPlayerModule m =  module.getManager(p.getId(), PlayerModuleImpl.class);
//        long t = System.currentTimeMillis();
//        t = System.currentTimeMillis() - t;
//        System.out.println(t);
//        System.out.println(m);
    }
//    public static void testredis(){
//        init();
//        FarmItemDaoImpl dao = ctx.getBean(FarmItemDaoImpl.class);
//        long t = System.currentTimeMillis();
//        for(int i=0;i<100000;i++){
//        FarmItem bean = new FarmItem();
//        bean.setId("abcd"+i);
//        bean.setCode("11111");
//        bean.setNum(1212);
//        dao.insertBean("farm.pkg.xxx",bean);
////        System.out.println(dao);
////        FarmItem item = dao.getBean("abc");
////        System.out.println(item);
//        }
//        System.out.println((System.currentTimeMillis()-t));
//    }

    private static class Channels implements ChannelHandlerContext{
        private Object token;
        @Override
        public Channel getChannel() {
            return null;  //TODO:方法需要实现
        }

        @Override
        public ChannelPipeline getPipeline() {
            return null;  //TODO:方法需要实现
        }

        @Override
        public String getName() {
            return null;  //TODO:方法需要实现
        }

        @Override
        public ChannelHandler getHandler() {
            return null;  //TODO:方法需要实现
        }

        @Override
        public boolean canHandleUpstream() {
            return false;  //TODO:方法需要实现
        }

        @Override
        public boolean canHandleDownstream() {
            return false;  //TODO:方法需要实现
        }

        @Override
        public void sendUpstream(ChannelEvent e) {
            //TODO:方法需要实现
        }

        @Override
        public void sendDownstream(ChannelEvent e) {
            //TODO:方法需要实现
        }

        @Override
        public Object getAttachment() {
            return token;  //TODO:方法需要实现
        }

        @Override
        public void setAttachment(Object attachment) {
            this.token = attachment;
        }
    }

    public static void testSiteDao(){
        init();
//        UserSecurityDao dao = ctx.getBean(UserSecurityDao.class);
//        List<UserProfile> list = dao.getFriendUser(1016);
//        System.out.println(list);

    }

    public static void main(String[] args){
        init();
//        Jedis jedis = new Jedis("192.168.10.125");
//        String val = jedis.get("aaa");
//        System.out.println(val);
        //testredis();
//        testModule();
//        testLogin();
        testRename();
//        testCreateRole();
//        testFarmDao();
//        testSiteDao();
//        testPlayerDao();
    }
}
