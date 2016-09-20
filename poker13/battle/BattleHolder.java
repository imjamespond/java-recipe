package com.chitu.poker.battle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BattleHolder {

	private long playerId;

	private PersistBattle persistBattle;

	private Map<Integer, InstanceRecord> passInstances;

	private SaveRecord saveRecord;

	private boolean update;

	private boolean needSave;

	public int currentBattleId;

	public BattleHolder(long playerId) {
		this.playerId = playerId;
	}

	private void init() {
		if (persistBattle != null) {
			return;
		}

		persistBattle = PersistBattle.get(playerId);
		if (persistBattle != null) {
			passInstances = PersistBattle.initPassInstance(persistBattle.getPassInstances());
			saveRecord = PersistBattle.initSaveRecord(persistBattle.getSaveRecord());
		} else {
			needSave = true;
			passInstances = new ConcurrentHashMap<Integer, InstanceRecord>();
			persistBattle = new PersistBattle();
			persistBattle.setId(playerId);
		}
	}

	public void persist() {
		if (persistBattle == null) {
			return;
		}

		persistBattle.setPassInstances(PersistBattle.passInstanceData(passInstances));
		persistBattle.setSaveRecord(PersistBattle.saveRecordData(saveRecord));
		if (needSave) {
			persistBattle.save();
			needSave = false;
		} else if (update) {
			persistBattle.update();
			update = false;
		}
	}

	public void destroy() {
		persistBattle = null;
		playerId = 0;
		if (passInstances != null) {
			passInstances.clear();
		}
	}

	public boolean isPass(int instanceId) {
		init();
		return passInstances.containsKey(instanceId);
	}

	public void passInstance(int instanceId) {
		init();
		InstanceRecord record = new InstanceRecord(instanceId, System.currentTimeMillis());
		passInstances.put(instanceId, record);
		update = true;
	}

	public InstanceRecord getInstanceRecord(int instanceId) {
		init();
		return passInstances.get(instanceId);
	}

	public void setSaveRecord(SaveRecord saveRecord) {
		init();
		this.saveRecord = saveRecord;
		update = true;
	}

}
