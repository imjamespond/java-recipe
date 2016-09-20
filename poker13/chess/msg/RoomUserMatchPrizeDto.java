package com.chitu.chess.msg;

import cn.gecko.broadcast.GeneralResponse;

public class RoomUserMatchPrizeDto extends GeneralResponse {

	public int type;
	public int num;//

	public RoomUserMatchPrizeDto() {

	}

	/**int 奖励数
	 * @return
	 */
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	/**int 奖励类型0无 1金币 2积分
	 * @return
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	
}
