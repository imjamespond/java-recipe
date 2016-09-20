package com.chitu.poker.store;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.persist.PersistObject;
import cn.gecko.persist.annotation.PersistEntity;

/**
 * 商店
 * @author open
 *
 */
@Entity
@Table(name = "poker_store")
@PersistEntity(cache = false)
public class PersistStore extends PersistObject {

	private long id;
	
    private int buyStrengthTimes;
	
	private int buyStrengthMaxTimes;
	
	/**购买宠物次数**/
	private int buyPetTimes;
	
	private int today;
	
	public static PersistStore get(long id) {
		return PersistObject.get(PersistStore.class, id);
	}
	
	@Override
	public Long id() {
		return id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getBuyStrengthTimes() {
		return buyStrengthTimes;
	}

	public void setBuyStrengthTimes(int buyStrengthTimes) {
		this.buyStrengthTimes = buyStrengthTimes;
	}

	public int getBuyStrengthMaxTimes() {
		return buyStrengthMaxTimes;
	}

	public void setBuyStrengthMaxTimes(int buyStrengthMaxTimes) {
		this.buyStrengthMaxTimes = buyStrengthMaxTimes;
	}

	public int getToday() {
		return today;
	}

	public void setToday(int today) {
		this.today = today;
	}

	public int getBuyPetTimes() {
		return buyPetTimes;
	}

	public void setBuyPetTimes(int buyPetTimes) {
		this.buyPetTimes = buyPetTimes;
	}


}
