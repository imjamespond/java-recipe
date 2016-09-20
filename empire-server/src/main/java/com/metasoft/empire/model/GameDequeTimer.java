package com.metasoft.empire.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public abstract class GameDequeTimer implements Runnable {

	private final Deque<GameRoom> gameDeq = new ArrayDeque<>();

	public GameDequeTimer() {
	}
	
	public synchronized int getDequeSize() {
		return gameDeq.size();
	}

	public synchronized void addGameDeq(GameRoom f) {
		this.gameDeq.add(f);
	}

	public abstract void checkDeque();

	@Override
	public synchronized void run() {
		try {
			Iterator<GameRoom> it = gameDeq.iterator();
			while (it.hasNext()) {
				GameRoom game = it.next();
				//检查是否结束
				if(game.check()){
					it.remove();
				}
			}
			//检视队列
			checkDeque();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}