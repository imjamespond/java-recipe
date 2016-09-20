package com.pengpeng.stargame.fashion.rule;

import com.pengpeng.stargame.model.BaseEntity;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-3下午5:00
 */
public class FashionGiftRule extends BaseEntity<String>{
    private String id;
    /**
     * 每日赠送次数
     */
    private int giveCount;

	/**
	 * 过期时间 ，单位：天
	 */
	private int expireDay;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getKey() {
        return id;
    }

    public boolean canGift(int num){
        return num>=giveCount;
    }

	public int getGiveCount() {
		return giveCount;
	}

	public void setGiveCount(int giveCount) {
		this.giveCount = giveCount;
	}

	public int getExpireDay() {
		return expireDay;
	}

	public void setExpireDay(int expireDay) {
		this.expireDay = expireDay;
	}
}
