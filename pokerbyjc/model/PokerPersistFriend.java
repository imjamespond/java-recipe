package com.chitu.poker.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.friend.model.PersistFriend;
import cn.gecko.persist.annotation.PersistEntity;

@Entity
@Table(name = "poker_friend")
@PersistEntity(cache = false)
public class PokerPersistFriend extends PersistFriend {

	public static PokerPersistFriend get(long id) {
		return get(PokerPersistFriend.class, id);
	}

}
