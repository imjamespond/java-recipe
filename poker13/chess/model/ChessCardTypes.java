package com.chitu.chess.model;

public class ChessCardTypes {

	public static final int CARDNUM = 52;
	public static final int POWERLEVELUP = 14;// 使权值提升一级
	public static final int PLAYERNUM = 4;
	public static final int PLAYERCARDNUM = 13;

	public static final int SPADE = 8;//(int) Math.pow(2, 3);// 8
	public static final int HEART = 4;//(int) Math.pow(2, 2);// 4
	public static final int CLUB = 2;//(int) Math.pow(2, 1);// 2
	public static final int DIAMOND = 1;//(int) Math.pow(2, 0);// 1
	public static final int DIAMOND_HEART = DIAMOND | HEART;
	public static final int CLUB_SPADE = SPADE | CLUB;

	//特殊牌型
	public static final int ALL_STRAIGHT_FLUSH = 21;
	public static final int ALL_STRAIGHT = 20;
	public static final int THIRTEEN_NOBLES = 19;
	public static final int TRIPLE_STRAIGHT_FLUSH = 18;
	public static final int TRIPLE_FOUR_OF_AKIND = 17;
	public static final int ALL_GREAT = 16;
	public static final int ALL_SMALL = 15;
	public static final int THE_SAME_COLOR = 14;
	public static final int FOUR_THREE_OF_AKIND = 13;
	public static final int FIVE_PAIRS_N_THREE_OF_AKIND = 12;
	public static final int SIX_PAIRS = 11;
	public static final int TRIPLE_STRAIGHT = 10;
	public static final int TRIPLE_FLUSH = 9;
	//每墩基本牌型
	public static final int STRAIGHT_FLUSH = 8;
	public static final int FOUR_OF_AKIND = 7;
	public static final int THREE_OF_AKIND = 6;
	public static final int FLUSH = 5;
	public static final int STRAIGHT = 4;
	public static final int THREE_ONLY = 3;
	public static final int DUAL_PAIR = 2;
	public static final int ONE_PAIR = 1;
	public static final int OOLONG = 0;
	//第一墩特殊牌型
	public static final int STRAIGHT3 = (int) Math.pow(2,0);//1
	public static final int FLUSH3 = (int) Math.pow(2,1);//2
	public static final int STRAIGHT_FLUSH3 = STRAIGHT3 | FLUSH3;
	
	
	public static final int[] CARDPATTERN = { DIAMOND, CLUB, HEART, SPADE, DIAMOND, CLUB, HEART, SPADE, DIAMOND, CLUB, HEART, SPADE, DIAMOND, CLUB, HEART, SPADE, DIAMOND, CLUB,
			HEART, SPADE, DIAMOND, CLUB, HEART, SPADE, DIAMOND, CLUB, HEART, SPADE, DIAMOND, CLUB, HEART, SPADE, DIAMOND, CLUB, HEART, SPADE, DIAMOND, CLUB, HEART, SPADE, DIAMOND,
			CLUB, HEART, SPADE, DIAMOND, CLUB, HEART, SPADE, DIAMOND, CLUB, HEART, SPADE };// 花样
	public static final int[] CARDPOINT = { 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 10, 11, 11, 11,
			11, 12, 12, 12, 12, 13, 13, 13, 13 };// 点数 1-13

}
