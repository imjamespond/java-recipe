package com.chitu.poker.battle.msg;

import java.util.ArrayList;
import java.util.List;

import com.chitu.poker.battle.Area;
import com.chitu.poker.battle.BattleManager;
import com.chitu.poker.battle.PveBattle;
import com.chitu.poker.data.StaticInstance;
import com.chitu.poker.model.PokerPlayer;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.commons.utils.SpringUtils;

/**
 * 区域DTO
 * 
 * @author ivan
 * 
 */
public class AreaDto implements BroadcastMessage {

	private int id;

	private String name;

	private boolean pass;

	private List<InstanceDto> parentInstances;

	public AreaDto(PokerPlayer player, Area area) {
		this.id = area.id;
		this.name = area.name;
		parentInstances = new ArrayList<InstanceDto>(area.instances.size());
		pass = true;
		for (StaticInstance instance : area.instances) {
			if(instance.getId()==PveBattle.NEWBIE_INSTANCE_ID&&player.newbieStep>=PveBattle.NEWBIE_BATTLE_STEP)
				continue;
			List<StaticInstance> sons = SpringUtils.getBeanOfType(BattleManager.class)
					.getSonInstances(instance.getId());
			InstanceDto dto = new InstanceDto(player, instance, sons);
			if (!dto.isPass())
				pass = false;
			parentInstances.add(dto);
		}
	}

	/**
	 * 区域ID
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 区域名字
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 是否全部副本通关
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
	 * 区域内副本列表，InstanceDto数组，在区域列表的时候InstanceDto.battles为null
	 * 
	 * @return
	 */
	public List<InstanceDto> getParentInstances() {
		return parentInstances;
	}

	public void setParentInstances(List<InstanceDto> parentInstances) {
		this.parentInstances = parentInstances;
	}

}
