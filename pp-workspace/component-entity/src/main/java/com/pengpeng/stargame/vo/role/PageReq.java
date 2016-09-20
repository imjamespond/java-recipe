package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @author jinli.yuan@com.pengpeng.com
 * @since 13-3-22 下午5:44
 */
@Desc("页码请求")
public class PageReq extends BaseReq {

	private int pageNo;

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
}
