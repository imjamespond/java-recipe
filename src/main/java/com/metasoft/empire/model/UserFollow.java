package com.metasoft.empire.model;

/**
 * 玩家追随关注
 * 
 * @author james
 *
 */
public class UserFollow {
	/**
	 * 追随计数
	 */
	private int count;
	/**
	 * 0为好友1为黑名单
	 */
	private int state;
	private long userId;

	/**
	 * @param count
	 * @param state
	 * @param userId
	 */
	public UserFollow(int count, int state, long userId) {
		super();
		this.count = count;
		this.state = state;
		this.userId = userId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void addCountByOne() {
		this.count++;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
