package com.chitu.chess.msg;

import cn.gecko.broadcast.GeneralResponse;


/**
 * 排行列表
 * @author Administrator
 *
 */

public class ChessPlayerRankListDto extends GeneralResponse {


	private ChessPlayerRankMDto[] moneyRank;
	private ChessPlayerRankPDto[] pointRank;



	public ChessPlayerRankListDto() {

	}


	/**
	 * 金币排行
	 * @return
	 */

	public ChessPlayerRankMDto[] getMoneyRank() {
		return moneyRank;
	}



	public void setMoneyRank(ChessPlayerRankMDto[] moneyRank) {
		this.moneyRank = moneyRank;
	}


	/**
	 * 积分排行
	 * @return
	 */

	public ChessPlayerRankPDto[] getPointRank() {
		return pointRank;
	}



	public void setPointRank(ChessPlayerRankPDto[] pointRank) {
		this.pointRank = pointRank;
	}









}
