package com.pengpeng.stargame.vo.fashion;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.BaseItemVO;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-29 上午10:38
 */
@Desc("时装物品 背包内的 单个时装")
public class FashionItemVO extends BaseItemVO {
	@Desc("穿戴状态，0:未穿,1:已穿")
	private int status;

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "FashionItemVO{" +
				"status=" + status +
				'}';
	}
}
