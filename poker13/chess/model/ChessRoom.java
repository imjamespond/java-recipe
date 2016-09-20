package com.chitu.chess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.chitu.chess.data.StaticDistrictTypes;
import com.chitu.chess.data.StaticSpecialCardScore;
import com.chitu.chess.model.ChessRoomPlayer.PlayerStates;
import com.chitu.chess.msg.ChessPlayerMissionNotifyDto;
import com.chitu.chess.msg.RoomUserCardDto;
import com.chitu.chess.msg.RoomUserReadyReturnDto;
import com.chitu.chess.msg.RoomUserResultDto;
import com.chitu.chess.msg.RoomUserShowCardDto;
import com.chitu.chess.msg.RoomUserShowDto;
import com.chitu.chess.service.ChessPlayerManager;
import com.chitu.chess.service.ChessRoomManager;

import cn.gecko.broadcast.Channel;
import cn.gecko.broadcast.ChannelManager;
import cn.gecko.commons.data.BillType;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.IdUtils;
import cn.gecko.commons.utils.SpringUtils;

/**
 * @author Administrator
 * 
 */
public class ChessRoom implements IChessRoom{

	// 房间参数
	protected int comparisonTime;
	private int moneyLimitation;
	private int rateOfExchange;
	private int tax;

	// 房间状态
	public long id;
	public int districtId;
	public int playerNum;
	public int playerReadyNum;
	public long deadline;
	public States roomState;

	// 房间玩家与牌信息
	public ChessRoomPlayer[] chessRoomPlayer = new ChessRoomPlayer[4];

	private int[] cardPattern;// 花样
	private int[] cardPoint;// 点数 1-13

	private int cardNum;
	private int powerLevelup;// 使权值提升一级
	private int powerLevelup2;// 使权值提升一级
	private int powerLevelup3;// 使权值提升一级
	private int powerLevelup4;// 使权值提升一级
	private int playerCardNum;

	private int[] playerCardAssign = new int[52];// 洗过的牌
	private int[] playerCardAssignIndex = new int[52];// 洗过牌的索引如:{2,4,6,8}playerCardAssignIndex[2]=0
	private int playerSwapCardLen;// 玩家换算总数
	private int[] playerSwapCard = new int[12];
	private int[] playerCardTemp = new int[52];
	protected int availableMoney;
	protected int amountScore;

	public enum States {
		IDLE, WAIT4START, WAIT4CHANGE, WAIT4COMPARE, WAIT4ANIMATION;
	}

	public ChessRoom(int districtId) {
		this.id = IdUtils.generateLongId();
		this.districtId = districtId;
		roomState = States.IDLE;

		cardInitialize();

		ChessUtils.chessLog.info("=======ChessRoom=======:"+id);
		// ChessUtils.chessLog.info(Arrays.toString(playerCardAssign));
		ChessUtils.chessLog.info(comparisonTime);
		ChessUtils.chessLog.info(moneyLimitation);
		ChessUtils.chessLog.info(rateOfExchange);
	}
	
	public void init(){
		StaticDistrictTypes staticDistrictTypes = StaticDistrictTypes.get(districtId);
		this.comparisonTime = staticDistrictTypes.getComparisonTime();
		this.moneyLimitation = staticDistrictTypes.getMoneyLimitation();
		this.rateOfExchange = staticDistrictTypes.getRateOfExchange();
		this.tax = staticDistrictTypes.getTax();		
	}

	public synchronized int addPlayer(long playerId) {
		playerNum = 0;
		int pos = -1;
		for (int i = 0; i < chessRoomPlayer.length; i++) {
			if (chessRoomPlayer[i].playerId == 0) {
				chessRoomPlayer[i].playerId = playerId;
				pos = i;
				break;
			}
		}
		for (int i = 0; i < chessRoomPlayer.length; i++) {
			ChessUtils.chessLog.info("addPlayer:" + chessRoomPlayer[i].playerId);
			if (chessRoomPlayer[i].playerId > 0) {
				playerNum++;
			}
		}
		return pos;
	}
	
	/**
	 * 如果已经发牌玩家回到房间
	 * 
	 * @param playerId
	 * @return
	 */
	public synchronized int setPlayer(long playerId) {
		for (int i = 0; i < chessRoomPlayer.length; i++) {
			if (chessRoomPlayer[i].playerId == playerId) {
				// 重连的算已发牌
				chessRoomPlayer[i].playerState = ChessRoomPlayer.PlayerStates.SHOWED;
				chessRoomPlayer[i].playerId = playerId;
				return i;
			}
		}
		return -1;
	}

	/**
	 * 踢走 掉线的玩家
	 * 
	 * @param playerId
	 * @return
	 */
	@Override
	public synchronized int setPlayerAbsent(long playerId) {
		ChessUtils.chessLog.info( "==========setPlayerAbsent=========:"+playerId);
		int pos = getPosition(playerId);
		if (pos >= 0) {
			if (chessRoomPlayer[pos].playerId == playerId) {
				if (roomState == States.IDLE || roomState == States.WAIT4START) {
					//已准才减一
					if (ChessRoomPlayer.PlayerStates.READY == chessRoomPlayer[pos].playerState) {
						playerReadyNum--;
					}
					chessRoomPlayer[pos].playerState = ChessRoomPlayer.PlayerStates.IDLE;
					chessRoomPlayer[pos].playerId = 0;
					playerNum--;
					ChessUtils.chessLog.info( playerReadyNum+"_"+playerNum);
				} else {
					// 若玩家没发牌则直接踢出,不然设置为离开
					if ((chessRoomPlayer[pos].playerState.state & ChessRoomPlayer.PlayerStates.ASSINGED_CHANGED_SHOWED_ABSENT.state) == 0) {
						chessRoomPlayer[pos].playerState = ChessRoomPlayer.PlayerStates.IDLE;
						chessRoomPlayer[pos].playerId = 0;
						//playerReadyNum--;dosnt matter
						playerNum--;
					} else {
						if (chessRoomPlayer[pos].playerState != ChessRoomPlayer.PlayerStates.SHOWED) {
							chessRoomPlayer[pos].cardAIThink();
						}
						chessRoomPlayer[pos].playerState = ChessRoomPlayer.PlayerStates.ABSENT_SHOWED;
						checkAllShowed();
					}
				}

				return pos;
			}
		}
		return -1;
	}

	/**
	 * 玩家准备
	 * 
	 * @param playerId
	 * @return
	 */
	@Override
	public synchronized int setPlayerReady(long playerId,ChessPlayer player) {
		
		if(null != player){
		//判断金币数量
		if(getMoneyLimitation() > player.wealthHolder.getMoney()){
			ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);
			//离开
			chessRoomManager.leaveRoom(player);
			//throw new GeneralException(ChessErrorCodes.MONEY_NOT_ENOUGH);
			return 0;
		}
		}
		
		// 房间空闲,准备时才能准备
		playerReadyNum = 0;
		if (roomState == States.WAIT4START || roomState == States.IDLE) {
			for (int i = 0; i < chessRoomPlayer.length; ++i) {
				if (chessRoomPlayer[i].playerId == playerId) {
					chessRoomPlayer[i].playerState = ChessRoomPlayer.PlayerStates.READY;
				}
				if (chessRoomPlayer[i].playerState == ChessRoomPlayer.PlayerStates.READY) {
					++playerReadyNum;
				}
			}
		}
		ChessUtils.chessLog.info("========setPlayerReady==========");
		ChessUtils.chessLog.info("roomState" + this.roomState);
		ChessUtils.chessLog.info("playerReadyNum:" + playerReadyNum);
		return 1;
	}

	
	public synchronized void setSwapCard(String str) {
		ChessUtils.Str2ArraySetLength(str, playerSwapCard, playerSwapCardLen);
	}

	public synchronized int getCardPos(int card) {
		if(card<=51 && card>=0)
		return playerCardAssignIndex[card];
		else
		return -1;
	}
	
	public synchronized int getPosition(long playerId) {
		for (int i = 0; i < chessRoomPlayer.length; ++i) {
			if (playerId == chessRoomPlayer[i].playerId) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public synchronized void showCard(String playerCard1, String playerCard2, String playerCard3, int specialType,ChessPlayer player){
		if (roomState == ChessRoom.States.WAIT4CHANGE || roomState == ChessRoom.States.WAIT4COMPARE) {
			int pos = getPosition(player.id);
			if (pos >= 0) {
				if (3 == ChessUtils.Str2Array(playerCard1, chessRoomPlayer[pos].playerCardSequence1)) {
					if (5 == ChessUtils.Str2Array(playerCard2, chessRoomPlayer[pos].playerCardSequence2)) {
						if (5 == ChessUtils.Str2Array(playerCard3, chessRoomPlayer[pos].playerCardSequence3)) {
							//validate player feedback card
							int baseNum = pos * ChessCardTypes.PLAYERCARDNUM;//pos * 13
							int[] arrValidRec = new int[ChessCardTypes.PLAYERCARDNUM];//len is 13,mark duplicated card
							//fix me,card trick
							//chessRoom.chessRoomPlayer[pos].playerCardSequence1[0] = 0;
							for(int i=0;i<chessRoomPlayer[pos].playerCardSequence1.length;i++){
								int cardPos = getCardPos(chessRoomPlayer[pos].playerCardSequence1[i]);
								int cardTmpPos = cardPos - baseNum;//
								if(cardTmpPos >= 0 && cardTmpPos <= ChessCardTypes.PLAYERCARDNUM){
									if(arrValidRec[cardTmpPos]>0)
										throw new GeneralException(ChessErrorCodes.INVALID_CARD);
									else
										arrValidRec[cardTmpPos] = 1;
								}else{
									throw new GeneralException(ChessErrorCodes.INVALID_CARD);
								}
							}
							for(int i=0;i<chessRoomPlayer[pos].playerCardSequence2.length;i++){
								int cardPos = getCardPos(chessRoomPlayer[pos].playerCardSequence2[i]);
								int cardTmpPos = cardPos - baseNum;
								if(cardTmpPos >= 0 && cardTmpPos <= ChessCardTypes.PLAYERCARDNUM){
									if(arrValidRec[cardTmpPos]>0)
										throw new GeneralException(ChessErrorCodes.INVALID_CARD);
									else
										arrValidRec[cardTmpPos] = 1;
								}else{
									throw new GeneralException(ChessErrorCodes.INVALID_CARD);
								}
							}							
							for(int i=0;i<chessRoomPlayer[pos].playerCardSequence3.length;i++){
								int cardPos = getCardPos(chessRoomPlayer[pos].playerCardSequence3[i]);
								int cardTmpPos = cardPos - baseNum;
								if(cardTmpPos >= 0 && cardTmpPos <= ChessCardTypes.PLAYERCARDNUM){
									if(arrValidRec[cardTmpPos]>0)
										throw new GeneralException(ChessErrorCodes.INVALID_CARD);
									else
										arrValidRec[cardTmpPos] = 1;
								}else{
									throw new GeneralException(ChessErrorCodes.INVALID_CARD);
								}
							}							
							
							chessRoomPlayer[pos].cardSpecialType = specialType;
							chessRoomPlayer[pos].playerState = ChessRoomPlayer.PlayerStates.SHOWED;
							checkAllShowed();
							
							ChannelManager channelManager = SpringUtils.getBeanOfType(ChannelManager.class);
							Channel roomChannel = channelManager.getChannel(String.valueOf(id));
							RoomUserShowDto showDto = new RoomUserShowDto(player.id);
							roomChannel.broadcast(showDto, player.getSessionId());
						}
					}
				}
			}
		}
	}
	
	@Override
	public synchronized void cardSpecify(int num, long playerId, String cardStr) {
		ChessUtils.chessLog.info( "==========cardSpecify=========");
		int pos = getPosition(playerId);
		chessRoomPlayer[pos].trickNum = num;
		ChessUtils.Str2Array(cardStr, chessRoomPlayer[pos].trickCardSequence);
		ChessUtils.chessLog.info(num + "_" + playerId + "_"+ cardStr);
	}
	
	public synchronized void checkAllShowed() {
		for (int i = 0; i < chessRoomPlayer.length; ++i) {
			ChessUtils.chessLog.info(chessRoomPlayer[i].playerId + "-----" + chessRoomPlayer[i].playerState);
			if (// chessRoomPlayer[i].playerId > 0&&
			(chessRoomPlayer[i].playerState.state & ChessRoomPlayer.PlayerStates.ASSINGED_CHANGED_SHOWED_ABSENT.state) > 0) {
				if ((ChessRoomPlayer.PlayerStates.SHOWED.state & chessRoomPlayer[i].playerState.state) == 0) {
					ChessUtils.chessLog.info(chessRoomPlayer[i].playerId + "_____" + chessRoomPlayer[i].playerState);
					return;
				}
			}
		}

		roomState = States.WAIT4COMPARE;
		long now = new java.util.Date().getTime();
		deadline = now;// 提前比牌
	}

	public int addNpcPlayer(long playerId) {
		int pos = addPlayer(playerId);
		if(pos<0){
			return pos;			
		}
		chessRoomPlayer[pos].npc = true;
		chessRoomPlayer[pos].playerState = PlayerStates.READY;
		return pos;
	}
	
	/**
	 * reset player
	 * 
	 */
	protected void resetPlayer() {
		availableMoney = 0;
		amountScore = 0;
		
		for (int i = 0; i < chessRoomPlayer.length; i++) {
			// reset data
			Arrays.fill(chessRoomPlayer[i].cardScore1, 0);
			Arrays.fill(chessRoomPlayer[i].cardScore2, 0);
			Arrays.fill(chessRoomPlayer[i].cardScore3, 0);
			Arrays.fill(chessRoomPlayer[i].cardCreep1, 0);
			Arrays.fill(chessRoomPlayer[i].cardCreep2, 0);
			Arrays.fill(chessRoomPlayer[i].cardCreep3, 0);
			Arrays.fill(chessRoomPlayer[i].cardSpecial, 0);
			Arrays.fill(chessRoomPlayer[i].shotPosition, 0);
			chessRoomPlayer[i].cardScoreSum = 0;
			chessRoomPlayer[i].cardMoneySum = 0;
			chessRoomPlayer[i].cardPower = 0;
			chessRoomPlayer[i].cardPower2 = 0;
			chessRoomPlayer[i].cardPower3 = 0;
			chessRoomPlayer[i].cardType = 0;
			chessRoomPlayer[i].cardType2 = 0;
			chessRoomPlayer[i].cardType3 = 0;
			chessRoomPlayer[i].shotNum = 0;
			
			// 踢走 刚才 发了牌 掉线的玩家
			if ((chessRoomPlayer[i].playerState.state & ChessRoomPlayer.PlayerStates.ABSENT.state) > 0) {
				chessRoomPlayer[i].playerId = 0;
				playerNum--;
				ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);
				chessRoomManager.addAvailableRoom(this);
			}
			chessRoomPlayer[i].playerState = ChessRoomPlayer.PlayerStates.IDLE;
			
			//npc 准备
			if (chessRoomPlayer[i].npc){
				setPlayerReady(chessRoomPlayer[i].playerId,null);
			}
		}
		//npc 不让自动开局
		playerReadyNum = 0;
	}

	/**
	 * cleanRoom chessroom ...
	 * 清房 所有空闲的人
	 */
	protected void cleanRoom() {
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);
		for (int i = 0; i < chessRoomPlayer.length; i++) {
			if (chessRoomPlayer[i].playerState == ChessRoomPlayer.PlayerStates.IDLE 
					&& chessRoomPlayer[i].playerId>0) {
				ChessPlayer player = chessPlayerManager.getAnyPlayerById(chessRoomPlayer[i].playerId);
				if (player != null) {
					chessRoomManager.leaveRoom(player);
					if(player.isConnected()){
						player.logout();
					}
				}
			}
		}
	}

	protected void assignPlayerCard() {
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);

		for (int i = 0; i < chessRoomPlayer.length; i++) {
			ChessUtils.chessLog.info(i + "_" + chessRoomPlayer[i].playerState);

			if (chessRoomPlayer[i].playerId <= 0) {
				continue;
			}
			if (chessRoomPlayer[i].playerState == ChessRoomPlayer.PlayerStates.READY) {
				// 确认玩家在线
				ChessPlayer player = chessPlayerManager.getOnlinePlayerById(chessRoomPlayer[i].playerId);
				if (player != null) {
					player.wealthHolder.decreaseMoney(tax, 0, 0, BillType
							.get(ChessBillTypes.TAX), "");
					player.deliver(new RoomUserCardDto(chessRoomPlayer[i].playerCardSequence));
					ChessUtils.chessLog.info("player:"+player.id+Arrays.toString(chessRoomPlayer[i].playerCardSequence));
				}
				chessRoomPlayer[i].playerState = ChessRoomPlayer.PlayerStates.ASSINGED;
				
				// npc出牌
				if (chessRoomPlayer[i].npc){
					chessRoomPlayer[i].cardAIThink();
					chessRoomPlayer[i].playerState = ChessRoomPlayer.PlayerStates.SHOWED;
				}
			}
		}
	}

	/**
	 * 换算牌
	 * 
	 * @param swapCard
	 */
	protected void swapCardRamdon() {
		// ChessUtils.chessLog.info(Arrays.toString(_playerCardAssign));
		// ChessUtils.chessLog.info(Arrays.toString(_playerCardAssignIndex));
		int counterNum = playerSwapCardLen;
		while (--counterNum >= 0) {
			int ramdon = (int) Math.floor(counterNum * Math.random());
			playerCardAssign[playerCardAssignIndex[playerSwapCard[ramdon]]] = playerSwapCard[counterNum];// 随机抽一张牌 将此牌在所有玩家牌中 换成 swapcard中最后一张
			playerCardAssign[playerCardAssignIndex[playerSwapCard[counterNum]]] = playerSwapCard[ramdon];
			int temp = playerCardAssignIndex[playerSwapCard[ramdon]];// 更新索引
			playerCardAssignIndex[playerSwapCard[ramdon]] = playerCardAssignIndex[playerSwapCard[counterNum]];
			playerCardAssignIndex[playerSwapCard[counterNum]] = temp;
			playerSwapCard[ramdon] = playerSwapCard[counterNum];
		}

		Arrays.fill(playerSwapCard, 0);

		// 将牌发回玩家
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);

		for (int i = 0; i < chessRoomPlayer.length; i++) {
			ChessUtils.chessLog.info(i + "_" + chessRoomPlayer[i].playerState);

			if (chessRoomPlayer[i].playerId <= 0) {
				continue;
			}
			if (chessRoomPlayer[i].playerState == ChessRoomPlayer.PlayerStates.CHANGED) {
				// 确认玩家在线
				ChessPlayer player = chessPlayerManager.getOnlinePlayerById(chessRoomPlayer[i].playerId);
				if (player != null) {
					player.deliver(new RoomUserCardDto(chessRoomPlayer[i].playerCardSequence));
					ChessUtils.chessLog.info(Arrays.toString(chessRoomPlayer[i].playerCardSequence));
				}
				chessRoomPlayer[i].playerState = ChessRoomPlayer.PlayerStates.ASSINGED;
			}
		}
		// ChessUtils.chessLog.info(Arrays.toString(_playerCardAssign));
		// ChessUtils.chessLog.info(Arrays.toString(_playerCardAssignIndex));

	}

	protected void comparePlayerCard() {

		for (int i = 0; i < chessRoomPlayer.length; i++) {

			// 玩家没出牌,掉线,换了牌不出牌 和 出了牌
			if ((chessRoomPlayer[i].playerState.state & ChessRoomPlayer.PlayerStates.ASSINGED_CHANGED_SHOWED_ABSENT.state) > 0) {
				// 不出牌的玩家
				if ((chessRoomPlayer[i].playerState.state & ChessRoomPlayer.PlayerStates.ASSINGED_CHANGED.state) > 0) {
					chessRoomPlayer[i].playerState = ChessRoomPlayer.PlayerStates.SHOWED;
					chessRoomPlayer[i].cardAIThink();
				}

				cardAnalysis3(i);
				cardAnalysis5(i, 2, chessRoomPlayer[i].playerCardSequence2);
				cardAnalysis5(i, 3, chessRoomPlayer[i].playerCardSequence3);
				if (chessRoomPlayer[i].cardSpecialType > 0) {
					if (!cardAnalysisSpecial(chessRoomPlayer[i])) {
						chessRoomPlayer[i].cardSpecialType = 0;
					}
				}
			}
		}


		// 逐个玩家对比
		for (int i = 0; i < chessRoomPlayer.length; ++i) {
			ChessUtils.chessLog.info(i + "_" + chessRoomPlayer[i].playerState);
			// 出牌玩家
			if ((chessRoomPlayer[i].playerState.state & ChessRoomPlayer.PlayerStates.SHOWED.state) > 0) {
				// 互比
				for (int j = i + 1; j < chessRoomPlayer.length; ++j) {
					if ((chessRoomPlayer[j].playerState.state & ChessRoomPlayer.PlayerStates.SHOWED.state) > 0) {
						if (chessRoomPlayer[i].cardSpecialType == 0 && chessRoomPlayer[j].cardSpecialType == 0) {
							cardCompare(i, j);
						}
					}
				}
				// 全垒打
				if (chessRoomPlayer[i].shotNum == 3) {

					int avg = (int) Math.floor(chessRoomPlayer[i].cardScoreSum / 3);
					for (int k = 0; k < chessRoomPlayer.length; ++k) {
						if (chessRoomPlayer[i].playerId != chessRoomPlayer[k].playerId) {
							chessRoomPlayer[k].cardScoreSum -= avg;
						}
					}
					chessRoomPlayer[i].cardScoreSum += chessRoomPlayer[i].cardScoreSum;
				}
				// 特殊牌计分
				for (int k = i + 1; k < chessRoomPlayer.length; ++k) {
					if ((chessRoomPlayer[k].playerState.state & ChessRoomPlayer.PlayerStates.SHOWED.state) > 0) {
						if (chessRoomPlayer[i].cardSpecialType > chessRoomPlayer[k].cardSpecialType) {
							StaticSpecialCardScore sScore = StaticSpecialCardScore.get(chessRoomPlayer[i].cardSpecialType);
							chessRoomPlayer[i].cardScoreSum += sScore.getScore();
							chessRoomPlayer[k].cardScoreSum -= sScore.getScore();
							chessRoomPlayer[k].cardSpecial[i] = sScore.getScore();// 4*4矩阵,竖列表示加分,i大,i列加分
							chessRoomPlayer[i].cardSpecial[k] = 0;// 横排表示减分
						} else if (chessRoomPlayer[k].cardSpecialType > chessRoomPlayer[i].cardSpecialType) {
							StaticSpecialCardScore sScore = StaticSpecialCardScore.get(chessRoomPlayer[k].cardSpecialType);
							chessRoomPlayer[i].cardScoreSum -= sScore.getScore();
							chessRoomPlayer[k].cardScoreSum += sScore.getScore();
							chessRoomPlayer[k].cardSpecial[i] = 0;// 4*4矩阵,竖列表示加分
							chessRoomPlayer[i].cardSpecial[k] = sScore.getScore();// 横行表示减分,k大,i行减分
						}
					}
				}
			}// if show
		}// for

	}		

		
	protected void settlement() {
		
		ChessUtils.chessLog.info("==========amountScore:" + amountScore + "============");
		ChessUtils.chessLog.info("==========availableMoney:" + availableMoney + "============");
		
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
		// 广播给其他人的消息
		List<RoomUserShowCardDto> liRoomUserShowCard = new ArrayList<RoomUserShowCardDto>();

		// 逐个玩家对比
		for (int i = 0; i < chessRoomPlayer.length; ++i) {
			ChessUtils.chessLog.info(i + "_" + chessRoomPlayer[i].playerState);
			// 出牌玩家
			if ((chessRoomPlayer[i].playerState.state & ChessRoomPlayer.PlayerStates.SHOWED.state) > 0) {
				// 结果
				ChessPlayer playerTmp = chessPlayerManager.getAnyPlayerById(chessRoomPlayer[i].playerId);

				// 算分
				if (chessRoomPlayer[i].cardScoreSum < 0) {// 失败方
					chessRoomPlayer[i].cardMoneySum = chessRoomPlayer[i].cardScoreSum * this.rateOfExchange;
					if (chessRoomPlayer[i].cardMoneySum + playerTmp.wealthHolder.getMoney() < 0) {
						availableMoney += playerTmp.wealthHolder.getMoney();// 不够钱,将剩余的取出
						chessRoomPlayer[i].cardMoneySum = -playerTmp.wealthHolder.getMoney();
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

				// 结果
				ChessPlayer playerTmp = chessPlayerManager.getAnyPlayerById(chessRoomPlayer[i].playerId);

				// 算分
				if (chessRoomPlayer[i].cardScoreSum < 0) {// 失败方
					playerTmp.wealthHolder.decreaseMoney(Math.abs(chessRoomPlayer[i].cardMoneySum), Math.abs(chessRoomPlayer[i].cardScoreSum), 0, BillType
						.get(ChessBillTypes.GAME), "");
				}else if (chessRoomPlayer[i].cardScoreSum > 0) {// 胜利方
					chessRoomPlayer[i].cardMoneySum = Math.round(availableMoney * chessRoomPlayer[i].cardScoreSum / amountScore);
					playerTmp.wealthHolder.increaseMoney(chessRoomPlayer[i].cardMoneySum, chessRoomPlayer[i].cardScoreSum, 0, BillType.get(ChessBillTypes.GAME), "");
				}
				

				// 任务成就
				playerTmp.missionHolder.addGameAmount();
				if (chessRoomPlayer[i].cardScoreSum > 0) {
					playerTmp.missionHolder.addVictoryAmount();
					playerTmp.missionHolder.addContinuousVictory();
				} else {
					playerTmp.missionHolder.resetContinuousVictory();
				}
				// 任务通知
				if (playerTmp.missionHolder.mission1.state == Mission.State.DONE || playerTmp.missionHolder.mission2.state == Mission.State.DONE) {
					playerTmp.deliver(new ChessPlayerMissionNotifyDto(true));
				}

				// 广播给其他人的消息
				RoomUserShowCardDto roomUserShowCard = new RoomUserShowCardDto(chessRoomPlayer[i]);
				roomUserShowCard.position = i;
				roomUserShowCard.setPointAmount(playerTmp.wealthHolder.getPoint());
				roomUserShowCard.setMoneyAmount(playerTmp.wealthHolder.getMoney());
				roomUserShowCard.setGameAmount(playerTmp.missionHolder.getGameAmount());
				roomUserShowCard.setVictoryAmount(playerTmp.missionHolder.getVictoryAmount());
				liRoomUserShowCard.add(roomUserShowCard);

				// Debug
				ChessUtils.chessLog.info("==========player:" + playerTmp.accountId + "============");
				ChessUtils.chessLog.info(i + "号特殊牌:" + chessRoomPlayer[i].cardSpecialType);
				ChessUtils.chessLog.info(i + "号得分:" + chessRoomPlayer[i].cardScoreSum);
				ChessUtils.chessLog.info(i + "号金币:" + chessRoomPlayer[i].cardMoneySum);
				ChessUtils.chessLog.info(i + "号胜利:" + roomUserShowCard.getVictoryAmount());
				ChessUtils.chessLog.info(i + "号局数:" + roomUserShowCard.getGameAmount());
				ChessUtils.chessLog.info(i + "号总积分:" + roomUserShowCard.getPointAmount());
				ChessUtils.chessLog.info(i + "号总金币:" + roomUserShowCard.getMoneyAmount());
				ChessUtils.chessLog.info(i + "号第一墩:" + Arrays.toString(chessRoomPlayer[i].cardScore1));
				ChessUtils.chessLog.info(i + "号第二墩:" + Arrays.toString(chessRoomPlayer[i].cardScore2));
				ChessUtils.chessLog.info(i + "号第三墩:" + Arrays.toString(chessRoomPlayer[i].cardScore3));
				ChessUtils.chessLog.info(i + "号第一墩怪:" + Arrays.toString(chessRoomPlayer[i].cardCreep1));
				ChessUtils.chessLog.info(i + "号第二墩怪:" + Arrays.toString(chessRoomPlayer[i].cardCreep2));
				ChessUtils.chessLog.info(i + "号第三墩怪:" + Arrays.toString(chessRoomPlayer[i].cardCreep3));
				ChessUtils.chessLog.info(i + "号特殊牌分:" + Arrays.toString(chessRoomPlayer[i].cardSpecial) + "\n");
			}// if show
		}// for
		
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

	public synchronized void checkState() {

		long now = new java.util.Date().getTime();

		switch (roomState) {
		case IDLE:
			if (playerReadyNum >= 2) {
				ChessUtils.chessLog.info("==========playerReadyNum:" + playerReadyNum + "_" + playerNum + "============");
				if (playerReadyNum >= playerNum) {				
					deadline = now;
					roomState = States.WAIT4START;// 玩家准备倒计时
				}
			}
			else if(playerNum>1 || playerReadyNum == 0){
				
				if (now > deadline) {
					//ChessUtils.chessLog.info("==========cleanRoom:" + this.districtId + "_" + this.id + "============");
					cleanRoom();
				}
				
			}
			break;
		case WAIT4START:
			if (now > deadline) {
				//不够人继续等待
				if (playerReadyNum < 2) {
					roomState = States.IDLE;
					deadline += 10000;
					break;
				}
				
				//洗牌
				cardReset();
				cardRandom();
				cardAssign();
				//发牌
				assignPlayerCard();
				deadline = 35000 + now;
				roomState = States.WAIT4COMPARE;
			}
			break;
		// 玩家在此前应该将 换牌数据 发回
		case WAIT4CHANGE:
			if (now > deadline) {
				ChessUtils.chessLog.info("start change card");
				swapCardRamdon();
				deadline = comparisonTime + now;
				roomState = States.WAIT4COMPARE;
			}
			break;
		// 玩家在此前应该将 出牌数据 发回
		case WAIT4COMPARE:
			if (now > deadline) {
				ChessUtils.chessLog.info("show card");
				comparePlayerCard();
				settlement();
				
				playerReadyNum = 0;				
				deadline = 70000 + now;
				roomState = States.IDLE;
				
				resetPlayer();
			}
			break;
		}
	}

	// 牌局数据运算

	protected void cardInitialize() {

		cardPattern = ChessCardTypes.CARDPATTERN;// 花样
		cardPoint = ChessCardTypes.CARDPOINT;// 点数 1-13

		cardNum = ChessCardTypes.CARDNUM;
		powerLevelup = ChessCardTypes.POWERLEVELUP;// 使权值提升一级
		powerLevelup2 = powerLevelup * powerLevelup;
		powerLevelup3 = powerLevelup2 * powerLevelup;
		powerLevelup4 = powerLevelup3 * powerLevelup;
		playerCardNum = ChessCardTypes.PLAYERCARDNUM;

		// 初始化玩家
		for (int i = 0; i < chessRoomPlayer.length; i++) {
			chessRoomPlayer[i] = new ChessRoomPlayer();
		}
	}

	protected void cardReset() {
		// 初始化
		for (int i = 0; i < cardNum; i++) {
			playerCardTemp[i] = i;
		}
	}

	protected void cardRandom() {
		// 洗牌分牌
		int counterNum = cardNum;
		while (--counterNum >= 0) {
			int index = (int) Math.floor(counterNum * Math.random());
			playerCardAssign[counterNum] = playerCardTemp[index];
			playerCardTemp[index] = playerCardTemp[counterNum];

			playerCardAssignIndex[playerCardAssign[counterNum]] = counterNum;
		}

		// cardRandomTripleFourOfAKind(0);
		// cardRandomTripleFourOfAKind(12);
		// cardRandomTripleFourOfAKind(24);
		// cardRandomStraightFlush();
		// cardRandomAllStraightFlush();
		// cardRandomTripleFourOfAKind();
		//cardRandomAllGreat();
		// cardAllStraight();
		// cardRandomSixPairs();
		// cardRandomSixPairs();
		// cardRandomSixPairs();
		cardTrick();
	}
	

	private void cardTrick() {
		ChessUtils.chessLog.info( "==========cardTrick=========");
		ChessUtils.chessLog.info( "CardSequence:"+Arrays.toString(playerCardAssign));
		for(int pos=0;pos<chessRoomPlayer.length;pos++){
			ChessUtils.chessLog.info( "trickNum:"+chessRoomPlayer[pos].trickNum);
			if(chessRoomPlayer[pos].trickNum>0){
				ChessUtils.chessLog.info( "trickCardSequence:"+Arrays.toString(chessRoomPlayer[pos].trickCardSequence));
				
				int specifyId = 0;//
				int specifyIndex = 0;//指定牌之位置
				int targetIndex = pos * 13;// 玩家第一张牌位置
				int targetId = playerCardAssign[targetIndex];// 这张牌id
				int tmpId = 0;
				int tmpIndex = 0;
				
				for(int j=0;j<chessRoomPlayer[pos].trickCardSequence.length;j++){				
					specifyId = chessRoomPlayer[pos].trickCardSequence[j];
					specifyIndex = playerCardAssignIndex[specifyId];
									
					targetId = playerCardAssign[targetIndex];// 这张牌后面 一张牌 id

					tmpId = playerCardAssign[specifyIndex];
					playerCardAssign[specifyIndex] = targetId;// 开始交换牌
					playerCardAssign[targetIndex] = tmpId;
					tmpIndex = playerCardAssignIndex[specifyId];
					playerCardAssignIndex[specifyId] = targetIndex;// 更新索引
					playerCardAssignIndex[targetId] = tmpIndex;
				
					targetIndex++;// 玩家牌索引递增
				}
				chessRoomPlayer[pos].trickNum--;
			}
		}
		ChessUtils.chessLog.info( "CardSequence:"+Arrays.toString(playerCardAssign));
	}

	private void cardRandomStraightFlush() {

		// ChessUtils.chessLog.info("============="+Arrays.toString(playerCardAssign));
		// 洗牌分牌
		int random = (int) Math.floor((cardNum - 16) * Math.random());// 随机抽一张牌
																		// id

		int frontId = random;// 这张牌 id
		int frontIndex = playerCardAssignIndex[frontId];
		int nextIndex = ++frontIndex % 51;
		int nextId = playerCardAssign[nextIndex];// 这张牌后面 一张牌 id
		ChessUtils.chessLog.info("=============:" + frontId + "_" + frontIndex % 51);
		int targetId = frontId + 4;
		int targetIndex = playerCardAssignIndex[targetId];// 这张牌 同花顺的 下张牌 的索引
		playerCardAssign[nextIndex] = targetId;// 开始交换牌
		playerCardAssign[targetIndex] = nextId;
		playerCardAssignIndex[targetId] = nextIndex;// 更新索引
		playerCardAssignIndex[nextId] = targetIndex;

		for (int i = 1; i < 4; i++) {
			frontId = targetId;// 这张牌 id
			frontIndex = playerCardAssignIndex[frontId];
			nextIndex = ++frontIndex % 51;
			nextId = playerCardAssign[nextIndex];// 这张牌后面 一张牌 id
			ChessUtils.chessLog.info("=============:" + frontId + "_" + frontIndex % 51);
			targetId = frontId + 4;
			targetIndex = playerCardAssignIndex[targetId];// 这张牌 同花顺的 下张牌 的索引
			playerCardAssign[nextIndex] = targetId;// 开始交换牌
			playerCardAssign[targetIndex] = nextId;
			playerCardAssignIndex[targetId] = nextIndex;// 更新索引
			playerCardAssignIndex[nextId] = targetIndex;
			// ChessUtils.chessLog.info("============="+Arrays.toString(playerCardAssign));
		}

	}

	// 清龙
	private void cardRandomAllStraightFlush() {
		int specialId = 0;// 清龙第一张牌 id
		int specialIndex = playerCardAssignIndex[specialId];
		int targetIndex = 0;// 玩家随机第一张牌位置
		int targetId = playerCardAssign[targetIndex];// 这张牌 id

		int tmpId = 0;
		int tmpIndex = 0;
		tmpId = playerCardAssign[specialIndex];
		playerCardAssign[specialIndex] = targetId;// 开始交换牌
		playerCardAssign[targetIndex] = tmpId;
		tmpIndex = playerCardAssignIndex[specialId];
		playerCardAssignIndex[specialId] = targetIndex;// 更新索引
		playerCardAssignIndex[targetId] = tmpIndex;

		for (int i = 0; i < 12; i++) {
			specialId += 4;// 清龙递增span
			specialIndex = playerCardAssignIndex[specialId];
			targetIndex++;// 索引递增
			targetId = playerCardAssign[targetIndex];// 这张牌后面 一张牌 id

			tmpId = playerCardAssign[specialIndex];
			playerCardAssign[specialIndex] = targetId;// 开始交换牌
			playerCardAssign[targetIndex] = tmpId;
			tmpIndex = playerCardAssignIndex[specialId];
			playerCardAssignIndex[specialId] = targetIndex;// 更新索引
			playerCardAssignIndex[targetId] = tmpIndex;
		}
	}

	// 三分天下
	private void cardRandomTripleFourOfAKind(int specialId) {
		// int specialId = 0;// 三分天下第一张牌id
		int specialIndex = playerCardAssignIndex[specialId];
		int targetIndex = (int) Math.floor(4 * Math.random()) * 13;// 玩家第一张牌位置
		int targetId = playerCardAssign[targetIndex];// 这张牌id

		int tmpId = 0;
		int tmpIndex = 0;
		tmpId = playerCardAssign[specialIndex];
		playerCardAssign[specialIndex] = targetId;// 开始交换牌
		playerCardAssign[targetIndex] = tmpId;
		tmpIndex = playerCardAssignIndex[specialId];
		playerCardAssignIndex[specialId] = targetIndex;// 更新索引
		playerCardAssignIndex[targetId] = tmpIndex;

		for (int i = 0; i < 11; i++) {
			specialId++;// 三分天下 递增span
			specialIndex = playerCardAssignIndex[specialId];
			targetIndex++;// 玩家牌索引递增
			targetId = playerCardAssign[targetIndex];// 这张牌后面 一张牌 id

			tmpId = playerCardAssign[specialIndex];
			playerCardAssign[specialIndex] = targetId;// 开始交换牌
			playerCardAssign[targetIndex] = tmpId;
			tmpIndex = playerCardAssignIndex[specialId];
			playerCardAssignIndex[specialId] = targetIndex;// 更新索引
			playerCardAssignIndex[targetId] = tmpIndex;
		}
	}

	// 全大
	private void cardRandomAllGreat() {
		int addSelf = 0;

		int specialId = 24;// 三分天下第一张牌id
		int specialIndex = playerCardAssignIndex[specialId];
		int targetIndex = 0;// 玩家第一张牌位置
		int targetId = playerCardAssign[targetIndex];// 这张牌id

		int tmpId = 0;
		int tmpIndex = 0;
		tmpId = playerCardAssign[specialIndex];
		playerCardAssign[specialIndex] = targetId;// 开始交换牌
		playerCardAssign[targetIndex] = tmpId;
		tmpIndex = playerCardAssignIndex[specialId];
		playerCardAssignIndex[specialId] = targetIndex;// 更新索引
		playerCardAssignIndex[targetId] = tmpIndex;

		for (int i = 0; i < 12; i++) {
			specialId += ++addSelf % 2 + 1;// 三分天下 递增span
			specialIndex = playerCardAssignIndex[specialId];
			targetIndex++;// 玩家牌索引递增
			targetId = playerCardAssign[targetIndex];// 这张牌后面 一张牌 id

			tmpId = playerCardAssign[specialIndex];
			playerCardAssign[specialIndex] = targetId;// 开始交换牌
			playerCardAssign[targetIndex] = tmpId;
			tmpIndex = playerCardAssignIndex[specialId];
			playerCardAssignIndex[specialId] = targetIndex;// 更新索引
			playerCardAssignIndex[targetId] = tmpIndex;
		}
	}

	// 6对半
	private void cardRandomSixPairs() {
		int addSelf = 1;

		int specialId = 12;// 6对半 第一张牌id
		int specialIndex = playerCardAssignIndex[specialId];
		int targetIndex = 0;// 玩家第一张牌位置
		int targetId = playerCardAssign[targetIndex];// 这张牌id

		int tmpId = 0;
		int tmpIndex = 0;
		tmpId = playerCardAssign[specialIndex];
		playerCardAssign[specialIndex] = targetId;// 开始交换牌
		playerCardAssign[targetIndex] = tmpId;
		tmpIndex = playerCardAssignIndex[specialId];
		playerCardAssignIndex[specialId] = targetIndex;// 更新索引
		playerCardAssignIndex[targetId] = tmpIndex;

		for (int i = 0; i < 12; i++) {
			specialId += ++addSelf % 3 + 1;// 6对半 递增span
			specialIndex = playerCardAssignIndex[specialId];
			targetIndex++;// 玩家牌索引递增
			targetId = playerCardAssign[targetIndex];// 这张牌后面 一张牌 id

			tmpId = playerCardAssign[specialIndex];
			playerCardAssign[specialIndex] = targetId;// 开始交换牌
			playerCardAssign[targetIndex] = tmpId;
			tmpIndex = playerCardAssignIndex[specialId];
			playerCardAssignIndex[specialId] = targetIndex;// 更新索引
			playerCardAssignIndex[targetId] = tmpIndex;
		}
	}

	// 一条龙
	private void cardRandomAllStraight() {

		int specialId = 0;// 6对半 第一张牌id
		int specialIndex = playerCardAssignIndex[specialId];
		int targetIndex = 0;// 玩家第一张牌位置
		int targetId = playerCardAssign[targetIndex];// 这张牌id

		int tmpId = 0;
		int tmpIndex = 0;
		tmpId = playerCardAssign[specialIndex];
		playerCardAssign[specialIndex] = targetId;// 开始交换牌
		playerCardAssign[targetIndex] = tmpId;
		tmpIndex = playerCardAssignIndex[specialId];
		playerCardAssignIndex[specialId] = targetIndex;// 更新索引
		playerCardAssignIndex[targetId] = tmpIndex;

		for (int i = 0; i < 12; i++) {
			specialId += ((specialId % 4) == 3 ? 1 : 5);// 6对半 递增span
			specialIndex = playerCardAssignIndex[specialId];
			targetIndex++;// 玩家牌索引递增
			targetId = playerCardAssign[targetIndex];// 这张牌后面 一张牌 id

			tmpId = playerCardAssign[specialIndex];
			playerCardAssign[specialIndex] = targetId;// 开始交换牌
			playerCardAssign[targetIndex] = tmpId;
			tmpIndex = playerCardAssignIndex[specialId];
			playerCardAssignIndex[specialId] = targetIndex;// 更新索引
			playerCardAssignIndex[targetId] = tmpIndex;
		}
	}

	protected void cardAssign() {
		// 分牌
		for (int i = 0; i < chessRoomPlayer.length; i++) {
			// 按默认发牌顺序发牌
			for (int j = 0; j < playerCardNum; j++) {
				// ChessUtils.chessLog.info(i +"_"+ j+"_"+(j + playerCardNum *
				// i));
				chessRoomPlayer[i].playerCardSequence[j] = playerCardAssign[j + playerCardNum * i];
			}
		}
	}

	/**
	 * 第一墩牌
	 * 
	 */
	private void cardAnalysis3(int pos) {
		// int[] playerCard = {0,18,30};

		int[] playerCard = chessRoomPlayer[pos].playerCardSequence1;

		// 排序
		Arrays.sort(playerCard);

		// 权值
		int power1 = 0;// 单张
		int power2 = 0;

		if (cardPoint[playerCard[0]] == cardPoint[playerCard[1]]) {
			if (cardPoint[playerCard[2]] == cardPoint[playerCard[1]]) {
				// 3冲三
				chessRoomPlayer[pos].cardType = 3;
				power1 = cardPoint[playerCard[0]];
			} else {
				// 1一对
				chessRoomPlayer[pos].cardType = 1;
				power1 = cardPoint[playerCard[2]];// 单张点数
				power2 = cardPoint[playerCard[0]] * powerLevelup;
			}
		} else {
			if (cardPoint[playerCard[2]] == cardPoint[playerCard[1]]) {
				// 1一对
				chessRoomPlayer[pos].cardType = 1;
				power1 = cardPoint[playerCard[0]];// 单张点数
				power2 = cardPoint[playerCard[1]] * powerLevelup;
			}
		}
		// 乌龙
		if (0 == chessRoomPlayer[pos].cardType) {
			power1 = cardPoint[playerCard[1]] * this.powerLevelup2 + cardPoint[playerCard[0]] * this.powerLevelup;
			power2 = cardPoint[playerCard[2]] * this.powerLevelup3;
		}

		// 特殊牌顺同花
		if (cardPoint[playerCard[0]] + 1 == cardPoint[playerCard[1]]) {
			if (cardPoint[playerCard[1]] + 1 == cardPoint[playerCard[2]]) {
				chessRoomPlayer[pos].cardType_ = ChessCardTypes.STRAIGHT3;
			}
		}
		//A23
		if(cardPoint[playerCard[0]] == 1){
			if(cardPoint[playerCard[1]] == 2){
				if(cardPoint[playerCard[2]] == 13){
					chessRoomPlayer[pos].cardType_ = ChessCardTypes.STRAIGHT3;
				}
			}
		}
		if (cardPattern[playerCard[0]] == cardPattern[playerCard[1]]) {
			if (cardPattern[playerCard[0]] == cardPattern[playerCard[2]]) {
				if (chessRoomPlayer[pos].cardType_ == ChessCardTypes.STRAIGHT3) {
					chessRoomPlayer[pos].cardType_ = ChessCardTypes.STRAIGHT_FLUSH3;
				} else {
					chessRoomPlayer[pos].cardType_ = ChessCardTypes.FLUSH3;
				}
			}
		}

		chessRoomPlayer[pos].cardPower = power1 + power2;
		/*
		 * 
		 * ChessUtils.chessLog.info(playerCard.length);
		 * ChessUtils.chessLog.info(card[0]);
		 * ChessUtils.chessLog.info(Arrays.toString(cardPower));
		 * ChessUtils.chessLog.info(cardPower.length);
		 * 
		 * 
		 * ChessUtils.chessLog.info(theSame.get(1));
		 * 
		 * // ChessUtils.chessLog.info(Arrays.toString(cardPattern)); //
		 * ChessUtils.chessLog.info(Arrays.toString(_cardPoint)); //
		 * ChessUtils.chessLog.info(Arrays.toString(playerCard));
		 * 
		 * ChessUtils.chessLog.info(flush); ChessUtils.chessLog.info(straight);
		 */

		ChessUtils.chessLog.info(Arrays.toString(playerCard));
		ChessUtils.chessLog.info(chessRoomPlayer[pos].cardType);
		ChessUtils.chessLog.info(chessRoomPlayer[pos].cardType_);
		ChessUtils.chessLog.info(chessRoomPlayer[pos].cardPower + "\n");
	}

	/**
	 * @param pos
	 *            玩家位置
	 * @param n
	 *            牌墩
	 * @param playerCard
	 *            玩家的牌
	 */
	private void cardAnalysis5(int pos, int n, int[] playerCard) {

		// 排序
		Arrays.sort(playerCard);

		// type牌型
		int type = 0;

		// 顺子Straight
		// 同花Flush
		boolean straight = true;
		boolean flush = true;

		// 权值
		int power = 0;

		int[] pairs = new int[4];

		// 相同点数记录
		for (int i = 1; i < playerCard.length; ++i) {
			// 相同点数记录
			int last = i-1;
			if (cardPoint[playerCard[i]] == cardPoint[playerCard[last]]) {
				pairs[last] = cardPoint[playerCard[i]];
				type = 1;
			}
			// 花样不同
			if (cardPattern[playerCard[i]] != cardPattern[playerCard[last]]) {
				flush = false;
			}
			// 非顺
			if (cardPoint[playerCard[i]] != cardPoint[playerCard[last]] + 1) {
				straight = false;
			}
		}

		// 一对
		if (1 == type) {
			// 对子在最后
			if(pairs[0]>0){//一对 i+1,+2,+3
				power = cardPoint[playerCard[2]] + cardPoint[playerCard[3]] * powerLevelup2 + cardPoint[playerCard[4]] * powerLevelup3 + pairs[0] * powerLevelup4;
			}else if(pairs[1]>0){
				power = cardPoint[playerCard[0]] + cardPoint[playerCard[3]] * powerLevelup2 + cardPoint[playerCard[4]] * powerLevelup3 + pairs[1] * powerLevelup4;
			}else if(pairs[2]>0){
				power = cardPoint[playerCard[0]] + cardPoint[playerCard[1]] * powerLevelup2 + cardPoint[playerCard[4]] * powerLevelup3 + pairs[2] * powerLevelup4;
			}else if(pairs[3]>0){
				power = cardPoint[playerCard[0]] + cardPoint[playerCard[1]] * powerLevelup2 + cardPoint[playerCard[2]] * powerLevelup3 + pairs[3] * powerLevelup4;
			}

			// 两对
			if ((pairs[0] * pairs[3]) > 0) {
				power = cardPoint[playerCard[2]] + pairs[0] * powerLevelup + pairs[3] * powerLevelup2;
				type = ChessCardTypes.DUAL_PAIR;
			}
			if ((pairs[1] * pairs[3]) > 0) {
				power = cardPoint[playerCard[0]] + pairs[1] * powerLevelup + pairs[3] * powerLevelup2;
				type = ChessCardTypes.DUAL_PAIR;
			}
			if ((pairs[0] * pairs[2]) > 0) {
				power = cardPoint[playerCard[4]] + pairs[0] * powerLevelup + pairs[2] * powerLevelup2;
				type = ChessCardTypes.DUAL_PAIR;
			}
			// 三条
			if ((pairs[1] * pairs[2]) > 0) {
				power = pairs[1] * powerLevelup;
				type = ChessCardTypes.THREE_ONLY;
			}
			if ((pairs[2] * pairs[3]) > 0) {
				power = pairs[2] * powerLevelup;
				type = ChessCardTypes.THREE_ONLY;
			}
			if ((pairs[0] * pairs[1]) > 0) {
				power = pairs[0] * powerLevelup;
				type = ChessCardTypes.THREE_ONLY;
			}
			// 葫芦
			if ((pairs[0] * pairs[1] * pairs[3]) > 0) {
				power = pairs[3] + pairs[0] * powerLevelup;
				type = ChessCardTypes.THREE_OF_AKIND;
			}
			if ((pairs[0] * pairs[2] * pairs[3]) > 0) {
				power = pairs[0] + pairs[3] * powerLevelup;
				type = ChessCardTypes.THREE_OF_AKIND;
			}

			// 铁支-连续四张为对子
			if ((pairs[0] * pairs[1] * pairs[2]) > 0) {
				power = cardPoint[playerCard[4]] + pairs[0] * powerLevelup;
				type = ChessCardTypes.FOUR_OF_AKIND;
			}
			if ((pairs[1] * pairs[2] * pairs[3]) > 0) {
				power = cardPoint[playerCard[0]] + pairs[1] * powerLevelup;
				type = ChessCardTypes.FOUR_OF_AKIND;
			}
		}

		// 特殊的顺子,2,3,4,5,A
		if (cardPoint[playerCard[0]] == 1) {
			if (cardPoint[playerCard[1]] == 2) {
				if (cardPoint[playerCard[2]] == 3) {
					if (cardPoint[playerCard[3]] == 4) {
						if (cardPoint[playerCard[4]] == 13) {
							straight = true;
						}
					}
				}
			}
		}

		// 4顺
		// 5同花
		// 8同花顺
		if (straight) {
			type = ChessCardTypes.STRAIGHT;
			power = cardPoint[playerCard[3]] + cardPoint[playerCard[4]] * powerLevelup;
		} else if (flush) {
			type = ChessCardTypes.FLUSH;
			power = cardPoint[playerCard[0]] + cardPoint[playerCard[1]] * powerLevelup + cardPoint[playerCard[2]] * powerLevelup2 + cardPoint[playerCard[3]] * powerLevelup3 + cardPoint[playerCard[4]] * powerLevelup4;
		}
		if (straight && flush) {
			type = ChessCardTypes.STRAIGHT_FLUSH;
			power = cardPoint[playerCard[3]] + cardPoint[playerCard[4]] * powerLevelup;
		}

		// 乌龙
		if (0 == type) {
			power = cardPoint[playerCard[0]] + cardPoint[playerCard[1]] * powerLevelup + cardPoint[playerCard[2]] * powerLevelup2 + cardPoint[playerCard[3]] * powerLevelup3 + cardPoint[playerCard[4]] * powerLevelup4;
		}

		if (2 == n) {
			chessRoomPlayer[pos].cardPower2 = power;
			chessRoomPlayer[pos].cardType2 = type;
		} else if (3 == n) {
			chessRoomPlayer[pos].cardPower3 = power;
			chessRoomPlayer[pos].cardType3 = type;
		}

		ChessUtils.chessLog.info(cardPoint[playerCard[0]] + "_" + cardPoint[playerCard[1]] + "_" + cardPoint[playerCard[2]] + "_" + cardPoint[playerCard[3]] + "_"
				+ cardPoint[playerCard[4]]);
		ChessUtils.chessLog.info(Arrays.toString(playerCard));
		ChessUtils.chessLog.info(Arrays.toString(pairs));
		ChessUtils.chessLog.info(type);
		ChessUtils.chessLog.info(power + "\n");

	}

	private boolean cardAnalysisSpecial(ChessRoomPlayer chessRoomPlayer) {

		int i_four = 0;
		int i_fourNum = 0;
		int i_three = 0;
		int i_threeNum = 0;
		// int i_two = 0;
		int i_twoNum = 0;
		boolean the_same_color = true;
		boolean all_small = true;
		boolean all_great = true;
		boolean all_straight = true;
		boolean all_straight_flush = true;
		boolean thirteen_nobles = true;

		// 排序
		Arrays.sort(chessRoomPlayer.playerCardSequence);

		for (int i = 0; i < chessRoomPlayer.playerCardSequence.length; ++i) {
			// 铁支
			if (i < chessRoomPlayer.playerCardSequence.length - 3) {// i为倒数第4张
				if (cardPoint[chessRoomPlayer.playerCardSequence[i + 1]] == cardPoint[chessRoomPlayer.playerCardSequence[i]]) {// 前后两张牌点数相同
					if (cardPoint[chessRoomPlayer.playerCardSequence[i + 2]] == cardPoint[chessRoomPlayer.playerCardSequence[i]]) {
						if (cardPoint[chessRoomPlayer.playerCardSequence[i + 3]] == cardPoint[chessRoomPlayer.playerCardSequence[i]]) {
							i_four = cardPoint[chessRoomPlayer.playerCardSequence[i]];
							// 统计所有四条
							i_fourNum++;
						}
					}
				}
			}
			// 三条
			if (i < chessRoomPlayer.playerCardSequence.length - 2) {// i为倒数第3张
				if (cardPoint[chessRoomPlayer.playerCardSequence[i + 1]] == cardPoint[chessRoomPlayer.playerCardSequence[i]]) {// 前后两张牌点数相同
					if (cardPoint[chessRoomPlayer.playerCardSequence[i + 2]] == cardPoint[chessRoomPlayer.playerCardSequence[i]]) {
						if (i_four != cardPoint[chessRoomPlayer.playerCardSequence[i]]) {// 排除4条
							i_three = cardPoint[chessRoomPlayer.playerCardSequence[i]];
							// 统计所有三条
							i_threeNum++;
						}
					}
				}
			}
			// 最大一对
			if (i < chessRoomPlayer.playerCardSequence.length - 1) {// i为倒数第2张
				if (cardPoint[chessRoomPlayer.playerCardSequence[i + 1]] == cardPoint[chessRoomPlayer.playerCardSequence[i]]) {// 前后两张牌点数相同
					if (i_four != cardPoint[chessRoomPlayer.playerCardSequence[i]] && i_three != cardPoint[chessRoomPlayer.playerCardSequence[i]]) {
						// i_two =
						// cardPoint[chessRoomPlayer.playerCardSequence[i]];
						// 统计所有对子
						i_twoNum++;
					}
				}

				// 凑一色
				if ((cardPattern[chessRoomPlayer.playerCardSequence[i]] & ChessCardTypes.DIAMOND_HEART) > 0) {
					if ((cardPattern[chessRoomPlayer.playerCardSequence[i + 1]] & ChessCardTypes.DIAMOND_HEART) == 0) {
						the_same_color = false;
					}
				} else if ((cardPattern[chessRoomPlayer.playerCardSequence[i]] & ChessCardTypes.CLUB_SPADE) > 0) {
					if ((cardPattern[chessRoomPlayer.playerCardSequence[i + 1]] & ChessCardTypes.CLUB_SPADE) == 0) {
						the_same_color = false;
					}
				}

				// 一条龙 //清龙
				if (cardPoint[chessRoomPlayer.playerCardSequence[i + 1]] != cardPoint[chessRoomPlayer.playerCardSequence[i]] + 1) {
					all_straight_flush = false;
					all_straight = false;
				}

				if (cardPattern[chessRoomPlayer.playerCardSequence[i + 1]] != cardPattern[chessRoomPlayer.playerCardSequence[i]]) {
					all_straight_flush = false;
				}

			}// if 前后两张牌

			// 全<=8(7)
			if (cardPoint[chessRoomPlayer.playerCardSequence[i]] > 7) {
				all_small = false;
			}
			// 全>=8(7)
			if (cardPoint[chessRoomPlayer.playerCardSequence[i]] < 7) {
				all_great = false;
			}
			// 全贵族>=j(10)
			if (cardPoint[chessRoomPlayer.playerCardSequence[i]] < 10) {
				thirteen_nobles = false;
			}
		}// for

		switch (chessRoomPlayer.cardSpecialType) {
		case ChessCardTypes.ALL_STRAIGHT_FLUSH:
			if (all_straight_flush) {
				return true;
			}
			break;
		case ChessCardTypes.ALL_STRAIGHT:
			if (all_straight) {
				return true;
			}
			break;
		case ChessCardTypes.THIRTEEN_NOBLES:
			if (thirteen_nobles) {
				return true;
			}
			break;
		case ChessCardTypes.TRIPLE_STRAIGHT_FLUSH:
			// 三同花顺
			ChessUtils.chessLog.info("TRIPLE_STRAIGHT_FLUSH_" + chessRoomPlayer.cardType_ + "_" + chessRoomPlayer.cardType2 + "_" + chessRoomPlayer.cardType3);
			if ((chessRoomPlayer.cardType_ == ChessCardTypes.STRAIGHT_FLUSH3) && chessRoomPlayer.cardType2 == ChessCardTypes.STRAIGHT_FLUSH
					&& chessRoomPlayer.cardType3 == ChessCardTypes.STRAIGHT_FLUSH) {
				return true;
			}
			break;
		case ChessCardTypes.TRIPLE_FOUR_OF_AKIND:
			if (i_fourNum == 3) {
				return true;
			}
			break;
		case ChessCardTypes.ALL_GREAT:
			if (all_great) {
				return true;
			}
			break;
		case ChessCardTypes.ALL_SMALL:
			if (all_small) {
				return true;
			}
			break;
		case ChessCardTypes.THE_SAME_COLOR:
			if (the_same_color) {
				return true;
			}
			break;
		case ChessCardTypes.FOUR_THREE_OF_AKIND:
			if (i_threeNum == 4) {
				return true;
			}
			break;
		case ChessCardTypes.FIVE_PAIRS_N_THREE_OF_AKIND:
			if (i_twoNum + i_fourNum*2 == 5 && i_threeNum == 1) {
				return true;
			}
			break;
		case ChessCardTypes.SIX_PAIRS:
			if (i_twoNum + i_fourNum*2 == 6) {
				return true;
			}
			break;
		case ChessCardTypes.TRIPLE_STRAIGHT:
			// 三顺
			if (((chessRoomPlayer.cardType_ & ChessCardTypes.STRAIGHT3) > 0) ){
				if( chessRoomPlayer.cardType2 == ChessCardTypes.STRAIGHT_FLUSH){
					if( chessRoomPlayer.cardType3 == ChessCardTypes.STRAIGHT ||  chessRoomPlayer.cardType3 == ChessCardTypes.STRAIGHT_FLUSH) {
						return true;
					}
				}
				if( chessRoomPlayer.cardType2 == ChessCardTypes.STRAIGHT){
					if( chessRoomPlayer.cardType3 == ChessCardTypes.STRAIGHT ||  chessRoomPlayer.cardType3 == ChessCardTypes.STRAIGHT_FLUSH) {
						return true;
					}
				}

			}
			break;
		case ChessCardTypes.TRIPLE_FLUSH:
			// 三同花
			if (((chessRoomPlayer.cardType_ & ChessCardTypes.FLUSH3) > 0) ){
				if( chessRoomPlayer.cardType2 == ChessCardTypes.STRAIGHT_FLUSH){
					if( chessRoomPlayer.cardType3 == ChessCardTypes.FLUSH ||  chessRoomPlayer.cardType3 == ChessCardTypes.STRAIGHT_FLUSH) {
						return true;
					}
				}
				if( chessRoomPlayer.cardType2 == ChessCardTypes.FLUSH){
					if( chessRoomPlayer.cardType3 == ChessCardTypes.FLUSH ||  chessRoomPlayer.cardType3 == ChessCardTypes.STRAIGHT_FLUSH) {
						return true;
					}
				}

			}
			break;
		default:
			return false;
		}
		return false;
	}

	/**
	 * @param i
	 *            第一位玩家
	 * @param j
	 *            第二位玩家
	 */
	private void cardCompare(int i, int j) {

		// i大+ j大- 打枪;
		int gunShot = 3;
		int score = 0;
		// 怪物牌分
		int iCreepVal = 0;
		int jCreepVal = 0;

		// 第一墩 牌型
		iCreepVal = getCreepyVal(1, chessRoomPlayer[i].cardType);
		jCreepVal = getCreepyVal(1, chessRoomPlayer[j].cardType);
		if (chessRoomPlayer[i].cardType < chessRoomPlayer[j].cardType) {
			chessRoomPlayer[i].cardCreep1[j] = -jCreepVal;
			chessRoomPlayer[i].cardScore1[j] = -1;
			chessRoomPlayer[j].cardCreep1[i] = jCreepVal;
			chessRoomPlayer[j].cardScore1[i] = 1;
			gunShot--;
		} else if (chessRoomPlayer[i].cardType == chessRoomPlayer[j].cardType) {
			// 相同牌型下的权值
			if (chessRoomPlayer[i].cardPower < chessRoomPlayer[j].cardPower) {
				chessRoomPlayer[i].cardCreep1[j] = -jCreepVal;
				chessRoomPlayer[i].cardScore1[j] = -1;
				chessRoomPlayer[j].cardCreep1[i] = jCreepVal;
				chessRoomPlayer[j].cardScore1[i] = 1;
				gunShot--;
			} else if (chessRoomPlayer[i].cardPower == chessRoomPlayer[j].cardPower) {
				// 打平
				ChessUtils.chessLog.info("1打平");
			} else {
				chessRoomPlayer[i].cardCreep1[j] = iCreepVal;
				chessRoomPlayer[i].cardScore1[j] = 1;
				chessRoomPlayer[j].cardCreep1[i] = -iCreepVal;
				chessRoomPlayer[j].cardScore1[i] = -1;
				gunShot++;
			}
		} else {
			chessRoomPlayer[i].cardCreep1[j] = iCreepVal;
			chessRoomPlayer[i].cardScore1[j] = 1;
			chessRoomPlayer[j].cardCreep1[i] = -iCreepVal;
			chessRoomPlayer[j].cardScore1[i] = -1;
			gunShot++;
		}

		// 第二墩 牌型
		iCreepVal = getCreepyVal(2, chessRoomPlayer[i].cardType2);
		jCreepVal = getCreepyVal(2, chessRoomPlayer[j].cardType2);
		if (chessRoomPlayer[i].cardType2 < chessRoomPlayer[j].cardType2) {
			chessRoomPlayer[i].cardCreep2[j] = -jCreepVal;
			chessRoomPlayer[i].cardScore2[j] = -1;
			chessRoomPlayer[j].cardCreep2[i] = jCreepVal;
			chessRoomPlayer[j].cardScore2[i] = 1;
			gunShot--;
		} else if (chessRoomPlayer[i].cardType2 == chessRoomPlayer[j].cardType2) {
			// 相同牌型下的权值
			if (chessRoomPlayer[i].cardPower2 < chessRoomPlayer[j].cardPower2) {
				chessRoomPlayer[i].cardCreep2[j] = -jCreepVal;
				chessRoomPlayer[i].cardScore2[j] = -1;
				chessRoomPlayer[j].cardCreep2[i] = jCreepVal;
				chessRoomPlayer[j].cardScore2[i] = 1;
				gunShot--;
			} else if (chessRoomPlayer[i].cardPower2 == chessRoomPlayer[j].cardPower2) {
				// 打平
				ChessUtils.chessLog.info("2打平");
			} else {
				chessRoomPlayer[i].cardCreep2[j] = iCreepVal;
				chessRoomPlayer[i].cardScore2[j] = 1;
				chessRoomPlayer[j].cardCreep2[i] = -iCreepVal;
				chessRoomPlayer[j].cardScore2[i] = -1;
				gunShot++;
			}
		} else {
			chessRoomPlayer[i].cardCreep2[j] = iCreepVal;
			chessRoomPlayer[i].cardScore2[j] = 1;
			chessRoomPlayer[j].cardCreep2[i] = -iCreepVal;
			chessRoomPlayer[j].cardScore2[i] = -1;
			gunShot++;
		}

		// 第三墩 牌型
		iCreepVal = getCreepyVal(3, chessRoomPlayer[i].cardType3);
		jCreepVal = getCreepyVal(3, chessRoomPlayer[j].cardType3);
		if (chessRoomPlayer[i].cardType3 < chessRoomPlayer[j].cardType3) {
			chessRoomPlayer[i].cardCreep3[j] = -jCreepVal;
			chessRoomPlayer[i].cardScore3[j] = -1;
			chessRoomPlayer[j].cardCreep3[i] = jCreepVal;
			chessRoomPlayer[j].cardScore3[i] = 1;
			gunShot--;
		} else if (chessRoomPlayer[i].cardType3 == chessRoomPlayer[j].cardType3) {
			// 相同牌型下的权值
			if (chessRoomPlayer[i].cardPower3 < chessRoomPlayer[j].cardPower3) {
				chessRoomPlayer[i].cardCreep3[j] = -jCreepVal;
				chessRoomPlayer[i].cardScore3[j] = -1;
				chessRoomPlayer[j].cardCreep3[i] = jCreepVal;
				chessRoomPlayer[j].cardScore3[i] = 1;
				gunShot--;
			} else if (chessRoomPlayer[i].cardPower3 == chessRoomPlayer[j].cardPower3) {
				// 打平
				ChessUtils.chessLog.info("3打平");
			} else {
				chessRoomPlayer[i].cardCreep3[j] = iCreepVal;
				chessRoomPlayer[i].cardScore3[j] = 1;
				chessRoomPlayer[j].cardCreep3[i] = -iCreepVal;
				chessRoomPlayer[j].cardScore3[i] = -1;
				gunShot++;
			}
		} else {
			chessRoomPlayer[i].cardCreep3[j] = iCreepVal;
			chessRoomPlayer[i].cardScore3[j] = 1;
			chessRoomPlayer[j].cardCreep3[i] = -iCreepVal;
			chessRoomPlayer[j].cardScore3[i] = -1;
			gunShot++;
		}

		// i对j的分数
		score = chessRoomPlayer[i].cardCreep1[j] + chessRoomPlayer[i].cardCreep2[j] + chessRoomPlayer[i].cardCreep3[j] + chessRoomPlayer[i].cardScore1[j]
				+ chessRoomPlayer[i].cardScore2[j] + chessRoomPlayer[i].cardScore3[j];
		// 记分
		chessRoomPlayer[i].cardScoreSum += score;
		chessRoomPlayer[j].cardScoreSum -= score;
		if (gunShot == 6) {// i对j打枪
			chessRoomPlayer[i].cardScoreSum += score;
			chessRoomPlayer[j].cardScoreSum -= score;
			chessRoomPlayer[i].shotPosition[j] = 1;
			chessRoomPlayer[i].shotNum++;
		} else if (gunShot == 0) {// i被j打枪
			chessRoomPlayer[i].cardScoreSum += score;
			chessRoomPlayer[j].cardScoreSum -= score;
			chessRoomPlayer[j].shotPosition[i] = 1;
			chessRoomPlayer[j].shotNum++;
		}

		ChessUtils.chessLog.info(i + "<-player->" + j);
		ChessUtils.chessLog.info(chessRoomPlayer[i].cardScoreSum + "<-credit->" + chessRoomPlayer[j].cardScoreSum);
		// ChessUtils.chessLog.info(chessRoomPlayer[i].cardType
		// +"_"+chessRoomPlayer[j].cardType );
		// ChessUtils.chessLog.info(chessRoomPlayer[i].cardPower
		// +"_"+chessRoomPlayer[j].cardPower );
	}

	private int getCreepyVal(int index, int type) {
		if (1 == index && 3 == type) {
			return 2;// 冲三
		} else if (2 == index && 6 == type) {
			return 1;// 中墩葫芦
		} else if (2 == index && 7 == type) {
			return 6;// 中墩铁支
		} else if (3 == index && 7 == type) {
			return 3;// 末墩铁支
		} else if (2 == index && 8 == type) {
			return 8;// 中墩同花顺
		} else if (3 == index && 8 == type) {
			return 5;// 末墩同花顺
		}

		return 0;
	}

	@Override
	public int getMoneyLimitation() {
		return moneyLimitation;
	}

	@Override
	public int getRateOfExchange() {
		return rateOfExchange;
	}

	@Override
	public int getComparisonTime() {
		return comparisonTime;
	}

	@Override
	public int getRoomState() {
		if (roomState == States.WAIT4COMPARE) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int getPlayerNum(){
		return playerNum;
	}
	
	@Override
	public long getChessRoomPlayerId(int pos){
		return chessRoomPlayer[pos].playerId;
	}
	
	@Override
	public int getChessRoomPlayerNum(){
		return playerNum;
	}
	
	@Override
	public int getChessRoomPlayerState(int pos){
		return chessRoomPlayer[pos].playerState.state;
	}
	
	@Override
	public int getTax() {
		return tax;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setRate(int rate){
		this.rateOfExchange = rate;
	}

	@Override
	public boolean isIdle() {
		// TODO Auto-generated method stub
		return roomState == States.IDLE;
	}

	




}
