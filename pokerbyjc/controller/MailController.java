package com.chitu.poker.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.gecko.commons.data.BillType;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.ValidatorUtils;
import cn.gecko.mail.controller.BaseMailController;
import cn.gecko.mail.model.Mail;
import cn.gecko.mail.model.MailAttachment;
import cn.gecko.mail.model.MailErrorCodes;
import cn.gecko.mail.model.MailHolder;
import cn.gecko.mail.model.PersistMail;
import cn.gecko.player.model.Player;
import cn.gecko.player.msg.ListDto;

import com.chitu.poker.mail.PokerMail;
import com.chitu.poker.mail.PokerMailManager;
import com.chitu.poker.model.PokerBillTypes;
import com.chitu.poker.model.PokerErrorCodes;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.service.PokerPlayerManager;

@Controller
public class MailController extends BaseMailController {

	@Autowired
	private PokerPlayerManager playerManager;
	
	@Autowired
	private PokerMailManager mailManager;
	
	/**
	 * 发信息
	 * @param toPlayerId
	 * @param context
	 */
	public void sendMail(String toPlayerId, String context){
		ValidatorUtils.checkLength(context, 0, 300);
		ValidatorUtils.checkRegKeyword(context);
		PokerPlayer toPlayer = playerManager.getAnyPlayerById(Long.valueOf(toPlayerId));
		if(toPlayer == null){
			throw new GeneralException(MailErrorCodes.MAIL_TOPLAYER_NOT_EXIST);
		}
		
		PokerPlayer player = playerManager.getRequestPlayer();
		PokerMail mail = mailManager.normalMail(player.id, context);
		mailManager.sendMail(mail, toPlayer.id);
	}
	
	/**
	 * 删除选中信件,元素String
	 * @param mailIds 邮件ID以“,”分隔
	 */
	public ListDto deleteMails(String mailIds){
		if(StringUtils.isEmpty(mailIds)){
			throw new GeneralException(MailErrorCodes.MAIL_PARAM_ERROR);
		}
		
		List<String> mailIdStr = new ArrayList<String>();
		String[] mailIdStrArry = mailIds.split(",");
		long[] mailId = new long[mailIdStrArry.length];
		for(int i=0;i<mailIdStrArry.length;i++){
			mailId[i] = Long.valueOf(mailIdStrArry[i]);
			mailIdStr.add(mailIdStrArry[i]);
		}
		
		Player player = playerManager.getRequestPlayer();
		MailHolder mailHolder = getMailHolder(player);
		mailHolder.deleteMail(mailId);
		return new ListDto(mailIdStr);
	}
	
	/**
	 * 获取附件
	 * @param mailId
	 */
    public void getMailAttachment(String mailId){
    	if(StringUtils.isEmpty(mailId)){
			throw new GeneralException(MailErrorCodes.MAIL_PARAM_ERROR);
		}
    	
    	PokerPlayer player = playerManager.getRequestPlayer();
		MailHolder mailHolder = getMailHolder(player);
		Mail mail = mailHolder.getMail(Long.valueOf(mailId));
		if (mail == null) {
			throw new GeneralException(MailErrorCodes.MAIL_PARAM_ERROR);
		}
		//宠物栏已满
		int petCount = 0;
		for(MailAttachment attachment : mail.attachments){
			if(attachment.itemCount <= 0){
				continue;
			}
			if(attachment.itemId == 1 || attachment.itemId == 2){
				continue;
			}
			petCount += attachment.itemCount;
		}
		if(player.petHolder.tryAddPet(petCount)){
			throw new GeneralException(PokerErrorCodes.PET_MAX_COUNT);
		}
		
		for(MailAttachment attachment : mail.attachments){
			if(attachment.itemCount <= 0){
				continue;
			}
			beOverlayOwned(player,attachment);
		}
		mail.attachments.clear();
		mail.getPersistMail().setAttachment(PersistMail.mailAttachmentData(mail.attachments));
		mail.getPersistMail().update();
	}
	
    /**
     * 附件物品
     * @param player
     * @param attachment
     */
    private void beOverlayOwned(PokerPlayer player,MailAttachment attachment){
    	//金币
    	if(attachment.itemId == 1){
    		player.wealthHolder.increaseMoney(attachment.itemCount,BillType.get(PokerBillTypes.MAIL_ATTACHMENT), "");
    	}
    	//魔石
    	else if(attachment.itemId == 2){
    		player.wealthHolder.increasePoint(attachment.itemCount,BillType.get(PokerBillTypes.MAIL_ATTACHMENT), "");
    	}
    	//宠物
    	else{
    		for(int i=0;i<attachment.itemCount;i++){
    			player.petHolder.beOverlayOwned(attachment.itemId);
    		}
    	}
    }
    
    
	
}
