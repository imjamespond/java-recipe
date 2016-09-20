package com.chitu.poker.mail.msg;

import java.nio.ByteBuffer;

import cn.gecko.persist.ByteBufferUtils;

import com.chitu.poker.mail.PokerMail;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.pet.msg.PetDto;

public class SnsNotifyMailDto extends PokerMailDto {
	public static final byte MAIL_VERSION_1 = 1;
	public static final byte MAIL_VERSION = MAIL_VERSION_1;
	
	/**通知类型**/
	public enum SnsNotifyType {
		/**申请加为好友0**/
		Invite;
		
		public static SnsNotifyType from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	};
	
	/**通知类型**/
	private int notifyType;
	
	/**申请者ID**/
	private String operaterId;
	
	/**申请者名称**/
	private String operaterName;
	
	/**申请者等级**/
	private int operaterGrade;
	
	/**申请者主宠**/
	private PetDto operaterPet;
	
	public SnsNotifyMailDto(PokerMail mail) {
		super(mail);
		
		ByteBuffer buffer = ByteBuffer.wrap(mail.context);
		byte version = buffer.get();
		if(version == MAIL_VERSION_1){
			this.notifyType = buffer.getInt();
			this.operaterId = String.valueOf(buffer.getLong());
			this.operaterName = ByteBufferUtils.readString(buffer);
			this.operaterGrade = buffer.getInt();
			
			Pet pet = new Pet();
			pet.initBuffer(buffer);
			this.operaterPet = pet.toDto();
		}
	}

	/**通知类型**/
	public int getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(int notifyType) {
		this.notifyType = notifyType;
	}

	/**申请者ID**/
	public String getOperaterId() {
		return operaterId;
	}

	public void setOperaterId(String operaterId) {
		this.operaterId = operaterId;
	}

	/**申请者名称**/
	public String getOperaterName() {
		return operaterName;
	}

	public void setOperaterName(String operaterName) {
		this.operaterName = operaterName;
	}

	/**申请者主宠**/
	public PetDto getOperaterPet() {
		return operaterPet;
	}

	public void setOperaterPet(PetDto operaterPet) {
		this.operaterPet = operaterPet;
	}

	/**申请者等级**/
	public int getOperaterGrade() {
		return operaterGrade;
	}

	public void setOperaterGrade(int operaterGrade) {
		this.operaterGrade = operaterGrade;
	}

	
	
	
	
	
	
}
