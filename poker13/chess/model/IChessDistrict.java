package com.chitu.chess.model;

public interface IChessDistrict {
	
	public boolean checkRoomMutex(long playerId);

	public void leavingCheck(long playerId, IChessRoom chessRoom);
	
	public boolean containRoom(long roomId);

	public IChessRoom getIChessRoom(long roomId);

	public void decPlayerNum();

	public void incPlayerNum();

	public int getPlayerNum();

}
