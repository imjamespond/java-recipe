package com.chitu.poker.battle;


public class NoJokerEvaluator extends HandEvaluator {

	protected NoJokerEvaluator(Card[] cards) {
		super(cards);
		initModelHand();
	}

	protected boolean checkJoker() {
		return false;
	}

	protected boolean checkForRoyalFlush() {
		return cards[0].rank == Card.Rank.TEN && checkForStraightFlush();
	}

	protected boolean checkForStraightFlush() {
		return isStraight(cards) && isFlush(cards);
	}

	protected boolean checkForFourOfAKind() {
		return cards[0].rank == cards[3].rank || cards[1].rank == cards[4].rank;
	}

	protected boolean checkForFullHouse() {
		if (cards[0].rank == cards[2].rank && cards[3].rank == cards[4].rank)
			return true;
		if (cards[0].rank == cards[1].rank && cards[2].rank == cards[4].rank)
			return true;
		return false;
	}

	protected boolean checkForFlush() {
		return isFlush(cards);
	}

	protected boolean checkForStraight() {
		return isStraight(cards);
	}

	protected boolean checkForThreeOfAKind() {
		return hasThree(cards);
	}

	protected boolean checkForTwoPair() {
		return hasTwoPair(cards);
	}

	protected boolean checkForOnePair() {
		return hasOnePair(cards);
	}

}
