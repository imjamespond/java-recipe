package com.chitu.poker.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.gecko.broadcast.MultiGeneralController;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.player.msg.ListDto;

import com.chitu.poker.battle.Area;
import com.chitu.poker.battle.Battle;
import com.chitu.poker.battle.BattleManager;
import com.chitu.poker.battle.InviteMember;
import com.chitu.poker.battle.PveBattle;
import com.chitu.poker.battle.msg.AreaDto;
import com.chitu.poker.battle.msg.BattleDto;
import com.chitu.poker.battle.msg.ChangeJokerResult;
import com.chitu.poker.battle.msg.InstanceDto;
import com.chitu.poker.battle.msg.InstanceRewardResult;
import com.chitu.poker.battle.msg.InviteMemberDto;
import com.chitu.poker.battle.msg.PveBattleDto;
import com.chitu.poker.battle.msg.PveRoundResult;
import com.chitu.poker.data.StaticBattle;
import com.chitu.poker.data.StaticInstance;
import com.chitu.poker.model.PokerErrorCodes;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.pet.PetTeam;
import com.chitu.poker.service.PokerPlayerManager;

@Controller
public class PveBattleController extends MultiGeneralController {

	@Autowired
	private BattleManager battleManager;

	@Autowired
	private PokerPlayerManager pokerPlayerManager;

	/**
	 * 区域列表，元素为AreaDto数组
	 * 
	 * @return
	 */
	public ListDto areas() {
		PokerPlayer player = pokerPlayerManager.getRequestPlayer();
		Collection<Area> areas = battleManager.getAllAreas();
		List<AreaDto> areaDtos = new ArrayList<AreaDto>(areas.size());
		for (Area area : areas) {
			areaDtos.add(new AreaDto(player, area));
		}
		return new ListDto(areaDtos);
	}

	/**
	 * 
	 * @param areaId
	 * @return
	 */
	// public AreaDto instances(int areaId) {
	// PokerPlayer player = pokerPlayerManager.getRequestPlayer();
	// Area area = battleManager.getArea(areaId);
	// AreaDto areaDto=new AreaDto(player, area);
	// return areaDto;
	// }

	/**
	 * 显示子副本内容
	 * 
	 * @param instanceId
	 *            副本ID
	 * @return
	 */
	public InstanceDto showInstance(int instanceId) {
		PokerPlayer player = pokerPlayerManager.getRequestPlayer();
		StaticInstance instance = StaticInstance.get(instanceId);
		InstanceDto dto = new InstanceDto(player, instance, null);
		PveBattle battle = (PveBattle) battleManager.getBattle(player.battleHolder.currentBattleId);
		List<StaticBattle> sBattles = battleManager.getInstanceBattles(instanceId);
		if (sBattles == null)
			sBattles = Collections.emptyList();
		List<BattleDto> battles = new ArrayList<BattleDto>(sBattles.size());
		for (StaticBattle sBattle : sBattles) {
			boolean pass = false;
			if (battle != null)
				pass = ArrayUtils.contains(battle.saveRecord.passBattles, sBattle.getId());
			battles.add(new BattleDto(sBattle, pass));
		}
		dto.setBattles(battles);
		if (battle != null) {
			dto.setCurrentHp(battle.saveRecord.currentHp);
			dto.setMaxHp(battle.saveRecord.maxHp);
			dto.setRsumeHpCount(battle.saveRecord.resumeHpCount);
		} else {
			PetTeam petTeam = player.petHolder.getTeam();
			int maxHp = 0;
			for (long petId : petTeam.pet) {
				if (petId <= 0)
					continue;
				Pet pet = player.petHolder.getPet(petId);
				if (pet == null)
					continue;
				maxHp += pet.maxHp;
			}
			InviteMember inviteMember = player.battleInviteHolder.currentInviteMember;
			if (inviteMember != null)
				maxHp += inviteMember.pet.maxHp;
			dto.setCurrentHp(maxHp);
			dto.setMaxHp(maxHp);
			dto.setRsumeHpCount(instance.getRsumeHpCount());
		}
		return dto;
	}

	/**
	 * 开始打关卡
	 * 
	 * @param staticBattleId
	 */
	public PveBattleDto startPveBattle(int staticBattleId) {
		PokerPlayer player = pokerPlayerManager.getRequestPlayer();
		StaticBattle staticBattle = StaticBattle.get(staticBattleId);
		if (staticBattle == null)
			throw new GeneralException(PokerErrorCodes.STATIC_BATTLE_NOT_EXIST);
		StaticInstance staticInstance = StaticInstance.get(staticBattle.getInstanceId());
		PveBattle battle = null;
		if (player.battleHolder.currentBattleId > 0) {
			battle = (PveBattle) battleManager.getBattle(player.battleHolder.currentBattleId);
		}
		if (battle == null) {
			InviteMember inviteMember = player.battleInviteHolder.currentInviteMember;
			battle = new PveBattle(player, staticInstance, inviteMember);
			player.battleInviteHolder.currentInviteMember = null;
			battleManager.addBattle(battle);
			player.battleHolder.currentBattleId = battle.id;
			if (inviteMember != null) {
				player.battleInviteHolder.addHourInvite(inviteMember.inviteId);
			}
		}
		battle.startBattle(staticBattle);
		if (battle.instance.getId() == PveBattle.NEWBIE_INSTANCE_ID
				|| battle.instance.getParentId() == PveBattle.NEWBIE_INSTANCE_ID)
			battle.saveRecord.passBattles = ArrayUtils.add(battle.saveRecord.passBattles, staticBattleId);
		return new PveBattleDto(battle);
	}

	private Battle getBattle() {
		PokerPlayer player = pokerPlayerManager.getRequestPlayer();
		Battle battle = battleManager.getBattle(player.battleHolder.currentBattleId);
		if (battle == null)
			throw new GeneralException(PokerErrorCodes.BATTLE_NOT_EXIST);
		return battle;
	}

	private PveBattle getPveBattle() {
		Battle battle = getBattle();
		if (battle instanceof PveBattle)
			return (PveBattle) battle;
		return null;
	}

//	private PvpBattle getPvpBattle() {
//		Battle battle = getBattle();
//		if (battle instanceof PvpBattle)
//			return (PvpBattle) battle;
//		return null;
//	}

	/**
	 * 放牌
	 * 
	 * @param tableIndex1
	 *            桌面位置索引1
	 * @param pokerIndex1
	 *            手上牌的位置索引1
	 * @param tableIndex2
	 *            桌面位置索引2
	 * @param pokerIndex2
	 *            手上牌的位置索引2
	 */
	public PveRoundResult putPoker(int tableIndex1, int pokerIndex1, int tableIndex2, int pokerIndex2) {
		Battle battle = getBattle();
		return battle.putPoker(battle.myMember, tableIndex1, pokerIndex1, tableIndex2, pokerIndex2);
	}

	/**
	 * 放弃本回合
	 * 
	 * @return
	 */
	public PveRoundResult pass() {
		Battle battle = getBattle();
		return battle.pass(battle.myMember);
	}

	/**
	 * 恢复HP
	 */
	public void resumeHp() {
		PveBattle battle = getPveBattle();
		battle.resumeHp(battle.myMember);
	}

	/**
	 * 复活
	 */
	public void relive() {
		PveBattle battle = getPveBattle();
		battle.relive(battle.myMember);
	}

	/**
	 * 离开副本
	 */
	public void exitInstance() {
		PokerPlayer player = pokerPlayerManager.getRequestPlayer();
		PveBattle battle = (PveBattle) battleManager.getBattle(player.battleHolder.currentBattleId);
		if (battle == null)
			return;
		battle.exit(battle.myMember);
		battleManager.destroyBattle(player.battleHolder.currentBattleId);
		player.battleHolder.currentBattleId = 0;
	}

	/**
	 * 查看副本累计奖励
	 */
	public InstanceRewardResult reward() {
		PokerPlayer player = pokerPlayerManager.getRequestPlayer();
		PveBattle battle = (PveBattle) battleManager.getBattle(player.battleHolder.currentBattleId);
		if (battle != null)
			return new InstanceRewardResult(battle.saveRecord);
		else
			return new InstanceRewardResult("");
	}

	/**
	 * 获得副本累计奖励
	 */
	public void getInstanceReward() {
		PokerPlayer player = pokerPlayerManager.getRequestPlayer();
		PveBattle battle = getPveBattle();
		battle.getInstanceReward();
		battle.exit(battle.myMember);
		battleManager.destroyBattle(player.battleHolder.currentBattleId);
		player.battleHolder.currentBattleId = 0;
		if (battle.instance.getId() == PveBattle.NEWBIE_INSTANCE_ID
				|| battle.instance.getParentId() == PveBattle.NEWBIE_INSTANCE_ID)
			player.newbieStep = PveBattle.NEWBIE_BATTLE_STEP;
	}

	/**
	 * 能量满换万能牌
	 * 
	 * @param pokerIndex
	 */
	public ChangeJokerResult changeJoker(int pokerIndex) {
		PveBattle battle = getPveBattle();
		return battle.changeJoker(battle.myMember, pokerIndex);
	}

	/**
	 * 邀请好友战斗列表，元素为InviteMemberDto
	 */
	public ListDto inviteList() {
		PokerPlayer player = pokerPlayerManager.getRequestPlayer();
		List<InviteMember> invites = player.battleInviteHolder.getInviteList();
		List<InviteMemberDto> inviteDtos = new ArrayList<InviteMemberDto>(invites.size());
		for (InviteMember invite : invites) {
			inviteDtos.add(new InviteMemberDto(invite));
		}
		return new ListDto(inviteDtos);
	}

	/**
	 * 
	 * @param inviteId
	 *            邀请者ID
	 */
	public void invite(String inviteId) {
		PokerPlayer player = pokerPlayerManager.getRequestPlayer();
		player.battleInviteHolder.invite(Long.parseLong(inviteId));
	}

}
