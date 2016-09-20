package com.chitu.chess.model;

import cn.gecko.broadcast.api.ConnectorManager;
import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.player.event.BackpackEvent;
import cn.gecko.player.event.PlayerEvent;
import cn.gecko.player.event.PlayerEventListener;
import cn.gecko.player.event.PlayerHeartbeatEvent;
import cn.gecko.player.event.PlayerPersistEvent;
import cn.gecko.player.model.Backpack;
import cn.gecko.player.model.Player;

import com.chitu.chess.msg.RoomUserResultReplayDto;
import com.chitu.chess.service.ChessRoomManager;

public class ChessPlayer extends Player {

	public static int SAVE_ON_HEARTBEAT = 10;

	public enum Gender {
		/**
		 * 女性
		 */
		Female,
		/**
		 * 男性
		 */
		Male;
	}

	public Gender gender;
	public int avatar;
	public long flags;
	public int exp;
	public long createTime;
	public long loginTime;
	public int onlineTime;

	public int battleId;

	public MissionHolder missionHolder;
	public WealthHolder wealthHolder;

	private boolean hasBackpack;
	private boolean backpackChange;
	private int heartbeatCount = 0;
	private PersistChessPlayer persistPlayer;

	public ChessPlayer(int sid, ConnectorManager connectorManager) {
		super(sid, connectorManager);
		addEventListener(BackpackEvent.class, new BackpackListener());
	}

	public void update() {
		//保存背包
		updateBackpack();
		// 保存用户信息
		updatePlayer();
	}

	protected void updatePlayer() {
		persistPlayer.setGrade(grade);
		persistPlayer.setExp(exp);
		persistPlayer.setFlags(flags);
		persistPlayer.setOnlineTime(onlineTime);
		persistPlayer.setBattleId(battleId);
		persistPlayer.setNickname(nickname);
		persistPlayer.setGender(gender.ordinal());
		persistPlayer.setAvatar(avatar);
		persistPlayer.update();
	}

	public void heartbeat() {
		heartbeatCount++;
		onlineTime++;
		dispatchEvent(new PlayerHeartbeatEvent(heartbeatCount));
		if (heartbeatCount % SAVE_ON_HEARTBEAT == 0) {
			persistAll();
		}
	}

	public void persistAll() {
		update();
		dispatchEvent(new PlayerPersistEvent());
	}

	public void init(long id) {
		PersistChessPlayer player = PersistChessPlayer.get(id);
		init(player);
	}

	public void init(PersistChessPlayer player) {
		this.persistPlayer = player;
		this.id = persistPlayer.getId();
		this.accountId = persistPlayer.getAccountId();
		this.grade = persistPlayer.getGrade();
		this.exp = player.getExp();
		this.nickname = player.getNickname();
		this.gender = Gender.values()[player.getGender()];
		this.avatar = player.getAvatar();
		this.flags = player.getFlags();
		this.onlineTime = player.getOnlineTime();
		this.createTime = persistPlayer.getCreateTime();
		this.loginTime = persistPlayer.getLoginTime();
		this.battleId = persistPlayer.getBattleId();

		initMission();	
		initWealth();
	}


	
	protected void initWealth() {
		wealthHolder = new WealthHolder(persistPlayer);
	}

	protected void initMission() {
		missionHolder = new MissionHolder(persistPlayer);
	}


	
	public void setPassword(String password) {
		persistPlayer.setPassword(password);
	}	
	public String getPassword() {
		return persistPlayer.getPassword();
	}	
	public String getSalt() {
		return persistPlayer.getSalt();
	}
	
	public void setReplay(byte[] replay){
		persistPlayer.setReplay(replay);
	}
	
	public RoomUserResultReplayDto getReplay(){
		return persistPlayer.initReplay();
	}	
	
	
	
	private boolean isBackpackInit() {
		return super.getBackpack() != null;
	}

	public synchronized Backpack getBackpack() {
		if (!isBackpackInit()) {
			initBackpack();
		}
		return super.getBackpack();
	}
	
	protected void initBackpack() {
		PersistBackpack persist = PersistBackpack.get(id);
		Backpack backpack = null;
		if (persist != null) {
			hasBackpack = true;
			// 初始化背包
			backpack = PersistBackpack.initBackpack(1, id, persist.getBackpack());
			if (backpack != null)
				setBackpack(backpack);
		}
		if (backpack == null) {
			setBackpack(PersistBackpack.createBackpack(1, id));
		}
	}
	
	protected void updateBackpack() {
		if (!backpackChange)
			return;
		if (super.getBackpack() == null)
			return;
		PersistBackpack persistBackpack = new PersistBackpack();
		persistBackpack.setId(id);
		persistBackpack.setBackpack(PersistBackpack.backpackData(getBackpack()));
		if (hasBackpack)
			persistBackpack.update();
		else {
			persistBackpack.save();
			hasBackpack = true;
		}
		backpackChange = false;
	}

	public void logout() {
		ChessRoomManager chessRoomManager = SpringUtils
				.getBeanOfType(ChessRoomManager.class);
		chessRoomManager.leaveRoom(this);
		super.logout();
		persistAll();
	}
	
	private class BackpackListener implements PlayerEventListener {
		@Override
		public void onEvent(PlayerEvent event) {
			if (event instanceof BackpackEvent) {
				backpackChange = true;
			}
		}
	}

	@Override
	public void login() {
		super.login();
	}

	public void destroy() {

	}
	


}
