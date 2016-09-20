package com.chitu.poker.battle;

public class Card implements Comparable<Card> {

	/**
	 * 牌值
	 * 
	 * @author open
	 */
	public enum Rank {
		TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE, JOKER;

		public static Rank from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	}

	/**
	 * 花色
	 * 
	 * @author open
	 */
	public enum Suit {
		/** 方块0 **/
		DIAMONDS,
		/** 梅花1 **/
		CLUBS,
		/** 红桃2 **/
		HEARTS,
		/** 黑桃3 **/
		SPADES,
		/** 小王 **/
		BLACK,
		/** 大王 **/
		RED;

		public static Suit from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	}

	public final Rank rank;
	public final Suit suit;

	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final Card other = (Card) obj;
		if (this.rank != other.rank && (this.rank == null || !this.rank.equals(other.rank))) {
			return false;
		}
		if (this.suit != other.suit && (this.suit == null || !this.suit.equals(other.suit))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + (this.rank != null ? this.rank.hashCode() : 0);
		hash = 23 * hash + (this.suit != null ? this.suit.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return (this.rank.ordinal() + 2) + "-" + this.suit.ordinal();
	}

	public int compareTo(Card o) {
		if (o == null)
			throw new NullPointerException("Argument cannot be null.");

		if (getClass() != o.getClass())
			throw new ClassCastException("'o' is not an instance of Card.");

		final Card that = (Card) o;
		if (this.rank.ordinal() < that.rank.ordinal()) {
			return -1;
		} else if (this.rank.ordinal() > that.rank.ordinal()) {
			return 1;
		} else if (this.suit.ordinal() < that.rank.ordinal()) {
			return -1;
		} else if (this.suit.ordinal() > that.rank.ordinal()) {
			return 1;
		} else
			return 0;
	}

}
