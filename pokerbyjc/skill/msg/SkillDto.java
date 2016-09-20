package com.chitu.poker.skill.msg;

import cn.gecko.broadcast.BroadcastMessage;

public class SkillDto implements BroadcastMessage{

    private int skillId;
    
    private int grade;
    
    private boolean maxGrade;
	
	private int name;
	
	private int icon;
	
	private int desc;
	
	private int cdTime;

	/**技能ID**/
	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	/**技能等级**/
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**技能名称**/
	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	/**图标**/
	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	/**说明**/
	public int getDesc() {
		return desc;
	}

	public void setDesc(int desc) {
		this.desc = desc;
	}

	/**CD时间**/
	public int getCdTime() {
		return cdTime;
	}

	public void setCdTime(int cdTime) {
		this.cdTime = cdTime;
	}

	/**是否最高级**/
	public boolean isMaxGrade() {
		return maxGrade;
	}

	public void setMaxGrade(boolean maxGrade) {
		this.maxGrade = maxGrade;
	}
	
	
}
