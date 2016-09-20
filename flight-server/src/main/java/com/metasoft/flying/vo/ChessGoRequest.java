package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("棋子请求")
public class ChessGoRequest extends GeneralRequest {

	@DescAnno("棋子16位有效")
	private int bits;

	public int getBits() {
		return bits;
	}

	public void setBits(int bits) {
		this.bits = bits;
	}


}
