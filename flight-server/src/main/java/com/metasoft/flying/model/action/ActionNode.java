package com.metasoft.flying.model.action;

import com.metasoft.flying.model.Flight;

public abstract class ActionNode {

	public static final long kDice6 = 4;// 扔六出飞机
	public static final long kCheat = 2;//三格内必吃
	public static final long kMove3 = 8;// 困难走棋
	public static final long kItem = 1;// 使用道具
	public ActionNode next;

	/**
	 * @param flight
	 * @return true if decided false not decided
	 */
	abstract public boolean act(Flight flight);
}