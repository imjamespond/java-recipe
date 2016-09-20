package com.chitu.chess.msg;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.GeneralResponse;
import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;

import com.chitu.chess.model.ChessPlayer;
import com.chitu.chess.model.Mission;
import com.chitu.chess.model.ChessPlayer.Gender;
import com.chitu.chess.model.ChessUtils;

/**
 * 用户数据Dto
 * 
 * @author ivan
 * 
 */
/**
 * @author Administrator
 *
 */
@IncludeEnums({ @IncludeEnum(Gender.class) })
public class ChessPlayerDto extends GeneralResponse implements BroadcastMessage {

	private String id;
	private int grade;
	private int exp;
	private String accountId;
	private String nickname;
	private String title;
	private int gender;
	private int money;
	private int point;
	private int rmb;
	private int prestige;
	private int battleId;
	private int avatar;
	
	private int gameAmount;
	private int victoryAmount;
	
	private boolean missionDone = false;

	public ChessPlayerDto(ChessPlayer player, boolean self) {
		this.id = String.valueOf(player.id);
		this.grade = player.grade;
		this.exp = player.exp;
		this.accountId = player.accountId;
		this.nickname = player.nickname;
		this.title = ChessUtils.point2Title(player.wealthHolder.getPoint());
		this.gender = player.gender.ordinal();
		this.battleId = player.battleId;
		this.avatar = player.avatar;
		
		if (self) {
			
			this.gameAmount = player.missionHolder.getGameAmount();
			this.victoryAmount = player.missionHolder.getVictoryAmount();
			
			this.money = player.wealthHolder.getMoney();
			this.point = player.wealthHolder.getPoint();
			this.rmb = player.wealthHolder.getRmb();
			this.prestige = player.wealthHolder.getPrestige();
			
			//有任务完成
			if(player.missionHolder.mission1.state == Mission.State.DONE ||
					player.missionHolder.mission2.state == Mission.State.DONE){
				missionDone = true;
			}
		}
	}

	/** 用户ID */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/** 等级 */
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	/** 经验 */
	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	/** 账号*/
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/** 昵称 */
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/*
	 * 玩家称号*/
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/** 性别 */
	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	/** 金币 */
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	/** 积分 */
	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
	
	/**威望**/
	public int getPrestige() {
		return prestige;
	}

	public void setPrestige(int prestige) {
		this.prestige = prestige;
	}

	/**
	 * 在进行的关卡ID，没有为0
	 * 
	 * @return
	 */
	public int getBattleId() {
		return battleId;
	}

	public void setBattleId(int battleId) {
		this.battleId = battleId;
	}

	/**头像
	 * @return
	 */
	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}

	/**游戏总局数
	 * @return
	 */
	public int getGameAmount() {
		return gameAmount;
	}

	public void setGameAmount(int gameAmount) {
		this.gameAmount = gameAmount;
	}

	/**胜利总局数
	 * @return
	 */
	public int getVictoryAmount() {
		return victoryAmount;
	}

	public void setVictoryAmount(int victoryAmount) {
		this.victoryAmount = victoryAmount;
	}

	/**有任务完成
	 * @return
	 */
	public boolean getMissionDone() {
		return missionDone;
	}

	public void setMissionDone(boolean missionDone) {
		this.missionDone = missionDone;
	}

	/**元宝
	 * @return
	 */
	public int getRmb() {
		return rmb;
	}

	public void setRmb(int rmb) {
		this.rmb = rmb;
	}

	

}
