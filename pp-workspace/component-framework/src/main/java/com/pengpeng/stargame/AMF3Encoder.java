package com.pengpeng.stargame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.pengpeng.stargame.util.NetUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

/**
 * amf3发送编码器
 * 
 * @author ericSong
 * 
 */
public class AMF3Encoder extends OneToOneEncoder {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	public static final byte END = '\0';

	SerializationContext serializationContext = null;
	public AMF3Encoder(){
		serializationContext = new SerializationContext();
	}
	/**
	 * AMF的编码
	 */
//	@Override
//	protected Object encode(ChannelHandlerContext arg0, Channel arg1,
//			Object data) throws Exception {
//		if (data instanceof String) {
//			/**
//			 * 发送策略文件
//			 */
//			String policy = (String) data;
//			byte[] ps = policy.getBytes();
//			ChannelBuffer buffer = ChannelBuffers.buffer(ps.length);
//			buffer.writeBytes(ps);
//			return buffer;
//
//		}
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		Amf3Output amf3Output = new Amf3Output(serializationContext);
//		amf3Output.setOutputStream(stream);
//		try {
//			amf3Output.writeObject(data);
//		} catch (IOException e) {
//			log.error("amf序列化失败!");
//		}
//
//		byte[] objSe = stream.toByteArray();
//
//		if (objSe != null && objSe.length > 0) {
//			ChannelBuffer buffer = ChannelBuffers.buffer((4+objSe.length));
//			buffer.writeInt(objSe.length);
//			buffer.writeBytes(objSe);
////			buffer.writeByte(END);
//			return buffer;
//		}
//		return null;
//	}

	@Override
	protected Object encode(ChannelHandlerContext arg0, Channel arg1,
			Object data) throws Exception {
//		if (data instanceof String) {
//			/**
//			 * 发送策略文件
//			 */
//			String policy = (String) data;
//			byte[] ps = policy.getBytes();
//			ChannelBuffer buffer = ChannelBuffers.buffer(ps.length);
//			buffer.writeBytes(ps);
//			return buffer;
//
//		}
		//log.debug("SEND|encoder|"+data);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Amf3Output amf3Output = new Amf3Output(serializationContext);
		amf3Output.setOutputStream(stream);
		try {
			amf3Output.writeObject(data);
		} catch (IOException e) {
			log.error("amf序列化失败!");
		}

		byte[] objSe = stream.toByteArray();

		if (objSe != null && objSe.length > 0) {
			ChannelBuffer buffer = ChannelBuffers.buffer((4+objSe.length));
			buffer.writeInt(objSe.length);
			buffer.writeBytes(objSe);
            //log.debug(NetUtil.bytesToHex(objSe));;
//			buffer.writeByte(END);
			return buffer;
		}
		return null;
	}

}
