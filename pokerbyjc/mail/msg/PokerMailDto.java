package com.chitu.poker.mail.msg;

import cn.gecko.mail.msg.MailDto;

import com.chitu.poker.mail.PokerMail;

public class PokerMailDto extends MailDto{

	/**邮件分类**/
	public enum MailCatalog {
		/**系统0**/
		System, 
		/**玩家1**/
		User;
		
		public static MailCatalog from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	};
	
	/**邮件内容模板类型**/
	public enum MailContextType {
		/**普通信息0**/
		Normal, 
		/**社交通知1**/
		SnsNotify,
		/**系统通知2**/
		SystemNotify;

		public static MailContextType from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	};
	
	public PokerMailDto(PokerMail mail) {
		super(mail);
	}
	
}
