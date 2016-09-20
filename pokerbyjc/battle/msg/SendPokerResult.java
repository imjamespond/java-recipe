package com.chitu.poker.battle.msg;

import cn.gecko.broadcast.BroadcastMessage;

import com.chitu.poker.battle.BattleMember;

/**
 * 补牌结果
 * 
 * @author ivan
 * 
 */
public class SendPokerResult implements BroadcastMessage {

	private PokerDto[] pokers;

	public SendPokerResult(BattleMember member) {
		pokers = new PokerDto[member.pokers.length];
		for (int i = 0; i < pokers.length; i++) {
			if (member.pokers[i] != null)
				pokers[i] = new PokerDto(member.pokers[i]);
			else
				pokers[i] = null;
		}
	}

	/**
	 * A方所有的牌，PokerDto数组
	 * 
	 * @return
	 */
	public PokerDto[] getPokers() {
		return pokers;
	}

	public void setPokers(PokerDto[] pokers) {
		this.pokers = pokers;
	}

}
