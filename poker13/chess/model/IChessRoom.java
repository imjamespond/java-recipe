package com.chitu.chess.model;

/**
 * @author Administrator
 * 
 */
public interface IChessRoom {
	
	public int setPlayerAbsent(long playerId);
	
	public int setPlayerReady(long playerId,ChessPlayer player);
	
	public boolean isIdle();
	
	public void checkState();
	
	public void showCard(String playerCard1, String playerCard2, String playerCard3, int specialType,ChessPlayer player);

	public int getPosition(long id);
	
	public long getId();
	
	public int getMoneyLimitation();

	public int getRateOfExchange();

	public int getComparisonTime();

	public int getRoomState();

	public int getPlayerNum();
	
	public int getTax();
	
	public long getChessRoomPlayerId(int pos);
	
	public int getChessRoomPlayerNum();
	
	public int getChessRoomPlayerState(int pos);

	public void setRate(int rate);

	public void cardSpecify(int num,long playerId,String cardStr);
}
