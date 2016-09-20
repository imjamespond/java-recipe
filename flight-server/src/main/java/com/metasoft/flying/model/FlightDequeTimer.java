package com.metasoft.flying.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public abstract class FlightDequeTimer implements Runnable {

	private final Deque<IFlight> flightDeq = new ArrayDeque<>();
	/**
	 * @param flightService
	 */
	public FlightDequeTimer() {
	}
	
	public synchronized int getDequeSize() {
		return flightDeq.size();
	}
//	public Deque<IFlight> getFlightDeq() {
//		return flightDeq;
//	}
	public synchronized void addFlightDeq(IFlight f) {
		this.flightDeq.add(f);
	}

	public abstract void checkDeque();

	@Override
	public synchronized void run() {
		try {
			Iterator<IFlight> it = flightDeq.iterator();
			while (it.hasNext()) {
				IFlight flight = it.next();
				//检查是否结束
				if(flight.check()){
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