package com.pengpeng.stargame.player.rule;

import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-3下午5:00
 */
@Deprecated
public class GiftRule extends BaseEntity<String>{
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
