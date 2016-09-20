package com.chitu.poker.mail;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.mail.model.PersistMail;
import cn.gecko.persist.GenericDao;
import cn.gecko.persist.annotation.PersistEntity;

@Entity
@Table(name = "poker_mail")
@PersistEntity(cache = false)
public class PokerPersistMail extends PersistMail {

	public static PokerPersistMail get(long id) {
		return get(PokerPersistMail.class, id);
	}

	public static List<PokerPersistMail> gets(long playerId) {
		List<PokerPersistMail> mails = SpringUtils.getBeanOfType(GenericDao.class).getAll(PokerPersistMail.class, 50,
				"WHERE toPlayerId=? ORDER BY mailDateTime DESC", playerId);
		return mails;
	}
}
