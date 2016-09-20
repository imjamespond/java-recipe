package com.chitu.chess.msg;

import cn.gecko.broadcast.GeneralResponse;


/**
 * 排行列表
 * @author Administrator
 *
 */

public class ChessPlayerMatchRankListDto extends GeneralResponse {


	private ChessPlayerMatchRankMDto[] matchRank;
	private int type;
	private String contact;
	private boolean prizeAvailable;

	public ChessPlayerMatchRankListDto() {
		prizeAvailable = false;
	}

	/**
	 * 比赛排行
	 * @return
	 */
	public ChessPlayerMatchRankMDto[] getMatchRank() {
		return matchRank;
	}
	public void setMatchRank(ChessPlayerMatchRankMDto[] matchRank) {
		this.matchRank = matchRank;
	}

	/**
	 * 联系
	 * @return
	 */
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * 是否可领奖
	 * @return
	 */
	public void setPrizeAvailable(boolean prizeAvailable) {
		this.prizeAvailable = prizeAvailable;
	}
	public boolean isPrizeAvailable() {
		return prizeAvailable;
	}

	/**
	 * 比赛区id
	 * @return
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}











}
