package com.chitu.chess.model;

import java.util.Arrays;

public class ChessRoomPlayer {
	public long playerId;
	public long roomId;
	public int position;
	public boolean npc = false;
	public PlayerStates playerState = PlayerStates.IDLE;

	// 特殊牌型
	public int cardSpecialType = 0;

	// 第一墩
	public int cardType = 0;
	public int cardType_ = 0;// 第一墩特殊牌型
	public int cardPower = 0;
	public int cardScore1[] = new int[4];
	public int cardCreep1[] = new int[4];

	// 第二墩
	public int cardType2 = 0;
	public int cardPower2 = 0;
	public int cardScore2[] = new int[4];
	public int cardCreep2[] = new int[4];
	// 第三墩
	public int cardType3 = 0;
	public int cardPower3 = 0;
	public int cardScore3[] = new int[4];
	public int cardCreep3[] = new int[4];
	// 特殊牌
	public int cardSpecial[] = new int[4];

	public int cardScoreSum = 0;
	public int cardMoneySum = 0;
	public int counter = 0;// 筹码
	// 打枪信息
	public int shotPosition[] = new int[4];
	public int shotNum = 0;

	public int playerCardSequence[] = new int[13];
	public int playerCardSequence1[] = new int[3];
	public int playerCardSequence2[] = new int[5];
	public int playerCardSequence3[] = new int[5];

	// trick
	public int trickNum = 0;
	public int trickCardSequence[] = new int[13];

	public enum PlayerStates {
		IDLE(0), READY(1), ASSINGED(Integer.valueOf("10", 2)), CHANGED(Integer.valueOf("100", 2)), SHOWED(Integer.valueOf("1000", 2)), ABSENT(Integer.valueOf("10000", 2)), ASSINGED_CHANGED_SHOWED_ABSENT(
				Integer.valueOf("11110", 2)), ASSINGED_CHANGED(Integer.valueOf("110", 2)), ABSENT_SHOWED(Integer.valueOf("11000", 2));
		public int state;

		private PlayerStates(int state) {
			this.state = state;
		}
	}

	public void cardAIThink() {
		// int playerCardSequence[] = { 35, 48, 49, 24, 14, 21, 26, 31, 9, 4,
		// 36, 23, 18 };
		// int playerCardSequence[] = { 42, 48, 32, 10, 49, 26, 4, 22, 16, 3,
		// 41, 27, 18};

		playerCardSequence3 = cardAI(playerCardSequence);
		Arrays.sort(playerCardSequence3);
		ChessUtils.chessLog.info(Arrays.toString(playerCardSequence3));

		int card8[] = new int[8];
		int eight = 8;
		for (int i = 0; i < playerCardSequence.length; ++i) {
			if (eight > 0) {
				if (Arrays.binarySearch(playerCardSequence3, playerCardSequence[i]) < 0) {// 该牌不在上墩牌中
					card8[--eight] = playerCardSequence[i];
				}
			}
		}

		playerCardSequence2 = cardAI(card8);
		Arrays.sort(playerCardSequence2);
		ChessUtils.chessLog.info(Arrays.toString(playerCardSequence2));

		int three = 3;
		for (int i = 0; i < card8.length; ++i) {
			if (three > 0) {
				if (Arrays.binarySearch(playerCardSequence2, card8[i]) < 0) {// 该牌不在上墩牌中
					playerCardSequence1[--three] = card8[i];
				}
			}
		}

		ChessUtils.chessLog.info(Arrays.toString(playerCardSequence1));
	}

	public int[] cardAI(int playerCardSequence[]) {

		ChessUtils.chessLog.info(Arrays.toString(playerCardSequence));

		Arrays.sort(playerCardSequence);

		int cardPattern[] = ChessCardTypes.CARDPATTERN;// 花样
		int cardPoint[] = ChessCardTypes.CARDPOINT;// 点数 1-13

		// 初始化牌	
		int four[] = new int[5];
		int three[] = new int[5];
		int dualPair[] = new int[5];
		int pair[] = new int[5];
		int oolong[] = new int[5];


		int i_four = 0;// 铁支点数
		int i_three = 0;// 三条点数
		int i_threeOfAKind = 0;// 葫芦点数
		int i_dualPair = 0;// 两对点数
		int i_pair = 0;// 对子点数
		int i_threeTheLast = 0;// 三条后面2张
		int i_pairTheLast = 0;// 对子后面3张
		int i_oolong = 0;// 乌龙张数

		
		StraightFlush sfd = new StraightFlush(48,32);
		StraightFlush sff = new StraightFlush(49,33);
		StraightFlush sfh = new StraightFlush(50,34);
		StraightFlush sfs = new StraightFlush(51,35);
		// 倒序检测同花顺
		int last = playerCardSequence.length - 1;
		for (int i = last; i >= 0; --i) {
			switch (cardPattern[playerCardSequence[i]]) {
			case ChessCardTypes.DIAMOND:
				if(detectStraightFlush(playerCardSequence[i],sfd))
					return sfd.cards;
				break;
			case ChessCardTypes.CLUB:
				if(detectStraightFlush(playerCardSequence[i],sff))
					return sff.cards;
				break;
			case ChessCardTypes.HEART:
				if(detectStraightFlush(playerCardSequence[i],sfh))
					return sfh.cards;
				break;
			case ChessCardTypes.SPADE:
				if(detectStraightFlush(playerCardSequence[i],sfs))
					return sfs.cards;
				break;
			}
		}
		// 检测普通同花顺
		sfd.reset();
		sff.reset();
		sfh.reset();
		sfs.reset();
		for (int i = last; i >= 0; --i) {
			switch (cardPattern[playerCardSequence[i]]) {
			case ChessCardTypes.DIAMOND:
				if(detectStraightFlush2(playerCardSequence[i],sfd))
					return sfd.cards;
				break;
			case ChessCardTypes.CLUB:
				if(detectStraightFlush2(playerCardSequence[i],sff))
					return sff.cards;
				break;
			case ChessCardTypes.HEART:
				if(detectStraightFlush2(playerCardSequence[i],sfh))
					return sfh.cards;
				break;
			case ChessCardTypes.SPADE:
				if(detectStraightFlush2(playerCardSequence[i],sfs))
					return sfs.cards;
				break;
			}
		}

		// 第一次遍确保所有牌型最大化 铁支,同花,葫芦
		for (int i = 0; i < playerCardSequence.length; ++i) {
			// 铁支
			if (i < playerCardSequence.length - 3) {// i为倒数第4张
				if (cardPoint[playerCardSequence[i + 1]] == cardPoint[playerCardSequence[i]]) {// 前后两张牌点数相同
					if (cardPoint[playerCardSequence[i + 2]] == cardPoint[playerCardSequence[i]]) {
						if (cardPoint[playerCardSequence[i + 3]] == cardPoint[playerCardSequence[i]]) {
							four[0] = playerCardSequence[i];
							four[1] = playerCardSequence[i + 1];
							four[2] = playerCardSequence[i + 2];
							four[3] = playerCardSequence[i + 3];
							i_four = cardPoint[playerCardSequence[i]];
						}
					}
				}
			}

			// 三条
			if (i < playerCardSequence.length - 2) {// i为倒数第3张
				if (cardPoint[playerCardSequence[i + 1]] == cardPoint[playerCardSequence[i]]) {// 前后两张牌点数相同
					if (cardPoint[playerCardSequence[i + 2]] == cardPoint[playerCardSequence[i]]) {
						three[0] = playerCardSequence[i];
						three[1] = playerCardSequence[i + 1];
						three[2] = playerCardSequence[i + 2];
						i_three = cardPoint[playerCardSequence[i]];
					}
				}
			}

			// 最大一对
			if (i < playerCardSequence.length - 1) {// i为倒数第2张
				if (cardPoint[playerCardSequence[i + 1]] == cardPoint[playerCardSequence[i]]) {// 前后两张牌点数相同

					dualPair[0] = playerCardSequence[i];
					dualPair[1] = playerCardSequence[i + 1];

					pair[0] = playerCardSequence[i];
					pair[1] = playerCardSequence[i + 1];
					i_pair = cardPoint[playerCardSequence[i]];

				}
			}

		}

		// 第二遍 组牌 铁支 葫芦 用最大的单牌作铁支可避免拆第二铁支的可能
		for (int i = 0; i < playerCardSequence.length; ++i) {

			if (i_four > 0) {// 算入铁支
				if (cardPoint[playerCardSequence[i]] != i_four) {
					four[4] = playerCardSequence[i];
				}
			}

			// 一对
			if (i < playerCardSequence.length - 1) {// i为倒数第2张
				if (cardPoint[playerCardSequence[i + 1]] == cardPoint[playerCardSequence[i]]) {// 前后两张牌点数相同

					if (i_three > 0) {// 算入葫芦
						if (cardPoint[playerCardSequence[i]] != i_three) {
							three[3] = playerCardSequence[i];
							three[4] = playerCardSequence[i + 1];
							i_threeOfAKind = cardPoint[playerCardSequence[i]];
						}
					}

					if (i_pair > 0) {// 算入两对
						if (cardPoint[playerCardSequence[i]] != i_pair) {
							dualPair[2] = playerCardSequence[i];
							dualPair[3] = playerCardSequence[i + 1];
							i_dualPair = cardPoint[playerCardSequence[i]];
						}
					}
				}
			}

		}

		// 铁支返回
		if (i_four > 0) {
			return four;
		}

		// 葫芦返回
		if (i_threeOfAKind > 0) {
			return three;
		}

		Flush fd = new Flush();
		Flush ff = new Flush();
		Flush fh = new Flush();
		Flush fs = new Flush();
		// 同花最大的返回
		for (int i = last; i >= 0; --i) {
			// 同花
			switch (cardPattern[playerCardSequence[i]]) {
			case ChessCardTypes.DIAMOND:
				if(detectFlush(playerCardSequence[i],fd))
					return fd.cards;
				break;
			case ChessCardTypes.CLUB:
				if(detectFlush(playerCardSequence[i],ff))
					return fd.cards;
				break;
			case ChessCardTypes.HEART:
				if(detectFlush(playerCardSequence[i],fh))
					return fd.cards;
				break;
			case ChessCardTypes.SPADE:
				if(detectFlush(playerCardSequence[i],fs))
					return fd.cards;
				break;
			}
		}

		
		Straight s = new Straight();
		// 倒序检测顺
		for (int i = last; i >= 0; --i) {
			if(detectStraight(playerCardSequence[i],s))
				return s.cards;
		}
		s.reset();
		for (int i = last; i >= 0; --i) {
			if(detectStraight2(playerCardSequence[i],s))
				return s.cards;
		}


		// 第三遍 乌龙 三条 两对 一对
		for (int i = 0; i < playerCardSequence.length; ++i) {

			// 乌龙
			if (i_oolong < 5) {
				oolong[i_oolong++] = playerCardSequence[i];
			} else {
				oolong[4] = playerCardSequence[i];
			}

			// 算入三条
			if (i_three > 0) {
				if (cardPoint[playerCardSequence[i]] != i_pair && cardPoint[playerCardSequence[i]] != i_three) {
					if (i_threeTheLast < 2) {
						three[3 + i_threeTheLast++] = playerCardSequence[i];// 后2张牌
					}
				}
			}

			// 算入两对
			if (i_dualPair > 0) {
				if (cardPoint[playerCardSequence[i]] != i_dualPair && cardPoint[playerCardSequence[i]] != i_pair) {
					dualPair[4] = playerCardSequence[i];
				}
			}

			// 算入一对
			if (i_pair > 0) {
				if (cardPoint[playerCardSequence[i]] != i_pair) {
					if (i_pairTheLast < 3) {
						pair[2 + i_pairTheLast++] = playerCardSequence[i];// 后3张牌
					}
				}
			}

		}

		// 三条返回
		if (i_three > 0) {
			return three;
		}
		// 两对返回
		if (i_dualPair > 0) {
			return dualPair;
		}
		// 一对返回
		if (i_pair > 0) {
			return pair;
		}
		return oolong;

		// ChessUtils.chessLog.info(Arrays.toString(straightFlush));
		// ChessUtils.chessLog.info(Arrays.toString(straight));
		//
		// ChessUtils.chessLog.info(Arrays.toString(flushDimond));
		// ChessUtils.chessLog.info(Arrays.toString(flushClub));
		// ChessUtils.chessLog.info(Arrays.toString(flushHeart));
		// ChessUtils.chessLog.info(Arrays.toString(flushSpade));
		// ChessUtils.chessLog.info(i_flushDimond);
		// ChessUtils.chessLog.info(i_flushClub);
		// ChessUtils.chessLog.info(i_flushHeart);
		// ChessUtils.chessLog.info(i_flushSpade);

		// ChessUtils.chessLog.info(Arrays.toString(four));
		// ChessUtils.chessLog.info(Arrays.toString(three));
		// ChessUtils.chessLog.info(Arrays.toString(dualPair));
		// ChessUtils.chessLog.info(Arrays.toString(pair));
		// ChessUtils.chessLog.info(Arrays.toString(oolong));

	}
	
	
	
	private boolean detectStraightFlush(int playerCard,StraightFlush sf){
		
		if (playerCard == sf.ace) {//是否存在A
			sf.haveA = true;
		}
		if (sf.cursor > 0) {//同花顺前后牌对比
			if (ChessCardTypes.CARDPOINT[sf.cards[(sf.cursor - 1) % 5]] != ChessCardTypes.CARDPOINT[playerCard] + 1) {// 与前一张不为顺序重新计算
				sf.cursor = 0;
			}
		}
		sf.cards[sf.cursor++ % 5] = playerCard;//设置当前牌,并将游标向后移
		if (sf.cursor > 4) {// 10JQKA, 同花顺有5张或以上, 
			if (sf.haveA && playerCard == sf.ten) 
				return true;
		}	
		if (sf.cursor > 3) {//A2345, 牌有4张或以上并且当前张为2,则判断是否有A的存 
			if (ChessCardTypes.CARDPOINT[playerCard] == 1 && sf.haveA) {
				sf.cards[sf.cursor % 5] = sf.ace;
				return true;
			}
		}
		return false;
	}
	
	private boolean detectStraightFlush2(int playerCard,StraightFlush sf){
		if (sf.cursor > 0) {
			if (ChessCardTypes.CARDPOINT[sf.cards[(sf.cursor - 1) % 5]] != ChessCardTypes.CARDPOINT[playerCard] + 1) {// 与前一张不为顺序重新计算
				sf.cursor = 0;
			}
		}
		sf.cards[sf.cursor++ % 5] = playerCard;//设置当前牌,并将游标向后移
		if (sf.cursor > 4) {
			return true;
		}
		return false;
	}
	
	private boolean detectStraight(int playerCard,Straight s){	
		// 顺
		if (ChessCardTypes.CARDPOINT[playerCard] == 13) {//是否存在A
			s.haveA = true;
			s.ace = playerCard;
		}
		if (s.cursor > 0) {// i为倒数第5张
			if (ChessCardTypes.CARDPOINT[s.cards[(s.cursor - 1) % 5]] == ChessCardTypes.CARDPOINT[playerCard]) {
				return false;//continue;
			}
			if (ChessCardTypes.CARDPOINT[s.cards[(s.cursor - 1) % 5]] != ChessCardTypes.CARDPOINT[playerCard] + 1) {// 前后两张牌点数为顺
				s.cursor = 0;
			}
		}
		s.cards[s.cursor++ % 5] = playerCard;
		if (s.cursor > 4) {
			if ( s.haveA && ChessCardTypes.CARDPOINT[playerCard] == 9) // 10JQKA
				return true;
		}
		if (s.cursor > 3) {
			if (ChessCardTypes.CARDPOINT[playerCard] == 1 && s.haveA) {// 最后（大）一张是A
				s.cards[s.cursor++ % 5] = s.ace;
				return true;
			}
		}
		return false;
	}
	
	private boolean detectStraight2(int playerCard,Straight s){	
		// 顺
		if (s.cursor > 0) {// i为倒数第5张
			if (ChessCardTypes.CARDPOINT[s.cards[(s.cursor - 1) % 5]] == ChessCardTypes.CARDPOINT[playerCard]) {
				return false;//continue;
			}
			if (ChessCardTypes.CARDPOINT[s.cards[(s.cursor - 1) % 5]] != ChessCardTypes.CARDPOINT[playerCard] + 1) {// 前后两张牌点数为顺
				s.cursor = 0;
			}
		}
		s.cards[s.cursor++ % 5] = playerCard;
		if (s.cursor > 4) {
			return true;
		}
		return false;
	}
	
	
	private boolean detectFlush(int playerCard,Flush f){	
		f.cards[f.cursor++ % 5] = playerCard;
		if (f.cursor > 4) {
			return true;
		}
		return false;
	}
}


class StraightFlush{
	public int cards[] = new int[5];
	public int cursor = 0;
	public boolean haveA = false;
	public int ace = 0;
	public int ten = 0;
	StraightFlush(int ace,int ten){
		this.ace = ace;
		this.ten = ten;
	}
	
	public void reset(){
		cursor = 0;
	}
}


class Straight{
	public int cards[] = new int[5];
	public int cursor = 0;
	public boolean haveA = false;
	public int ace = 0;
	
	public void reset(){
		cursor = 0;
	}
}


class Flush{
	public int cards[] = new int[5];
	public int cursor = 0;
	
	public void reset(){
		cursor = 0;
	}
}
