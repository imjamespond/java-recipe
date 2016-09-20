package com.pengpeng.stargame;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

import java.io.ByteArrayInputStream;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-10-29下午7:54
 */
public class AMF3Decoder2 extends LengthFieldBasedFrameDecoder {
    protected final Logger log = Logger.getLogger(this.getClass());

    public AMF3Decoder2(){
        super(Integer.MAX_VALUE, 0,4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        log.info("start decodeing 包长度"+buffer.readableBytes());
        if (buffer.readableBytes() < 4) {
            return null;
        }
        buffer.markReaderIndex();
        int needBytes = buffer.readInt();
        log.info("start decodeing 数据"+needBytes);
        if (buffer.readableBytes() < needBytes) {
            buffer.resetReaderIndex();
            return null;
        }
        byte[] content = new byte[buffer.readableBytes()];
        buffer.readBytes(content);
        Amf3Input amf3Input = new Amf3Input(SerializationContext.getSerializationContext());
        amf3Input.setInputStream(new ByteArrayInputStream(content));
        log.info("decode end");
        return amf3Input.readObject();
    }
}
