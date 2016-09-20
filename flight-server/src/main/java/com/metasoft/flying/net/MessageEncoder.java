package com.metasoft.flying.net;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.metasoft.flying.util.AMF3Utils;

import flex.messaging.io.SerializationContext;

@Component
@Scope("prototype")
public class MessageEncoder extends OneToOneEncoder {
	private static final int HEADER_LENGTH = 4;

	// private static final Logger logger =
	// LoggerFactory.getLogger(MessageEncoder.class);

	SerializationContext serializationContext = null;

	public MessageEncoder() {
		serializationContext = new SerializationContext();
	}

	@Override
	protected Object encode(ChannelHandlerContext arg0, Channel arg1, Object data) throws Exception {

		byte[] amfObj = AMF3Utils.getAmf3ByteArrayWithoutCompress(data);

		if (amfObj != null && amfObj.length > 0) {
			ChannelBuffer buffer = ChannelBuffers.buffer((HEADER_LENGTH + amfObj.length));
			buffer.writeInt(amfObj.length);
			buffer.writeBytes(amfObj);

			return buffer;
		}
		return null;
	}

}
