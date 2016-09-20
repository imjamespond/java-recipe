package com.metasoft.flying.node.net;

import java.util.zip.Adler32;

import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.CodedOutputStream;
import com.metasoft.flying.node.RpcProto.RpcMessage;

@Sharable
public class RpcEncoder extends OneToOneEncoder {
	private static final Logger logger = LoggerFactory.getLogger(RpcEncoder.class);
    public RpcEncoder() {
        super();
    }

    @Override
    public Object encode(ChannelHandlerContext ctx, Channel channel, Object obj)
            throws Exception {
        if (!(obj instanceof RpcMessage)) {
            return obj;
        }
        RpcMessage message = (RpcMessage) obj;
        int size = message.getSerializedSize();
        
        //header + version + size + checksum
        int length = 4 + size + 4;
        
        ChannelBuffer buffer = new BigEndianHeapChannelBuffer(length);
        buffer.writeBytes("RPC0".getBytes());
        int writerIndex = buffer.writerIndex();
        CodedOutputStream output = CodedOutputStream.newInstance(
                buffer.array(), buffer.writerIndex(), buffer.writableBytes() - 4);
        message.writeTo(output);
        output.checkNoSpaceLeft();//write to buffer without move writerIndex

        buffer.writerIndex(writerIndex + size);//make readable
        Adler32 checksum = new Adler32();
        checksum.update(buffer.array(), buffer.arrayOffset(), buffer.readableBytes());
        buffer.writeInt((int) checksum.getValue());
        
        ChannelBuffer msgBuffer = new BigEndianHeapChannelBuffer(4+length);
        msgBuffer.writeInt(length);
        msgBuffer.writeBytes(buffer);
        logger.trace(String.format("length:%d, checksum:%d", length, (int)checksum.getValue()));
        return msgBuffer;
    }
    
    static {
    	logger.trace("RpcEncoder");
    }
}