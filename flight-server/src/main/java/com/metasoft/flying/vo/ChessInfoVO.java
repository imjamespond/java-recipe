package com.metasoft.flying.vo;

import java.util.List;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("棋子信息")
@EventAnno(desc = "", name = "event.chessinfo")
public class ChessInfoVO {
	@DescAnno("标记,1为第一架出发")
	protected int sign;
	@DescAnno("回合")
	protected int round;
	@DescAnno("所有棋子 List<ChessVO>")
	private List<ChessPlayerVO> players;
	@DescAnno("所有道具ChessItemVO")
	private ChessItemVO[] items;

	public List<ChessPlayerVO> getPlayers() {
		return players;
	}

	public void setPlayers(List<ChessPlayerVO> players) {
		this.players = players;
	}

	public ChessItemVO[] getItems() {
		return items;
	}

	public void setItems(ChessItemVO[] items) {
		this.items = items;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}


}
