package com.chitu.poker.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import cn.gecko.commons.data.DataChangeListener;
import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.player.event.PlayerEvent;
import cn.gecko.player.event.PlayerEventListener;
import cn.gecko.player.event.PlayerLogoutEvent;

import com.chitu.poker.data.StaticBattle;
import com.chitu.poker.data.StaticInstance;
import com.chitu.poker.model.PokerPlayer;

@Service
public class BattleManager implements DataChangeListener {

	public static PlayerEventListener logoutListener = new PlayerEventListener() {
		@Override
		public void onEvent(PlayerEvent event) {
			if (!(event instanceof PlayerLogoutEvent))
				return;
			if (!(event.getDispatcher() instanceof PokerPlayer))
				return;
			PokerPlayer player = (PokerPlayer) event.getDispatcher();
			if (player.battleHolder.currentBattleId > 0) {
				SpringUtils.getBeanOfType(BattleManager.class).destroyBattle(player.battleHolder.currentBattleId);
				player.battleHolder.currentBattleId = 0;
			}
		}
	};

	private Map<Integer, Battle> battleCache = new ConcurrentHashMap<Integer, Battle>();

	private Map<Integer, Area> areaCache = new ConcurrentHashMap<Integer, Area>();
	
	private List<Area> sortAreaCache=new CopyOnWriteArrayList<Area>();

	private Map<Integer, List<StaticInstance>> parentInstanceCache = new ConcurrentHashMap<Integer, List<StaticInstance>>();

	private Map<Integer, List<StaticBattle>> instanceBattleCache = new ConcurrentHashMap<Integer, List<StaticBattle>>();

	public void addBattle(Battle battle) {
		destroyBattle(battle.id);
		battleCache.put(battle.id, battle);
	}

	public void destroyBattle(int battleId) {
		Battle battle = battleCache.remove(battleId);
		if (battle != null)
			battle.destroy();
	}

	public Battle getBattle(int battleId) {
		return battleCache.get(battleId);
	}

	public List<Area> getAllAreas() {
		return sortAreaCache;
	}

	public Area getArea(int areaId) {
		return areaCache.get(areaId);
	}

	public List<StaticInstance> getSonInstances(int parentInstanceId) {
		return parentInstanceCache.get(parentInstanceId);
	}

	public List<StaticBattle> getInstanceBattles(int instanceId) {
		return instanceBattleCache.get(instanceId);
	}

	@Override
	public Class<?>[] interestClass() {
		return new Class<?>[] { StaticInstance.class, StaticBattle.class };
	}

	@Override
	public void change(Class<?> clazz, Map<Integer, Object> dataMap) {
		if (clazz.equals(StaticInstance.class)) {
			Map<Integer, Area> areaMaps = new HashMap<Integer, Area>();
			Map<Integer, List<StaticInstance>> parentInstanceMaps = new HashMap<Integer, List<StaticInstance>>();
			for (Object value : dataMap.values()) {
				StaticInstance instance = (StaticInstance) value;
				Area area = areaMaps.get(instance.getAreaId());
				if (area == null) {
					area = new Area();
					area.id = instance.getAreaId();
					area.name = instance.getAreaName();
					area.instances = new ArrayList<StaticInstance>();
					areaMaps.put(instance.getAreaId(), area);
				}
				if (instance.getParentId() == 0) {
					area.instances.add(instance);
				} else {
					List<StaticInstance> sons = parentInstanceMaps.get(instance.getParentId());
					if (sons == null) {
						sons = new ArrayList<StaticInstance>();
						parentInstanceMaps.put(instance.getParentId(), sons);
					}
					sons.add(instance);
				}

			}
			for (Area area : areaMaps.values()) {
				if (area.instances != null)
					Collections.sort(area.instances);
			}
			for (List<StaticInstance> instances : parentInstanceMaps.values()) {
				if (instances != null)
					Collections.sort(instances);
			}
			areaCache.clear();
			areaCache.putAll(areaMaps);
			List<Area> areas=new ArrayList<Area>(areaMaps.values());
			Collections.sort(areas);
			sortAreaCache.clear();
			sortAreaCache.addAll(areas);
			parentInstanceCache.clear();
			parentInstanceCache.putAll(parentInstanceMaps);
		}
		//
		if (clazz.equals(StaticBattle.class)) {
			Map<Integer, List<StaticBattle>> battleMap = new HashMap<Integer, List<StaticBattle>>();
			for (Object value : dataMap.values()) {
				StaticBattle battle = (StaticBattle) value;
				List<StaticBattle> battles = battleMap.get(battle.getInstanceId());
				if (battles == null) {
					battles = new ArrayList<StaticBattle>();
					battleMap.put(battle.getInstanceId(), battles);
				}
				battles.add(battle);
			}
			instanceBattleCache.clear();
			instanceBattleCache.putAll(battleMap);
		}
	}

}
