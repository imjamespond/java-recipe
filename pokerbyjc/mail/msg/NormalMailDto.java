package com.chitu.poker.mail.msg;

import java.nio.ByteBuffer;

import cn.gecko.persist.ByteBufferUtils;

import com.chitu.poker.mail.PokerMail;

/**
 * 普通信件
 * @author open
 *
 */
public class NormalMailDto extends PokerMailDto {
	public static final byte MAIL_VERSION_1 = 1;
	public static final byte MAIL_VERSION = MAIL_VERSION_1;
	
	private String fullText;
	
	public NormalMailDto(PokerMail mail) {
		super(mail);
		
		ByteBuffer buffer = ByteBuffer.wrap(mail.context);
		byte version = buffer.get();
		if(version == MAIL_VERSION_1){
			this.fullText = ByteBufferUtils.readString(buffer);
		}
	}

	/**全文**/
	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}
	
	
	
	
	

}
