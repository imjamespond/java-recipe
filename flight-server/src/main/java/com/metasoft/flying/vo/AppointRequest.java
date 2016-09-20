package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("预约")
public class AppointRequest extends GeneralRequest {
	@DescAnno("预约时间")
	private long appoint;

	public long getAppoint() {
		return appoint;
	}

	public void setAppoint(long appoint) {
		this.appoint = appoint;
	}

}
