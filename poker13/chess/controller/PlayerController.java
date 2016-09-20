package com.chitu.chess.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.gecko.commons.data.BillType;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.ValidatorUtils;
import cn.gecko.player.controller.BasePlayerController;

import com.chitu.chess.DevScene;
import com.chitu.chess.data.StaticDistrictTypes;
import com.chitu.chess.model.ChessBillTypes;
import com.chitu.chess.model.ChessErrorCodes;
import com.chitu.chess.model.ChessPlayer;
import com.chitu.chess.model.Mission;
import com.chitu.chess.model.PersistChessPlayer;
import com.chitu.chess.msg.ChessDistrictDto;
import com.chitu.chess.msg.ChessDistrictsInfoDto;
import com.chitu.chess.msg.ChessPlayerMissionDto;
import com.chitu.chess.msg.ChessPlayerMissionPrizeDto;
import com.chitu.chess.msg.ChessPlayerRankListDto;
import com.chitu.chess.msg.RoomUserResultReplayDto;
import com.chitu.chess.service.ChessPlayerManager;
import com.chitu.chess.service.ChessPlayerRankManager;
import com.chitu.chess.service.ChessRoomManager;



/**
 * 
 * @author ivan
 * 
 */
@Controller
public class PlayerController extends BasePlayerController {

	@Autowired
	private ChessPlayerManager chessPlayerManager;

	@Autowired
	private ChessRoomManager chessRoomManager;
	
	@Autowired
	private ChessPlayerRankManager chessPlayerRankManager;
	
	/**
	 * 检查是否存在角色
	 * 
	 * @param token
	 * @return
	 */
	public void existNickname(String nickname) {
		ValidatorUtils.checkLength(nickname, 4, 14);
		ValidatorUtils.checkRegKeyword(nickname);
		PersistChessPlayer player = PersistChessPlayer.getByNickname(nickname);
		if (player != null)
			throw new GeneralException(ChessErrorCodes.NICKNAME_EXIST);
		else
			throw new GeneralException(ChessErrorCodes.ROLE_NOT_EXIST);
	}
	
	/**
	 * 频道聊天 本接口会有长度检查，频率检查，关键字检查
	 * 
	 * @param channelType  ChannelTalk.ChannelType_系列常量
	 * @param message 聊天内容
	 */
	public void talkToChannel(int channelType, String message) {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		DevScene.chatCmd(player, message);
		super.talkToChannel(channelType, message);
		
		
	}
	
	/**
	 * 获取区信息
	 * 
	 */
	public ChessDistrictsInfoDto getDistrictsInfo() {
		
		// 给自己的消息
		List<ChessDistrictDto> cdList = new ArrayList<ChessDistrictDto>();
		
		for (Entry<Integer, StaticDistrictTypes> entry : StaticDistrictTypes.getAll().entrySet()) {
			int mid = entry.getKey();
			StaticDistrictTypes sdt = entry.getValue();
			
			ChessDistrictDto cd = new ChessDistrictDto();
			cd.minimunMoney = sdt.getMoneyLimitation();
			cd.maximunMoney = sdt.getMoneyMaxLimitation();
			cd.type = mid;
			cd.playerNum = chessRoomManager.getDistPlayerNum(mid);
			cd.rate = sdt.getRateOfExchange();
			cd.name = sdt.getName();
			cdList.add(cd);
			//ChessUtils.chessLog.info("ID:"+mid+"_"+sdt.getName());
		}
		
		return new ChessDistrictsInfoDto(cdList);
	}	
	
	/**
	 * 获取玩家任务
	 * 
	 */
	public ChessPlayerMissionDto getMission() {
		
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		return new ChessPlayerMissionDto(player.missionHolder);
	}
	
	/**
	 * int type 1局数 2胜利局数
	 * 获取玩家任务奖励
	 * 
	 */
	public ChessPlayerMissionPrizeDto getMissionPrize(int type) {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		ChessPlayerMissionPrizeDto mp;
		mp = player.missionHolder.setNext(type);
		if(mp != null){
			player.wealthHolder.increaseMoney(mp.getMoney(), 0, 0, BillType
							.get(ChessBillTypes.MISSION), "");
			mp.setPlayerMoney(player.wealthHolder.getMoney());
			
			//有任务完成
			if(player.missionHolder.mission1.state == Mission.State.DONE ||
					player.missionHolder.mission2.state == Mission.State.DONE){
				mp.setMissionDone(true);
			}
		}
		return mp;
	}
	
	/**
	 * 获取玩家排行
	 * 
	 */
	public ChessPlayerRankListDto getRank() {
		return chessPlayerRankManager.rankList;
	}
	
	/**
	 * 获取玩家上局录像
	 * 
	 */
	public RoomUserResultReplayDto getReplay() {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		//
		//ChessPlayer playerTmp = chessPlayerManager.getAnyPlayerById(playerId);
		RoomUserResultReplayDto rp = player.getReplay();
		if(null == rp){
			throw new GeneralException(ChessErrorCodes.NO_DATA);
		}
		return rp;
	}
	

}
