package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("首次登陆奖励消息")
@EventAnno(desc = "", name = "event.signin.award")
public class SignInAwardVO {
	@DescAnno("金币")
	private int gold;


	public SignInAwardVO() {

	}
	public SignInAwardVO(int gold) {
		this.gold = gold;
	}


	public int getGold() {
		return gold;
	}


	public void setGold(int gold) {
		this.gold = gold;
	}

}
