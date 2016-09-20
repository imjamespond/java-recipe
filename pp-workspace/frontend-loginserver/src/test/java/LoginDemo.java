import com.pengpeng.user.service.IUserSecurityService;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-18 下午3:08
 */
public class LoginDemo {


	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"context-frontend.xml"});

//		RmiProxyFactoryBean proxy1 = new RmiProxyFactoryBean();
//		proxy1.setServiceUrl("rmi://192.168.10.124:2299/status");
//		proxy1.setServiceInterface(IBackendService.class);
//		proxy1.afterPropertiesSet();
//		IBackendService logicService = (IBackendService)proxy1.getObject();

		// 107219
		IUserSecurityService dao = (IUserSecurityService)context.getBean("remotingUserSecurityService");
		String token = dao.buildUserToken(107319);
		//token ="107439_F2FF643014531AA3E82DCE459CA9F3CE";
		System.out.println(token);
		//logicService.process("p.login",null,token);

//		UserCmd userCmd =context.getBean(UserCmd.class);
//
//		TokenReq tokenReq = new TokenReq();
//		tokenReq.setToken(token);
//
//		SimpleContext simpleContext =new SimpleContext();
//		Object object =  userCmd.login(simpleContext, tokenReq).getData();
//		if(object instanceof UserInfo){
//			UserInfo userInfo = (UserInfo) object;
//			System.out.println(userInfo.getId());
//
//			RoleReq roleReq = new RoleReq();
//			roleReq.setName(userInfo.getNickName());
//			roleReq.setRoleType(userInfo.getSex());
//
//			Object obj = userCmd.createRole(simpleContext,roleReq).getData();
//			System.out.println("obj>>>>"+obj.toString());
//		}
//		if(object instanceof ServerVO){
//			ServerVO vo = (ServerVO) object;
//			System.out.println(vo.getIp()+" -"+vo.getPort()+" - "+vo.getPlayerId()+" - "+vo.getToken());
//		}

	}

	private static class SimpleContext implements ChannelHandlerContext {
		private Object attach;
		@Override
		public Channel getChannel() {
			return null;
		}

		@Override
		public ChannelPipeline getPipeline() {
			return null;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public ChannelHandler getHandler() {
			return null;
		}

		@Override
		public boolean canHandleUpstream() {
			return false;
		}

		@Override
		public boolean canHandleDownstream() {
			return false;
		}

		@Override
		public void sendUpstream(ChannelEvent e) {
		}

		@Override
		public void sendDownstream(ChannelEvent e) {
		}

		@Override
		public Object getAttachment() {
			return attach;
		}

		@Override
		public void setAttachment(Object attachment) {
			attach = attachment;
		}
	};
}
