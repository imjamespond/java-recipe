package com.chitu.poker.msg;

import cn.gecko.broadcast.BroadcastMessage;

import com.chitu.poker.data.StaticPlayerGrade;
import com.chitu.poker.model.PokerPlayer;

/**
 * 经验变更通知
 * @author open
 *
 */
public class ExpNotify implements BroadcastMessage {

	private String playerId;
	
	private int grade;
	
	private int exp;
	
	private int maxExp;
	
	
	public ExpNotify(PokerPlayer player){
		this.playerId = String.valueOf(player.id);
		this.grade = player.grade;
		this.exp = player.getExp();
		StaticPlayerGrade staticPlayer = StaticPlayerGrade.get(player.grade);
		this.maxExp = staticPlayer.getUpdateExp();
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	/**当前经验**/
	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	/**最大经验**/
	public int getMaxExp() {
		return maxExp;
	}

	public void setMaxExp(int maxExp) {
		this.maxExp = maxExp;
	}

	/**当前等级**/
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}
	
}
