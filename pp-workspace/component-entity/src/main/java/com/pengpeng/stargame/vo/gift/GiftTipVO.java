package com.pengpeng.stargame.vo.gift;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-9 下午12:02
 */
@Desc("通用物品")
public class GiftTipVO {
	@Desc("物品类型")
	private int presentType;

	@Desc("物品数量")
	private int num;

	public GiftTipVO() {
	}

	public GiftTipVO(int presentType, int num) {
		this.presentType = presentType;
		this.num = num;
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
}
