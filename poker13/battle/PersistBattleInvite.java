package com.chitu.poker.battle;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.persist.PersistObject;
import cn.gecko.persist.annotation.PersistEntity;

@Entity
@Table(name = "poker_battle_invite")
@PersistEntity(cache = false)
public class PersistBattleInvite extends PersistObject{

	public static final byte INVITE_VERSION_1 = 1;

	public static final byte INVITE_VERSION = INVITE_VERSION_1;
	
	public static PersistBattleInvite get(long id) {
		return PersistObject.get(PersistBattleInvite.class, id);
	}

	private long id;

	private byte[] dailyInvites;

	private byte[] hourInvites;
	
	public Long id(){
		return id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getDailyInvites() {
		return dailyInvites;
	}

	public void setDailyInvites(byte[] dailyInvites) {
		this.dailyInvites = dailyInvites;
	}

	public byte[] getHourInvites() {
		return hourInvites;
	}

	public void setHourInvites(byte[] hourInvites) {
		this.hourInvites = hourInvites;
	}

	/**
	 * 
	 * @param invites
	 * 
	 * @return Map<Long, Long>,key:用户ID,value:邀请时间
	 */
	public static Map<Long, Long> initInvites(byte[] invites) {
		Map<Long, Long> dailyMap = new ConcurrentHashMap<Long, Long>();
		if (invites == null || invites.length == 0) {
			return dailyMap;
		}

		ByteBuffer buffer = ByteBuffer.wrap(invites);
		byte version = buffer.get();
		if (version == INVITE_VERSION_1) {
			int size = buffer.getInt();
			for (int i = 0; i < size; i++) {
				long inviteId = buffer.getLong();
				long ms = buffer.getLong();
				dailyMap.put(inviteId, ms);
			}
		}
		return dailyMap;
	}

	public static byte[] invitesData(Map<Long, Long> inviteMap) {
		int length = 1 + 4 + inviteMap.size() * 16;
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(INVITE_VERSION);
		buffer.putInt(inviteMap.size());
		for (Entry<Long, Long> entry : inviteMap.entrySet()) {
			buffer.putLong(entry.getKey());
			buffer.putLong(entry.getValue());
		}

		buffer.flip();
		byte[] data = new byte[buffer.limit()];
		buffer.get(data);
		return data;
	}

}
