package com.chitu.poker.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.commons.data.StaticDataManager;

/**
 * 玩家等级数据表
 * 
 * @author open
 * 
 */
public class StaticPlayerGrade implements Comparable<StaticPlayerGrade>, BroadcastMessage {

	public static final int MAX_GRADE = 60;

	private int id;

	private int updateExp;

	private int totalExp;

	private int maxStrength;

	private int maxControl;
	
	private int maxFriend;

	public static StaticPlayerGrade get(int id) {
		return StaticDataManager.getInstance().get(StaticPlayerGrade.class, id);
	}

	public static StaticPlayerGrade getStaticPlayerGrade(int totleExp) {
		Map<Integer, StaticPlayerGrade> map = StaticDataManager.getInstance().getMap(StaticPlayerGrade.class);
		List<StaticPlayerGrade> list = new ArrayList<StaticPlayerGrade>();
		list.addAll(map.values());
		Collections.sort(list);
		for (StaticPlayerGrade data : list) {
			if (data.getTotalExp() > totleExp) {
				return data;
			}
		}
		return null;
	}

	@Override
	public int compareTo(StaticPlayerGrade o) {
		return this.id - o.id;
	}

	/**
	 * 等级
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
	 * 升级需要经验
	 * 
	 * @return
	 */
	public int getUpdateExp() {
		return updateExp;
	}

	public void setUpdateExp(int updateExp) {
		this.updateExp = updateExp;
	}

	/**
	 * 总经验
	 * 
	 * @return
	 */
	public int getTotalExp() {
		return totalExp;
	}

	public void setTotalExp(int totalExp) {
		this.totalExp = totalExp;
	}

	/**
	 * 体力上限
	 * 
	 * @return
	 */
	public int getMaxStrength() {
		return maxStrength;
	}

	public void setMaxStrength(int maxStrength) {
		this.maxStrength = maxStrength;
	}

	/**
	 * 统御上限
	 * 
	 * @return
	 */
	public int getMaxControl() {
		return maxControl;
	}

	public void setMaxControl(int maxControl) {
		this.maxControl = maxControl;
	}

	/**好友数上限**/
	public int getMaxFriend() {
		return maxFriend;
	}

	public void setMaxFriend(int maxFriend) {
		this.maxFriend = maxFriend;
	}

	
}
