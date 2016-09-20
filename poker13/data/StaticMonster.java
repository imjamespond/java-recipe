package com.chitu.poker.data;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.commons.data.StaticDataManager;

/**
 * 怪物数据表
 * @author open
 *
 */
public class StaticMonster implements BroadcastMessage{

	private int id;
	
	private String name;
	
	private String desc;
	
	private int icon;
	
	private int pet0;
	
	private int pet1;
	
	private int pet2;
	
	private int pet3;
	
	private int pet4;
	
	public static StaticMonster get(int id) {
		return StaticDataManager.getInstance().get(StaticMonster.class, id);
	}

	/**怪物ID**/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**怪物名称**/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**说明**/
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**图标**/
	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	/**主将**/
	public int getPet0() {
		return pet0;
	}

	public void setPet0(int pet0) {
		this.pet0 = pet0;
	}

	/**副将**/
	public int getPet1() {
		return pet1;
	}

	public void setPet1(int pet1) {
		this.pet1 = pet1;
	}

	/**副将**/
	public int getPet2() {
		return pet2;
	}

	public void setPet2(int pet2) {
		this.pet2 = pet2;
	}

	/**副将**/
	public int getPet3() {
		return pet3;
	}

	public void setPet3(int pet3) {
		this.pet3 = pet3;
	}

	/**副将**/
	public int getPet4() {
		return pet4;
	}

	public void setPet4(int pet4) {
		this.pet4 = pet4;
	}

	
	
}
