package com.chitu.poker.battle;

import org.apache.commons.lang.ArrayUtils;

import cn.gecko.commons.data.BillType;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.RandomUtils;
import cn.gecko.player.event.PlayerLogoutEvent;

import com.chitu.poker.battle.Card.Rank;
import com.chitu.poker.battle.Card.Suit;
import com.chitu.poker.battle.msg.BattleResult;
import com.chitu.poker.battle.msg.ChangeJokerResult;
import com.chitu.poker.battle.msg.PokerDto;
import com.chitu.poker.data.StaticBattle;
import com.chitu.poker.data.StaticBattle.PosType;
import com.chitu.poker.data.StaticInstance;
import com.chitu.poker.data.StaticMonster;
import com.chitu.poker.model.ExpType;
import com.chitu.poker.model.PokerBillTypes;
import com.chitu.poker.model.PokerErrorCodes;
import com.chitu.poker.model.PokerExpTypes;
import com.chitu.poker.model.PokerPlayer;

public class PveBattle extends Battle {

	public static int NEWBIE_INSTANCE_ID = 900001;
	public static int NEWBIE_BATTLE_STEP = 1000;

	public SaveRecord saveRecord;

	public StaticInstance instance;

	public StaticBattle currentBattle;

	public StaticMonster currentMonster;

	public InviteMember inviteMember;

	public PveBattle(PokerPlayer player, StaticInstance instance, InviteMember inviteMember) {
		super();
		this.player = player;
		this.instance = instance;
		this.inviteMember = inviteMember;
		player.addEventListener(PlayerLogoutEvent.class, BattleManager.logoutListener);
		myMember = new BattleMember(player, instance, inviteMember);
		myMember.resumeHpCount = instance.getRsumeHpCount();
		saveRecord = new SaveRecord();
		saveRecord.instanceId = instance.getId();
		saveRecord.maxHp = myMember.maxHp;
		saveRecord.currentHp = myMember.currentHp;
		saveRecord.resumeHpCount = instance.getRsumeHpCount();
		if (inviteMember != null)
			saveRecord.invitePlayerId = inviteMember.inviteId;
	}

	/**
	 * 开始打关卡
	 * 
	 * @param staticBattle
	 */
	public void startBattle(StaticBattle staticBattle) {
		if (myMember.currentHp <= 0)
			throw new GeneralException(PokerErrorCodes.HAS_DIE);
		if (!isBattlePass(staticBattle.getPreBattleId()))
			throw new GeneralException(PokerErrorCodes.PRE_BATTLE_NOT_PASS);
		if (isBattlePass(staticBattle.getId()))
			throw new GeneralException(PokerErrorCodes.BATTLE_HAS_PASS);
		if (player.strength < staticBattle.getNeedStrength())
			throw new GeneralException(PokerErrorCodes.STRENGTH_NOT_ENOUGH);
		//
		this.currentBattle = staticBattle;
		this.currentMonster = StaticMonster.get(currentBattle.getMonsterId());
		antiMember = new BattleMember(currentMonster);
		myMember.clearPokers();
		createNewSetPoker();
		resetTablePokers();
		// 我的牌补满，对方补两张牌
		givePokerToMember(myMember);
		antiMember.pokers[0] = battlePokers.remove(0);
		antiMember.pokers[1] = battlePokers.remove(0);
	}

	private void clearSaveRecord() {
		saveRecord.passBattles = null;
		saveRecord.rewardExp = 0;
		saveRecord.rewardMoney = 0;
		saveRecord.rewardPetIds = null;
		saveRecord.resumeHpCount = 0;
	}

	/**
	 * 获得副本奖励
	 */
	public void getInstanceReward() {
		if (!isFinalBattlePass())
			throw new GeneralException(PokerErrorCodes.FINAL_BATTLE_NOT_PASS);
		getPlayer(myMember).incExp(saveRecord.rewardExp, ExpType.get(PokerExpTypes.INSTANCE_REWARD),
				String.valueOf(instance.getId()));
		getPlayer(myMember).wealthHolder.increaseMoney(saveRecord.rewardMoney,
				BillType.get(PokerBillTypes.INSTANCE_REWARD), String.valueOf(instance.getId()));
		if (saveRecord.rewardPetIds != null && saveRecord.rewardPetIds.length > 0) {
			getPlayer(myMember).petHolder.addPet(saveRecord.rewardPetIds);
		}
		exit(myMember);
	}

	/**
	 * 换王
	 */
	public ChangeJokerResult changeJoker(BattleMember member, int pokerIndex) {
		if (member.power < Battle.MEMBER_MAX_POWER)
			throw new GeneralException(PokerErrorCodes.POWER_NOT_ENOUGH);
		boolean hasBigJoker = false;
		boolean hasSmallJoker = false;
		// 判断台面上的牌的大小王情况
		for (Card card : myMember.pokers) {
			if (card == null)
				continue;
			if (card.suit == Suit.BLACK)
				hasSmallJoker = true;
			if (card.suit == Suit.RED)
				hasBigJoker = true;
		}
		for (Card card : antiMember.pokers) {
			if (card == null)
				continue;
			if (card.suit == Suit.BLACK)
				hasSmallJoker = true;
			if (card.suit == Suit.RED)
				hasBigJoker = true;
		}
		for (Card card : battleTable) {
			if (card == null)
				continue;
			if (card.suit == Suit.BLACK)
				hasSmallJoker = true;
			if (card.suit == Suit.RED)
				hasBigJoker = true;
		}
		// 台面上大小王都出现了，不能换王
		if (hasBigJoker && hasSmallJoker)
			throw new GeneralException(PokerErrorCodes.HAS_TWO_JOKER);
		Card newPoker = null;
		// 有大王了就换成小王
		if (hasBigJoker) {
			removePokerFromRestPokers(Rank.JOKER, Suit.BLACK);
			newPoker = new Card(Rank.JOKER, Suit.BLACK);
		}
		// 其他一律变大王
		else {
			removePokerFromRestPokers(Rank.JOKER, Suit.RED);
			newPoker = new Card(Rank.JOKER, Suit.RED);
		}
		member.power -= MEMBER_MAX_POWER;
		member.pokers[pokerIndex] = newPoker;
		return new ChangeJokerResult(new PokerDto(newPoker), member.power);
	}

	/**
	 * 离开副本
	 */
	public void exit(BattleMember member) {
		clearSaveRecord();
		getPlayer(member).battleHolder.setSaveRecord(null);
	}

	/**
	 * 恢复Hp
	 */
	public void resumeHp(BattleMember member) {
		if (member.resumeHpCount <= 0)
			throw new GeneralException(PokerErrorCodes.RESUME_HP_NO_COUNT);
		if (member.currentHp <= 0)
			throw new GeneralException(PokerErrorCodes.HAS_DIE);
		member.currentHp = member.maxHp;
		member.resumeHpCount--;
		saveRecord.currentHp = member.currentHp;
		saveRecord.resumeHpCount = member.resumeHpCount;
	}

	/**
	 * 复活
	 */
	public void relive(BattleMember member) {
		if (!getPlayer(member).wealthHolder.hasEnoughPoint(RELIVE_NEED_POINT))
			throw new GeneralException(PokerErrorCodes.POINT_NOT_ENOUGH);
		getPlayer(member).wealthHolder.decreasePoint(RELIVE_NEED_POINT, BillType.get(PokerBillTypes.BATTLE_RELIVE),
				instance.getId() + ":" + currentBattle.getId());
		member.currentHp = member.maxHp;
		saveRecord.currentHp = member.currentHp;
	}

	protected boolean isBattleEnd() {
		return currentBattle == null;
	}

	protected int battleResultId() {
		return currentBattle.getId();
	}

	protected void myWin(BattleMember member, BattleResult myResult) {
		getPlayer(member).decreaseStrength(currentBattle.getNeedStrength(),
				BillType.get(PokerBillTypes.BATTLE_WIN_PAY), instance.getId() + ":" + currentBattle.getId());
		myResult.setRewardExp(currentBattle.getRewardExp());
		myResult.setRewardMoney(RandomUtils.nextInt(currentBattle.getRewardMinMoney(),
				currentBattle.getRewardMaxMoney()));
		if (currentBattle.getRewardPetId1() > 0
				&& RandomUtils.randomHit(RandomUtils.BASE, currentBattle.getRewardPetRate1()))
			myResult.setRewardPets(ArrayUtils.add(myResult.getRewardPets(), currentBattle.getRewardPetId1()));
		if (currentBattle.getRewardPetId2() > 0
				&& RandomUtils.randomHit(RandomUtils.BASE, currentBattle.getRewardPetRate2()))
			myResult.setRewardPets(ArrayUtils.add(myResult.getRewardPets(), currentBattle.getRewardPetId2()));
		if (currentBattle.getRewardPetId3() > 0
				&& RandomUtils.randomHit(RandomUtils.BASE, currentBattle.getRewardPetRate3()))
			myResult.setRewardPets(ArrayUtils.add(myResult.getRewardPets(), currentBattle.getRewardPetId3()));
		if (currentBattle.getRewardPetId4() > 0
				&& RandomUtils.randomHit(RandomUtils.BASE, currentBattle.getRewardPetRate4()))
			myResult.setRewardPets(ArrayUtils.add(myResult.getRewardPets(), currentBattle.getRewardPetId4()));
		if (currentBattle.getRewardPetId5() > 0
				&& RandomUtils.randomHit(RandomUtils.BASE, currentBattle.getRewardPetRate5()))
			myResult.setRewardPets(ArrayUtils.add(myResult.getRewardPets(), currentBattle.getRewardPetId5()));

		saveRecord.currentHp = myMember.currentHp;
		saveRecord.maxHp = myMember.maxHp;
		saveRecord.instanceId = instance.getId();
		saveRecord.resumeHpCount = myMember.resumeHpCount;
		saveRecord.needRelive = 0;
		saveRecord.passBattles = ArrayUtils.add(saveRecord.passBattles, currentBattle.getId());
		saveRecord.rewardExp += myResult.getRewardExp();
		saveRecord.rewardMoney += myResult.getRewardMoney();
		if (myResult.getRewardPets() != null && myResult.getRewardPets().length > 0) {
			saveRecord.rewardPetIds = ArrayUtils.addAll(saveRecord.rewardPetIds, myResult.getRewardPets());
		}
		if (currentBattle.isFinalBattle()) {
			getPlayer(member).battleHolder.passInstance(instance.getId());
		}
		if (inviteMember != null) {
			getPlayer(member).battleInviteHolder.addDailyInvite(inviteMember.inviteId);
			if (inviteMember.friendPoint > 0)
				getPlayer(member).increaseFriendPoint(inviteMember.friendPoint,
						BillType.get(PokerBillTypes.BATTLE_WIN_GET), String.valueOf(instance.getId()));
		}
		getPlayer(member).battleHolder.setSaveRecord(saveRecord);
		currentBattle = null;
		currentMonster = null;
	}

	protected void antiWin() {
		saveRecord.currentHp = myMember.currentHp;
		saveRecord.maxHp = myMember.maxHp;
		saveRecord.instanceId = instance.getId();
		saveRecord.resumeHpCount = myMember.resumeHpCount;
		saveRecord.needRelive = 1;
		getPlayer(myMember).battleHolder.setSaveRecord(saveRecord);
		currentBattle = null;
		currentMonster = null;
	}

	/**
	 * 判断某个副本是否通过
	 * 
	 * @param staticBattleId
	 * @return
	 */
	public boolean isBattlePass(int staticBattleId) {
		if (staticBattleId <= 0)
			return true;
		StaticBattle sb = StaticBattle.get(staticBattleId);
		if (sb == null)
			return false;
		// 前置为入口或者出口的时候
		if (sb.getPosType() == PosType.Entrance.ordinal() || sb.getPosType() == PosType.Exit.ordinal())
			return true;
		return ArrayUtils.contains(saveRecord.passBattles, staticBattleId);
	}

	/**
	 * 是否通过最终关卡
	 * 
	 * @param staticBattleId
	 * @return
	 */
	public boolean isFinalBattlePass() {
		if (saveRecord.passBattles == null || saveRecord.passBattles.length == 0) {
			return false;
		}
		for (int staticBattleId : saveRecord.passBattles) {
			StaticBattle sb = StaticBattle.get(staticBattleId);
			if (sb == null)
				continue;
			if (sb.isFinalBattle())
				return true;
		}
		return false;
	}

}
