package com.chitu.poker.msg;

import java.util.Collection;

import cn.gecko.broadcast.GeneralResponse;

import com.chitu.poker.data.StaticArena;
import com.chitu.poker.data.StaticArenaMine;
import com.chitu.poker.data.StaticMonster;
import com.chitu.poker.data.StaticNewbieStep;
import com.chitu.poker.data.StaticPet;
import com.chitu.poker.data.StaticPlayerGrade;
import com.chitu.poker.data.StaticRankReward;
import com.chitu.poker.data.StaticRepeatReward;
import com.chitu.poker.data.StaticTensei;

/**
 * 静态数据
 * 
 * @author open
 * 
 */
public class DataCacheDto extends GeneralResponse {

	private Collection<StaticPet> pets;

	private Collection<StaticTensei> tenseis;

	private Collection<StaticMonster> monsters;

	private Collection<StaticPlayerGrade> grades;

	private Collection<StaticNewbieStep> newbies;
	
	private Collection<StaticArena> arenas;
	
	private Collection<StaticArenaMine> arenaMines;
	
	private Collection<StaticRankReward> rankRewards;
	
	private Collection<StaticRepeatReward> repeatRewards;
	
	

	/**
	 * 所有宠物数据,元素StaticPet
	 * 
	 * @return
	 */
	public Collection<StaticPet> getPets() {
		return pets;
	}

	public void setPets(Collection<StaticPet> pets) {
		this.pets = pets;
	}

	/** 进阶数据表,元素StaticTensei **/
	public Collection<StaticTensei> getTenseis() {
		return tenseis;
	}

	public void setTenseis(Collection<StaticTensei> tenseis) {
		this.tenseis = tenseis;
	}

	/** 怪物数据表,元素StaticMonster **/
	public Collection<StaticMonster> getMonsters() {
		return monsters;
	}

	public void setMonsters(Collection<StaticMonster> monsters) {
		this.monsters = monsters;
	}

	/** 用户等级数据表,元素StaticPlayerGrade **/
	public Collection<StaticPlayerGrade> getGrades() {
		return grades;
	}

	public void setGrades(Collection<StaticPlayerGrade> grades) {
		this.grades = grades;
	}

	/** 用户等级数据表,元素StaticNewbieStep **/
	public Collection<StaticNewbieStep> getNewbies() {
		return newbies;
	}

	public void setNewbies(Collection<StaticNewbieStep> newbies) {
		this.newbies = newbies;
	}

	/**战区数据表,元素StaticArena**/
	public Collection<StaticArena> getArenas() {
		return arenas;
	}

	public void setArenas(Collection<StaticArena> arenas) {
		this.arenas = arenas;
	}

	/**战区矿数据表,元素StaticArenaMine**/
	public Collection<StaticArenaMine> getArenaMines() {
		return arenaMines;
	}

	public void setArenaMines(Collection<StaticArenaMine> arenaMines) {
		this.arenaMines = arenaMines;
	}

	/**世界排名奖励数据表,元素StaticRankReward**/
	public Collection<StaticRankReward> getRankRewards() {
		return rankRewards;
	}

	public void setRankRewards(Collection<StaticRankReward> rankRewards) {
		this.rankRewards = rankRewards;
	}

	/**PVP连胜奖励数据表,元素StaticRepeatReward**/
	public Collection<StaticRepeatReward> getRepeatRewards() {
		return repeatRewards;
	}

	public void setRepeatRewards(Collection<StaticRepeatReward> repeatRewards) {
		this.repeatRewards = repeatRewards;
	}

	
}
