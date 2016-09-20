package com.chitu.poker.battle.msg;

import java.util.ArrayList;
import java.util.List;

import cn.gecko.broadcast.GeneralResponse;

import com.chitu.poker.data.StaticInstance;
import com.chitu.poker.model.PokerPlayer;

public class InstanceDto extends GeneralResponse {

	private StaticInstance instanceData;

	private boolean pass;

	private List<InstanceDto> sonInstances;

	private List<BattleDto> battles;

	private int currentHp;

	private int maxHp;

	private int rsumeHpCount;

	public InstanceDto(PokerPlayer player, StaticInstance instance, List<StaticInstance> sons) {
		this.instanceData = instance;
		if (sons != null) {
			sonInstances = new ArrayList<InstanceDto>(sons.size());
			this.pass = true;
			for (StaticInstance sonInstance : sons) {
				sonInstances.add(new InstanceDto(player, sonInstance, null));
				if (!player.battleHolder.isPass(sonInstance.getId()))
					this.pass = false;
			}
		} else
			this.pass = player.battleHolder.isPass(instance.getId());
	}

	/**
	 * 副本数据
	 * 
	 * @return
	 */
	public StaticInstance getInstanceData() {
		return instanceData;
	}

	public void setInstanceData(StaticInstance instanceData) {
		this.instanceData = instanceData;
	}

	/**
	 * 是否已经通过副本
	 * 
	 * @return
	 */
	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	/**
	 * 子副本列表，InstanceDto数组，子副本时本数组为null
	 * 
	 * @return
	 */
	public List<InstanceDto> getSonInstances() {
		return sonInstances;
	}

	public void setSonInstances(List<InstanceDto> sonInstances) {
		this.sonInstances = sonInstances;
	}

	/**
	 * 副本内的关卡列表，BattleDto数组
	 * 
	 * @return
	 */
	public List<BattleDto> getBattles() {
		return battles;
	}

	public void setBattles(List<BattleDto> battles) {
		this.battles = battles;
	}

	/**
	 * 当前HP
	 * 
	 * @return
	 */
	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

	/**
	 * 最大HP
	 * 
	 * @return
	 */
	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	/**
	 * HP恢复次数
	 * 
	 * @return
	 */
	public int getRsumeHpCount() {
		return rsumeHpCount;
	}

	public void setRsumeHpCount(int rsumeHpCount) {
		this.rsumeHpCount = rsumeHpCount;
	}

}
