package com.metasoft.empire.vo;

import java.util.ArrayList;
import java.util.List;

import com.metasoft.empire.common.annotation.DescAnno;
import com.metasoft.empire.common.annotation.EventAnno;

@DescAnno("战斗消息")
@EventAnno(desc = "", name = "event.game.combat")
public class GameCombatVO {
	private int turn;
	private int state;
	private int oper0;
	private int oper1;
	private int[] swap0;
	private int[] swap1;
	private List<GameAnimVO> anim0 = new ArrayList<>(3);
	private List<GameAnimVO> anim1 = new ArrayList<>(3);
	
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int[] getSwap0() {
		return swap0;
	}
	public void setSwap0(int[] swap0) {
		this.swap0 = swap0;
	}
	public int[] getSwap1() {
		return swap1;
	}
	public void setSwap1(int[] swap1) {
		this.swap1 = swap1;
	}
	public List<GameAnimVO> getAnim0() {
		return anim0;
	}
	public void setAnim0(List<GameAnimVO> anim0) {
		this.anim0 = anim0;
	}
	public List<GameAnimVO> getAnim1() {
		return anim1;
	}
	public void setAnim1(List<GameAnimVO> anim1) {
		this.anim1 = anim1;
	}
	public int getOper0() {
		return oper0;
	}
	public void setOper0(int oper0) {
		this.oper0 = oper0;
	}
	public int getOper1() {
		return oper1;
	}
	public void setOper1(int oper1) {
		this.oper1 = oper1;
	}
	
}
