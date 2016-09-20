package com.chitu.poker.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.time.DateUtils;

import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.friend.model.Friend;

import com.chitu.poker.model.PersistPokerPlayer;
import com.chitu.poker.model.PokerErrorCodes;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.pet.PetTeam;
import com.chitu.poker.service.PokerPlayerManager;

public class BattleInviteHolder {

	private long playerId;

	private PersistBattleInvite persistBattleInvite;

	private Map<Long, Long> dailyInvites;

	private Map<Long, Long> hoursInvites;

	private List<InviteMember> currentInviteList;

	public InviteMember currentInviteMember;

	private boolean update;

	private boolean needSave;

	public BattleInviteHolder(long playerId) {
		this.playerId = playerId;
	}

	private void init() {
		if (persistBattleInvite != null) {
			return;
		}

		persistBattleInvite = PersistBattleInvite.get(playerId);
		if (persistBattleInvite != null) {
			dailyInvites = PersistBattleInvite.initInvites(persistBattleInvite.getDailyInvites());
			hoursInvites = PersistBattleInvite.initInvites(persistBattleInvite.getHourInvites());
		} else {
			needSave = true;
			persistBattleInvite = new PersistBattleInvite();
			persistBattleInvite.setId(playerId);
		}
		if (dailyInvites == null) {
			dailyInvites = new ConcurrentHashMap<Long, Long>();
		}
		if (hoursInvites == null) {
			hoursInvites = new ConcurrentHashMap<Long, Long>();
		}
		heartbeat();
	}

	public void heartbeat() {
		List<Long> removeIds = new ArrayList<Long>();
		if (hoursInvites != null && hoursInvites.size() > 0) {
			for (Entry<Long, Long> entry : hoursInvites.entrySet()) {
				if (System.currentTimeMillis() - entry.getValue() > DateUtils.MILLIS_PER_HOUR)
					removeIds.add(entry.getKey());
			}
			if (removeIds.size() > 0) {
				for (Long removeId : removeIds) {
					hoursInvites.remove(removeId);
				}
			}
		}
		//
		removeIds.clear();
		if (dailyInvites != null && dailyInvites.size() > 0) {
			for (Entry<Long, Long> entry : dailyInvites.entrySet()) {
				int inviteDay = (int) (entry.getValue() / DateUtils.MILLIS_PER_DAY);
				int currentDay = (int) (System.currentTimeMillis() / DateUtils.MILLIS_PER_DAY);
				if (currentDay != inviteDay)
					removeIds.add(entry.getKey());
			}
			if (removeIds.size() > 0) {
				for (Long removeId : removeIds) {
					dailyInvites.remove(removeId);
				}
			}
		}

	}

	public void persist() {
		if (persistBattleInvite == null) {
			return;
		}

		persistBattleInvite.setDailyInvites(PersistBattleInvite.invitesData(dailyInvites));
		persistBattleInvite.setHourInvites(PersistBattleInvite.invitesData(hoursInvites));
		if (needSave) {
			persistBattleInvite.save();
			needSave = false;
		} else if (update) {
			persistBattleInvite.update();
			update = false;
		}
	}

	public void destroy() {
		persistBattleInvite = null;
		playerId = 0;
		if (dailyInvites != null) {
			dailyInvites.clear();
		}
		if (hoursInvites != null) {
			hoursInvites.clear();
		}
	}

	public void addDailyInvite(long inviteId) {
		init();
		dailyInvites.put(inviteId, System.currentTimeMillis());
		update = true;
	}

	public void addHourInvite(long inviteId) {
		init();
		hoursInvites.put(inviteId, System.currentTimeMillis());
		update = true;
	}

	public List<InviteMember> getInviteList() {
		init();
		PokerPlayerManager playerManager = SpringUtils.getBeanOfType(PokerPlayerManager.class);
		PokerPlayer player = playerManager.getAnyPlayerById(playerId);
		List<Friend> friends = new ArrayList<Friend>(player.friendHolder.getFriends());
		Collections.shuffle(friends);
		List<InviteMember> inviteMembers = new ArrayList<InviteMember>(5);
		int friendPoint = 20;

		for (Friend friend : friends) {
			if (hoursInvites.containsKey(friend.mateId))
				continue;
			PokerPlayer matePlayer = playerManager.getAnyPlayerById(friend.mateId);
			Pet pet = getMainPet(matePlayer);
			if (pet == null)
				continue;
			InviteMember inviteMember = new InviteMember();
			inviteMember.friendPoint = dailyInvites.containsKey(friend.mateId) ? 0 : friendPoint;
			inviteMember.grade = matePlayer.grade;
			inviteMember.inviteId = friend.mateId;
			inviteMember.name = matePlayer.nickname;
			inviteMember.pet = pet;
			inviteMembers.add(inviteMember);
			if (inviteMembers.size() >= 3)
				break;
		}
		//
		int strangerPoint = 10;
		List<PersistPokerPlayer> strangerPlayers = PersistPokerPlayer
				.findByGrade(player.grade - 5, player.grade + 5, 5);
		for (PersistPokerPlayer pokerPlayer : strangerPlayers) {
			PokerPlayer strangerPlayer = playerManager.getAnyPlayerById(pokerPlayer.getId());
			Pet pet = getMainPet(strangerPlayer);
			if (pet == null)
				continue;
			InviteMember inviteMember = new InviteMember();
			inviteMember.friendPoint = dailyInvites.containsKey(pokerPlayer.getId()) ? 0 : strangerPoint;
			inviteMember.grade = strangerPlayer.grade;
			inviteMember.inviteId = pokerPlayer.getId();
			inviteMember.name = strangerPlayer.nickname;
			inviteMember.pet = pet;
			inviteMembers.add(inviteMember);
			if (inviteMembers.size() >= 5)
				break;
		}
		currentInviteList = inviteMembers;
		return inviteMembers;
	}

	private Pet getMainPet(PokerPlayer player) {
		PetTeam team = player.petHolder.getTeam();
		if (team == null)
			return null;
		if (team.pet == null || team.pet.length == 0)
			return null;
		return player.petHolder.getPet(team.pet[0]);

	}

	public void invite(long inviteId) {
		currentInviteMember = null;
		if (currentInviteList == null || currentInviteList.size() == 0)
			throw new GeneralException(PokerErrorCodes.NO_INVITER);
		for (InviteMember inviter : currentInviteList) {
			if (inviter.inviteId == inviteId) {
				currentInviteMember = inviter;
				currentInviteList = null;
				break;
			}
		}
	}

}
