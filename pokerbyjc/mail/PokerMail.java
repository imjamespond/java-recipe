package com.chitu.poker.mail;

import cn.gecko.mail.model.Mail;

import com.chitu.poker.mail.msg.NormalMailDto;
import com.chitu.poker.mail.msg.PokerMailDto;
import com.chitu.poker.mail.msg.PokerMailDto.MailContextType;
import com.chitu.poker.mail.msg.SnsNotifyMailDto;
import com.chitu.poker.mail.msg.SystemNotifyMailDto;

public class PokerMail extends Mail{

	@Override
	public PokerMailDto toDto(){
		MailContextType type = MailContextType.from(this.contextType);
		switch (type) {
		case Normal:
			return new NormalMailDto(this);
		case SnsNotify:
			return new SnsNotifyMailDto(this);
		case SystemNotify:
			return new SystemNotifyMailDto(this);
		}
		
		return new PokerMailDto(this);
	}
	
	
}
