package com.metasoft.empire.service;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metasoft.empire.common.GeneralException;
import com.metasoft.empire.common.GeneralResponse;
import com.metasoft.empire.model.GameDequeTimer;
import com.metasoft.empire.model.GameRoom;
import com.metasoft.empire.model.GameRoomPVE;
import com.metasoft.empire.model.User;
import com.metasoft.empire.model.UserUpgrade;
import com.metasoft.empire.model.data.RoleData;
import com.metasoft.empire.service.common.ScheduleService;
import com.metasoft.empire.service.common.SpringService;
import com.metasoft.empire.vo.GameVO;
import com.metasoft.empire.vo.PlayerVO;

@Component
public class GameService  {
	
	private static final Logger logger = LoggerFactory.getLogger(GameService.class);
	static {logger.debug("GameService");} 

	private GameDequeTimer gameDeque;

	@Autowired
	private ScheduleService scheduleService;
	
	@PostConstruct
	public void init() {
		gameDeque = new GameDequeTimer(){
			private int i = 0;
			@Override
			public void checkDeque() {
				if(++i%10==0)
				logger.debug("deque size:{}", this.getDequeSize());
				}
		};
		scheduleService.scheduleAtFixedRate(gameDeque, DateUtils.MILLIS_PER_SECOND, DateUtils.MILLIS_PER_SECOND * 5);
	}
	
	private HashSet<Long> gameAllocSet = new HashSet<Long>();
	
	public void cancel(long uid){
		gameAllocSet.remove(uid);
	}
	
	public void accept(User self, long rivaluid) throws GeneralException{
		
		GameRoom preGameRoom = self.getGameRoom();
		if(null!=preGameRoom){
			GameVO vo = getGameVO(preGameRoom);
			String json = GeneralResponse.newJsonObj(vo);
			self.getConn().deliver(json);
			return ;
		}
		
		if(gameAllocSet.contains(rivaluid)){
			cancel(rivaluid);
			UserService us = SpringService.getBean(UserService.class);
			User rival = us.getOnlineUserById(rivaluid);
			if(null!=rival){
				
				GameRoom gameRoom = createGameRoom(self, rival);		
				self.gameRoom = new WeakReference<GameRoom>(gameRoom);
				self.gameRoom = new WeakReference<GameRoom>(gameRoom);
				gameDeque.addGameDeq(gameRoom);
				gameRoom.begin();
				
				GameVO vo = getGameVO(gameRoom);
				String json = GeneralResponse.newJsonObj(vo);
				self.getConn().deliver(json);
				rival.getConn().deliver(json);
			}
		}else{
			throw new GeneralException(0, "invitation.expired");
		}
	}
	
	public boolean allocate(User user0){
		if(gameAllocSet.contains(user0.getId())){
			//throw new GeneralException(0,"being.allocted");
		}
		
		GameRoom preGameRoom = user0.getGameRoom();
		if(null!=preGameRoom){
			GameVO vo = getGameVO(preGameRoom);
			String json = GeneralResponse.newJsonObj(vo);
			user0.getConn().deliver(json);
			return false;
		}
		
		Iterator<Long> it = gameAllocSet.iterator();
		while(it.hasNext()){
			long uid = it.next();
			if(uid==user0.getId())
				continue;
			
			it.remove();
			UserService us = SpringService.getBean(UserService.class);
			User user1 = us.getOnlineUserById(uid);
			if(null!=user1){
				
				GameRoom gameRoom = createGameRoom(user0, user1);		
				user0.gameRoom = new WeakReference<GameRoom>(gameRoom);
				user1.gameRoom = new WeakReference<GameRoom>(gameRoom);
				gameDeque.addGameDeq(gameRoom);
				gameRoom.begin();
				
				GameVO vo = getGameVO(gameRoom);
				String json = GeneralResponse.newJsonObj(vo);
				user0.getConn().deliver(json);
				user1.getConn().deliver(json);
				return false;
			}
		}
		
		gameAllocSet.add(user0.getId());
		return true;
	}

	public void pveAlloc(User user) {
		cancel(user.getId());
		
		GameRoom preGameRoom = user.getGameRoom();
		if(null!=preGameRoom){
			GameVO vo = getGameVO(preGameRoom);
			String json = GeneralResponse.newJsonObj(vo);
			user.getConn().deliver(json);
			return ;
		}
		
		GameRoom gameRoom = createGameRoomPVE(user);	
		user.gameRoom = new WeakReference<GameRoom>(gameRoom);
		gameDeque.addGameDeq(gameRoom);
		gameRoom.begin();
		
		GameVO vo = getGameVO(gameRoom);
		String json = GeneralResponse.newJsonObj(vo);
		user.getConn().deliver(json);
	}
	
	private static GameVO getGameVO(GameRoom gameRoom)
	{
		GameVO vo = new GameVO();
		BeanUtils.copyProperties(gameRoom.player0.vo, vo.getPlayer0() );
		BeanUtils.copyProperties(gameRoom.player1.vo, vo.getPlayer1() );
		vo.setTurn(gameRoom.turn);
		vo.setMap(gameRoom.map);
		vo.setState(gameRoom.state);
		vo.setType(gameRoom.type);
		return vo;
	}
	
	private static PlayerVO getPlayerVO(User user){
		PlayerVO vo = new PlayerVO();
		
		vo.setUid(user.getId());
		vo.setName(user.getUserPersist().getUsername());
		int roles[] = vo.getRoles();
		System.arraycopy( user.roles, 0, roles, 0,
                Math.min(user.roles.length, roles.length));
		Map<Integer, UserUpgrade> upMap = user.getUpgradeMap();
		for(int role:roles){
			UserUpgrade uu = upMap.get(role);
			if(null!=uu){
				vo.setHp(uu.getHp() + vo.getHp());
				vo.setAttack(uu.getAttack() + vo.getAttack());
			}
		}
		return vo;
	}
	
	
	private static PlayerVO getPvePlayerVO(){
		PlayerVO vo = new PlayerVO();
		
		vo.setUid(-1);
		vo.setName("pve npc");
		int roles[] = vo.getRoles();
		int roles_data[] = {1,2,3,4};
		System.arraycopy( roles_data, 0, roles, 0,
				roles.length);
		int level = 5;
		
		StaticDataService sds = SpringService.getBean(StaticDataService.class);
		for(int role:roles){
			RoleData rd = sds.roleMap.get(role);
			if(null!=rd){
				vo.setHp(rd.getBasehp()+(level-1)*rd.getFactorhp() + vo.getHp());
				vo.setAttack(rd.getBaseatk() + (level-1)*rd.getFactoratk() + vo.getAttack());
			}
		}
		return vo;
	}
	
	private static GameRoom createGameRoom(User u1, User u2)
	{
		GameRoom game = new GameRoom();
		game.map = u2.map;
		PlayerVO p0 = getPlayerVO(u1);	
		game.player0.vo = p0;
		game.player0.maxhp = game.player0.vo.getHp();
		PlayerVO p1 = getPlayerVO(u2);
		game.player1.vo = p1;
		game.player1.maxhp = game.player1.vo.getHp();
		return game;
	}
	private static GameRoom createGameRoomPVE(User user)
	{
		GameRoom game = new GameRoomPVE();
		game.map = user.map;
		game.type = 1;
		PlayerVO p0 =  getPvePlayerVO();
		game.player0.vo = p0;
		game.player0.maxhp = game.player0.vo.getHp();
		PlayerVO p1 =getPlayerVO(user);
		game.player1.vo = p1;
		game.player1.maxhp = game.player1.vo.getHp();
		return game;
	}
}
