package com.chitu.poker.mail;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.IdUtils;
import cn.gecko.mail.model.MailAttachment;
import cn.gecko.mail.model.MailErrorCodes;
import cn.gecko.mail.model.MailHolder;
import cn.gecko.mail.msg.MailNotify;
import cn.gecko.persist.ByteBufferUtils;

import com.chitu.poker.mail.msg.PokerMailDto.MailCatalog;
import com.chitu.poker.mail.msg.PokerMailDto.MailContextType;
import com.chitu.poker.mail.msg.SnsNotifyMailDto;
import com.chitu.poker.mail.msg.SnsNotifyMailDto.SnsNotifyType;
import com.chitu.poker.mail.msg.SystemNotifyMailDto;
import com.chitu.poker.mail.msg.SystemNotifyMailDto.SystemNotifyType;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.service.PokerPlayerManager;

@Service
public class PokerMailManager {

	public static int SYSTEM_ID = 0;
	
	public static String SYSTEM_NAME = "SYSTEM";
	
	@Autowired
	private PokerPlayerManager playerManager;
	
	/**
	 * 发信
	 * @param mail
	 * @param toPlayerId
	 */
	public void sendMail(PokerMail mail,long toPlayerId){
		mail.id = IdUtils.generateLongId();
		mail.toPlayerId = toPlayerId;
		
		PokerPersistMail persistMail = new PokerPersistMail();
		persistMail.config(mail);
		persistMail.save();
		mail.setPersistMail(persistMail);
		
		PokerPlayer toPlayer = playerManager.getOnlinePlayerById(toPlayerId);
		if(toPlayer != null){
			MailHolder mailHolder = toPlayer.getMailHolder();
			mailHolder.addMail(mail);
			toPlayer.deliver(new MailNotify(mail.toDto()));
		}
	}
	
	/***
	 * 创建普通邮件
	 * @param fromPlayerId
	 * @param toPlayerId
	 * @param context
	 * @return
	 */
	public PokerMail normalMail(long fromPlayerId, String context){
		PokerPlayer fromPlayer = playerManager.getOnlinePlayerById(fromPlayerId);
		if(fromPlayer == null){
			throw new GeneralException(MailErrorCodes.MAIL_PERMISSION);
		}
		
		PokerMail mail = new PokerMail();
		mail.fromPlayerId = fromPlayerId;
		mail.fromPlayerName = fromPlayer.nickname;
		mail.dateTime = System.currentTimeMillis();
		mail.read = false;
		mail.contextType = MailContextType.Normal.ordinal();
		mail.catalog = MailCatalog.User.ordinal();
		
		ByteBuffer buffer = ByteBuffer.allocate(ByteBufferUtils.stringLength(context));
		ByteBufferUtils.putString(buffer, context);
		buffer.flip();
		byte[] contextByte = new byte[buffer.limit()];
		buffer.get(contextByte);
		mail.context = contextByte;
		
		return mail;
	}
	
	/**
	 * 社交邮件
	 * @param operater
	 * @param type
	 * @return
	 */
	public PokerMail snsNotifyMail(PokerPlayer operater,SnsNotifyType type){
		PokerMail mail = new PokerMail();
		mail.fromPlayerId = PokerMailManager.SYSTEM_ID;
		mail.fromPlayerName = PokerMailManager.SYSTEM_NAME;
		mail.dateTime = System.currentTimeMillis();
		mail.read = false;
		mail.contextType = MailContextType.SnsNotify.ordinal();
		mail.catalog = MailCatalog.System.ordinal();
		//申请者主宠
		Pet pet = operater.petHolder.getTeamMainPet();
		int length = 1 + 4 + 8 + ByteBufferUtils.stringLength(operater.nickname) + 4 + pet.byteLength();
		
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(SnsNotifyMailDto.MAIL_VERSION);
		buffer.putInt(type.ordinal());
		buffer.putLong(operater.id);
		ByteBufferUtils.putString(buffer, operater.nickname);
		buffer.putInt(operater.grade);
		pet.toBuffer(buffer);
		
		buffer.flip();
		byte[] context = new byte[buffer.limit()];
		buffer.get(context);
		mail.context = context;
		return mail;
	}
	
	/**
	 * 系统邮件
	 * @param type
	 * @param packet
	 * @return
	 */
	public PokerMail systemNotifyMail(SystemNotifyType type, List<MailAttachment> packet){
		PokerMail mail = new PokerMail();
		mail.fromPlayerId = PokerMailManager.SYSTEM_ID;
		mail.fromPlayerName = PokerMailManager.SYSTEM_NAME;
		mail.dateTime = System.currentTimeMillis();
		mail.read = false;
		mail.contextType = MailContextType.SystemNotify.ordinal();
		mail.catalog = MailCatalog.System.ordinal();
		
		//Context
		int lengthContext = 1 + 4;
		
		ByteBuffer buffer = ByteBuffer.allocate(lengthContext);
		buffer.put(SystemNotifyMailDto.MAIL_VERSION);
		buffer.putInt(type.ordinal());
		
		buffer.flip();
		byte[] context = new byte[buffer.limit()];
		buffer.get(context);
		mail.context = context;
		
		//MailAttachment
		if(packet == null){
			mail.attachments = new ArrayList<MailAttachment>();
		}else{
			mail.attachments = packet;
		}
		
		return mail;
	}
	
	
	
	
	
}
