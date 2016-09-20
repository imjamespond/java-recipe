package com.chitu.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.gecko.broadcast.MultiGeneralController;
import cn.gecko.commons.data.StaticDataManager;
import cn.gecko.player.msg.ListDto;

import com.chitu.chess.data.StaticItem;
import com.chitu.chess.data.StaticMission;
import com.chitu.chess.data.StaticVersion;

@Controller
public class DataController extends MultiGeneralController {
 
	@Autowired
	private StaticDataManager staticDataManager;


	public ListDto mallItems(){
		return new ListDto(staticDataManager.getMap(StaticItem.class).values());
	}
	
	public ListDto missionList(){
		return new ListDto(staticDataManager.getMap(StaticMission.class).values());
	}
	
	public ListDto versionList(){
		return new ListDto(staticDataManager.getMap(StaticVersion.class).values());
	}
}
