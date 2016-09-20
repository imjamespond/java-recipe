package com.metasoft.flying.node.net;

import java.nio.charset.Charset;
import java.util.zip.Adler32;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.node.RpcProto.RpcMessage;

public class RpcDecoder implements ProtocolDecoder {
	private static final Logger logger = LoggerFactory.getLogger(RpcDecoder.class);

	private static int HEADER_LEN = 4;// indicate 4 bytes
	private static int MAX_HEADER_LEN = 1024 * 1024;
	
	private byte[] leftBytes;
	
    public Object doDecode(IoBuffer buffer, int length)
            throws Exception {
            if (buffer.remaining() > 8) {
            	
            	//read 4 bytes
            	//byte[] bytes = new byte[4];
            	//buffer.get(bytes);//will move position
                String version = new String(buffer.array(),buffer.position(),4, Charset.defaultCharset());              
                if (version.equals("RPC0")) {
                	int checksumLen = length - 4;//exclude header n checksum
                    Adler32 adler32 = new Adler32();
                    adler32.update(buffer.array(),
                            buffer.arrayOffset() + buffer.position(),
                            checksumLen);
                    logger.debug(String.format("offset:%d, len:%d, checksum:%d", buffer.arrayOffset() + buffer.position(), checksumLen, (int)adler32.getValue()));
                    int markReaderIndex = buffer.position();
                    buffer.position(buffer.position() + checksumLen);//move to checksum
                    int checksum = buffer.getInt();//the end
                    if (checksum == (int)adler32.getValue()) {
                        buffer.position(markReaderIndex);
                        RpcMessage message = RpcMessage.newBuilder().mergeFrom(
                                buffer.array(),
                                buffer.arrayOffset() + buffer.position() + 4,
                                length - 8).build();
                        buffer.position(buffer.position() + length);
                        return message;
                    }
                }
            }
        return buffer;
    }
    
	@Override
	public void decode(IoSession session, IoBuffer ioBuffer, ProtocolDecoderOutput out) throws Exception {
		if(leftBytes!=null){		
			logger.trace("allocate left:" + leftBytes.length + " remain:"+ ioBuffer.remaining());
			SimpleBufferAllocator bufAlloc = new SimpleBufferAllocator();
			ioBuffer = bufAlloc.allocate(leftBytes.length + ioBuffer.remaining(), true);
			leftBytes = null;
		}

		while(true){
			int bytesAvailable = ioBuffer.remaining();
			logger.trace("bytesAvailable:" + bytesAvailable);
	
			if (bytesAvailable >= HEADER_LEN) {
				int length = ioBuffer.getInt();
				logger.trace("length:" + length);
	
				if (ioBuffer.remaining() >= length) {// a complete msg
					Object obj = doDecode(ioBuffer, length);
					logger.trace("readableBytes left:" + ioBuffer.remaining());
					out.write(obj);
				} else if (length > MAX_HEADER_LEN || length < 0) {
					throw new ProtocolDecoderException("negative pre-adjustment length field: " + length);
				} else if (length == 0) {
					break;
				} else {// an incomplete msg
					leftBytes = new byte[bytesAvailable];
					ioBuffer.position(ioBuffer.position() - 4);//include header
					ioBuffer.get(leftBytes);
					logger.trace("an incomplete msg:" + bytesAvailable);
					break;
				}
			}else{
				leftBytes = new byte[bytesAvailable];
				//ioBuffer.position(ioBuffer.position() - 4);//include header
				ioBuffer.get(leftBytes);
				logger.trace("an incomplete header:" + bytesAvailable);
				break;
			}
		}
		out.write(null);
	}

	@Override
	public void dispose(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
    static {
    	logger.trace("RpcDecoder");
    }
}
