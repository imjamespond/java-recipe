package com.chitu.poker.arena;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gecko.commons.data.BillType;
import cn.gecko.commons.timer.ScheduleManager;

import com.chitu.poker.arena.msg.MinePlayerDto;
import com.chitu.poker.data.StaticArena;
import com.chitu.poker.data.StaticArenaMine;
import com.chitu.poker.model.PokerBillTypes;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.service.PokerPlayerManager;

@Service
public class ArenaManager {
	
	/**活动状态**/
	public enum ArenaStatus {
		/**结算中0**/
		Disable, 
		/**进行中1**/
		Enable;
		
		public static ArenaStatus from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	};

	/**检查矿间隔时间**/
	public static final int MINE_INTERVAL_TIME = 60 * 1000;
	
	@Autowired
	private ScheduleManager scheduleManager;
	
	@Autowired
	private PokerPlayerManager playerManager;
	
	/**竞技场状态**/
	private ArenaStatus status = ArenaStatus.Enable;
	
	/**下次检查矿时间**/
	private long checkMineTime;
	
	/**矿主**/
	private List<MinePlayerUnit> minePlayers = new ArrayList<MinePlayerUnit>();
	
	
	@PostConstruct
	public void init(){
		//定时检查
		scheduleManager.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				long curTime = System.currentTimeMillis();
				scheduleGame(curTime);
				scheduleMine(curTime);
			}
		}, 10 * DateUtils.MILLIS_PER_SECOND, 1 * DateUtils.MILLIS_PER_SECOND);
	}
	
	/**
	 * 竞技场刷新
	 * @param curTime
	 */
	private void scheduleGame(long curTime){
		if(this.status == ArenaStatus.Disable){
			return;
		}

		long beginTime = settingTime(18,30,0);
		long endTime = settingTime(18,35,0);
		
	    if(curTime == beginTime){
	    	this.status = ArenaStatus.Disable;
	    	PersistYesterdayRank.truncateTable();
	    	PersistTodayRank.copyTable();
	    	PersistTodayRank.truncateTable();
	    }
	    if(curTime >= endTime){
	    	this.status = ArenaStatus.Enable;
	    }
	}
	
	/**
	 * 矿刷新
	 * @param curTime
	 */
	private void scheduleMine(long curTime){
		if(this.status == ArenaStatus.Disable){
			return;
		}
		if(curTime < checkMineTime){
			return;
		}
		//矿奖励
		mineReward();
		//矿内排名
		mineRank();
		
		checkMineTime = curTime + MINE_INTERVAL_TIME;
	}
	
	/**
	 * 矿奖励
	 */
	private void mineReward(){
		for(MinePlayerUnit minePlayer : minePlayers){
			//奖励金币数
			StaticArenaMine mine = StaticArenaMine.get(minePlayer.mineId);
			int times = MINE_INTERVAL_TIME / mine.getOutPutTime();
			int money = times * mine.getOutPutMoney();
			//奖励
			PokerPlayer player = playerManager.getAnyPlayerById(minePlayer.playerId);
			player.wealthHolder.increaseMoney(money, BillType.get(PokerBillTypes.ARENA_MINE_REWARD), "");
		}
		minePlayers.clear();
	}
	
	/**
	 * 矿内排名
	 */
	private void mineRank(){
		Collection<StaticArena> arenas = StaticArena.gets();
		for(StaticArena arena : arenas){
			List<PersistTodayRank> newArenaRanks = PersistTodayRank.getArenaRanks(arena.getId(), arena.getMaxPlayer());
			for(StaticArenaMine mine : arena.mineArenas()){
				for(int rank=mine.getMixRank();rank<=mine.getMaxRank();rank++){
					int index = rank-1;
					PersistTodayRank arenaRank = newArenaRanks.get(index);
					MinePlayerUnit minePlayer = new MinePlayerUnit();
					minePlayer.playerId = arenaRank.getId();
					minePlayer.mineId = mine.getId();
					minePlayer.rank = rank;
					minePlayer.winCount = arenaRank.getWinCount();
					minePlayers.add(minePlayer);
				}
			}
		}
	}
	
	/**
	 * 设定时间
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	private long settingTime(int hour,int minute,int second){
		Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);
        long time = calendar.getTimeInMillis();
        return time;
	}
	
	/**
	 * 矿主
	 * @param mineId
	 * @return
	 */
	public List<MinePlayerDto> getMinePlayers(int mineId){
		List<MinePlayerDto> list = new ArrayList<MinePlayerDto>();
		for(MinePlayerUnit minePlayer : minePlayers){
			if(mineId == minePlayer.mineId){
				list.add(minePlayer.toDto());
			}
		}
		return list;
	}

	
}
