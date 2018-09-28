package com.metasoft.empire.model;

import com.metasoft.empire.model.data.RoleData;
import com.metasoft.empire.service.StaticDataService;
import com.metasoft.empire.service.common.SpringService;

public class UserUpgrade {
	private int roleid;
	private int number;
	private int upgrade;
	private int level;
	public UserUpgrade(int roleid, int number, int upgrade, int level) {
		super();
		this.roleid = roleid;
		this.number = number;
		this.upgrade = upgrade;
		this.level = level;
		updateLevel();
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
		updateLevel();
	}
	public int getUpgrade() {
		return upgrade;
	}
	public void setUpgrade(int upgrade) {
		this.upgrade = upgrade;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getLevel() {
		return level;
	}
	public boolean upgrade(){
		for(int lv=1; lv<11; lv++){
			int u = 1<<lv;
			if(upgrade<u&&u<=number){
				number-=u;
				upgrade+=u;
				level=lv+1;
				return true;
			}
		}
		return false;
	}
	public void updateLevel(){
		int u = upgrade;
		for(int lv=1; lv<11; lv++){
			u -= 1<<lv;
			if(u<0){
				level=lv;
				return;
			}
		}
	}
	
	public int getHp(){
		StaticDataService sds = SpringService.getBean(StaticDataService.class);
		RoleData rd = sds.roleMap.get(roleid);
		if(null==rd)
			return 0;
		return rd.getBasehp()+(level-1)*rd.getFactorhp();
	}
	public int getAttack(){
		StaticDataService sds = SpringService.getBean(StaticDataService.class);
		RoleData rd = sds.roleMap.get(roleid);
		if(null==rd)
			return 0;
		return rd.getBaseatk() + (level-1)*rd.getFactoratk();
	}
}
