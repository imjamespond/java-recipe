package com.chitu.chess.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.gecko.broadcast.Channel;
import cn.gecko.broadcast.ChannelManager;
import cn.gecko.broadcast.MultiGeneralController;
import cn.gecko.commons.model.GeneralException;

import com.chitu.chess.data.StaticDistrictTypes;
import com.chitu.chess.data.StaticMatch;
import com.chitu.chess.model.ChessErrorCodes;
import com.chitu.chess.model.ChessPlayer;
import com.chitu.chess.model.ChessRoom;
import com.chitu.chess.model.ChessUtils;
import com.chitu.chess.model.IChessRoom;
import com.chitu.chess.msg.ChessPlayerMatchRankListDto;
import com.chitu.chess.msg.ChessRoomDto;
import com.chitu.chess.msg.RoomUserChatDto;
import com.chitu.chess.msg.RoomUserDto;
import com.chitu.chess.msg.RoomUserMatchDto;
import com.chitu.chess.msg.RoomUserMatchFinishDto;
import com.chitu.chess.msg.RoomUserMatchListDto;
import com.chitu.chess.msg.RoomUserMatchPrizeDto;
import com.chitu.chess.msg.RoomUserReadyReturnDto;
import com.chitu.chess.service.ChessMatchManager;
import com.chitu.chess.service.ChessPlayerManager;
import com.chitu.chess.service.ChessRoomManager;

/**
 * @author Administrator 游戏分区=>游戏房间
 * 
 */

@Controller
public class RoomController extends MultiGeneralController {

	@Autowired
	private ChessPlayerManager chessPlayerManager;

	@Autowired
	private ChessRoomManager chessRoomManager;
	
	@Autowired
	private ChessMatchManager chessMatchManager;

	@Autowired
	private ChannelManager channelManager;

	/**
	 * 玩家进入房间
	 * 
	 * @param type
	 *            玩家进入游戏区
	 * @return
	 */
	public ChessRoomDto enter(int type) {
		// 不存在该区
		if (chessRoomManager.constructDistrictId(type) == 0) {
			throw new GeneralException(ChessErrorCodes.DISTRICT_NOT_EXIST);
		}
		
		StaticDistrictTypes st = StaticDistrictTypes.get(type);
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		
		// 玩家是否已经在比赛或...
		if (chessRoomManager.checkRoomMutex(player.id)) {
			throw new GeneralException(ChessErrorCodes.MATCH_INSIDE);
		}
		
		chessRoomManager.setDistrict(player.id, type);
		// 判断金币数量
		// if(StaticDistrictTypes.get(type).getMoneyLimitation() >
		// player.wealthHolder.getMoney()){
		// throw new GeneralException(ChessErrorCodes.MONEY_NOT_ENOUGH);
		// }

		// 找房间
		ChessRoom chessRoom = chessRoomManager.getAvailableRoom(player, type);
		int playerPos = chessRoom.getPosition(player.id);// 玩家位置

		// 玩家加入频道
		Channel typeChannel = channelManager.getChannel(String.valueOf(type));
		Channel roomChannel = channelManager.getChannel(String.valueOf(chessRoom.id));
		player.joinChannel(typeChannel.getName());
		player.joinChannel(roomChannel.getName());

		// 广播给其他人的消息
		RoomUserDto roomUser = new RoomUserDto();
		roomUser.setPlayerId(String.valueOf(player.id));
		roomUser.setPlayerName(player.nickname);
		roomUser.setAvatar(player.avatar);
		roomUser.setPosition(playerPos);
		roomUser.setState(chessRoom.chessRoomPlayer[playerPos].playerState.state);
		roomUser.setGender(player.gender.ordinal());
		roomUser.setPoint(player.wealthHolder.getPoint());
		roomUser.setMoney(player.wealthHolder.getMoney());
		roomUser.setGameAmount(player.missionHolder.getGameAmount());
		roomUser.setVictoryAmount(player.missionHolder.getVictoryAmount());
		// typeChannel.broadcast(roomUser);
		roomChannel.broadcast(roomUser, player.getSessionId());

		ChessUtils.chessLog.info("chessRoom.id:" + chessRoom.id);
		ChessUtils.chessLog.info("chessRoom.playerNum:" + chessRoom.playerNum);
		ChessUtils.chessLog.info("getPlayerId:" + roomUser.getPlayerId());
		ChessUtils.chessLog.info("getPlayerName:" + roomUser.getPlayerName());
		ChessUtils.chessLog.info("getPosition:" + roomUser.getPosition());
		ChessUtils.chessLog.info("getState:" + roomUser.getState());
		ChessUtils.chessLog.info("\n\n\n");

		// 给自己的消息
		List<RoomUserDto> tmpRoomUsers = new ArrayList<RoomUserDto>();
		for (int i = 0; i < chessRoom.chessRoomPlayer.length; i++) {
			if (chessRoom.chessRoomPlayer[i].playerId == 0) {// ||
																// chessRoom.chessRoomPlayer[i].playerId
																// == player.id)
																// {
				continue;
			}
			ChessPlayer playerTmp = chessPlayerManager.getAnyPlayerById(chessRoom.chessRoomPlayer[i].playerId);
			RoomUserDto tmpRoomUser = new RoomUserDto();
			int pos = chessRoom.getPosition(playerTmp.id);
			tmpRoomUser.setPlayerId(String.valueOf(chessRoom.chessRoomPlayer[i].playerId));
			tmpRoomUser.setPlayerName(playerTmp.nickname);
			tmpRoomUser.setAvatar(playerTmp.avatar);
			tmpRoomUser.setPosition(pos);
			tmpRoomUser.setGender(playerTmp.gender.ordinal());
			tmpRoomUser.setPoint(playerTmp.wealthHolder.getPoint());
			tmpRoomUser.setMoney(playerTmp.wealthHolder.getMoney());
			tmpRoomUser.setState(chessRoom.chessRoomPlayer[pos].playerState.state);
			tmpRoomUser.setGameAmount(playerTmp.missionHolder.getGameAmount());
			tmpRoomUser.setVictoryAmount(playerTmp.missionHolder.getVictoryAmount());
			tmpRoomUsers.add(tmpRoomUser);
		}

		ChessRoomDto chessRoomDto = new ChessRoomDto(tmpRoomUsers);
		chessRoomDto.setDistrictId(type);
		chessRoomDto.setRoomId(String.valueOf(chessRoom.id));
		chessRoomDto.setRoomName(st.getName());

		chessRoomDto.setMoney(chessRoom.getTax());
		chessRoomDto.setState(chessRoom.getRoomState());
		chessRoomDto.setRate(chessRoom.getRateOfExchange());
		chessRoomDto.setTime(chessRoom.getComparisonTime());
		chessRoomDto.setMinMoney(chessRoom.getMoneyLimitation());
		return chessRoomDto;// 返回房间信息
	}

	/**
	 * 玩家离开房间
	 * 
	 */
	public void leave() {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		// 离开
		chessRoomManager.leaveRoom(player);
		// player.wealthHolder.decreaseMoney(money, prestige, BillType.get(id),
		// desc);
		ChessUtils.chessLog.info("\n\n\n");
	}

	/**
	 * 换房
	 */
	public ChessRoomDto change() {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		int type = chessRoomManager.getDistrict(player.id);
		// 离开
		chessRoomManager.leaveRoom(player);
		// 找房
		return enter(type);
	}

	/**
	 * 玩家准备
	 * 
	 */
	public RoomUserReadyReturnDto ready() {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		ChessUtils.chessLog.info("\n\n\n" + player.id);
		IChessRoom chessRoom = chessRoomManager.getIChessRoom(player);
		if (null == chessRoom) {
			throw new GeneralException(ChessErrorCodes.ROOM_NOT_EXIST);
		}

		// 准备行为广播
		// RoomUserReadyDto readyDto = new RoomUserReadyDto(player.id);
		// Channel roomChannel =
		// channelManager.getChannel(String.valueOf(chessRoom.id));
		// roomChannel.broadcast(readyDto, player.getSessionId());

		ChessUtils.chessLog.info("mission1:" + player.missionHolder.mission.toString());
		ChessUtils.chessLog.info("\n\n\n");

		return new RoomUserReadyReturnDto(chessRoom.setPlayerReady(player.id, player));
	}

	/**
	 * 玩家换牌牌 String playerCard 最多三张牌
	 */
	public void changeCard(String playerCard) {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		IChessRoom chessRoom = chessRoomManager.getIChessRoom(player);
		if (null == chessRoom) {
			throw new GeneralException(ChessErrorCodes.CHANNEL_NOT_EXIST);
		}

		// if (chessRoom.roomState == ChessRoom.States.WAIT4CHANGE) {
		// chessRoom.setSwapCard(playerCard);

		// RoomUserChangeDto changeDto = new RoomUserChangeDto(player.id);
		// Channel roomChannel =
		// channelManager.getChannel(String.valueOf(chessRoom.id));
		// roomChannel.broadcast(changeDto, player.getSessionId());
		// }
	}

	/**
	 * 玩家换出牌 String playerCard1 3张牌 第一墩 String playerCard2 5张牌 第二墩 String
	 * playerCard3 5张牌 第三墩 int specialType 特殊牌型
	 */
	public void showCard(String playerCard1, String playerCard2, String playerCard3, int specialType) {

		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		IChessRoom chessRoom = chessRoomManager.getIChessRoom(player);
		if (null == chessRoom) {
			throw new GeneralException(ChessErrorCodes.ROOM_NOT_EXIST);
		}

		chessRoom.showCard(playerCard1, playerCard2, playerCard3, specialType, player);

		ChessUtils.chessLog.info(playerCard1 + "_" + playerCard2 + "_" + playerCard3 + "_" + specialType);
		ChessUtils.chessLog.info("\n\n\n");
	}

	/**
	 * 玩家聊天
	 * 
	 * @param type
	 *            聊天语句
	 * @return
	 */
	public void chat(int type) {

		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		// 找房间
		IChessRoom chessRoom = chessRoomManager.getIChessRoom(player);
		if (null == chessRoom) {
			throw new GeneralException(ChessErrorCodes.ROOM_NOT_EXIST);
		}
		int playerPos = chessRoom.getPosition(player.id);// 玩家位置

		// 玩家频道
		Channel roomChannel = channelManager.getChannel(String.valueOf(chessRoom.getId()));

		// 广播给其他人的消息
		RoomUserChatDto roomUser = new RoomUserChatDto();
		roomUser.setPlayerId(String.valueOf(player.id));
		roomUser.setPlayerName(player.nickname);
		roomUser.setPosition(playerPos);
		roomUser.setType(type);
		roomChannel.broadcast(roomUser, player.getSessionId());

	}

	/**
	 * 比赛列表
	 * 
	 * @param type
	 * @return
	 */
	public RoomUserMatchListDto getMatchList() {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		RoomUserMatchListDto ml = new RoomUserMatchListDto();
		List<RoomUserMatchDto> list = new ArrayList<RoomUserMatchDto>();
		Iterator<Entry<Integer, StaticMatch>> it = StaticMatch.getAll().entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, StaticMatch> en = it.next();
			StaticMatch sm = en.getValue();

			RoomUserMatchDto m = new RoomUserMatchDto();
			m.id = sm.getId();
			m.name = sm.getName();
			m.cost = sm.getCost();
			m.prize = sm.getIcon();
			m.date = sm.getDate();
			m.num = chessRoomManager.getMatchNum(sm.getId());
			m.state = chessRoomManager.getMatchState(sm.getId(), player.id);
			m.prizeAvail = chessRoomManager.isMatchPrizeAvail(sm.getId(), player.id);
			m.max = sm.getMaxPlayer();
			list.add(m);
		}

		RoomUserMatchDto[] arr = new RoomUserMatchDto[list.size()];
		list.toArray(arr);
		ml.setRoomUserMatchDto(arr);
		return ml;
	}

	/**
	 * 玩家报名
	 * 
	 * @param type
	 * @return
	 */
	public void enroll(int type) {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		// 不存在该区
		if (chessRoomManager.constructDistrictMatchId(type) == 0) {
			throw new GeneralException(ChessErrorCodes.DISTRICT_NOT_EXIST);
		}
		chessRoomManager.enrollMatch(type, player);
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public ChessRoomDto enterMatch(int type) {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		// 不存在该区
		if (!chessRoomManager.containsDistrictId(type)) {
			throw new GeneralException(ChessErrorCodes.DISTRICT_NOT_EXIST);
		}
		chessRoomManager.setDistrict(player.id, type);
		chessRoomManager.enterMatch(type, player.id);

		
		StaticMatch mConfig = StaticMatch.get(type);
		// 给自己的消息
		List<RoomUserDto> tmpRoomUsers = new ArrayList<RoomUserDto>();

		RoomUserDto tmpRoomUser = new RoomUserDto();
		tmpRoomUser.setPlayerId(String.valueOf(player.id));
		tmpRoomUser.setPlayerName(player.nickname);
		tmpRoomUser.setAvatar(player.avatar);
		tmpRoomUser.setGender(player.gender.ordinal());
		tmpRoomUser.setCounter(chessRoomManager.getMatchCounter(type,player.id));
		tmpRoomUsers.add(tmpRoomUser);
		
		ChessRoomDto chessRoomDto = new ChessRoomDto(tmpRoomUsers);
		chessRoomDto.setDistrictId(type);
		chessRoomDto.setRoomId("1000");
		chessRoomDto.setRoomName("等待比赛房间");
		chessRoomDto.setRate(mConfig.getInitRate());
		chessRoomDto.setType(2);
		return chessRoomDto;// 返回房间信息
	}
	
	/**
	 * @param type
	 * @return
	 */
	public ChessRoomDto express() {
		return enterMatch(chessMatchManager.curDitrictId);
	}

	/**
	 * 玩家比牌结束
	 * 
	 */
	public RoomUserMatchFinishDto finishMatch() {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		RoomUserMatchFinishDto f = new RoomUserMatchFinishDto();
		// 设置状态为准备
		f.state = chessRoomManager.putBackInMatchQueue(player);
		if(f.state == 0){
			f.rank = chessRoomManager.getMatchPlayerRank( player.id);
		}
		return f;
	}
	
	/**
	 * 玩家比赛排行
	 * 
	 */
	public ChessPlayerMatchRankListDto rankOfMatch(int type) {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		return chessRoomManager.getMatchFinalRank(type, player.id);
	}
	
	/**
	 * 玩家比赛领奖
	 * 
	 */
	public RoomUserMatchPrizeDto matchPrize(int type) {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		return chessRoomManager.getMatchPrize(type, player.id);
	}
	

}
