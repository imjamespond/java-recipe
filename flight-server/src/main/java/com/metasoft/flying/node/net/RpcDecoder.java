package com.metasoft.flying.node.net;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.zip.Adler32;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.CompositeChannelBuffer;
import org.jboss.netty.buffer.HeapChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.CorruptedFrameException;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.node.RpcProto.RpcMessage;

@Sharable
public class RpcDecoder extends FrameDecoder {
	private static final Logger logger = LoggerFactory.getLogger(RpcDecoder.class);

	private static int HEADER_LEN = 4;// indicate 4 bytes
	private static int MAX_HEADER_LEN = 1024 * 1024;
	
    public Object doDecode(Object obj, int length)
            throws Exception {
    	//由于上次read packet有剩余就会将剩余的和新的拼揍成CompositeChannelBuffer,
    	//于是不能用array()这种连续data的操作
        if (obj instanceof HeapChannelBuffer) {
        	HeapChannelBuffer buffer = (HeapChannelBuffer) obj;
            if (buffer.readableBytes() > 8) {//include version n checksum
                String version = buffer.toString(buffer.readerIndex(), 4, Charset.defaultCharset());
                if (version.equals("RPC0")) {
                	int checksumLen = length - 4;//exclude header n checksum
                    Adler32 adler32 = new Adler32();
                    adler32.update(buffer.array(),
                            buffer.arrayOffset() + buffer.readerIndex(),
                            checksumLen);
                    logger.trace(String.format("offset:%d, len:%d, checksum:%d", buffer.arrayOffset() + buffer.readerIndex(), checksumLen, (int)adler32.getValue()));
                    buffer.markReaderIndex();
                    buffer.readerIndex(buffer.readerIndex() + checksumLen);//move to checksum
                    int checksum = buffer.readInt();//the end
                    if (checksum == (int)adler32.getValue()) {
                        buffer.resetReaderIndex();
                        RpcMessage message = RpcMessage.newBuilder().mergeFrom(
                                buffer.array(),
                                buffer.arrayOffset() + buffer.readerIndex() + 4,
                                length - 8).build();////exclude version n checksum
                        buffer.readerIndex(buffer.readerIndex() + length);//move to end of readable
                        return message;
                    }
                }
            }
        } else if(obj instanceof CompositeChannelBuffer){
        	CompositeChannelBuffer buffer = (CompositeChannelBuffer) obj;
            if (buffer.readableBytes() > 8) {//include version n checksum
                String version = buffer.toString(buffer.readerIndex(), 4, Charset.defaultCharset());
                if (version.equals("RPC0")) {
                	int checksumLen = length - 4;//exclude header n checksum
                	
                	ByteBuffer byteBuffer = ByteBuffer.allocate(length);
    				buffer.readBytes(byteBuffer.array());//move to end
                	
                    Adler32 adler32 = new Adler32();
                    adler32.update(byteBuffer.array(), 0, checksumLen);
                    logger.trace(String.format("checksum:%d",(int)adler32.getValue()));

                    int checksum = byteBuffer.getInt(checksumLen);//the end
                    if (checksum == (int)adler32.getValue()) {
                        RpcMessage message = RpcMessage.newBuilder().mergeFrom(
                        		byteBuffer.array(), 4, length - 8).build();//exclude version, exclude version n checksum
                        return message;
                    }
                }
            }        	
        }
        return null;
    }

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		buffer.markReaderIndex();// mark current position
		int bytesAvailable = buffer.readableBytes();
		logger.trace("bytesAvailable:" + bytesAvailable);

		if (bytesAvailable >= HEADER_LEN) {
			int length = buffer.readInt();
			logger.trace("length:" + length);

			if (buffer.readableBytes() >= length) {// a complete msg			
				Object obj = doDecode(buffer, length);
				logger.trace("readableBytes left:" + buffer.readableBytes());
				return obj;
			} else if (length > MAX_HEADER_LEN || length < 0) {
				throw new CorruptedFrameException("negative pre-adjustment length field: " + length);
			} else if (length == 0) {
				//return new HeartbeatPackage();
			} else {
				// an incomplete msg
				buffer.resetReaderIndex();// return to the position marked
				logger.trace("an incomplete msg:" + bytesAvailable);
			}
		}
		return null;
	}
	
	
    static {
    	logger.trace("RpcDecoder");
    }
}
