package com.chitu.poker.data;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;
import cn.gecko.commons.data.StaticDataManager;

import com.chitu.poker.data.StaticPet.LandType;

/**
 * 副本数据
 * 
 * @author ivan
 * 
 */
@IncludeEnums({ @IncludeEnum(LandType.class) })
public class StaticInstance implements BroadcastMessage, Comparable<StaticInstance> {

	public enum InstanceType {
		/**
		 * 主线副本
		 */
		Main,
		/**
		 * 限时副本
		 */
		TimeLimit,
		/**
		 * 活动副本
		 */
		Activity
	}

	private int id;

	private String name;

	private int parentId;

	private int type = InstanceType.Main.ordinal();

	private int bgId;

	private int landType;

	private int areaId;

	private String areaName;

	private int rsumeHpCount;
	
	private int icon;

	public static StaticInstance get(int id) {
		return StaticDataManager.getInstance().get(StaticInstance.class, id);
	}

	/**
	 * ID
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
	 * 副本名字
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
	 * 所属父副本ID，0说明本身为父副本
	 * 
	 * @return
	 */
	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * 类型，参考InstanceType系列常量
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 副本场景背景ID
	 * 
	 * @return
	 */
	public int getBgId() {
		return bgId;
	}

	public void setBgId(int bgId) {
		this.bgId = bgId;
	}

	/**
	 * 地形类型，参考LandType系列常量
	 * 
	 * @return
	 */
	public int getLandType() {
		return landType;
	}

	public void setLandType(int landType) {
		this.landType = landType;
	}

	/**
	 * 区域ID
	 * 
	 * @return
	 */
	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	/**
	 * 区域名字
	 * 
	 * @return
	 */
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * 副本可恢复HP的次数
	 * 
	 * @return
	 */
	public int getRsumeHpCount() {
		return rsumeHpCount;
	}

	public void setRsumeHpCount(int rsumeHpCount) {
		this.rsumeHpCount = rsumeHpCount;
	}

	@Override
	public int compareTo(StaticInstance si) {
		return id - si.id;
	}

	/**
	 * 图标
	 * @return
	 */
	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
	
	

}
