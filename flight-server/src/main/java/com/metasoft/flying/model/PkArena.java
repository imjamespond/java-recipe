package com.metasoft.flying.model;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.controller.DressController;
import com.metasoft.flying.model.constant.ArenaConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.util.RandomUtils;
import com.metasoft.flying.vo.PkArenaStartVO;
import com.metasoft.flying.vo.PkFireRequest;
import com.metasoft.flying.vo.PkGoRequest;
import com.metasoft.flying.vo.PkHurtRequest;
import com.metasoft.flying.vo.PkJoinRequest;
import com.metasoft.flying.vo.PkRebirthRequest;
import com.metasoft.flying.vo.PkVoFactory;
import com.metasoft.flying.vo.general.GeneralResponse;

/**
 * @author james
 * 
 */
public class PkArena {
	private static final Logger logger = LoggerFactory.getLogger(PkArena.class);
	private static final int TEAM1 = 1;
	private static final int TEAM2 = 2;
static{
	logger.debug("PkArena");
}
	private int number;//几人房
	private int past;//逝去时间
	private float upgrade;//攻击升级
	private long time;//时间
	private String name;
	private PkPlayer[] pkPlayers;
	
	private WeakReference<GameRoom> red;
	private WeakReference<GameRoom> blue;
	// 配置

	// 状态
	private int state = 0;// 状态,1为准备2为开始
	private int pkType = 0;
	
	public PkArena(String name) {
		this.name = name;
		pkPlayers = new PkPlayer[8];
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PkPlayer[] getPkPlayers() {
		return pkPlayers;
	}

	public void setPkPlayers(PkPlayer[] pkPlayers) {
		this.pkPlayers = pkPlayers;
	}

	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public GameRoom getRed() {
		if(red != null){
			return red.get();
		}
		return null;
	}

	public void setRed(WeakReference<GameRoom> red) {
		this.red = red;
	}

	public GameRoom getBlue() {
		if(blue != null){
			return blue.get();
		}
		return null;
	}

	public void setBlue(WeakReference<GameRoom> blue) {
		this.blue = blue;
	}
	

	public int getPkType() {
		return pkType;
	}

	public int getPast() {
		return past;
	}

	public void setPast(int past) {
		this.past = past;
	}

	public void setPkType(int pkType) {
		this.pkType = pkType;
	}

	public float getUpgrade() {
		return upgrade;
	}

	public void setUpgrade(float upgrade) {
		this.upgrade = upgrade;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	/**
	 * 对战
	 * @param room
	 * @param req
	 * @throws GeneralException
	 */
	public void commence(GameRoom room, PkJoinRequest req) throws GeneralException {
		state = ArenaConstant.COMMENCE;
		setBlue(new WeakReference<GameRoom>(room));
		time = System.currentTimeMillis();
		room.setArena(new WeakReference<PkArena>(this));
		setPkType(0);
		//注册玩家
		for(long userId:req.getUserIdList()){
			regist(userId,PkArena.TEAM1);
		}
		//广播
		broadcast(PkVoFactory.getPkArenaStartVO(this));
	}

	public void ready(GameRoom room, PkJoinRequest req) throws GeneralException {
		state = ArenaConstant.READY;
		setRed(new WeakReference<GameRoom>(room));
		time = System.currentTimeMillis();
		room.setArena(new WeakReference<PkArena>(this));
		//注册玩家
		for(long userId:req.getUserIdList()){
			regist(userId,PkArena.TEAM2);
		}
		//广播
		broadcast(PkVoFactory.getPkMatchingVO(this));
	}

	/**
	 * 混战
	 */
	public void pk(){
		state = ArenaConstant.COMMENCE;
		time = System.currentTimeMillis();
		//安排队伍
		int team = 0;
		for(PkPlayer player:pkPlayers){
			if(player!=null&&player.getUserId()>0){
				player.setTeam(++team);
			}
		}
		//广播
		setPkType(ArenaConstant.TYPE_PK);
		PkArenaStartVO vo = PkVoFactory.getPkArenaStartVO(this);	
		broadcast(vo);
	}
	/**
	 * 三国杀
	 * @throws GeneralException 
	 */
	public void emperor(GameRoom room, PkJoinRequest req) throws GeneralException {
		state = ArenaConstant.COMMENCE;
		time = System.currentTimeMillis();
		setRed(new WeakReference<GameRoom>(room));
		room.setArena(new WeakReference<PkArena>(this));
		//注册玩家
		for(long userId:req.getUserIdList()){
			regist(userId,0);
		}
		//安排角色
		PkPlayer emperor = null;
		ArrayDeque<PkPlayer> deque = new ArrayDeque<PkPlayer>();
		for(PkPlayer player:pkPlayers){
			if(player!=null&&player.getUserId()>0){
				if(player.getUserId() == room.getMasterId()){
					emperor = player;
					continue;
				}
				deque.add(player);
			}
		}
		if(emperor==null){
			reset();
			return;
		}
		emperor.setTeam(1);//主公
		emperor.setHpMax(1000);
		emperor.setHp(1000);
		if(deque.size()<8){
			int[] roles = ArenaConstant.AREAN_EMPEROR[deque.size()];
			int pos = RandomUtils.nextInt(roles.length);
			for(PkPlayer player:deque){
				pos = (pos + roles.length)%roles.length;
				player.setTeam(roles[pos]);
				player.setHpMax(1000);
				player.setHp(1000);
				pos++;
			}
		}
		//主公hp增量
		switch(deque.size()){
		case 4:
		case 5:
			emperor.setHpMax(1500);
			emperor.setHp(1500);
			break;
		case 6:
		case 7:
			emperor.setHpMax(2000);
			emperor.setHp(2000);
			break;
		}		
		//广播
		this.setPkType(ArenaConstant.TYPE_EMPEROR);
		PkArenaStartVO vo = PkVoFactory.getPkArenaStartVO(this);		
		broadcast(vo);
	}
	
	public void cease() {
		//广播
		broadcast(PkVoFactory.getPkArenaEndVO(this, pkPlayers));
		reset();
	}
	
	public void interrupt() {
		//广播
		broadcast(PkVoFactory.getPkArenaEndVO2());
		reset();
	}
	
	public void ceaseEmperor(int role) {
		//广播
		broadcast(PkVoFactory.getPkEmperorEndVO(role, pkPlayers));
		reset();
	}
	
	private void reset(){
		state = ArenaConstant.IDLE;
		time = 0;
		number = 0;
		pkType = 0;
		upgrade = 0;
		past = 0;
		//重置玩家
		for(PkPlayer player:pkPlayers){
			if(null != player)
			player.reset();
		}
		GameRoom room = getRed();
		if(null!=room){
			room.setArena(null);
			red = null;
		}
		room = getBlue();
		if(null!=room){
			room.setArena(null);
			blue = null;
		}
	}


	/**
	 * 注册玩家,分队,
	 * @param userId
	 * @param team
	 * @throws GeneralException 
	 */
	private void regist(long userId, int team) throws GeneralException{
		for(int i=0; i<pkPlayers.length; ++i){
			PkPlayer player = pkPlayers[i];
			
			if(null == player){
				player = new PkPlayer();
				pkPlayers[i] = player;
			}
			
			if(player.getUserId() == 0l){
				player.reset();
				player.setX(ArenaConstant.AREAN_POSX[team==TEAM1?i:(ArenaConstant.AREAN_POSX.length-i-1)]);
				player.setY(ArenaConstant.AREAN_POSY[team==TEAM1?i:(ArenaConstant.AREAN_POSY.length-i-1)]);
				player.setPos(i);
				player.setUserId(userId);
				player.setTeam(team);
				
				UserService userService = SpringService.getBean(UserService.class);
				User user = userService.getAnyUserById(userId);
				player.setUser(user);
				DressController dressController = SpringService.getBean(DressController.class);
				player.setPlane(dressController.checkPlaneIsPutOn2(user));
				player.setPlaneType(dressController.checkPlaneIsPutOn(user));
				break;
			}
			
		}
	}

	public void go(PkGoRequest req) {
		PkPlayer player = pkPlayers[req.getPos()];
		if(null != player){
			player.setX(req.getX());
			player.setY(req.getY());
		
			//广播
			broadcast(PkVoFactory.getPkGoVO(req));	
		}
	}
	
	private void broadcast(Object obj){
		//广播
		GeneralResponse vo = GeneralResponse.newObject(obj);
		GameRoom room = getRed();
		if(null!=room){
			room.broadcast(vo);
		}
		room = getBlue();
		if(null!=room){
			room.broadcast(vo);
		}		
		
	}

	public void fire(PkFireRequest req) {
		PkPlayer player = pkPlayers[req.getPos()];
		PkPlayer target = pkPlayers[req.getTarget()];
		if(null != player && null != target){
			//广播
			broadcast(PkVoFactory.getPkFireVO(req));	
		}
	}

	public void hurt(PkHurtRequest req) {
		PkPlayer player = pkPlayers[req.getPos()];
		PkPlayer target = pkPlayers[req.getTarget()];
		if(null != player && null != target){
			int damage = (int) (req.getHurt()*(1+upgrade));
			if(target.decreaseHp(damage)<=0){
				target.setState(PkPlayer.STATE_DEAD);
				target.setRebirth(System.currentTimeMillis());
				player.setScore(player.getScore()+100);
			}else{
				player.setScore(player.getScore()+10);
			}
			//广播
			broadcast(PkVoFactory.getPkHurtVO(req, player, target, damage));	
		}
		
		if(pkType == ArenaConstant.TYPE_EMPEROR){
			checkEmperor();
		}
	}

	public void rebirth(PkRebirthRequest req) {
		PkPlayer player = pkPlayers[req.getPos()];
		if(null != player ){
			player.rebirth();
			//广播
			broadcast(PkVoFactory.getPkRebirthVO(player));	
		}
	}

	public void leave(long uid) {
		int count = 0;
		for(int i=0; i<pkPlayers.length; ++i){
			PkPlayer player = pkPlayers[i];
			if(null != player){
				if(player.getUserId() == uid){
					player.reset();
				}else if(player.getUserId() > 0){
					count++;
				}
			}
		}
		if(count <= 1){
			interrupt();
		}
	}
	
	private void checkEmperor() {
		int state_ = 0;
		for(PkPlayer player:pkPlayers){
			if(player!=null&&player.getUserId()>0&& player.getHp()>0){
				switch(player.getTeam()){
				case 1:
					state_ |= 1;//1主公
					break;
				case 2:
					state_ |= 8;//2忠臣
					break;
				case 3:
					state_ |= 2;//3反贼
					break;
				case 4:
					state_ |= 4;//4内奸
					break;
				}
			}
		}
		

		switch(state_){
		case 1://主公
		case 9://主公,忠臣
			ceaseEmperor(1);
			break;
		case 2://反贼
		case 10://反贼,忠臣
		case 14://反贼,内奸,忠臣
		case 6://反贼,内奸
			ceaseEmperor(2);
			break;
		case 4://内奸
		case 12://内奸,忠臣
			ceaseEmperor(4);
			break;
		}
	}
}
