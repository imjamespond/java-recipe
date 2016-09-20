package com.pengpeng.stargame.vo.gift;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-8 下午2:29
 */
@Desc("礼物商店请求")
public class ShopGiftReq extends BaseReq {
	@Desc("礼物类型")
	private int presentType;

	public int getPresentType() {
		return presentType;
	}

	public void setPresentType(int presentType) {
		this.presentType = presentType;
	}
}
