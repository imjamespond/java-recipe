package com.chitu.poker.msg;

import cn.gecko.broadcast.GeneralResponse;

import com.chitu.poker.model.PersistPokerPlayer;

public class RoleDto extends GeneralResponse {

	private String id;
	private int grade;
	private String nickname;
	
	public RoleDto(PersistPokerPlayer ptp){
		this.id = String.valueOf(ptp.getId());
		this.grade = ptp.getGrade();
		this.nickname = ptp.getNickname();
	}

	/**玩家ID**/
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**角色等级**/
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**角色呢称**/
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	
}
