package com.metasoft.flying.service;

import java.util.Deque;
import java.util.LinkedList;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.PkArena;
import com.metasoft.flying.model.constant.ArenaConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.common.ScheduleService;
import com.metasoft.flying.vo.PkJoinRequest;

@Service
public class PkArenaService{
	private static final Logger logger = LoggerFactory.getLogger(PkArenaService.class);

	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private UserService userService;

	// private Set<RoomVO> roomOfRandomSet;
	@Value("${room.check.period}")
	private int roomCheckPeriod;
	private Deque<PkArena> arenaList = new LinkedList<PkArena>();
	
	//@PostConstruct
	public void init() {
		MatchTimer timer = new MatchTimer();
		scheduleService.scheduleAtFixedRate(timer, DateUtils.MILLIS_PER_SECOND, DateUtils.MILLIS_PER_SECOND
				* roomCheckPeriod);
	}


	/**
	 * ready or commence
	 * @return
	 * @throws GeneralException 
	 */
	public void readyOrCommence(GameRoom room,PkJoinRequest req) throws GeneralException {
		PkArena arena_ = room.getArena();
		if(null != arena_){
			if(arena_.getState() == ArenaConstant.COMMENCE){
				arena_.cease();
			}else if(arena_.getState() == ArenaConstant.READY){
				return;
			}
		}
		//寻找准备的房间
		for (PkArena arena : arenaList) {
			if(ArenaConstant.READY == arena.getState()){
				//防止匹配自己
				GameRoom red = arena.getRed();
				if(red!=null){
					if(red.getMasterId() == room.getMasterId()){
						return;
					}
				}
				if(arena.getNumber() == req.getUserIdList().length){
					arena.commence(room,req);
					return;
				}
			}
		}
		
		for (PkArena arena : arenaList) {		
			if(ArenaConstant.IDLE == arena.getState()){
				arena.ready(room,req);
				arena.setNumber(req.getUserIdList().length);
				return;
			}
		}
		PkArena arena = new PkArena(room.getName());
		arena.ready(room,req);
		arena.setNumber(req.getUserIdList().length);
		
		synchronized(arenaList){
			arenaList.add(arena);
		}
		logger.debug("arenaList size:{}",arenaList.size());
	}
	

	public void commence(GameRoom room, PkJoinRequest req) throws GeneralException {
		PkArena arena_ = room.getArena();
		if(null != arena_){
			if(arena_.getState() == ArenaConstant.COMMENCE){
				arena_.cease();
			}
		}
		for (PkArena arena : arenaList) {		
			if(ArenaConstant.IDLE == arena.getState()){
				arena.emperor(room,req);
				arena.setNumber(req.getUserIdList().length);
				return;
			}
		}
		PkArena arena = new PkArena(room.getName());
		arena.emperor(room,req);
		
		synchronized(arenaList){
			arenaList.add(arena);
		}
	}

	private class MatchTimer implements Runnable {

		@Override
		public void run() {
			for (PkArena arena : arenaList) {
				if(ArenaConstant.READY == arena.getState()){
					if(((System.currentTimeMillis()-arena.getTime())>ArenaConstant.AREAN_WAIT)){
						arena.cease();
					}
				}else if(ArenaConstant.COMMENCE == arena.getState()){
					long pasted = System.currentTimeMillis()-arena.getTime();
					if(arena.getPkType() == ArenaConstant.TYPE_EMPEROR){
						int past = (int) (pasted/60000l);
						//5分钟后
						if(past>4){
							int diff = past - arena.getPast();
							arena.setUpgrade(arena.getUpgrade()+diff);
						}
						arena.setPast(past);						
					}else{
						if(pasted>ArenaConstant.AREAN_LENGTH){
							arena.cease();
						}
					}
				}
			}
		}
	}


}