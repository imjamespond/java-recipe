package com.chitu.poker.battle;

public class OneJokerEvaluator extends HandEvaluator {

	private Card[] prefixCards;

	protected OneJokerEvaluator(Card[] cards) {
		super(cards);
		prefixCards = new Card[] { cards[0], cards[1], cards[2], cards[3] };
		initModelHand();
	}

	protected boolean checkJoker() {
		return cards[0].rank == cards[3].rank;
	}

	protected boolean checkForRoyalFlush() {
		return cards[0].rank == Card.Rank.TEN && checkForStraightFlush();
	}

	protected boolean checkForStraightFlush() {
		return isStraight(prefixCards) && isFlush(prefixCards);
	}

	protected boolean checkForFourOfAKind() {
		return hasThree(prefixCards);
	}

	protected boolean checkForFullHouse() {
		return hasThree(prefixCards) || hasTwoPair(prefixCards);
	}

	protected boolean checkForFlush() {
		return isFlush(prefixCards);
	}

	protected boolean checkForStraight() {
		return isStraight(prefixCards);
	}

	protected boolean checkForThreeOfAKind() {
		return hasOnePair(prefixCards);
	}

	protected boolean checkForTwoPair() {
		return hasOnePair(prefixCards);
	}

	protected boolean checkForOnePair() {
		return true;
	}

}
