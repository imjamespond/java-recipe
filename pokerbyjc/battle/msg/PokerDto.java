package com.chitu.poker.battle.msg;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;

import com.chitu.poker.battle.Card;
import com.chitu.poker.battle.Card.Rank;
import com.chitu.poker.battle.Card.Suit;

/**
 * 扑克数据
 * 
 * @author ivan
 * 
 */
@IncludeEnums({ @IncludeEnum(Rank.class), @IncludeEnum(Suit.class) })
public class PokerDto implements BroadcastMessage {

	private int rank;
	private int suit;

	public PokerDto(int rank, int suit) {
		this.rank = rank;
		this.suit = suit;
	}

	public PokerDto(Card card) {
		this.rank = card.rank.ordinal();
		this.suit = card.suit.ordinal();
	}

	/**
	 * 扑克点数，参考Rank系列常量
	 * 
	 * @return
	 */
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * 扑克花式，参考Suit系列常量
	 * 
	 * @return
	 */
	public int getSuit() {
		return suit;
	}

	public void setSuit(int suit) {
		this.suit = suit;
	}

	public String toString() {
		return (rank+2) + "-" + suit;
	}

}
