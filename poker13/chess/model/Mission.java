package com.chitu.chess.model;

public class Mission {

	public enum State {
		NOTDONE, DONE, PRIZE;
	}

	public int id = 0;
	public State state = State.NOTDONE;// 局数任务id
}
