package com.chitu.poker.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.gecko.broadcast.MultiGeneralController;
import cn.gecko.commons.data.BillType;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.ValidatorUtils;
import cn.gecko.player.msg.ListDto;

import com.chitu.poker.DevScene;
import com.chitu.poker.model.PersistPokerPlayer;
import com.chitu.poker.model.PokerBillTypes;
import com.chitu.poker.model.PokerErrorCodes;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.service.PokerPlayerManager;

@Controller
public class PlayerController extends MultiGeneralController {

	@Autowired
	private PokerPlayerManager playerManager;
	
	/**
	 * GM命令
	 * @param message
	 */
	public void gmCmd(String message){
		PokerPlayer player = playerManager.getRequestPlayer();
		DevScene.chatCmd(player, message);
	}
	
	/**
	 * 游戏设置,元素SettingDto
	 * @return
	 */
	public ListDto settingDto(){
		PokerPlayer player = playerManager.getRequestPlayer();
		return player.settingHolder.toDto();
	}
	
	/**
	 * 设置游戏
	 * @param gameMusic
	 * @param gameSound
	 */
	public void setting(int gameMusic,int gameSound,int shock){
		PokerPlayer player = playerManager.getRequestPlayer();
		player.settingHolder.setting(gameMusic, gameSound,shock);
	}
	
	/**
	 * 修改呢称
	 * @param nickName
	 */
	public void changeNickName(String nickName){
		if(StringUtils.isBlank(nickName)){
			throw new GeneralException(PokerErrorCodes.NICKNAME_IS_NULL);
		}
		ValidatorUtils.checkLength(nickName, 4, 30);
		ValidatorUtils.checkRegKeyword(nickName);
		
		PokerPlayer player = playerManager.getRequestPlayer();
		int needPoint = 50;
		if(!player.wealthHolder.hasEnoughPoint(needPoint)){
			throw new GeneralException(PokerErrorCodes.POINT_NOT_ENOUGH);
		}
		
		PersistPokerPlayer persistPlayer = PersistPokerPlayer.getByNickname(nickName);
		if (persistPlayer != null){
			throw new GeneralException(PokerErrorCodes.NICKNAME_EXIST);
		}
		
		player.wealthHolder.decreasePoint(needPoint, BillType.get(PokerBillTypes.CHANGE_NICK_NAME),nickName);
		player.setNickName(nickName);
	}
	
	
	
}
