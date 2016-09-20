package com.pengpeng.stargame;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.pengpeng.stargame.util.NetUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.pengpeng.stargame.req.BaseReq;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

/**
 * amf3接收的解码器
 * 
 * @author ericSong
 * 
 */
public class AMF3Decoder extends LengthFieldBasedFrameDecoder {
    protected final Log log = LogFactory.getLog(this.getClass());
	private SerializationContext sc = null;

    /**
	 * 包头长度
	 */
	private final int PACKET_LENGTH = 4;
	private final int PACKET_MAX_LENGTH = 1024;
	private final String policy = "<policy-file-request/>";
	private static final String SECURITY_FILE = "<cross-domain-policy>"
			+ "<site-control permitted-cross-domain-policies=\"all\"/>"
			+ "<allow-access-from domain=\"*\" to-ports=\"*\" />"
			+ "</cross-domain-policy>" + "\n";

	
	public AMF3Decoder(){
        super(Integer.MAX_VALUE, 0,4);
		sc = new SerializationContext();
	}

    public void addClass(Class cls){
        sc.setDeserializerClass(cls);
    }
	@Override
	protected Object decode(ChannelHandlerContext arg0, Channel arg1,
			ChannelBuffer data) throws IOException, ClassNotFoundException {
		/**
		 * 判断是否为策略文件
		 */
//        log.info("总包长度data.readableBytes()........."+data.readableBytes());
//		if (data.readableBytes() == 23) {
//			String py =  data.toString("utf-8");
//			if (py.indexOf(policy) != -1) {
//				/**
//				 * 策略文件
//				 */
//				arg0.getChannel().write(SECURITY_FILE);
//				arg0.getChannel().close();
//				return null;
//			}
//		}
		/**
		 * 定下标记
		 */
		data.markReaderIndex();
//		if (data.readableBytes() == 0) {
//			return null;//throw new IOException("玩家的连接断开！");
//		}
//		if (data.readableBytes() < PACKET_LENGTH) {
//			throw new IOException("异常包,包头长度不够!");
//		}
		int length = data.readInt();
//        log.info("AMF包长度data.readInt()........."+data.readableBytes());
//		if (PACKET_MAX_LENGTH < length) {
//			throw new IOException("异常包,包头长度过长!");
//		}
//		int blenght = data.readableBytes();
//		if (length!=blenght){
//			throw new IOException("异常包,包头长度不对,抛弃!");
//		}
        try{
            byte[] bs = new byte[length];
		data.readBytes(bs);
//		byte end = data.readByte();
//		if (end != '\n') {
//			throw new IOException("异常包,此包的结束符不对，为未完成包,抛弃!");
//		}
        //log.debug(NetUtil.bytesToHex(bs));
        Amf3Input amf3Input =  new Amf3Input(sc);
		amf3Input.setInputStream(new ByteArrayInputStream(bs));
            BaseReq message = (BaseReq)amf3Input.readObject();
            return message;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

//	@SuppressWarnings("deprecation")
//	@Override
//	protected Object decode(ChannelHandlerContext arg0, Channel arg1,
//			ChannelBuffer data) throws IOException, ClassNotFoundException {
//		/**
//		 * 判断是否为策略文件
//		 */
//		if (data.readableBytes() == 23) {
//			String py =  data.toString("utf-8");
//			if (py.indexOf(policy) != -1) {
//				/**
//				 * 策略文件
//				 */
//				arg0.getChannel().write(SECURITY_FILE);
//				arg0.getChannel().close();
//				return null;
//			}
//		}
//		/**
//		 * 定下标记
//		 */
//		data.markReaderIndex();
//		if (data.readableBytes() == 0) {
//			return null;//throw new IOException("玩家的连接断开！");
//		}
//		if (data.readableBytes() < PACKET_LENGTH) {
//			throw new IOException("异常包,包头长度不够!");
//		}
//		int length = data.readInt();
//		if (PACKET_MAX_LENGTH < length) {
//			throw new IOException("异常包,包头长度过长!");
//		}
//		int blenght = data.readableBytes();
//		if (blenght != length) {
//			throw new IOException("异常包,此包的结束符不对，为未完成包,抛弃!");
//		}
//		byte[] bs = new byte[length];
//		data.readBytes(bs);
////		byte end = data.readByte();
////		if (end != '\0') {
////			throw new IOException("异常包,此包的结束符不对，为未完成包,抛弃!");
////		}
//
//		
//		Amf3Input amf3Input = new Amf3Input(sc);
//		amf3Input.setInputStream(new ByteArrayInputStream(bs));
//		Object message = amf3Input.readObject();
//		return message;
//	}
}
