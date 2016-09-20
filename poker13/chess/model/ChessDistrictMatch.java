package com.chitu.chess.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.time.DateUtils;

import com.chitu.chess.data.StaticMatch;
import com.chitu.chess.data.StaticStrings;
import com.chitu.chess.msg.ChessPlayerMatchRankListDto;
import com.chitu.chess.msg.ChessPlayerMatchRankMDto;
import com.chitu.chess.msg.ChessSpeakerDto;
import com.chitu.chess.msg.RoomUserMatchDto;
import com.chitu.chess.msg.RoomUserMatchPrizeDto;
import com.chitu.chess.service.ChessPlayerManager;
import com.chitu.chess.service.ChessRoomManager;
import cn.gecko.broadcast.Channel;
import cn.gecko.broadcast.ChannelManager;
import cn.gecko.commons.data.BillType;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.timer.ScheduleManager;
import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.player.service.ChannelTalkRuleManager;

public class ChessDistrictMatch implements IChessDistrict {

	public ConcurrentHashMap<Long, ChessRoomMatch> district;
	private ConcurrentHashMap<Long, ChessRoomMatchPlayer> enrollId;// 1个
	private Map<ChessRoomMatchPlayer, Long> finalsRank;
	private ArrayList<Long> queue;// 有序队列
	private ArrayList<Long> rankQueue;// 有序队列
	private ArrayList<ChessPlayerMatchRankMDto> rank;

	private int districtId;

	private int roomSize = 4;// 4人一桌
	private int enrollMin = 3;// 最少报名人数
	private int enrollNum;// 报名人数
	private int participateNum;// 参赛人数
	private int hour;
	private int minute;
	private int dynamicRate;// 动态兑换率
	private int finals = 5;
	private int finalsCount;// 决赛计数
	private long restPlayerId;// 一局剩下的一个人

	public long endDate;
	public long deadline;
	private States state;

	private StaticMatch mConfig;

	protected ScheduleManager scheduleManager;

	public enum States {
		IDLE, ENROLLED, WAIT4START, START, END;
	}

	// 玩家状态
	public final int IDLE = 0;
	public final int INQUEUE = (Integer.valueOf("1", 2));
	public final int ABSENT = (Integer.valueOf("10", 2));
	public final int GIVEUP = (Integer.valueOf("100", 2));
	public final int ENTER = (Integer.valueOf("1000", 2));
	public final int INGAME = (Integer.valueOf("10000", 2));
	public final int BACKGAME = (Integer.valueOf("100000", 2));
	public final int GETPRIZE = (Integer.valueOf("1000000", 2));
	public final int KNOCKOUT = (Integer.valueOf("10000000", 2));
	public final int ENTER_INGAME = (Integer.valueOf("11000", 2));
	public final int INGAME_INQUEUE = (Integer.valueOf("10001", 2));
	public final int ENTER_INGAME_BACKGAME = (Integer.valueOf("111000", 2));
	public final int ENTER_ABSENT_INQUEUE = (Integer.valueOf("1011", 2));
	public final int ENTER_ABSENT_INGAME = (Integer.valueOf("11010", 2));
	public final int ENTER_INQUEUE = (Integer.valueOf("1001", 2));

	public ChessDistrictMatch(ScheduleManager scheduleManager, int districtId) {

		district = new ConcurrentHashMap<Long, ChessRoomMatch>();
		enrollId = new ConcurrentHashMap<Long, ChessRoomMatchPlayer>();
		queue = new ArrayList<Long>();
		rankQueue = new ArrayList<Long>();
		rank = new ArrayList<ChessPlayerMatchRankMDto>();
		finalsRank = new TreeMap<ChessRoomMatchPlayer, Long>();
		state = States.END;

		this.districtId = districtId;
		this.scheduleManager = scheduleManager;
	}

	public void start() {
		mConfig = StaticMatch.get(districtId);
		if (null == mConfig) {
			return;
		}

		hour = mConfig.getHour();
		minute = mConfig.getMinute();
		dynamicRate = mConfig.getInitRate();
		enrollNum = 0;
		endDate = 0;
		enrollMin = mConfig.getMinPlayer();

		Runnable chessRoomTimer = new ChessRoomTimer();
		scheduleManager.scheduleAtFixedRate(chessRoomTimer, DateUtils.MILLIS_PER_SECOND, DateUtils.MILLIS_PER_SECOND * 2);
	}

	public synchronized void addNpc(long playerId) {
		// npc eroll
		ChessRoomMatchPlayer p = new ChessRoomMatchPlayer();
		p.playerState = IDLE;
		p.counter = mConfig.getInitCounter();// fix me
		if (!enrollId.containsKey(playerId)) {// 防止重复报名
			enrollId.put(playerId, p);
			p.playerId = playerId;
			p.enrollOrder = ++enrollNum;
		}

		// enter
		p.playerState |= ENTER;
		p.playerState |= ABSENT;
		ChessUtils.chessLog.info("========addNpc:" + Integer.toString(p.playerState, 2));
	}

	public synchronized void assignPlayerRoom() {

		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);
		// 普通比赛判断
		if (participateNum > roomSize) {
			while (queue.size() >= roomSize) {
				ChessRoomMatch cr = new ChessRoomMatch(districtId);
				cr.init();
				cr.setRate(dynamicRate);
				cr.name = mConfig.getName();
				int pos = -1;// 房间有效玩家
				for (int i = 0; i < roomSize; i++) {
					if (enrollId.containsKey(queue.get(0))) {// 已经报名
						ChessRoomMatchPlayer p = enrollId.get(queue.get(0));
						ChessUtils.chessLog.info("========assignPlayerRoom:" + Integer.toString(p.playerState, 2));
						if ((p.playerState & ENTER) > 0) {
							// 如果不在线设为npc
							if ((p.playerState & ABSENT) > 0) {// 不在线玩家
								ChessUtils.chessLog.info("========npc:" + Integer.toString(p.playerState, 2));
								p.playerState = ENTER_ABSENT_INGAME;
								pos = cr.addNpcPlayer(queue.get(0));
							} else {
								ChessUtils.chessLog.info("========player:" + Integer.toString(p.playerState, 2));
								p.playerState = ENTER_INGAME;
								pos = cr.addPlayer(queue.get(0));
							}
						}
						cr.chessRoomPlayer[i].counter = p.counter;//
						chessRoomManager.setRoomId(queue.get(0), cr.id);// 玩家与房间关联
						queue.remove(0);
					}
				}
				if (pos > 0) {
					cr.start();
					district.put(cr.id, cr);
				}// else{
					// fix me 如果房间建立不成功,将已在房间玩家放回队列
				// }
			}
		}
		ChessUtils.chessLog.info("========assignPlayerRoom:" + queue.size());
	}

	/**
	 * 决赛分房
	 */
	public synchronized boolean assignFinalsRoom() {
		int size = queue.size();//
		
		if (size > roomSize) {
			ChessUtils.chessLog.info("========correct participateNum:" + participateNum +"_"+size);
			participateNum = size;//万一人数错误,修正之
			return true;
		}

		if (finalsCount >= finals) {
			// to do
			return false;
		}

		if (participateNum == 1) {
			// to do
			return false;
		}

		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);

		ChessRoomMatch cr = new ChessRoomMatch(districtId);
		cr.init();
		cr.setRate(dynamicRate);
		cr.setFinals(++finalsCount);
		cr.name = mConfig.getName();

		int pos = -1;// 房间有效玩家
		String finalsList = "";
		int i = size;
		while (--i >= 0) {
			long playerId = queue.get(i);
			if (enrollId.containsKey(playerId)) {// 已经报名
				ChessRoomMatchPlayer p = enrollId.get(playerId);
				ChessPlayer playerTmp = chessPlayerManager.getAnyPlayerById(playerId);
				finalsList += (playerTmp.accountId + "、");
				ChessUtils.chessLog.info("========assignFinalsRoom:" + Integer.toString(p.playerState, 2));
				if ((p.playerState & ENTER) > 0) {
					// 如果不在线设为npc
					if ((p.playerState & ABSENT) > 0) {// 不在线玩家
						ChessUtils.chessLog.info("========npc:" + Integer.toString(p.playerState, 2));
						p.playerState = ENTER_ABSENT_INGAME;
						pos = cr.addNpcPlayer(playerId);
						cr.chessRoomPlayer[pos].counter = p.counter;//
					} else {
						p.playerState = ENTER_INGAME;
						ChessUtils.chessLog.info("========player:" + Integer.toString(p.playerState, 2));
						pos = cr.addPlayer(playerId);
						cr.chessRoomPlayer[pos].counter = p.counter;//
					}
				}
				chessRoomManager.setRoomId(playerId, cr.id);// 玩家与房间关联
				queue.remove(i);
			}
		}

		restPlayerId = 0;
		if (pos > 0) {
			cr.start();
			district.put(cr.id, cr);
		}
		if (finalsCount == 1) {
			StaticStrings ss1 = StaticStrings.get(5005);
			StaticStrings ss2 = StaticStrings.get(5006);
			ChessSpeakerDto cs = new ChessSpeakerDto();
			cs.setMsg(ss1.getName() + finalsList + ss2.getName());
			cs.setCount(3);
			ChannelManager channelManager = SpringUtils.getBeanOfType(ChannelManager.class);
			Channel typeChannel = channelManager.getChannel(String.valueOf(districtId));
			typeChannel.broadcast(cs);
		}
		ChessUtils.chessLog.info("========assignFinalsRoom:" + queue.size());
		return true;
	}

	public synchronized void enrollMatch(ChessPlayer player) {

		if (state == States.IDLE) {// 比赛中不可加入

			if (enrollNum >= mConfig.getMaxPlayer()) {
				throw new GeneralException(ChessErrorCodes.REACH_MAX);
			}

			// 报名费
			StaticMatch sm = StaticMatch.get(districtId);
			int cost = sm.getCost();

			if (player.wealthHolder.getMoney() < cost) {
				throw new GeneralException(ChessErrorCodes.MONEY_NOT_ENOUGH);
			} else {
				player.wealthHolder.decreaseMoney(cost, 0, 0, BillType.get(ChessBillTypes.PAY), "");
			}

			ChessRoomMatchPlayer p = new ChessRoomMatchPlayer();
			if (!enrollId.containsKey(player.id)) {// 防止重复报名
				enrollId.put(player.id, p);
				p.playerId = player.id;
				p.playerState = IDLE;
				p.counter = mConfig.getInitCounter();
				// p.counter = p.playerId ==
				// 332306263352283136L?9999999:p.counter;// fix me
				p.enrollOrder = ++enrollNum;
				
				//加入区频道
				ChannelManager channelManager = SpringUtils.getBeanOfType(ChannelManager.class);
				ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
				Channel typeChannel = channelManager.getChannel(String.valueOf(districtId));
				chessPlayerManager.getAnyPlayerById(player.id).joinChannel(typeChannel.getName());
			}

		}
	}

	/**
	 * false 成功 ,true 失败
	 * 
	 * @param playerId
	 * @return
	 */
	public synchronized void enterMatch(long playerId) {
		if (enrollId.containsKey(playerId)) {//
			//加入区频道
			ChannelManager channelManager = SpringUtils.getBeanOfType(ChannelManager.class);
			ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
			Channel typeChannel = channelManager.getChannel(String.valueOf(districtId));
			chessPlayerManager.getAnyPlayerById(playerId).joinChannel(typeChannel.getName());
			
			ChessRoomMatchPlayer p = enrollId.get(playerId);
			ChessUtils.chessLog.info("========enterMatch:" + Integer.toString(p.playerState, 2));
			if (state == States.WAIT4START) {
				if ((p.playerState & INQUEUE) == 0) {// 不在队列玩家
					p.playerState = ENTER;
					p.playerState &= ~ABSENT;// 取消absent状态
				}
				return;
			} else if (state == States.START) {// 比赛中
				
				if((p.playerState & KNOCKOUT)>0){
					throw new GeneralException(ChessErrorCodes.MATCH_KNOCKOUT);
				}
				
				if ((p.playerState & ENTER) > 0) {
					if ((p.playerState & INGAME_INQUEUE) == 0) {// 不在队列,也不在比赛
						if (p.counter > 0) {
							queue.add(playerId);
							p.playerState |= INQUEUE;// 加入队列
						}
					} else if ((p.playerState & INGAME) == INGAME) {// 在上局比赛中
						p.playerState |= BACKGAME;// 回到比赛
					}
					p.playerState &= ~ABSENT;// 取消absent状态
					return;
				}else{
					throw new GeneralException(ChessErrorCodes.MATCH_ALREADYSTART);
				}

			}

			ChessUtils.chessLog.info("========enterMatch:" + Integer.toString(p.playerState, 2));
			throw new GeneralException(ChessErrorCodes.MATCH_UNAVAILABLE);
		}
		
		throw new GeneralException(ChessErrorCodes.MATCH_NOTENROLL);
	}

	/**
	 * 每局结束更新各玩家筹码
	 * 
	 * @param playerId
	 * @param counter
	 * @return
	 */
	public synchronized int updateCounter(long playerId, int counter, int score) {
		ChessUtils.chessLog.info("=======updateCounter=======");
		ChessUtils.chessLog.info("=======playerId=======:" + playerId + "_" + counter + "_restPlayerNum:" + participateNum);
		if (state == States.START) {// 比赛中不可加入
			if (enrollId.containsKey(playerId)) {//
				ChessRoomMatchPlayer p = enrollId.get(playerId);

				p.counter += counter;
				p.playerState &= ~INGAME;// 取消在比赛中状态
				// 筹码大于0
				if (p.counter > 0) {
					restPlayerId = playerId;
					if (finalsCount >= finals) {// 决赛结束
						finalsRank.put(p, playerId);
						return p.counter;
					}
					ChessUtils.chessLog.info("========playerState:" + Integer.toString(p.playerState, 2));
					if ((p.playerState & ENTER) > 0) {// 已进房
						if ((p.playerState & INQUEUE) == 0) {// 不在队列
							if ((p.playerState & ABSENT) > 0) {// 离开托管
								p.playerState = ENTER_ABSENT_INQUEUE;
								queue.add(playerId);
							} else if ((p.playerState & BACKGAME) > 0) {// 结束托管
								p.playerState = ENTER_INQUEUE;
								queue.add(playerId);
							}
						}
					}
					ChessUtils.chessLog.info("========playerState:" + Integer.toString(p.playerState, 2));
				} else {
					p.rank = participateNum--;
					rankQueue.add(playerId);
					p.playerState |= KNOCKOUT;// 取消在比赛中状态
					ChessUtils.chessLog.info("=======KnockOut=======:" + playerId + "_" + p.counter + "_restPlayerNum:" + participateNum + "_" + rankQueue.toString());
				}
				return p.counter;
			}
		}
		return 0;
	}

	public synchronized void updateRank() {
		ChessUtils.chessLog.info("=======updateRank=======" + finalsCount);
		if (finalsCount > 0) {// 决赛开始
			ChessUtils.chessLog.info("=======restNum=======" + participateNum + "_" + restPlayerId);
			if (participateNum == 1) {// 提前只剩一人
				if (enrollId.containsKey(restPlayerId)) {//
					ChessRoomMatchPlayer p = enrollId.get(restPlayerId);
					p.rank = this.participateNum--;
					p.playerState |= KNOCKOUT;// 取消在比赛中状态
					rankQueue.add(restPlayerId);
					stopMatch();
					return;
				}
			}
			if (finalsCount >= finals) {
				ChessRoomMatchPlayer[] tmp = new ChessRoomMatchPlayer[finalsRank.size()];
				int i = 0;
				Iterator<Entry<ChessRoomMatchPlayer, Long>> it = finalsRank.entrySet().iterator();
				while (it.hasNext()) {
					Entry<ChessRoomMatchPlayer, Long> en = it.next();
					tmp[i++] = en.getKey();
				}
				// 倒序
				for (int j = i - 1; j >= 0; j--) {
					ChessRoomMatchPlayer p = tmp[j];
					p.rank = this.participateNum--;
					p.playerState |= KNOCKOUT;// 取消在比赛中状态
					rankQueue.add(p.playerId);
					ChessUtils.chessLog.info("=======updateFinalsRank:" + p.counter);
				}
				stopMatch();
			}
		}
	}

	public synchronized void updateDynRank() {
		ChessUtils.chessLog.info("=======updateDynRank=======");
		Map<ChessRoomMatchPlayer, Long> dynamicRank = new TreeMap<ChessRoomMatchPlayer, Long>();
		Iterator<Entry<Long, ChessRoomMatchPlayer>> it = enrollId.entrySet().iterator();
		// 更新排序
		while (it.hasNext()) {
			Entry<Long, ChessRoomMatchPlayer> en = it.next();
			long playerId = en.getKey();
			ChessRoomMatchPlayer p = en.getValue();
			if ((p.playerState & ENTER) > 0) {// 已进房
				p.dynRank = 0;
				dynamicRank.put(p, playerId);
			}
		}

		int i = 0;
		Iterator<Entry<ChessRoomMatchPlayer, Long>> it2 = dynamicRank.entrySet().iterator();
		// 更新排序
		while (it2.hasNext()) {
			Entry<ChessRoomMatchPlayer, Long> en = it2.next();
			long playerId = en.getValue();
			ChessRoomMatchPlayer p = en.getKey();
			p.dynRank = ++i;
			ChessUtils.chessLog.info(p.dynRank + "_" + p.rank + "_" + playerId + "_" + p.counter);
		}
		ChessUtils.chessLog.info("=======updateDynRank=======");
	}

	public synchronized int putBackInQueue(long playerId, ChessRoomMatch chessRoom) {
		if (state == States.START) {//
			chessRoom.setPlayerAbsent(playerId);// 将玩家踢出房间
			ChessUtils.chessLog.info("\n===\n===\nputBackInQueue:" + chessRoom.getChessRoomPlayerNum());
			if (chessRoom.getChessRoomPlayerNum() == 0) {
				district.remove(chessRoom.getId());
			}

			if (enrollId.containsKey(playerId)) {//
				ChessRoomMatchPlayer p = enrollId.get(playerId);
				if (p.counter > 0) {
					if ((p.playerState & ENTER) > 0) {
						if ((p.playerState & INQUEUE) == 0) {
							p.playerState = ENTER_INQUEUE;
							queue.add(playerId);
							return 1;
						}
					}
				}
			}
		}	
		
		//离开区频道
		ChannelManager channelManager = SpringUtils.getBeanOfType(ChannelManager.class);
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
		Channel typeChannel = channelManager.getChannel(String.valueOf(districtId));
		chessPlayerManager.getOnlinePlayerById(playerId).leaveChannel(typeChannel.getName());		
		return 0;
	}

	public synchronized void ascendRate() {
		dynamicRate += mConfig.getDeltaRate();

		// 比赛区广播
		StaticStrings ss1 = StaticStrings.get(5008);
		StaticStrings ss2 = StaticStrings.get(5009);
		ChessSpeakerDto cs = new ChessSpeakerDto();
		cs.setMsg(ss1.getName() + dynamicRate + ss2.getName());
		cs.setCount(1);
		ChannelManager channelManager = SpringUtils.getBeanOfType(ChannelManager.class);
		Channel typeChannel = channelManager.getChannel(String.valueOf(districtId));
		typeChannel.broadcast(cs);
	}

	// 30秒一次 invoked by manager
	public synchronized void updateState() {
		long now = new java.util.Date().getTime();

		switch (state) {
		case END:
			StaticMatch tmpConfig = StaticMatch.get(districtId);
			hour = tmpConfig.getHour();
			minute = tmpConfig.getMinute();

			//ChessUtils.chessLog.info("Match is END" + districtId + "_" + now + "_" + endDate);
			// if (now > deadline) {//是否到下局时间
			if (ChessUtils.timeHasCome(0, 0)) {// 是否到时
				if (!ChessUtils.isToday(endDate)) {// 检测今天是否已经开赛
					ChessUtils.chessLog.info("Match is set to IDLE");
					initMatch();
					resetMatch();

					// deadline = now + 90000;// 一分钟后开始
					state = States.IDLE;
				}
			}
			// }
			break;
		case IDLE:
			if (ChessUtils.timeHasCome(hour, minute)) {// 是否到时
				// if (now > deadline) {
				if (enrollNum >= enrollMin) {// fix me
					ChessUtils.chessLog.info("\n===\n===\nMatch is about to start:" + districtId + "\n===\n===\n");
					//全服广播
					StaticStrings ss = StaticStrings.get(5002);
					ChessSpeakerDto cs = new ChessSpeakerDto();
					cs.setMsg(ss.getName());
					cs.setCount(3);
					ChannelTalkRuleManager.WORLD_CHANNEL.broadcast(cs);
					
					//区内广播
					enterable();
					
					state = States.WAIT4START;
					deadline = now + 60000;// 一分钟后开始
				} else {
					// to do 全服广播
					StaticStrings ss = StaticStrings.get(5001);
					ChessSpeakerDto cs = new ChessSpeakerDto();
					cs.setMsg(ss.getName());
					cs.setCount(3);
					ChannelTalkRuleManager.WORLD_CHANNEL.broadcast(cs);
					// to do return money
					returnMoney();
					stopMatch();
					initMatch();
				}
			}
			break;
		case START:
			// 更新倍率
			ascendRate();
			updateDynRank();
			break;
		}
	}

	// 2秒一次
	public synchronized void checkState() {
		if (state == States.START) {
			// 分配房间
			assignPlayerRoom();

			if (district.size() > 0) {
				// 遍历所有房间
				Iterator<Entry<Long, ChessRoomMatch>> iter = district.entrySet().iterator();
				Map.Entry<Long, ChessRoomMatch> entry;
				while (iter.hasNext()) {
					entry = (Map.Entry<Long, ChessRoomMatch>) iter.next();
					ChessRoomMatch chessRoom = entry.getValue();
					chessRoom.checkState();
				}
			} else {
				// 1没有房间,2分配不到房间
				// endDate = new java.util.Date().getTime();

				if (!assignFinalsRoom()) {
					// to do rank
					// finalsRank
					stopMatch();
				}
			}
		}else if(state == States.WAIT4START){
			long now = new java.util.Date().getTime();
			if (now > deadline) {
				ChessUtils.chessLog.info("\n===\n===\nMatch start:" + districtId + "\n===\n===\n");
				if (makeQueue()) {
					updateDynRank();// 刷新排行
					state = States.START;
				} else {
					// to do 全服广播
					StaticStrings ss = StaticStrings.get(5004);
					ChessSpeakerDto cs = new ChessSpeakerDto();
					cs.setMsg(ss.getName());
					cs.setCount(3);
					ChannelTalkRuleManager.WORLD_CHANNEL.broadcast(cs);
					// to do return money
					returnMoney();
					stopMatch();
					initMatch();
					// deadline = now + 120000;// 一分钟后开始
				}
			}
		}
	}

	public synchronized void setAbsentSyn(long playerId) {
		ChessUtils.chessLog.info("========setAbsentSyn:" + queue.size());
		if (state == States.START) {//
			setAbsent(playerId);
		}
	}

	public synchronized void cleanMatch(long chessRoomId) {
		ChessUtils.chessLog.info("========cleanMatch:" + queue.size());
		district.remove(chessRoomId);
	}

	private boolean makeQueue() {
		ChessUtils.chessLog.info("=====queue:"+queue.toString());
		Iterator<Entry<Long, ChessRoomMatchPlayer>> it = enrollId.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, ChessRoomMatchPlayer> en = it.next();
			long playerId = en.getKey();
			ChessRoomMatchPlayer p = en.getValue();
			if ((p.playerState & ENTER) > 0) {
				if ((p.playerState & INQUEUE) == 0) {// 不在队列玩家
					p.playerState |= INQUEUE;
					queue.add(playerId);
					participateNum++;//
				}
			}
		}
		ChessUtils.chessLog.info("========queueOfMatch:" + queue.size() +"_"+participateNum);
		ChessUtils.chessLog.info( queue.toString());
		if (participateNum < enrollMin) {
			return false;
		}
		return true;
	}

	private void initMatch() {
		enrollId.clear();
		rankQueue.clear();
		rank.clear();
	}

	private void enterable() {

		RoomUserMatchDto m = new RoomUserMatchDto();
		m.id = mConfig.getId();
		m.name = mConfig.getName();
		m.cost = mConfig.getCost();
		m.prize = mConfig.getIcon();
		m.date = mConfig.getDate();
		m.num = enrollNum;
		m.state = States.WAIT4START.ordinal();
		m.max = mConfig.getMaxPlayer();

		ChannelManager channelManager = SpringUtils.getBeanOfType(ChannelManager.class);
		Channel typeChannel = channelManager.getChannel(String.valueOf(districtId));
		typeChannel.broadcast(m);
	}

	private void returnMoney() {
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
		StaticMatch sm = StaticMatch.get(districtId);
		int cost = sm.getCost();
		Iterator<Entry<Long, ChessRoomMatchPlayer>> it = enrollId.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, ChessRoomMatchPlayer> en = it.next();
			long playerId = en.getKey();
			ChessPlayer playerTmp = chessPlayerManager.getAnyPlayerById(playerId);
			playerTmp.wealthHolder.increaseMoney(cost, 0, 0, BillType.get(ChessBillTypes.MATCH), "");
		}
		ChessUtils.chessLog.info("========returnMoney");
	}

	private void resetMatch() {
		this.enrollNum = 0;
		this.participateNum = 0;
		this.dynamicRate = mConfig.getInitRate();

		this.queue.clear();

		this.finalsCount = 0;
		this.finalsRank.clear();
	}

	private void stopMatch() {
		generateRank();
		resetMatch();
		state = States.END;// 结束 显示排行
		long now = new java.util.Date().getTime();
		// deadline = now + 180000;// 一分钟后开始
		endDate = now;	
		
		// to do 全服广播
		RoomUserMatchDto m = new RoomUserMatchDto();
		m.id = mConfig.getId();
		m.name = mConfig.getName();
		m.cost = mConfig.getCost();
		m.prize = mConfig.getIcon();
		m.date = mConfig.getDate();
		m.num = enrollNum;
		m.state = States.END.ordinal();
		m.max = mConfig.getMaxPlayer();
		ChannelManager channelManager = SpringUtils.getBeanOfType(ChannelManager.class);
		Channel typeChannel = channelManager.getChannel(String.valueOf(districtId));
		typeChannel.broadcast(m);

		ChessUtils.chessLog.info("Match rank:" + rankQueue.size() + "_" + rankQueue.toString());
		ChessUtils.chessLog.info("Match End:" + districtId + "\n===\n===\n");
	}

	private void generateRank() {
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
		int i = rankQueue.size();
		int j = 0;
		while (--i >= 0) {
			long playerId = rankQueue.get(i);
			ChessPlayerMatchRankMDto pRank = new ChessPlayerMatchRankMDto();
			ChessPlayer playerTmp = chessPlayerManager.getAnyPlayerById(playerId);
			j++;
			int type = getPrizeType(j);
			int num = getPrizeNum(j);
			String strPrize = "";
			if (type > 10) {
				StaticStrings ss = StaticStrings.get(type);
				if (null != ss) {
					strPrize = ss.getName();
				}
			} else if (type == 1) {
				if (num > 0) {
					StaticStrings ss = StaticStrings.get(2001);// "金币奖励"
					strPrize = ss.getName() + String.valueOf(num);
				}
			}

			pRank.setPrize(strPrize);
			pRank.setAccountId(playerTmp.accountId);
			pRank.setAvatar(playerTmp.avatar);
			pRank.setGender(playerTmp.gender.ordinal());
			pRank.setNickname(playerTmp.nickname);
			rank.add(pRank);
		}
	}

	private int getPrizeType(int pos) {

		switch (pos) {
		case 1:
			return mConfig.getT1();
		case 2:
			return mConfig.getT2();
		case 3:
			return mConfig.getT3();
		case 4:
			return mConfig.getT4();
		case 5:
			return mConfig.getT5();
		case 6:
			return mConfig.getT6();
		case 7:
			return mConfig.getT7();
		case 8:
			return mConfig.getT8();
		case 9:
			return mConfig.getT9();
		case 10:
			return mConfig.getT10();
		default:
			return 0;
		}
	}

	private int getPrizeNum(int pos) {
		switch (pos) {
		case 1:
			return mConfig.getN1();
		case 2:
			return mConfig.getN2();
		case 3:
			return mConfig.getN3();
		case 4:
			return mConfig.getN4();
		case 5:
			return mConfig.getN5();
		case 6:
			return mConfig.getN6();
		case 7:
			return mConfig.getN7();
		case 8:
			return mConfig.getN8();
		case 9:
			return mConfig.getN9();
		case 10:
			return mConfig.getN10();
		default:
			return 0;
		}
	}

	public int getEnrollNum() {
		return enrollNum;
	}

	public int getRate() {
		return this.dynamicRate;
	}

	public int getPlayerDynRank(long playerId) {
		if (enrollId.containsKey(playerId)) {//
			ChessRoomMatchPlayer p = enrollId.get(playerId);
			return p.dynRank;
		}
		return 0;
	}

	public int getCounter(long playerId) {
		if (enrollId.containsKey(playerId)) {//
			ChessRoomMatchPlayer p = enrollId.get(playerId);
			return p.counter;
		}
		return 0;
	}

	public int getPlayerRank(long playerId) {
		if (enrollId.containsKey(playerId)) {//
			ChessRoomMatchPlayer p = enrollId.get(playerId);
			return p.rank;
		}
		return 0;
	}

	public int getState(long playerId) {

		// 如果已报名
		if (enrollId.containsKey(playerId)) {
			if (state == States.IDLE) {
				return States.ENROLLED.ordinal();
			} else if (state == States.START) {
				ChessRoomMatchPlayer p = enrollId.get(playerId);
				if ((p.playerState & ENTER) > 0) {
					if (p.counter > 0) {
						return States.WAIT4START.ordinal();// 报名后 比赛开始也可进入比赛
					} else {
						return States.START.ordinal();// 不让进入
					}
				} else {
					return States.START.ordinal();// 不让进入
				}
			} else
				return state.ordinal();
		} else {
			// 如果没报名
			if (state == States.WAIT4START)
				return States.START.ordinal();
			else
				return state.ordinal();
		}
		//
	}

	public ChessPlayerMatchRankListDto getFinalRank(long playerId) {
		ChessUtils.chessLog.info("\n===\n===\ngetFinalRank");
		// 如果已报名
		int iRank = 0;
		int num = 0;
		boolean prizeAvailable = false;
		if (enrollId.containsKey(playerId)) {
			ChessRoomMatchPlayer p = enrollId.get(playerId);
			if ((p.playerState & ENTER) > 0) {// 不在线玩家
				iRank = p.rank;
				num = getPrizeNum(iRank);
				if (num > 0 && (p.playerState & GETPRIZE) == 0) {
					prizeAvailable = true;
				}
			}
		}
		ChessUtils.chessLog.info("iRank:" + iRank + "_" + num);
		// ChessPlayer player = chessPlayerManager.getRequestPlayer();
		ChessPlayerMatchRankMDto[] matchRank = new ChessPlayerMatchRankMDto[rank.size()];
		rank.toArray(matchRank);
		StaticStrings ss = StaticStrings.get(5007);// string contact
		ChessPlayerMatchRankListDto c = new ChessPlayerMatchRankListDto();
		c.setType(districtId);
		c.setMatchRank(matchRank);
		c.setContact(ss.getName());
		c.setPrizeAvailable(prizeAvailable);
		return c;
	}

	public RoomUserMatchPrizeDto getPrize(long playerId) {
		// TODO Auto-generated method stub
		// 如果已报名
		if (enrollId.containsKey(playerId)) {
			ChessRoomMatchPlayer p = enrollId.get(playerId);
			if ((p.playerState & GETPRIZE) > 0) {
				return null;
			}
			if ((p.playerState & ENTER) > 0) {// 不在线玩家
				int num = getPrizeNum(p.rank);
				int type = getPrizeType(p.rank);
				if (num > 0) {
					ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
					ChessPlayer player = chessPlayerManager.getAnyPlayerById(playerId);
					switch (type) {
					case 1:
						player.wealthHolder.increaseMoney(num, 0, 0, BillType.get(ChessBillTypes.MATCH), "");
						RoomUserMatchPrizeDto mp = new RoomUserMatchPrizeDto();
						mp.num = num;
						mp.type = type;
						p.playerState |= GETPRIZE;// 标记已领奖
						return mp;
					}
				}
			}
		}
		return null;
	}

	public boolean isPrizeAvail(long playerId) {
		// TODO Auto-generated method stub
		// 如果已报名
		if (enrollId.containsKey(playerId)) {
			ChessRoomMatchPlayer p = enrollId.get(playerId);
			if ((p.playerState & GETPRIZE) > 0) {
				return false;
			}
			if ((p.playerState & ENTER) > 0) {// 不在线玩家
				int type = getPrizeType(p.rank);
				if (type > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isAbsent(long playerId) {
		// 如果已报名
		if (enrollId.containsKey(playerId)) {
			ChessRoomMatchPlayer p = enrollId.get(playerId);
			if ((p.playerState & ABSENT) > 0) {// 不在线玩家
				return true;
			}
		}
		return false;
	}

	public void setAbsent(long playerId) {
		ChessUtils.chessLog.info("========setAbsent:");
		if (enrollId.containsKey(playerId)) {//
			ChessRoomMatchPlayer p = enrollId.get(playerId);
			if ((p.playerState & ENTER) > 0) {
				if ((p.playerState & INGAME_INQUEUE) == 0) {// 不在队列,也不在比赛
					if (p.counter > 0) {
						queue.add(playerId);
						p.playerState |= INQUEUE;// 不在线
					}
				}
				p.playerState |= ABSENT;// 不在线
			}
			ChessUtils.chessLog.info("========setAbsent:" + Integer.toString(p.playerState, 2));
		}
	}

	public void setStateStart() {
		state = States.START;
	}

	public class ChessRoomTimer implements Runnable {

		public ChessRoomTimer() {

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			checkState();
		}
	}

	@Override
	public synchronized void leavingCheck(long playerId, IChessRoom chessRoom) {
		ChessUtils.chessLog.info("========leavingCheck:");
		if (chessRoom != null) {
			chessRoom.setPlayerAbsent(playerId);// 将玩家踢出房间
			ChessUtils.chessLog.info("===========playerNum===========:" + chessRoom.getChessRoomPlayerNum());

			if (chessRoom.getChessRoomPlayerNum() == 0) {
				district.remove(chessRoom.getId());
			}
		}

		// 此处为断线专用
		if (state == States.WAIT4START) {
			if (enrollId.containsKey(playerId)) {//
				ChessRoomMatchPlayer p = enrollId.get(playerId);
				p.playerState |= ABSENT;//退出
			}
		}
		if (state == States.START) {//
			setAbsent(playerId);
		}

	}

	@Override
	public boolean containRoom(long roomId) {
		// TODO Auto-generated method stub
		return district.containsKey(roomId);
	}

	@Override
	public IChessRoom getIChessRoom(long roomId) {
		// TODO Auto-generated method stub
		return district.get(roomId);
	}

	@Override
	public void decPlayerNum() {
		// TODO Auto-generated method stub

	}

	@Override
	public void incPlayerNum() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPlayerNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean checkRoomMutex(long playerId) {
		// TODO Auto-generated method stub
		if (enrollId.containsKey(playerId)) {//
			ChessRoomMatchPlayer p = enrollId.get(playerId);
			if ((p.playerState & KNOCKOUT) > 0)
				return false;
			if ((p.playerState & ENTER) > 0)
				return true;
		}
		return false;
	}

}
