package com.chitu.poker.battle.msg;

import cn.gecko.broadcast.BroadcastMessage;

import com.chitu.poker.battle.Card;

/**
 * 台面牌重置结果
 * 
 * @author ivan
 * 
 */
public class PokerResetResult implements BroadcastMessage {

	private PokerDto[] tablePokers;

	public PokerResetResult(Card[] allPokers) {
		tablePokers = new PokerDto[allPokers.length];
		for (int i = 0; i < allPokers.length; i++) {
			if (allPokers[i] != null)
				tablePokers[i] = new PokerDto(allPokers[i]);
			else
				tablePokers[i] = null;
		}

	}

	/**
	 * 台面的所有牌，PokerDto数组，没牌的位置为null
	 * 
	 * @return
	 */
	public PokerDto[] getTablePokers() {
		return tablePokers;
	}

	public void setTablePokers(PokerDto[] tablePokers) {
		this.tablePokers = tablePokers;
	}

}
