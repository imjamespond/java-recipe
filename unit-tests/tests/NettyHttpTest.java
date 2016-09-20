package test;


import java.util.Map.Entry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

// 测试coder 和 handler 的混合使用
public class NettyHttpTest {
	private static Logger logger = LoggerFactory.getLogger(NettyHttpTest.class);

	public void start(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); 
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap(); 
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) 
					.childHandler(new ChannelInitializer<SocketChannel>() { 
								@Override
								public void initChannel(SocketChannel ch) throws Exception {
									// 都属于ChannelOutboundHandler，逆序执行
									ch.pipeline().addLast(new HttpResponseEncoder());
									ch.pipeline().addLast(new StringEncoder());
									
									// 都属于ChannelIntboundHandler，按照顺序执行
									ch.pipeline().addLast(new HttpRequestDecoder());
									ch.pipeline().addLast(new StringDecoder());
									ch.pipeline().addLast(new BusinessHandler());
								}
							}).option(ChannelOption.SO_BACKLOG, 128) 
					.childOption(ChannelOption.SO_KEEPALIVE, true); 

			ChannelFuture f = b.bind(port).sync(); 

			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		logger.debug("launching...");
		
		NettyHttpTest server = new NettyHttpTest();
		//server.start(8000);
	}
}
//把String转换成httpResponse
class StringEncoder extends ChannelOutboundHandlerAdapter {
	private Logger	logger	= LoggerFactory.getLogger(StringEncoder.class);

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		logger.info("StringEncoder response to client.");
		String serverMsg = (String) msg;

		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(serverMsg
				.getBytes()));
		response.headers().set(CONTENT_TYPE, "text/plain");
		response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
		response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		ctx.write(response);
		ctx.flush();
	}
}
class StringDecoder extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(StringDecoder.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.debug("msg type: {}", msg.getClass());
		if (msg instanceof DefaultHttpRequest) {
			HttpRequest request = (DefaultHttpRequest) msg;
			HttpHeaders headers = request.headers();
			for(Entry<String, String> entry :headers.entries()){
				if(entry.getKey()!=null && entry.getValue()!=null){
					logger.debug("header {}: {}", entry.getKey(), entry.getValue());
				}
			}
			//HttpHeaders.getContentLength(request);
		}

		if (msg instanceof HttpContent) {
			HttpContent content = (HttpContent) msg;
			logger.debug("content: {}",new String( content.content().array()));
		}
	}

}
class BusinessHandler extends ChannelInboundHandlerAdapter {
	private Logger	logger	= LoggerFactory.getLogger(BusinessHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String clientMsg = "client said : " + (String) msg;
		logger.info("BusinessHandler read msg from client :" + clientMsg);
		ctx.write("I am very OK!");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
}


