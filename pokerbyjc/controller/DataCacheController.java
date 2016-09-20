package com.chitu.poker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.gecko.broadcast.MultiGeneralController;
import cn.gecko.commons.data.StaticDataManager;

import com.chitu.poker.data.StaticArena;
import com.chitu.poker.data.StaticArenaMine;
import com.chitu.poker.data.StaticMonster;
import com.chitu.poker.data.StaticNewbieStep;
import com.chitu.poker.data.StaticPet;
import com.chitu.poker.data.StaticPlayerGrade;
import com.chitu.poker.data.StaticRankReward;
import com.chitu.poker.data.StaticRepeatReward;
import com.chitu.poker.data.StaticTensei;
import com.chitu.poker.msg.DataCacheDto;

@Controller
public class DataCacheController extends MultiGeneralController {

	@Autowired
	private StaticDataManager staticDataManager;

	/**
	 * 所有静态数据
	 * 
	 * @return
	 */
	public DataCacheDto getData() {
		DataCacheDto dto = new DataCacheDto();
		// StaticPet
		dto.setPets(staticDataManager.getMap(StaticPet.class).values());

		// StaticTensei
		dto.setTenseis(staticDataManager.getMap(StaticTensei.class).values());

		// StaticMonster
		dto.setMonsters(staticDataManager.getMap(StaticMonster.class).values());

		// StaticPlayerGrade
		dto.setGrades(staticDataManager.getMap(StaticPlayerGrade.class).values());

		// StaticNewbieStep
		dto.setNewbies(staticDataManager.getMap(StaticNewbieStep.class).values());
		
		dto.setArenas(staticDataManager.getMap(StaticArena.class).values());
		
		dto.setArenaMines(staticDataManager.getMap(StaticArenaMine.class).values());

		dto.setRankRewards(staticDataManager.getMap(StaticRankReward.class).values());
		
		dto.setRepeatRewards(staticDataManager.getMap(StaticRepeatReward.class).values());
		
		return dto;
	}
}
