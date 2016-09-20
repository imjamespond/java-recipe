package com.chitu.poker.battle;

import cn.gecko.commons.data.BillType;
import cn.gecko.player.event.PlayerLogoutEvent;

import com.chitu.poker.battle.msg.BattleResult;
import com.chitu.poker.model.PokerBillTypes;
import com.chitu.poker.model.PokerPlayer;

public class PvpBattle extends Battle {

	public static int NEED_STRENGTH = 10;

	public PvpBattle(PokerPlayer myPlayer, PokerPlayer antiPlayer) {
		super();
		this.player = myPlayer;
		player.addEventListener(PlayerLogoutEvent.class, BattleManager.logoutListener);
		myMember = new BattleMember(myPlayer);
		antiMember = new BattleMember(antiPlayer);
	}

	public void start() {
		getPlayer(myMember).decreaseStrength(NEED_STRENGTH, BillType.get(PokerBillTypes.PVP_PAY),
				String.valueOf(antiMember.id));
	}

	protected void myWin(BattleMember member, BattleResult myResult) {

	}

	protected void antiWin() {

	}
}
