package com.chitu.poker.model;

import java.sql.Timestamp;
import java.util.List;

import cn.gecko.broadcast.MessageUtils;
import cn.gecko.broadcast.api.ConnectorManager;
import cn.gecko.commons.data.BillType;
import cn.gecko.commons.logs.LogUtils;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.friend.FriendModule;
import cn.gecko.friend.model.FriendHolder;
import cn.gecko.friend.model.PersistFriend;
import cn.gecko.mail.MailModule;
import cn.gecko.mail.model.MailHolder;
import cn.gecko.mail.model.PersistMail;
import cn.gecko.player.event.PlayerGradeEvent;
import cn.gecko.player.event.PlayerHeartbeatEvent;
import cn.gecko.player.event.PlayerPersistEvent;
import cn.gecko.player.model.Player;

import com.chitu.poker.arena.ArenaHolder;
import com.chitu.poker.battle.BattleHolder;
import com.chitu.poker.battle.BattleInviteHolder;
import com.chitu.poker.data.StaticPlayerGrade;
import com.chitu.poker.mail.PokerMail;
import com.chitu.poker.mail.PokerPersistMail;
import com.chitu.poker.msg.CapabilityNotify;
import com.chitu.poker.msg.ExpNotify;
import com.chitu.poker.msg.FriendPointNotify;
import com.chitu.poker.msg.StrengthNotify;
import com.chitu.poker.pet.PetHolder;
import com.chitu.poker.service.PokerPlayerManager;
import com.chitu.poker.setting.SettingHolder;
import com.chitu.poker.store.StoreHolder;

public class PokerPlayer extends Player implements MailModule, FriendModule {

	public static int SAVE_ON_HEARTBEAT = 10;

	private int exp;
	public int strength;
	public Timestamp createTime;
	public Timestamp loginTime;
	public int onlineTime;
	public int newbieStep;
	public int friendPoint;
	public int capability;

	public FriendHolder friendHolder;
	public MailHolder mailHolder;
	public WealthHolder wealthHolder;
	public PetHolder petHolder;
	public BattleHolder battleHolder;
	public StoreHolder storeHolder;
	public SettingHolder settingHolder;
	public BattleInviteHolder battleInviteHolder;
	public ArenaHolder arenaHolder;

	private int heartbeatCount = 0;
	private PersistPokerPlayer persistPlayer;

	public PokerPlayer(int sid, ConnectorManager connectorManager) {
		super(sid, connectorManager);
	}

	public void update() {
		// 保存战斗信息
		battleHolder.persist();
		// 保存好友
		friendHolder.persist();
		// 保存宠物
		petHolder.persist();
		// 商店
		storeHolder.persist();
		// 设置信息
		settingHolder.persist();
		// 战斗邀请信息
		battleInviteHolder.persist();
		// 竞技场
		arenaHolder.persist();
		// 保存用户信息
		updatePlayer();
	}

	protected void updatePlayer() {
		persistPlayer.setGrade(grade);
		persistPlayer.setExp(exp);
		persistPlayer.setStrength(strength);
		persistPlayer.setOnlineTime(onlineTime);
		persistPlayer.setNewbieStep(newbieStep);
		persistPlayer.setFriendPoint(friendPoint);
		persistPlayer.setCapability(capability);
		persistPlayer.update();
	}

	public void heartbeat() {
		heartbeatCount++;
		onlineTime++;
		battleInviteHolder.heartbeat();
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
		PersistPokerPlayer player = PersistPokerPlayer.get(id);
		init(player);
	}

	public void init(PersistPokerPlayer player) {
		PokerPlayer offplayer = SpringUtils.getBeanOfType(PokerPlayerManager.class).removeOffPlayer(player.getId());
		if (offplayer != null) {
			// 保存离时修改的数据
			offplayer.persistAll();
			this.persistPlayer = offplayer.persistPlayer;
		} else {
			this.persistPlayer = player;
		}

		this.persistPlayer = player;
		this.id = persistPlayer.getId();
		this.accountId = persistPlayer.getAccountId();
		this.grade = player.getGrade();
		this.exp = player.getExp();
		this.strength = player.getStrength();
		this.nickname = player.getNickname();
		this.onlineTime = player.getOnlineTime();
		this.createTime = persistPlayer.getCreateTime();
		this.loginTime = persistPlayer.getLoginTime();
		this.newbieStep = persistPlayer.getNewbieStep();
		this.capability = persistPlayer.getCapability();

		initFriendHolder();
		initMailHolder();
		initWealth();
		initPetHolder();
		initBattleHolder();
		initStoreHolder();
		initSettingHolder();
		initBattleInviteHolder();
		initArenaHolder();
	}
	
	protected void initArenaHolder(){
		arenaHolder = new ArenaHolder(id);
	}

	protected void initBattleInviteHolder() {
		battleInviteHolder = new BattleInviteHolder(id);
	}

	protected void initSettingHolder() {
		settingHolder = new SettingHolder(id);
	}

	protected void initStoreHolder() {
		storeHolder = new StoreHolder(id);
	}

	protected void initBattleHolder() {
		battleHolder = new BattleHolder(id);
	}

	protected void initPetHolder() {
		petHolder = new PetHolder(id);
	}

	protected void initWealth() {
		wealthHolder = new WealthHolder(persistPlayer);
	}

	protected void initFriendHolder() {
		friendHolder = new FriendHolder(id) {
			@Override
			protected PersistFriend instancePersistFriend() {
				return new PokerPersistFriend();
			}

			@Override
			protected PersistFriend getPersistFriend(long playerId) {
				return PokerPersistFriend.get(playerId);
			}
		};
	}

	protected void initMailHolder() {
		mailHolder = new MailHolder(id) {

			@Override
			public PersistMail instancePersistMail() {
				return new PokerPersistMail();
			}

			@Override
			protected List<? extends PersistMail> getPersistMails(long playerId) {
				return PokerPersistMail.gets(playerId);

			}

			@Override
			public PersistMail getPersistMail(long id) {
				return PokerPersistMail.get(id);
			}

			@Override
			public PokerMail createMail() {
				return new PokerMail();
			}
		};
	}

	@Override
	public FriendHolder getFriendHolder() {
		return friendHolder;
	}

	@Override
	public MailHolder getMailHolder() {
		return mailHolder;
	}

	@Override
	public void login() {
		super.login();
	}

	public void logout() {
		super.logout();
		persistAll();
	}

	public void destroy() {
		petHolder.destroy();
		storeHolder.destroy();
		settingHolder.destroy();
		friendHolder.destroy();
		mailHolder.destroy();
		arenaHolder.destroy();
	}

	public int getExp() {
		return this.exp;
	}

	/**
	 * 增加经验
	 * 
	 * @param exp
	 * @param expType
	 * @param desc
	 */
	public void incExp(int exp, ExpType expType, String desc) {
		if (exp <= 0) {
			return;
		}
		if (this.grade >= StaticPlayerGrade.MAX_GRADE) {
			return;
		}

		StaticPlayerGrade staticData = StaticPlayerGrade.get(this.grade);
		int totleExp = staticData.getTotalExp() - staticData.getUpdateExp() + this.exp + exp;
		StaticPlayerGrade data = StaticPlayerGrade.getStaticPlayerGrade(totleExp);

		ExpLog expLog = new ExpLog(id, exp, expType, desc);
		expLog.setBeforeGrade(this.grade);
		expLog.setBeforeExp(this.exp);
		if (data == null) {
			this.grade = StaticPlayerGrade.MAX_GRADE;
			this.exp = 0;
		} else {
			this.grade = data.getId();
			this.exp = totleExp - (data.getTotalExp() - data.getUpdateExp());
		}
		expLog.setAfterGrade(this.grade);
		expLog.setAfterExp(this.exp);
		LogUtils.log("exp.log", expLog);

		if (isConnected()) {
			MessageUtils.addMessage(this, new ExpNotify(this));
			if (expLog.getBeforeGrade() != expLog.getAfterGrade()) {
				dispatchEvent(new PlayerGradeEvent(expLog.getBeforeGrade(), expLog.getAfterGrade()));
			}
		}
	}

	public void increaseFriendPoint(int point, BillType billType, String desc) {
		if (point <= 0) {
			throw new GeneralException(PokerErrorCodes.NEGATIVE_ERROR);
		}

		int before = friendPoint;
		friendPoint += point;
		int after = friendPoint;
		deliver(new FriendPointNotify(this));
		LogUtils.log("friendPoint.log", new WealthLog(id, point, before, after, billType, desc));
	}

	public void decreaseFriendPoint(int point, BillType billType, String desc) {
		if (point < 0) {
			throw new GeneralException(PokerErrorCodes.NEGATIVE_ERROR);
		}
		if (friendPoint < point) {
			throw new GeneralException(PokerErrorCodes.FRIEND_POINT_NOT_ENOUGH);
		}

		int before = friendPoint;
		friendPoint -= point;
		int after = friendPoint;
		deliver(new FriendPointNotify(this));
		LogUtils.log("friendPoint.log", new WealthLog(id, point, before, after, billType, desc));
	}
	
	public void increaseStrength(int point, BillType billType, String desc) {
		if (point <= 0) {
			throw new GeneralException(PokerErrorCodes.NEGATIVE_ERROR);
		}

		int before = strength;
		strength += point;
		int after = strength;
		deliver(new StrengthNotify(this));
		LogUtils.log("strength.log", new WealthLog(id, point, before, after, billType, desc));
	}

	public void decreaseStrength(int point, BillType billType, String desc) {
		if (point < 0) {
			throw new GeneralException(PokerErrorCodes.NEGATIVE_ERROR);
		}
		if (strength < point) {
			throw new GeneralException(PokerErrorCodes.STRENGTH_NOT_ENOUGH);
		}

		int before = strength;
		strength -= point;
		int after = strength;
		deliver(new StrengthNotify(this));
		LogUtils.log("strength.log", new WealthLog(id, point, before, after, billType, desc));
	}

	/**
	 * 设置新呢称
	 * 
	 * @param newNickName
	 */
	public void setNickName(String newNickName) {
		this.nickname = newNickName;
		this.persistPlayer.setNickname(newNickName);
	}

	/**
	 * 重新计算战力
	 * @param broadcast是否发送广播
	 */
	public void curCapability(boolean broadcast) {
		this.capability = 0;

		if (broadcast) {
			deliver(new CapabilityNotify(this));
		}
	}
}
