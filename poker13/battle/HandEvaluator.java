package com.chitu.poker.battle;

import java.util.Arrays;
import java.util.HashSet;

import com.chitu.poker.battle.Card.Suit;

public class HandEvaluator {

	/** 牌型 **/
	public enum ModelType {
		/** 不成牌0 **/
		NONE,
		/** 一对1 **/
		ONE_PAIR,
		/** 两对2 **/
		TWO_PAIR,
		/** 三条3 **/
		THREE_OF_A_KIND,
		/** 顺子4 **/
		STRAIGHT,
		/** 同花5 **/
		FLUSH,
		/** 葫芦6 **/
		FULL_HOUSE,
		/** 铁枝7 **/
		FOUR_OF_A_KIND,
		/** 同花顺8 **/
		STRAIGHT_FLUSH,
		/** 大同花顺9 **/
		ROYAL_FLUSH,
		/** 大王牌10 **/
		JOKER;
		public static ModelType from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	}

	public static HandEvaluator createHandEvaluator(Card[] cards) {
		if (cards == null)
			throw new NullPointerException("Argument cannot be null.");
		if (cards.length < 5 || hasDuplicates(cards))
			throw new IllegalArgumentException("'hand' must have 5 cards with no duplicates.");
		Card[] copyCards = new Card[5];
		System.arraycopy(cards, 0, copyCards, 0, cards.length);
		Arrays.sort(copyCards);
		if (copyCards[3].rank == Card.Rank.JOKER)
			return new TwoJokerEvaluator(copyCards);
		if (copyCards[4].rank == Card.Rank.JOKER)
			return new OneJokerEvaluator(copyCards);
		return new NoJokerEvaluator(copyCards);

	}

	/**
	 * 是否有重复牌
	 * 
	 * @param cards
	 * @return
	 */
	public static boolean hasDuplicates(Card[] cards) {
		HashSet<Card> set = new HashSet<Card>(5);
		for (int i = 0; i < cards.length; i++) {
			if (!set.add(cards[i])) {
				return true;
			}
		}
		return false;
	}

	protected final Card[] cards;
	protected ModelType modelType;

	protected HandEvaluator(Card[] cards) {
		this.cards = cards;
	}
	
	protected void initModelHand(){
		this.modelType = this.modelHand();
	}

	public ModelType getModelType() {
		return this.modelType;
	}

	public Card[] getHand() {
		return cards;
	}

	private ModelType modelHand() {
		if (this.checkJoker()) {
			return ModelType.JOKER;
		} else if (this.checkForRoyalFlush()) {
			return ModelType.ROYAL_FLUSH;
		} else if (this.checkForStraightFlush()) {
			return ModelType.STRAIGHT_FLUSH;
		} else if (this.checkForFourOfAKind()) {
			return ModelType.FOUR_OF_A_KIND;
		} else if (this.checkForFullHouse()) {
			return ModelType.FULL_HOUSE;
		} else if (this.checkForFlush()) {
			return ModelType.FLUSH;
		} else if (this.checkForStraight()) {
			return ModelType.STRAIGHT;
		} else if (this.checkForThreeOfAKind()) {
			return ModelType.THREE_OF_A_KIND;
		} else if (this.checkForTwoPair()) {
			return ModelType.TWO_PAIR;
		} else if (this.checkForOnePair()) {
			return ModelType.ONE_PAIR;
		} else {
			return ModelType.NONE;
		}
	}

	protected boolean checkJoker() {
		return false;
	}

	protected boolean checkForRoyalFlush() {
		return false;
	}

	protected boolean checkForStraightFlush() {
		return false;
	}

	protected boolean checkForFourOfAKind() {
		return false;
	}

	protected boolean checkForFullHouse() {
		return false;
	}

	protected boolean checkForFlush() {
		return false;
	}

	protected boolean checkForStraight() {
		return false;
	}

	protected boolean checkForThreeOfAKind() {
		return false;
	}

	protected boolean checkForTwoPair() {
		return false;
	}

	protected boolean checkForOnePair() {
		return false;
	}

	// 以下是便捷方法
	protected boolean isStraight(Card[] subCards) {
		// 如果有重复的牌，不为顺子
		for (int i = 0; i < subCards.length - 1; i++) {
			if (subCards[i].rank == subCards[i + 1].rank) {
				return false;
			}
		}
		boolean straight = subCards[subCards.length - 1].rank.ordinal() - subCards[0].rank.ordinal() <= 4;
		// 最大不为A的时候，最大牌与最小牌差不超过5
		if (subCards[subCards.length - 1].rank != Card.Rank.ACE) {
			return straight;
		} else {
			// 10 j q k 1的情况
			if (straight) {
				return straight;
			}
			// 1 2 3 4 5的情况
			else {
				return subCards[subCards.length - 2].rank.ordinal() < Card.Rank.SIX.ordinal();
			}
		}
		//

	}

	protected boolean isFlush(Card[] subCards) {
		Suit suit = subCards[0].suit;
		for (int i = 1; i < subCards.length; i++) {
			if (subCards[i].suit != suit)
				return false;
		}
		return true;
	}

	protected boolean hasOnePair(Card[] subCards) {
		if (subCards.length > 1 && subCards[0].rank == subCards[1].rank) {
			return true;
		}
		if (subCards.length > 2 && subCards[1].rank == subCards[2].rank) {
			return true;
		}
		if (subCards.length > 3 && subCards[2].rank == subCards[3].rank) {
			return true;
		}
		if (subCards.length > 4 && subCards[3].rank == subCards[4].rank) {
			return true;
		}
		return false;
	}

	protected boolean hasTwoPair(Card[] subCards) {
		if (subCards.length > 3 && subCards[0].rank == subCards[1].rank && subCards[2].rank == subCards[3].rank) {
			return true;
		}
		if (subCards.length > 4 && subCards[0].rank == subCards[1].rank && subCards[3].rank == subCards[4].rank) {
			return true;
		}
		if (subCards.length > 4 && subCards[1].rank == subCards[2].rank && subCards[3].rank == subCards[4].rank) {
			return true;
		}
		return false;
	}

	protected boolean hasThree(Card[] subCards) {
		if (subCards.length > 2 && subCards[0].rank == subCards[2].rank) {
			return true;
		}
		if (subCards.length > 3 && subCards[1].rank == subCards[3].rank) {
			return true;
		}
		if (subCards.length > 4 && subCards[2].rank == subCards[4].rank) {
			return true;
		}
		return false;
	}

}
