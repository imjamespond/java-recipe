package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("pk加入请求")
public class PkJoinRequest extends GeneralRequest {
	@DescAnno("玩家数组")
	private Long[] userIdList;

	public Long[] getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(Long[] userIdList) {
		this.userIdList = userIdList;
	}
	
	
}
