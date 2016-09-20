package com.chitu.poker.arena;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.gecko.commons.data.BillType;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.IdUtils;
import cn.gecko.commons.utils.SpringUtils;

import com.chitu.poker.arena.msg.ArenaDto;
import com.chitu.poker.arena.msg.ArenaLogDto;
import com.chitu.poker.arena.msg.ArenaLogDto.ArenaLogType;
import com.chitu.poker.arena.msg.MinePlayerDto;
import com.chitu.poker.data.StaticRankReward;
import com.chitu.poker.data.StaticRepeatReward;
import com.chitu.poker.model.PokerBillTypes;
import com.chitu.poker.model.PokerErrorCodes;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.pet.msg.PetDto;
import com.chitu.poker.service.PokerPlayerManager;

public class ArenaHolder {
	
	/**复仇奖励**/
	public static final int REVENGE_WIN_MONEY = 100;

	private long playerId;
	
	/**竞技日志**/
	private LinkedList<ArenaLog> logs = new LinkedList<ArenaLog>(); 
	
    private PersistArena persistArena;
	
	private boolean update;

	private boolean needSave;
	
	public ArenaHolder(long playerId) {
		this.playerId = playerId;
	}
	
	private void init() {
		if (persistArena != null) {
			return;
		}
		
		persistArena = PersistArena.get(playerId);
		if (persistArena != null) {
			this.logs = PersistArena.initLogs(persistArena.getLogs());
		} else {
			needSave = true;
			persistArena = new PersistArena();
			persistArena.setId(this.playerId);
			persist();
		}
	}
	
	/**
	 * 持久化
	 */
	public void persist() {
		if (persistArena == null) {
			return;
		}
		
		persistArena.setLogs(PersistArena.logsData(this.logs));

		if (needSave) {
			persistArena.save();
			needSave = false;
		}else if (update) {
			persistArena.update();
			update = false;
		}
	}
	
	/**
	 * 角色下线
	 */
	public void destroy() {
		persistArena = null;
		playerId = 0;
		logs.clear();
	}
	
	
	
	
	/**
	 * 竞技场DTO
	 * @return
	 */
	public ArenaDto toDto(){
		int todayWin = 0;
		int todayRank = 0;
		List<Integer> activeRepeatReward = new ArrayList<Integer>();
		List<Integer> obtainRepeatReward = new ArrayList<Integer>();
		int yesterdayRank = 0;
		int obtainRankReward = 0;
		
		PersistTodayRank persistToday = PersistTodayRank.get(playerId);
		if(persistToday != null){
			todayWin = persistToday.getWinCount();
			todayRank = PersistTodayRank.getWorldRank(todayWin);
			activeRepeatReward = PersistTodayRank.initReward(persistToday.getActiveRepeatReward());
			obtainRepeatReward = PersistTodayRank.initReward(persistToday.getObtainRepeatReward());
		}
		PersistYesterdayRank persistYesterday = PersistYesterdayRank.get(playerId);
		if(persistYesterday != null){
			yesterdayRank = PersistYesterdayRank.getWorldRank(persistYesterday.getWinCount());
			obtainRankReward = persistYesterday.getObtainRankReward();
		}
		
		ArenaDto dto = new ArenaDto();
		dto.setTodayWin(todayWin);
		dto.setTodayRank(todayRank);
		dto.setYesterdayRank(yesterdayRank);
		dto.setActiveRepeatReward(activeRepeatReward);
		dto.setObtainRepeatReward(obtainRepeatReward);
		dto.setObtainRankReward(obtainRankReward);
		
		PersistTodayRank persistFristRank = PersistTodayRank.getWorldFristRank();
		if(persistFristRank != null){
			PokerPlayer fristPlayer = SpringUtils.getBeanOfType(PokerPlayerManager.class).getAnyPlayerById(persistFristRank.getId());
			dto.setFristRankId(String.valueOf(fristPlayer.id));
			dto.setFristRankName(fristPlayer.nickname);
			dto.setFristRankWinCount(persistFristRank.getWinCount());
		}
		return dto;
	}
	
	/**
	 * 获取连胜奖励
	 * @param repeatRewardId
	 * @return
	 */
	public List<PetDto> getRepeatReward(int repeatRewardId){
		PersistTodayRank persistToday = PersistTodayRank.get(playerId);
		if(persistToday == null){
			throw new GeneralException(PokerErrorCodes.REPEAT_REWARD_LIMIT);
		}
		
		List<Integer> activeRepeatReward = PersistTodayRank.initReward(persistToday.getActiveRepeatReward());
		List<Integer> obtainRepeatReward = PersistTodayRank.initReward(persistToday.getObtainRepeatReward());
		
		if(obtainRepeatReward.contains(repeatRewardId)){
			throw new GeneralException(PokerErrorCodes.HAS_REPEAT_REWARD);
		}
		if(!activeRepeatReward.contains(repeatRewardId)){
			throw new GeneralException(PokerErrorCodes.REPEAT_REWARD_LIMIT);
		}
		
		PokerPlayer player = SpringUtils.getBeanOfType(PokerPlayerManager.class).getAnyPlayerById(playerId);
		StaticRepeatReward repeatReward = StaticRepeatReward.get(repeatRewardId);
		int petCount = repeatReward.getPetCount1() + repeatReward.getPetCount2();
		if(player.petHolder.tryAddPet(petCount)){
			throw new GeneralException(PokerErrorCodes.PET_MAX_COUNT);
		}
		
		if(repeatReward.getMoney() > 0){
			player.wealthHolder.increaseMoney(repeatReward.getMoney(), BillType.get(PokerBillTypes.ARENA_REPEAT_REWARD), "");
		}
		if(repeatReward.getPoint() > 0){
			player.wealthHolder.increasePoint(repeatReward.getPoint(), BillType.get(PokerBillTypes.ARENA_REPEAT_REWARD), "");
		}
		
		List<PetDto> petDtos = new ArrayList<PetDto>();
		for(int i=0;i<repeatReward.getPetCount1();i++){
			Pet pet = player.petHolder.beOverlayOwned(repeatReward.getStaticPet1());
			petDtos.add(pet.toDto());
		}
		for(int i=0;i<repeatReward.getPetCount2();i++){
			Pet pet = player.petHolder.beOverlayOwned(repeatReward.getStaticPet2());
			petDtos.add(pet.toDto());
		}
		
		obtainRepeatReward.add(repeatRewardId);
		persistToday.setObtainRepeatReward(PersistTodayRank.repeatData(obtainRepeatReward));
		persistToday.update();
		return petDtos;
	}
	
	/**
	 * 领取排名奖励
	 * @return
	 */
	public List<PetDto> getRankReward(){
		PersistYesterdayRank persistYesterday = PersistYesterdayRank.get(playerId);
		if(persistYesterday == null){
			throw new GeneralException(PokerErrorCodes.RANK_REWARD_LIMIT);
		}
		
		int obtainRankReward = persistYesterday.getObtainRankReward();
		if(obtainRankReward <= 0){
			throw new GeneralException(PokerErrorCodes.HAS_RANK_REWARD);
		}
		
		PokerPlayer player = SpringUtils.getBeanOfType(PokerPlayerManager.class).getAnyPlayerById(playerId);
		StaticRankReward rankReward = StaticRankReward.get(obtainRankReward);
		int petCount = rankReward.getPetCount1() + rankReward.getPetCount2() + rankReward.getPetCount3() + rankReward.getPetCount4();
		if(player.petHolder.tryAddPet(petCount)){
			throw new GeneralException(PokerErrorCodes.PET_MAX_COUNT);
		}
		
		if(rankReward.getMoney() > 0){
			player.wealthHolder.increaseMoney(rankReward.getMoney(), BillType.get(PokerBillTypes.ARENA_RANK_REWARD), "");
		}
		if(rankReward.getPoint() > 0){
			player.wealthHolder.increasePoint(rankReward.getPoint(), BillType.get(PokerBillTypes.ARENA_RANK_REWARD), "");
		}
		
		List<PetDto> petDtos = new ArrayList<PetDto>();
		for(int i=0;i<rankReward.getPetCount1();i++){
			Pet pet = player.petHolder.beOverlayOwned(rankReward.getStaticPet1());
			petDtos.add(pet.toDto());
		}
		for(int i=0;i<rankReward.getPetCount2();i++){
			Pet pet = player.petHolder.beOverlayOwned(rankReward.getStaticPet2());
			petDtos.add(pet.toDto());
		}
		for(int i=0;i<rankReward.getPetCount3();i++){
			Pet pet = player.petHolder.beOverlayOwned(rankReward.getStaticPet3());
			petDtos.add(pet.toDto());
		}
		for(int i=0;i<rankReward.getPetCount4();i++){
			Pet pet = player.petHolder.beOverlayOwned(rankReward.getStaticPet4());
			petDtos.add(pet.toDto());
		}
		
		persistYesterday.setObtainRankReward(0);
		persistYesterday.update();
		return petDtos;
	}
	
	
	/**
	 * 战后处理
	 * @param arenaId
	 * @param target
	 * @param battleWin
	 * @return
	 */
	public ArenaDto battleResult(int arenaId, PokerPlayer target, boolean battleWin){
		PersistTodayRank persistToday = PersistTodayRank.get(playerId);
		int winCount = 0, repeatWin = 0;
		List<Integer> activeRepeatReward = new ArrayList<Integer>();
		if(persistToday != null){
			winCount = persistToday.getWinCount();
			repeatWin = persistToday.getRepeatWin();
			activeRepeatReward = PersistTodayRank.initReward(persistToday.getActiveRepeatReward());
		}
		
		if(battleWin){
			winCount++;
			repeatWin++;
			//连胜奖励激活
			List<StaticRepeatReward> rewards = StaticRepeatReward.getByRepeat(repeatWin);
			for(StaticRepeatReward data : rewards){
				if(!activeRepeatReward.contains(data.getId())){
					activeRepeatReward.add(data.getId());
				}
			}
		}else{
			repeatWin = 0;
		}
		//保存结果
		if(persistToday == null){
			persistToday = new PersistTodayRank();
			persistToday.setId(playerId);
			persistToday.setWinCount(winCount);
			persistToday.setRepeatWin(repeatWin);
			persistToday.setArenaId(arenaId);
			persistToday.setActiveRepeatReward(PersistTodayRank.repeatData(activeRepeatReward));
			persistToday.setObtainRepeatReward(PersistTodayRank.repeatData(new ArrayList<Integer>()));
			persistToday.save();
		}else{
			persistToday.setWinCount(winCount);
			persistToday.setRepeatWin(repeatWin);
			persistToday.setArenaId(arenaId);
			persistToday.setActiveRepeatReward(PersistTodayRank.repeatData(activeRepeatReward));
			persistToday.update();
		}
		//战报
		log(target,battleWin);
		return toDto();
	}
	
	
	/**
	 * 记录挑战行为
	 * @param competitor 对手
	 * @param beforeRank 本人之前排名
	 * @param battleRecord 战斗结果
	 * @param camp 本人阵营
	 * @return
	 */
	private void log(PokerPlayer competitor,boolean dareWin){
		ArenaLog attackLog = new ArenaLog();
		attackLog.id = IdUtils.generateLongId();
		attackLog.competitorId = competitor.id;
		attackLog.logType = ArenaLogType.Attack;
		attackLog.win = dareWin;
		attackLog.logTime = System.currentTimeMillis();
		addLog(attackLog);
		
		ArenaLog defendLog = new ArenaLog();
		defendLog.id = IdUtils.generateLongId();
		defendLog.competitorId = this.playerId;
		defendLog.logType = ArenaLogType.Defend;
		defendLog.win = !dareWin;
		defendLog.logTime = System.currentTimeMillis();
		competitor.arenaHolder.addLog(defendLog);
	}
	
	/**
	 * 添加日志,最多20条记录
	 * @param log
	 */
	public void addLog(ArenaLog log){
		if(log == null){
			return;
		}
		
		this.logs.add(log);
		if(this.logs.size() > 20){
			this.logs.removeFirst();
		}
		update = true;
	}
	
	
	/**
	 * 矿主DTO
	 * @param mineId
	 * @return
	 */
	public List<MinePlayerDto> minePlayer(int mineId){
		ArenaManager arenaManager = SpringUtils.getBeanOfType(ArenaManager.class);
		List<MinePlayerDto> dtos = arenaManager.getMinePlayers(mineId);
		return dtos;
	}
	
	/**
	 * 获取战报
	 * @param logId
	 * @return
	 */
	public ArenaLog getLog(long logId){
		init();
		for(ArenaLog log : this.logs){
			if(log.id == logId){
				return log;
			}
		}
		return null;
	}
	
	/**
	 * 删除战报
	 * @param logId
	 */
	public void deleteLog(long logId){
		ArenaLog log = getLog(logId);
		this.logs.remove(log);
	}
	
	
	/**
	 * 战报列表DTO
	 * @return
	 */
	public List<ArenaLogDto> logList(){
		init();
		List<ArenaLogDto> logDtos = new ArrayList<ArenaLogDto>();
		for(ArenaLog log : this.logs){
			logDtos.add(log.toDto());
		}
		return logDtos;
	}
	
	/**
	 * 复仇
	 * @param target
	 */
	public void revengeResult(long logId, boolean battleWin){
		if(battleWin){
			PokerPlayerManager playerManager = SpringUtils.getBeanOfType(PokerPlayerManager.class);
			PokerPlayer player = playerManager.getAnyPlayerById(playerId);
			player.wealthHolder.increaseMoney(REVENGE_WIN_MONEY, BillType.get(PokerBillTypes.REVENGE_REWARD), "");
		}

		deleteLog(logId);
	}
	
	
	
	
}
