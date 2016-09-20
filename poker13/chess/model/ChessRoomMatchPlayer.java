package com.chitu.chess.model;

public class ChessRoomMatchPlayer implements Comparable<ChessRoomMatchPlayer> {

	public Long playerId;
	public int enrollOrder;
	public int counter;//筹码
	public int score;//本局分数
	public int rank;
	public int dynRank;
	public int playerState;
	



	public ChessRoomMatchPlayer() {
		this.counter = 0;
	}
	
	@Override
	public int compareTo(ChessRoomMatchPlayer cp) {
		int tmp = cp.counter-this.counter;
		int tmps = cp.score-this.score;
		if(tmp == 0){
			tmp = tmps;
		}
		return tmp == 0?(this.enrollOrder - cp.enrollOrder):tmp;
	}
}
