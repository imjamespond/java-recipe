package com.pengpeng.stargame.player.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商店礼物列表
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-8 下午2:10
 */
@Entity()
@Table(name="sg_gift_rule")
public class BaseGiftRule extends BaseEntity<String> {

	@Id
	private String presentId;
	@Column
	private int presentType;
	@Column
	private String itemId;
	@Column
	private int num;

	//有效时间(天)
	@Column
	private long validityTime;

	public long getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(long validityTime) {
		this.validityTime = validityTime;
	}

	@Override
	public String getId() {
		return presentId;
	}

	@Override
	public void setId(String id) {
		this.presentId = id;
	}

	@Override
	public String getKey() {
		return presentId;
	}

	public String getPresentId() {
		return presentId;
	}

	public void setPresentId(String presentId) {
		this.presentId = presentId;
	}

	public int getPresentType() {
		return presentType;
	}

	public void setPresentType(int presentType) {
		this.presentType = presentType;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
