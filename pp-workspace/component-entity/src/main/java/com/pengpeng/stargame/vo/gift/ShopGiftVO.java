package com.pengpeng.stargame.vo.gift;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-8 下午2:28
 */
@Desc("商店礼物物品")
public class ShopGiftVO {

	@Desc("物品id")
	private String itemId;

	@Desc("物品类型")
	private int presentType;

	@Desc("物品数量")
	private int num;

	@Desc("有效时间(天)")
	private long validityTime;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getPresentType() {
		return presentType;
	}

	public void setPresentType(int presentType) {
		this.presentType = presentType;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public long getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(long validityTime) {
		this.validityTime = validityTime;
	}
}
