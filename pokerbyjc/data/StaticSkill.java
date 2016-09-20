package com.chitu.poker.data;

import java.util.Map;

import cn.gecko.commons.data.StaticDataManager;

import com.chitu.poker.skill.msg.SkillDto;

/**
 * 技能数据表
 * @author open
 *
 */
public class StaticSkill implements Comparable<StaticSkill>{

	private int id;
	
	private int name;
	
	private int icon;
	
	private int desc;
	
	private int skillId;
	
	private int grade;
	
	private boolean maxGrade;
	
	private int cdTime;
	
	private int updateRate;
	
	private int logicId;
	
	private int logicParam1;
	
	private int logicParam2;
	
	@Override
	public int compareTo(StaticSkill o) {
		if (this.skillId == o.skillId) {
			return this.grade - o.grade;
		} else {
			return this.skillId - o.skillId;
		}
	}
	
	public static StaticSkill get(int id) {
		return StaticDataManager.getInstance().get(StaticSkill.class, id);
	}
	
	public static StaticSkill get(int skillId,int grade){
		Map<Integer,StaticSkill> map = StaticDataManager.getInstance().getMap(StaticSkill.class);
		for(StaticSkill skill : map.values()){
			if(skill.getSkillId() == skillId && skill.getGrade() == grade){
				return skill;
			}
		}
		return null;
	}
	
	public static StaticSkill getMaxGrade(int skillId){
		Map<Integer,StaticSkill> map = StaticDataManager.getInstance().getMap(StaticSkill.class);
		for(StaticSkill skill : map.values()){
			if(skill.getSkillId() == skillId && skill.isMaxGrade()){
				return skill;
			}
		}
		return null;
	}
	
	public SkillDto toDto(){
		SkillDto dto = new SkillDto();
		dto.setSkillId(this.skillId);
		dto.setGrade(this.grade);
		dto.setName(this.name);
		dto.setIcon(this.icon);
		dto.setDesc(this.desc);
		dto.setCdTime(this.cdTime);
		dto.setMaxGrade(this.maxGrade);
		
		return dto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getDesc() {
		return desc;
	}

	public void setDesc(int desc) {
		this.desc = desc;
	}

	public int getCdTime() {
		return cdTime;
	}

	public void setCdTime(int cdTime) {
		this.cdTime = cdTime;
	}

	public int getLogicId() {
		return logicId;
	}

	public void setLogicId(int logicId) {
		this.logicId = logicId;
	}

	public int getLogicParam1() {
		return logicParam1;
	}

	public void setLogicParam1(int logicParam1) {
		this.logicParam1 = logicParam1;
	}

	public int getLogicParam2() {
		return logicParam2;
	}

	public void setLogicParam2(int logicParam2) {
		this.logicParam2 = logicParam2;
	}

	public int getUpdateRate() {
		return updateRate;
	}

	public void setUpdateRate(int updateRate) {
		this.updateRate = updateRate;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public boolean isMaxGrade() {
		return maxGrade;
	}

	public void setMaxGrade(boolean maxGrade) {
		this.maxGrade = maxGrade;
	}

	
	
	
}
