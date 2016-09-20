package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("开局请求")
public class ChessBeginRequest extends GeneralRequest {
	@DescAnno("玩家id")
	private Long[] userIdList;
	@DescAnno("开局表示设置次数限制")
	private int dice;
	
	public Long[] getUserIdList() {
		return userIdList;
	}
	public void setUserIdList(Long[] userIdList) {
		this.userIdList = userIdList;
	}
	public int getDice() {
		return dice;
	}
	public void setDice(int dice) {
		this.dice = dice;
	}


}
