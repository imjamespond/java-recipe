package com.chitu.poker.battle;


public class TwoJokerEvaluator extends HandEvaluator {

	private Card[] prefixCards;

	protected TwoJokerEvaluator(Card[] cards) {
		super(cards);
		prefixCards = new Card[] { cards[0], cards[1], cards[2] };
		initModelHand();
	}

	protected boolean checkJoker() {
		return cards[0].rank == cards[2].rank;
	}

	protected boolean checkForRoyalFlush() {
		return cards[0].rank == Card.Rank.TEN && checkForStraightFlush();
	}

	protected boolean checkForStraightFlush() {
		return isStraight(prefixCards) && isFlush(prefixCards);
	}

	protected boolean checkForFourOfAKind() {
		return hasOnePair(prefixCards);
	}

	protected boolean checkForFullHouse() {
		return hasOnePair(prefixCards);
	}

	protected boolean checkForFlush() {
		return isFlush(prefixCards);
	}

	protected boolean checkForStraight() {
		return isStraight(prefixCards);
	}

	protected boolean checkForThreeOfAKind() {
		return true;
	}

	protected boolean checkForTwoPair() {
		return true;
	}

	protected boolean checkForOnePair() {
		return true;
	}

}
