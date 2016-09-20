package com.chitu.poker.msg;

import cn.gecko.broadcast.BroadcastMessage;

import com.chitu.poker.model.PokerPlayer;

public class NewFriendNotify implements BroadcastMessage {

private FriendPlayerDto friendPlayerDto;
	
	public NewFriendNotify(PokerPlayer player){
		this.friendPlayerDto = new FriendPlayerDto(player);
	}
	
	public FriendPlayerDto getFriendPlayerDto() {
		return friendPlayerDto;
	}

	public void setFriendPlayerDto(FriendPlayerDto friendPlayerDto) {
		this.friendPlayerDto = friendPlayerDto;
	}
}
