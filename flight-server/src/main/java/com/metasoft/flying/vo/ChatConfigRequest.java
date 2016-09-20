package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("房间配置")
public class ChatConfigRequest extends GeneralRequest {
	@DescAnno("聊天等级")
	private int chatlevel;

	@DescAnno("聊天等级")
	private int talklevel;

	@DescAnno("申请玫瑰限制")
	private int roseLimit;

	public int getChatlevel() {
		return chatlevel;
	}

	public void setChatlevel(int chatlevel) {
		this.chatlevel = chatlevel;
	}

	public int getTalklevel() {
		return talklevel;
	}

	public void setTalklevel(int talklevel) {
		this.talklevel = talklevel;
	}

	public int getRoseLimit() {
		return roseLimit;
	}

	public void setRoseLimit(int roseLimit) {
		this.roseLimit = roseLimit;
	}
}
