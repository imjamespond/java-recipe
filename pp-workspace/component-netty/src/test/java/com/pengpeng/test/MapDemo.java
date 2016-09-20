package com.pengpeng.test;

import com.pengpeng.ServerConfig;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 地图,场景测试
 * @author jinli.yuan@com.pengpeng.com
 * @since 13-3-22 上午11:35
 */
public class MapDemo {
	private static ApplicationContext ctx;
	private static void init(){
		if (ctx==null){
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServerConfig.class);
			context.scan("com.com.pengpeng.stargame", "org.springframework");
			ctx = context;
		}
	}

	private static ChannelHandlerContext channel = new Channels();

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

	public static void testLogin(){
		init();
//		IModuleContainer module = ctx.getBean(IModuleContainer.class);
//		UserCmd cmd = ctx.getBean(UserCmd.class);
//		IUserSecurityService dao = (IUserSecurityService)ctx.getBean("remotingUserSecurityService");
//		String token = dao.buildUserToken(107219);
//		System.out.println("token>>>"+token);
//
//		TokenReq req = new TokenReq();
//		req.setToken(token);
//		Response rsp = cmd.login(channel, req);
//		System.out.println("rsp >>>"+rsp.toString());
	}

	public static void testCreateRole(){
		init();
//		IModuleContainer module = ctx.getBean(IModuleContainer.class);
//		UserCmd cmd = ctx.getBean(UserCmd.class);
//		RoleReq req = new RoleReq();
//		req.setName("mikk30");
//		req.setRoleType(1);
//		Response rsp = cmd.createRole(channel, req);
//		System.out.println(rsp);
	}

	public static void testGetCurrendMap(){
		init();
//		ISceneRuleContainer module = (ISceneRuleContainer) ctx.getBean(ISceneRuleContainer.class);
//		IUserSecurityService dao = (IUserSecurityService)ctx.getBean("remotingUserSecurityService");

//		MapCmd cmd = ctx.getBean(MapCmd.class);
//
//		UserCmd userCmd = ctx.getBean(UserCmd.class);
//		Response rsp = userCmd.getPlayer("7f3343f64f5d4ad9a8277ffb2da7997b",null);
//		PlayerVO playerVO = (PlayerVO) rsp.getData();
//		System.out.println("nickName>>>"+playerVO.getNickName());
//
//		Player player = null;
//		MapReq req = null;

		//cmd.getCurrendMap(player,req);
	}

	public static void main(String[] args) {
		//testLogin();
		//testGetCurrendMap();
	}
}
