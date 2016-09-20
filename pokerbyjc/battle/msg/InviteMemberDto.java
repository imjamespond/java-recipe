package com.chitu.poker.battle.msg;

import com.chitu.poker.battle.InviteMember;
import com.chitu.poker.pet.msg.PetDto;

import cn.gecko.broadcast.BroadcastMessage;

/**
 * 
 * @author ivan
 * 
 */
public class InviteMemberDto implements BroadcastMessage {

	private String inviteId;

	private String name;

	private int grade;

	private int friendPoint;

	private PetDto pet;

	public InviteMemberDto(InviteMember inviteMember) {
		this.inviteId = String.valueOf(inviteMember.inviteId);
		this.name = inviteMember.name;
		this.grade = inviteMember.grade;
		this.friendPoint = inviteMember.friendPoint;
		pet = inviteMember.pet.toDto();
	}

	/**
	 * 邀请者ID
	 * 
	 * @return
	 */
	public String getInviteId() {
		return inviteId;
	}

	public void setInviteId(String inviteId) {
		this.inviteId = inviteId;
	}

	/**
	 * 邀请者名字
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 邀请者等级
	 * 
	 * @return
	 */
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**
	 * 邀请者友情点
	 * 
	 * @return
	 */
	public int getFriendPoint() {
		return friendPoint;
	}

	public void setFriendPoint(int friendPoint) {
		this.friendPoint = friendPoint;
	}

	/**
	 * 邀请者主宠信息
	 * 
	 * @return
	 */
	public PetDto getPet() {
		return pet;
	}

	public void setPet(PetDto pet) {
		this.pet = pet;
	}

}
