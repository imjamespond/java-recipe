package com.metasoft.flying.net;

import java.io.IOException;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.CorruptedFrameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.vo.ChatRequest;
import com.metasoft.flying.vo.ChessRequest;
import com.metasoft.flying.vo.LoginRequest;
import com.metasoft.flying.vo.UserVO;
import com.metasoft.flying.vo.general.GeneralResponse;

public class NettyChannelPipelineFactory implements ChannelPipelineFactory {
	private static final Logger logger = LoggerFactory.getLogger(NettyChannelPipelineFactory.class);

	private int num;
	private long uid;
	private String name;
	
	public NettyChannelPipelineFactory(int num){
		this.num = num;
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline p = Channels.pipeline();
		p.addLast("decoder", new MessageDecoder());
		p.addLast("encoder", new MessageEncoder());
		p.addLast("handler", new SimpleChannelUpstreamHandler() {
			@Override
			public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
				//logger.debug("channelConnected");
				Channel channel = e.getChannel();

				LoginRequest req = new LoginRequest();
				req.setCmd("login.login");
				req.setSerial(1);
				req.setName("a"+String.valueOf(num));
				req.setGender(GeneralConstant.FEMALE);
				channel.write(req);
			}

			@Override
			public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
				System.err.println("channelDisonnected");
				// rpcPeer.channelDisconnected(e.getChannel());
			}

			@Override
			public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
				//logger.debug("messageReceived");
				Channel c = e.getChannel();
				Object obj = e.getMessage();
				if(obj instanceof GeneralResponse){
					GeneralResponse response = (GeneralResponse) obj;
					int serial = response.getSerial();
					
					switch(serial){
					case 1:
						UserVO vo = (UserVO) response.getData();
						uid = vo.getUserId();
						name = vo.getName();
						//logger.debug(vo.getName());
						
						if(vo.getGender() == 0){
							LoginRequest req1 = new LoginRequest();
							req1.setCmd("login.change");
							req1.setSerial(1);
							req1.setName("如花"+String.valueOf(num));
							req1.setGender(GeneralConstant.FEMALE);
							
							c.write(req1);							
						}
						
						ChatRequest req = new ChatRequest();
						req.setGroup(String.valueOf(uid));
						req.setCmd("chat.join");
						req.setSerial(3);
						
						c.write(req);
						break;
					case 2:
						//@SuppressWarnings("unchecked")
						//List<ItemVO> vo2 = (List<ItemVO>) response.getData();
						//logger.debug("List<ItemVO> "+vo2.size());
						break;
					case 3:
						//ChessRoomVO vo3= (ChessRoomVO) response.getData();
						//logger.debug("ChessRoomVO "+vo3.getUsers().size());
						
						ChessRequest req3 = new ChessRequest();
						req3.setCmd("chess.designate");
						req3.setPos(0);
						req3.setSerial(4);
						req3.setUserId(uid);
						
						c.write(req3);						
						break;
					}
				}
			}

			@Override
			public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
				if ((e.getCause() instanceof CorruptedFrameException)) {
					logger.debug("CorruptedFrameException");
				} else if (e.getCause() instanceof IOException) {
					IOException ioException = (IOException) e.getCause();
					logger.debug("IOException:" + ioException.getMessage());
				}
			}
		});
		return p;
	}

}