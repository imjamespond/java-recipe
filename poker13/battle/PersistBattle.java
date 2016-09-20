package com.chitu.poker.battle;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.persist.ByteBufferUtils;
import cn.gecko.persist.PersistObject;
import cn.gecko.persist.annotation.PersistEntity;

@Entity
@Table(name = "poker_battle")
@PersistEntity(cache = false)
public class PersistBattle extends PersistObject {

	public static final byte PASS_INSTANCE_VERSION_1 = 1;

	public static final byte PASS_INSTANCE_VERSION = PASS_INSTANCE_VERSION_1;

	public static final byte SAVE_RECORD_VERSION_1 = 1;

	public static final byte SAVE_RECORD_VERSION = SAVE_RECORD_VERSION_1;

	public static PersistBattle get(long id) {
		return PersistObject.get(PersistBattle.class, id);
	}

	private long id;

	private byte[] passInstances;

	private byte[] saveRecord;

	@Override
	public Long id() {
		return id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getPassInstances() {
		return passInstances;
	}

	public void setPassInstances(byte[] passInstances) {
		this.passInstances = passInstances;
	}

	public byte[] getSaveRecord() {
		return saveRecord;
	}

	public void setSaveRecord(byte[] saveRecord) {
		this.saveRecord = saveRecord;
	}

	/**
	 * 
	 * @param passInstances
	 *            
	 * @return Map<Integer, InstanceRecord>,key:副本ID,value:通关副本记录
	 */
	public static Map<Integer, InstanceRecord> initPassInstance(byte[] passInstances) {
		Map<Integer, InstanceRecord> gameMap = new ConcurrentHashMap<Integer, InstanceRecord>();
		if (passInstances == null || passInstances.length == 0) {
			return gameMap;
		}

		ByteBuffer buffer = ByteBuffer.wrap(passInstances);
		byte version = buffer.get();
		if (version == PASS_INSTANCE_VERSION_1) {
			int size = buffer.getInt();
			for (int i = 0; i < size; i++) {
				int instanceId = buffer.getInt();
				long ms = buffer.getLong();
				gameMap.put(instanceId, new InstanceRecord(instanceId, ms));
			}
		}
		return gameMap;
	}

	public static byte[] passInstanceData(Map<Integer, InstanceRecord> instanceMap) {
		int length = 1 + 4 + instanceMap.size() * 12;
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(PASS_INSTANCE_VERSION);
		buffer.putInt(instanceMap.size());
		for (Entry<Integer, InstanceRecord> entry : instanceMap.entrySet()) {
			InstanceRecord record = entry.getValue();
			buffer.putInt(record.instanceId);
			buffer.putLong(record.recordTime);
		}

		buffer.flip();
		byte[] data = new byte[buffer.limit()];
		buffer.get(data);
		return data;
	}

	/**
	 * 
	 * @param saveRecord
	 *            保存记录
	 * @return
	 */
	public static SaveRecord initSaveRecord(byte[] saveRecord) {
		if (saveRecord == null || saveRecord.length == 0) {
			return null;
		}

		SaveRecord record = new SaveRecord();
		ByteBuffer buffer = ByteBuffer.wrap(saveRecord);
		byte version = buffer.get();
		if (version == SAVE_RECORD_VERSION_1) {
			record.instanceId = buffer.getInt();
			record.currentHp = buffer.getInt();
			record.maxHp = buffer.getInt();
			record.resumeHpCount = buffer.getInt();
			record.rewardExp = buffer.getInt();
			record.rewardMoney = buffer.getInt();
			record.needRelive = buffer.get();
			record.passBattles = ByteBufferUtils.readIntArray(buffer);
			record.rewardPetIds = ByteBufferUtils.readIntArray(buffer);
		}
		return record;
	}

	public static byte[] saveRecordData(SaveRecord record) {
		if (record == null || record.instanceId == 0)
			return null;
		int length = 1 + 25 + ByteBufferUtils.intArrayLength(record.passBattles)
				+ ByteBufferUtils.intArrayLength(record.rewardPetIds);
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(SAVE_RECORD_VERSION);
		buffer.putInt(record.instanceId);
		buffer.putInt(record.currentHp);
		buffer.putInt(record.maxHp);
		buffer.putInt(record.resumeHpCount);
		buffer.putInt(record.rewardExp);
		buffer.putInt(record.rewardMoney);
		buffer.put((byte) record.needRelive);
		ByteBufferUtils.putIntArray(buffer, record.passBattles);
		ByteBufferUtils.putIntArray(buffer, record.rewardPetIds);
		buffer.flip();
		byte[] data = new byte[buffer.limit()];
		buffer.get(data);
		return data;
	}
}
