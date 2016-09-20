package com.metasoft.flying.model;

import java.util.Deque;

import com.metasoft.flying.model.constant.ChessConstant;

public class Chess {

	// 路程
	protected int journey;//
	// 之前路程
	protected int preCoord;//
	// 当前坐标")
	protected int curCoord;//
	// 棋子所属0,1,2,3号玩家")
	protected int pos;//
	// 棋子位置0,1,2,3号")
	protected int chessPos;//
	// 棋子id 1,2,4,8
	//protected int chessId;//
	// 状态0未起飞1起飞中2飞行中4结束 8为不再触发道具")
	protected int state;

	public Chess(int pos, int chessPos) {
		journey = -1;
		this.pos = pos;
		this.chessPos = chessPos;
	}

	public void reset() {
		journey = -1;// 路程
		curCoord = ChessConstant.START[pos];// 当前坐标
		preCoord = 0;
		state = 0;
	}

	public void restart() {
		journey = -1;// 路程
		curCoord = ChessConstant.START[pos];// 当前坐标
		preCoord = 0;
		state = ChessConstant.CHESS_READY;
	}

	public void complete() {
		journey = -1;// 路程
		curCoord = ChessConstant.START[pos];// 当前坐标
		state = ChessConstant.CHESS_FINISH;
	}

	public static void go(Deque<Chess> list, int dice) {
		for (Chess chess : list) {
			int num = dice;
			// 刚起飞
			if (chess.state == ChessConstant.CHESS_READY) {
				chess.state = ChessConstant.CHESS_FLIGHT;
				chess.preCoord = chess.curCoord - 1;
			}else{
				chess.preCoord = chess.curCoord;
			}
			// 行程
			chess.journey += num;
			if (chess.journey >= ChessConstant.JOURNEY) {
				chess.state = ChessConstant.CHESS_FINISH;
				chess.curCoord = -1;
			} else if(chess.journey < 0){
				chess.state = ChessConstant.CHESS_READY;
				chess.journey = -1;
				chess.curCoord = -1;
			} else{
				// 坐标
				chess.curCoord = (chess.journey + ChessConstant.START[chess.getPos()]) % ChessConstant.DISTANCE;	
			}
			//System.out.printf("chess pos:%d coord:%d dice:%d\n", chess.getPos(), chess.curCoord, dice);
		}
	}

	public int getJourney() {
		return journey;
	}
	
	public int getCurCoord() {
		return curCoord;
	}

	public static void setJourney(Deque<Chess> list, int journey) {
		for (Chess chess : list) {
			// 刚起飞
			if (chess.state == ChessConstant.CHESS_READY) {
				chess.state = ChessConstant.CHESS_FLIGHT;
				chess.preCoord = chess.curCoord - 1;
			}else{
				chess.preCoord = chess.curCoord;
			}
			// 行程
			chess.journey = journey;
			if (chess.journey >= ChessConstant.JOURNEY) {
				chess.state = ChessConstant.CHESS_FINISH;
				chess.curCoord = -1;
			} else if(chess.journey < 0){
				chess.state = ChessConstant.CHESS_READY;
				chess.journey = -1;
				chess.curCoord = -1;
			} else{
				// 坐标
				chess.curCoord = (chess.journey + ChessConstant.START[chess.getPos()]) % ChessConstant.DISTANCE;	
			}
			//System.out.printf("chess pos:%d coord:%d dice:%d\n", chess.getPos(), chess.curCoord, dice);
		}
	}
	
	public static int getCoord( int journey, int pos) {
		return (journey + ChessConstant.START[pos]) % ChessConstant.DISTANCE;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getState() {
		return state;
	}
	
	public boolean isReady() {
		return (state & ChessConstant.CHESS_READY)>0;
	}
	
	public boolean isFlying() {
		return (state & ChessConstant.CHESS_FLIGHT)>0;
	}
	
	public boolean isOutside() {
		return getJourney() < ChessConstant.JOURNEY_OUT;
	}
	
	public boolean canJump() {
		return getJourney() < ChessConstant.JOURNEY_JUMP;
	}
	
	public boolean isFlyingOutside() {
		return isFlying()&&isOutside();
	}

	public void setState(int state) {
		this.state = state;
	}
	public void addState(int state) {
		this.state |= state;
	}
	public void baseState() {
		this.state &= ChessConstant.CHESS_BASE;
	}

	public int getChessPos() {
		return chessPos;
	}

	public void setChessPos(int chessPos) {
		this.chessPos = chessPos;
	}

	public int getPreCoord() {
		return preCoord;
	}

}
