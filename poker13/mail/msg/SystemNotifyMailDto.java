package com.chitu.poker.mail.msg;

import java.nio.ByteBuffer;

import com.chitu.poker.mail.PokerMail;

/**
 * 系统通知类邮件
 * @author open
 *
 */
public class SystemNotifyMailDto extends PokerMailDto {
	public static final byte MAIL_VERSION_1 = 1;
	public static final byte MAIL_VERSION = MAIL_VERSION_1;
	
	/**公会通知类型**/
	public enum SystemNotifyType {
		/**宠物栏溢出0**/
		PetColumn;
		
		public static SystemNotifyType from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	};
	
	/**通知类型**/
	private int notifyType;
	
	public SystemNotifyMailDto(PokerMail mail) {
		super(mail);
		
		ByteBuffer buffer = ByteBuffer.wrap(mail.context);
		byte version = buffer.get();
		if(version == MAIL_VERSION_1){
			this.notifyType = buffer.getInt();
		}
	}

	/**通知类型**/
	public int getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(int notifyType) {
		this.notifyType = notifyType;
	}
	
}
