package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("棋子请求")
public class ChessLandRequest extends GeneralRequest {

	@DescAnno("行程")
	private int journey;

	public int getJourney() {
		return journey;
	}

	public void setJourney(int journey) {
		this.journey = journey;
	}

}
