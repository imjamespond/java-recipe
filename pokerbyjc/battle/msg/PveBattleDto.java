package com.chitu.poker.battle.msg;

import com.chitu.poker.battle.Card;
import com.chitu.poker.battle.PveBattle;

import cn.gecko.broadcast.GeneralResponse;

public class PveBattleDto extends GeneralResponse {

	private BattleMemberDto myMember;

	private BattleMemberDto antiMember;

	private PokerDto[] tablePokers;

	private int battleId;

	public PveBattleDto(PveBattle pveBattle) {
		myMember = new BattleMemberDto(pveBattle.myMember);
		antiMember = new BattleMemberDto(pveBattle.antiMember);
		tablePokers = new PokerDto[pveBattle.battleTable.length];
		for (int i = 0; i < pveBattle.battleTable.length; i++) {
			Card card = pveBattle.battleTable[i];
			if (card != null)
				tablePokers[i] = new PokerDto(card.rank.ordinal(), card.suit.ordinal());
			else
				tablePokers[i] = null;
		}
		battleId = pveBattle.currentBattle.getId();
	}

	/**
	 * 我方的资料
	 * 
	 * @return
	 */
	public BattleMemberDto getMyMember() {
		return myMember;
	}

	public void setMyMember(BattleMemberDto myMember) {
		this.myMember = myMember;
	}

	/**
	 * 对方的资料
	 * 
	 * @return
	 */
	public BattleMemberDto getAntiMember() {
		return antiMember;
	}

	public void setAntiMember(BattleMemberDto antiMember) {
		this.antiMember = antiMember;
	}

	/**
	 * 台面的扑克，PokerDto数组，数组的下标跟位置对应，没牌的位置为null<br>
	 * 0---1---2---3---4 <br>
	 * 5---6---7---8---9 <br>
	 * 10--11--12--13--14 <br>
	 * 15--16--17--18--19 <br>
	 * 20--21--22--23--24 <br>
	 * 
	 * @return
	 */
	public PokerDto[] getTablePokers() {
		return tablePokers;
	}

	public void setTablePokers(PokerDto[] tablePokers) {
		this.tablePokers = tablePokers;
	}

	/**
	 * 关卡ID
	 * 
	 * @return
	 */
	public int getBattleId() {
		return battleId;
	}

	public void setBattleId(int battleId) {
		this.battleId = battleId;
	}

}
