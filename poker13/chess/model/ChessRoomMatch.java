package com.chitu.chess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.gecko.broadcast.Channel;
import cn.gecko.broadcast.ChannelManager;
import cn.gecko.commons.utils.SpringUtils;

import com.chitu.chess.msg.ChessRoomDto;
import com.chitu.chess.msg.RoomUserCardDto;
import com.chitu.chess.msg.RoomUserDto;
import com.chitu.chess.msg.RoomUserMatchStartDto;
import com.chitu.chess.msg.RoomUserResultDto;
import com.chitu.chess.msg.RoomUserShowCardDto;
import com.chitu.chess.service.ChessPlayerManager;
import com.chitu.chess.service.ChessRoomManager;

/**
 * @author Administrator
 * 
 */
public class ChessRoomMatch extends ChessRoom {
	private int finals;//决赛圈数
	public String name;//比赛名称

	public ChessRoomMatch(int districtId) {
		super(districtId);
		// TODO Auto-generated constructor stub
	}

	public void init(){
		
	}
	
	/**
	 * 玩家准备下局比赛
	 * 
	 * @param playerId
	 * @return
	 */
	public synchronized int setPlayerReady(long playerId,ChessPlayer player) {
		
		// 房间空闲,准备时才能准备
		playerReadyNum = 0;
		if (roomState == States.IDLE) {
			for (int i = 0; i < chessRoomPlayer.length; ++i) {
				if (chessRoomPlayer[i].playerId == playerId) {
					chessRoomPlayer[i].playerState = ChessRoomPlayer.PlayerStates.READY;
				}
			}
		}
		
		ChessUtils.chessLog.info("========setPlayerReady4Match==========");
		ChessUtils.chessLog.info("roomState" + this.roomState);
		ChessUtils.chessLog.info("playerReadyNum:" + playerReadyNum);
		return 1;
	}

	public synchronized boolean checkAllFinish() {
		for (int i = 0; i < chessRoomPlayer.length; ++i) {
			if (chessRoomPlayer[i].playerId != 0) {
				return false;
			}
		}

		return true;
	}
	
	public synchronized void checkState() {

		long now = new java.util.Date().getTime();

		switch (roomState) {
		case IDLE:
			// 每个人都播完了动画 就 立即清除房间 否则 等待
			if(checkAllFinish()){
				cleanRoom();
			}
			
			if (now > deadline) {
				ChessUtils.chessLog.info("==========cleanRoom:" + this.districtId + "_" + this.id + "============");
				cleanRoom();
			}
			break;
		case WAIT4COMPARE:
			if (now > deadline) {
				ChessUtils.chessLog.info("show card");
				comparePlayerCard();
				settlement();
				resetPlayer();
				deadline = 70000 + now;//90秒后将未 准备下局的清除
				roomState = States.IDLE;
			}
			break;
		}
	}
	
	public void start() {
		long now = new java.util.Date().getTime();
		//洗牌
		cardReset();
		cardRandom();
		cardAssign();
		//发牌
		assignPlayerCard();
		deadline = 35000 + now;
		roomState = States.WAIT4COMPARE;		
	}
	
	
	/**
	 * reset player
	 * 
	 */
	protected void resetPlayer() {
		// 踢走 刚才 发了牌 掉线的玩家
		ChessUtils.chessLog.info("==========resetPlayer============");
		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);
		for (int i = 0; i < chessRoomPlayer.length; i++) {
			//npc 为开始托管,ABSENT 可能为中途托管
			if (chessRoomPlayer[i].npc || (chessRoomPlayer[i].playerState.state & ChessRoomPlayer.PlayerStates.ABSENT.state)>0) {
				//chessRoomManager.setMatchAbsent(districtId,chessRoomPlayer[i].playerId);//needless
				chessRoomPlayer[i].playerId = 0;
				playerNum--;
			}
		}
		if(playerNum == 0){
			chessRoomManager.cleanMatch(districtId,id);
		}
	}
	
	protected void assignPlayerCard() {
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);
		ChannelManager channelManager = SpringUtils.getBeanOfType(ChannelManager.class);
		Channel roomChannel = channelManager.getChannel(String.valueOf(id));
		List<RoomUserDto> tmpRoomUsers = new ArrayList<RoomUserDto>();
		for (int i = 0; i < chessRoomPlayer.length; i++) {
			if (chessRoomPlayer[i].playerId > 0){
				
				ChessPlayer playerTmp = chessPlayerManager.getAnyPlayerById(chessRoomPlayer[i].playerId);
				if(playerTmp.isConnected() && chessRoomPlayer[i].npc == false)//本局为托管,不加入频道
				playerTmp.joinChannel(roomChannel.getName());
				
				RoomUserDto tmpRoomUser = new RoomUserDto();
				tmpRoomUser.setPlayerId(String.valueOf(chessRoomPlayer[i].playerId));
				tmpRoomUser.setPlayerName(playerTmp.nickname);
				tmpRoomUser.setAvatar(playerTmp.avatar);
				tmpRoomUser.setPosition(i);
				tmpRoomUser.setCounter(chessRoomPlayer[i].counter);
				tmpRoomUser.setRank(chessRoomManager.getMatchRank(districtId, chessRoomPlayer[i].playerId));
				tmpRoomUser.setGender(playerTmp.gender.ordinal());
				tmpRoomUser.setState(chessRoomPlayer[i].playerState.state);
				tmpRoomUsers.add(tmpRoomUser);			
			}
		}
		
		//房间信息
		ChessRoomDto chessRoomDto = new ChessRoomDto(tmpRoomUsers);
		chessRoomDto.setDistrictId(districtId);
		chessRoomDto.setRoomId(String.valueOf(id));
		chessRoomDto.setRoomName(name);
		chessRoomDto.setRate(chessRoomManager.getMatchRate(districtId));
		chessRoomDto.setType(1);//比赛
		chessRoomDto.setFinals(finals);

		for (int i = 0; i < chessRoomPlayer.length; i++) {
			ChessUtils.chessLog.info(i + "_" + chessRoomPlayer[i].playerState);

			if (chessRoomPlayer[i].playerId <= 0) {
				continue;
			}

			// 确认玩家在线
			ChessPlayer player = chessPlayerManager.getOnlinePlayerById(chessRoomPlayer[i].playerId);
			if (player != null && chessRoomPlayer[i].npc == false) {//如果
				//一个玩家的牌
				RoomUserCardDto userCard = new RoomUserCardDto(chessRoomPlayer[i].playerCardSequence);
				RoomUserMatchStartDto ms = new RoomUserMatchStartDto();
				ms.setChessRoom(chessRoomDto);
				ms.setUserCard(userCard);
		
				player.deliver(ms);
				ChessUtils.chessLog.info("player:"+player.id+Arrays.toString(chessRoomPlayer[i].playerCardSequence));
			}
			chessRoomPlayer[i].playerState = ChessRoomPlayer.PlayerStates.ASSINGED;
			
			// npc出牌
			if (chessRoomPlayer[i].npc){
				chessRoomPlayer[i].cardAIThink();
				chessRoomPlayer[i].playerState = ChessRoomPlayer.PlayerStates.ABSENT_SHOWED;//set to be absent for auto clean
			}		
		}
		checkAllShowed();
	}

	protected void settlement() {

		ChessUtils.chessLog.info("==========amountScore:" + amountScore + "============");
		ChessUtils.chessLog.info("==========availableMoney:" + availableMoney + "============");

		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);
		// 广播给其他人的消息
		List<RoomUserShowCardDto> liRoomUserShowCard = new ArrayList<RoomUserShowCardDto>();
		
		
		// 逐个玩家对比
		for (int i = 0; i < chessRoomPlayer.length; ++i) {
			ChessUtils.chessLog.info(i + "_" + chessRoomPlayer[i].playerState);
			// 出牌玩家
			if ((chessRoomPlayer[i].playerState.state & ChessRoomPlayer.PlayerStates.SHOWED.state) > 0) {
				// 算分
				if (chessRoomPlayer[i].cardScoreSum < 0) {// 失败方
					chessRoomPlayer[i].cardMoneySum = chessRoomPlayer[i].cardScoreSum * getRateOfExchange();
					if (chessRoomPlayer[i].cardMoneySum + chessRoomPlayer[i].counter < 0) {
						availableMoney += chessRoomPlayer[i].counter;// 不够钱,将剩余的取出
						chessRoomPlayer[i].cardMoneySum = -chessRoomPlayer[i].counter;
					} else {
						availableMoney -= chessRoomPlayer[i].cardMoneySum;// 变正
					}
				} else {// 胜利方
					amountScore += chessRoomPlayer[i].cardScoreSum;
				}
			}// if
		}// for
		

		// 逐个玩家对比
		for (int i = 0; i < chessRoomPlayer.length; ++i) {
			ChessUtils.chessLog.info(i + "_" + chessRoomPlayer[i].playerState);
			// 出牌玩家
			if ((chessRoomPlayer[i].playerState.state & ChessRoomPlayer.PlayerStates.SHOWED.state) > 0) {

				//chessRoomPlayer[i].cardMoneySum =  chessRoomPlayer[i].cardScoreSum * getRateOfExchange();//本局输赢筹码
				//变动筹码并返回总值
				if (chessRoomPlayer[i].cardScoreSum < 0) {// 失败方
					chessRoomPlayer[i].counter = chessRoomManager.updateMatchCounter(districtId, chessRoomPlayer[i].playerId, chessRoomPlayer[i].cardMoneySum,chessRoomPlayer[i].cardScoreSum );
				}else if (chessRoomPlayer[i].cardScoreSum > 0) {// 胜利方
					chessRoomPlayer[i].cardMoneySum = Math.round(availableMoney * chessRoomPlayer[i].cardScoreSum / amountScore);
					chessRoomPlayer[i].counter = chessRoomManager.updateMatchCounter(districtId, chessRoomPlayer[i].playerId, chessRoomPlayer[i].cardMoneySum,chessRoomPlayer[i].cardScoreSum );
				}else if(chessRoomPlayer[i].cardScoreSum == 0){//打平
					chessRoomPlayer[i].counter = chessRoomManager.updateMatchCounter(districtId, chessRoomPlayer[i].playerId, 0,0 );
				}
				
				// 广播给其他人的消息
				RoomUserShowCardDto roomUserShowCard = new RoomUserShowCardDto(chessRoomPlayer[i]);
				roomUserShowCard.position = i;
				roomUserShowCard.setCounter(chessRoomPlayer[i].counter);
				liRoomUserShowCard.add(roomUserShowCard);

				ChessPlayer playerTmp = chessPlayerManager.getAnyPlayerById(chessRoomPlayer[i].playerId);
				// Debug
				ChessUtils.chessLog.info("==========comp:" + playerTmp.accountId + "============");
				ChessUtils.chessLog.info(i + "号特殊牌:" + chessRoomPlayer[i].cardSpecialType);
				ChessUtils.chessLog.info(i + "号得分:" + chessRoomPlayer[i].cardScoreSum);
				ChessUtils.chessLog.info(i + "号倍率:" +  getRateOfExchange());
				ChessUtils.chessLog.info(i + "号总筹码:" + roomUserShowCard.getCounter());
				ChessUtils.chessLog.info(i + "号第一墩:" + Arrays.toString(chessRoomPlayer[i].cardScore1));
				ChessUtils.chessLog.info(i + "号第二墩:" + Arrays.toString(chessRoomPlayer[i].cardScore2));
				ChessUtils.chessLog.info(i + "号第三墩:" + Arrays.toString(chessRoomPlayer[i].cardScore3));
				ChessUtils.chessLog.info(i + "号第一墩怪:" + Arrays.toString(chessRoomPlayer[i].cardCreep1));
				ChessUtils.chessLog.info(i + "号第二墩怪:" + Arrays.toString(chessRoomPlayer[i].cardCreep2));
				ChessUtils.chessLog.info(i + "号第三墩怪:" + Arrays.toString(chessRoomPlayer[i].cardCreep3));
				ChessUtils.chessLog.info(i + "号特殊牌分:" + Arrays.toString(chessRoomPlayer[i].cardSpecial) + "\n");
				
			}// if show
		}// for
		chessRoomManager.updateMatchFinalsRank(districtId);//更新决赛名次
		
		//牌局记录
		byte[] replay = PersistChessPlayer.ReplaySerialization(this);
		// 逐个玩家
		for (int i = 0; i < chessRoomPlayer.length; ++i) {
			// 出牌玩家
			if ((chessRoomPlayer[i].playerState.state & ChessRoomPlayer.PlayerStates.SHOWED.state) > 0) {
				ChessPlayer playerTmp = chessPlayerManager.getAnyPlayerById(chessRoomPlayer[i].playerId);
				playerTmp.setReplay(replay);
			}
		}

		// 广播给其他人的消息
		RoomUserResultDto roomUserRs = new RoomUserResultDto(liRoomUserShowCard);
		ChannelManager channelManager = SpringUtils.getBeanOfType(ChannelManager.class);
		Channel roomChannel = channelManager.getChannel(String.valueOf(id));
		roomChannel.broadcast(roomUserRs);
	}

	
	
	
	/**
	 * cleanRoom chessroom ...
	 * 如果有人不做动画结束请求就 清房  id不为0的人
	 */
	protected void cleanRoom() {
		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
		for (int i = 0; i < chessRoomPlayer.length; i++) {
			ChessUtils.chessLog.info("==========cleanRoom:" + chessRoomPlayer[i].playerId + "============");
			if(chessRoomPlayer[i].playerId>0){
				ChessPlayer player = chessPlayerManager.getAnyPlayerById(chessRoomPlayer[i].playerId);
				if (player != null) {
					if(player.isConnected()){
						player.logout();
					}
				}
				chessRoomManager.setMatchAbsent(districtId,chessRoomPlayer[i].playerId);//区状态置为托管
			}
		}
		chessRoomManager.cleanMatch(districtId,id);
	}

	public void setFinals(int finals) {
		this.finals = finals;
	}
}
