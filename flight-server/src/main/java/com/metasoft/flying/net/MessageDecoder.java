package com.metasoft.flying.net;

import java.net.InetSocketAddress;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.metasoft.flying.util.AMF3Utils;
import com.metasoft.flying.vo.HeartbeatPackage;

@Component
@Scope("prototype")
public class MessageDecoder extends FrameDecoder {
	private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

	private static int HEADER_LEN = 4;// indicate 4 bytes
	private static int MAX_HEADER_LEN = 1024 * 1024;

	public MessageDecoder() {
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {

		buffer.markReaderIndex();// mark current position
		int bytesAvailable = buffer.readableBytes();

		if (bytesAvailable >= HEADER_LEN) {
			int length = buffer.readInt();
			logger.trace("length:" + length);

			if (length > MAX_HEADER_LEN || length < 0) {
				InetSocketAddress addr = (InetSocketAddress) channel.getRemoteAddress();
				//throw new CorruptedFrameException("negative pre-adjustment length field: " + length);
				logger.warn("wrong header ip:{} length:{}, avail:{}", addr.getHostName() , length, buffer.readableBytes());
				buffer.discardReadBytes();
				channel.setReadable(false);
				channel.close();
			} else if (length == 0) {
				return new HeartbeatPackage();
			} else if (buffer.readableBytes() >= length) {// a complete msg

				byte[] content = new byte[length];
				buffer.readBytes(content);
				bytesAvailable = buffer.readableBytes();
				// length = 0;
				logger.trace("readableBytes left:{}" , bytesAvailable);
				return AMF3Utils.getObjectFromAmf3ByteArrayWithoutDecompress(content);

			} else {// an incomplete msg
				buffer.resetReaderIndex();// return to the position marked
				logger.trace("an incomplete msg:{}" , bytesAvailable);
			}
		}

		return null;
	}
}
