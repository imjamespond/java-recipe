package com.metasoft.flying.node.net;

import java.nio.ByteOrder;
import java.util.zip.Adler32;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.CodedOutputStream;
import com.metasoft.flying.node.RpcProto.RpcMessage;

public class RpcEncoder implements ProtocolEncoder {
	private static final Logger logger = LoggerFactory.getLogger(RpcEncoder.class);
    
    public RpcEncoder() {
        super();
    }

	@Override
	public void dispose(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) throws Exception {
        if (!(obj instanceof RpcMessage)) {
        	out.write(obj);
        }
        RpcMessage message = (RpcMessage) obj;
        int size = message.getSerializedSize(); 
        
        //header + version + size + checksum
        int length = 4 + size + 4;
        
        IoBuffer buffer = IoBuffer.allocate(length);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put("RPC0".getBytes());
        int writerIndex = buffer.position();
        CodedOutputStream output = CodedOutputStream.newInstance(
                buffer.array(), buffer.position(), buffer.remaining() - 4);
        message.writeTo(output);
        output.checkNoSpaceLeft();//write to buffer without move position

        buffer.position(writerIndex + size);//make readable
        Adler32 checksum = new Adler32();
        checksum.update(buffer.array(), buffer.arrayOffset(), buffer.position());
        buffer.putInt((int) checksum.getValue());
        
        IoBuffer msgbuffer = IoBuffer.allocate(length+4);
        msgbuffer.order(ByteOrder.BIG_ENDIAN);
        msgbuffer.putInt(length);
        msgbuffer.put(buffer.array(), buffer.arrayOffset(), buffer.limit());
        logger.debug(String.format("length:%d, checksum:%d", length, (int)checksum.getValue()));
        out.write(msgbuffer.flip());//重置buffer读可读状态
	}    
    static {
    	logger.trace("RpcEncoder");
    }
}