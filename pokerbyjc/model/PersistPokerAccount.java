package com.chitu.poker.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.persist.PersistObject;
import cn.gecko.persist.annotation.PersistEntity;

@Entity
@Table(name = "poker_account")
@PersistEntity(cache = false)
public class PersistPokerAccount extends PersistObject {

	public static PersistPokerAccount get(long id) {
		return PersistPokerAccount.get(PersistPokerAccount.class, id);
	}
	
	@Override
	public Long id() {
		return id;
	}

	private long id;
	
	private String accountId;

	private String password;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	
}
