package com.chitu.poker.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.gecko.broadcast.annotation.Response;
import cn.gecko.broadcast.annotation.Responses;
import cn.gecko.commons.data.StaticConfig;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.friend.controller.BaseFriendController;
import cn.gecko.friend.model.Friend;
import cn.gecko.friend.model.Friend.FriendType;
import cn.gecko.friend.model.FriendErrorCodes;
import cn.gecko.friend.model.FriendHolder;
import cn.gecko.player.model.PlayerErrorCodes;
import cn.gecko.player.msg.ListDto;

import com.chitu.poker.data.StaticPlayerGrade;
import com.chitu.poker.mail.PokerMail;
import com.chitu.poker.mail.PokerMailManager;
import com.chitu.poker.mail.msg.SnsNotifyMailDto.SnsNotifyType;
import com.chitu.poker.model.PersistPokerPlayer;
import com.chitu.poker.model.PokerErrorCodes;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.model.PokerStaticConfigs;
import com.chitu.poker.msg.FriendPlayerDto;
import com.chitu.poker.msg.NewFriendNotify;
import com.chitu.poker.service.PokerPlayerManager;

/**
 * 社交操作类
 * @author open
 *
 */
@Controller
public class SnsController extends BaseFriendController{

	public SnsController() {
		MAX_FRIEND_COUNT_KEY = PokerStaticConfigs.MAX_FRIEND_COUNT;
		MAX_BLACK_COUNT_KEY = PokerStaticConfigs.MAX_BLACK_COUNT;
		MAX_GROUP_COUNT_KEY = PokerStaticConfigs.MAX_GROUP_COUNT;
	}
	
	@Autowired
	private PokerPlayerManager playerManager;
	
	/**
	 * 好友列表，元素为FriendPlayerDto
	 */
	public ListDto friendList() {
		PokerPlayer player = playerManager.getRequestPlayer();
		Collection<Friend> friends = player.friendHolder.getFriends();
		List<FriendPlayerDto> dtos = new ArrayList<FriendPlayerDto>(friends.size());
		for (Friend friend : friends) {
			PokerPlayer p = playerManager.getAnyPlayerById(friend.mateId);
			if (p != null)
				dtos.add(new FriendPlayerDto(p));
		}
		return new ListDto(dtos);
	}
	
	/**
	 * 发送添加好友请求
	 * @param mateNameString 目标好友名称
	 */
	public void sendInviteFriend(String mateNameString) {
		PokerPlayer player = playerManager.getRequestPlayer();
		if(mateNameString.equalsIgnoreCase(player.nickname)){
			throw new GeneralException(FriendErrorCodes.CAN_NOT_FRIEND_SELF);
		}
		
		PersistPokerPlayer persistPlayer = PersistPokerPlayer.getByNickname(mateNameString);
		if(persistPlayer == null){
			throw new GeneralException(PlayerErrorCodes.PLAYER_NULL);
		}
		PokerPlayer matePlayer = playerManager.getAnyPlayerById(persistPlayer.getId());
		
		FriendHolder myHolder = getHolder(player);
		if (myHolder.isMyFriend(matePlayer.id))
			throw new GeneralException(FriendErrorCodes.ALREADY_FRIEND);
		if (myHolder.isBlack(matePlayer.id))
			throw new GeneralException(FriendErrorCodes.ALREADY_BLACK);
		StaticPlayerGrade staticData = StaticPlayerGrade.get(player.grade);
		int maxCount = staticData.getMaxFriend();
		if (myHolder.friendCount() >= maxCount)
			throw new GeneralException(FriendErrorCodes.FRIEND_BEYOND, maxCount);
			
		FriendHolder mateHolder = getHolder(matePlayer);
		List<Long> mateInvites = mateHolder.getInvites();
		if(mateInvites.contains(player.id)){
			return;
		}
		mateHolder.addInvite(player.id);
		
		PokerMailManager mailManager =  SpringUtils.getBeanOfType(PokerMailManager.class);
		PokerMail mail = mailManager.snsNotifyMail(player, SnsNotifyType.Invite);
		mailManager.sendMail(mail, matePlayer.id);
	}
	
	
	/**
	 * 发送添加好友请求
	 * @param mateIdStrings 目标好友群ID,好友ID以","相间隔
	 */
	public void sendInviteFriends(String mateIdStrings){
		if(StringUtils.isBlank(mateIdStrings)){
			return;
		}
		
		PokerPlayer player = playerManager.getRequestPlayer();
		StaticPlayerGrade staticData = StaticPlayerGrade.get(player.grade);
		if(player.friendHolder.friendCount() >= staticData.getMaxFriend()){
			throw new GeneralException(PokerErrorCodes.FRIENDNUM_REACH_MAX);
		}
		
		FriendHolder myHolder = getHolder(player);
		String[] mateIds = mateIdStrings.split(",");
		
		int maxCount = staticData.getMaxFriend();
		if (myHolder.friendCount()+mateIds.length >= maxCount)
			throw new GeneralException(FriendErrorCodes.FRIEND_BEYOND, maxCount);
		
		PokerMailManager mailManager =  SpringUtils.getBeanOfType(PokerMailManager.class);
		for(String mateIdStr : mateIds){
			Long mateId = Long.valueOf(mateIdStr);
			if (player.id == mateId){
				continue;
			}
			if (myHolder.isMyFriend(mateId)){
				continue;
			}
			if (myHolder.isBlack(mateId)){
				continue;
			}
			
			PokerPlayer matePlayer = playerManager.getAnyPlayerById(mateId);
			FriendHolder mateHolder = getHolder(matePlayer);
			List<Long> mateInvites = mateHolder.getInvites();
			if(mateInvites.contains(player.id)){
				continue;
			}
			mateHolder.addInvite(player.id);
			
			PokerMail mail = mailManager.snsNotifyMail(player, SnsNotifyType.Invite);
			mailManager.sendMail(mail, mateId);
		}
	}
	
	
	/**
	 * 回复好友请求
	 * @param mateIdString 请求者ID
	 * @param action 操作项 0:忽略或拒绝 1:同意
	 */
	@Responses({ @Response(type = NewFriendNotify.class, broadcast = true, desc = "回复好友请求") })
	public void replyInviteFriend(String mateIdString, int action) {
		PokerPlayer player = playerManager.getRequestPlayer();
		FriendHolder myHolder = getHolder(player);
		long mateId = Long.valueOf(mateIdString);
		myHolder.removeInvite(mateId);
		
        if(action == 0){
			return;
		}
		
        //同意请求
        PokerPlayer matePlayer = playerManager.getAnyPlayerById(mateId);
        FriendHolder mateHolder = getHolder(matePlayer);
		if (myHolder.isBlack(mateId))
			throw new GeneralException(FriendErrorCodes.ALREADY_BLACK);
		int maxCount = StaticConfig.get(MAX_FRIEND_COUNT_KEY).getAsInt(100);
		if (myHolder.friendCount() >= maxCount)
			throw new GeneralException(FriendErrorCodes.FRIEND_BEYOND, maxCount);
		if (mateHolder.isBlack(player.id))
			throw new GeneralException(FriendErrorCodes.ALREADY_BLACK_MATE);
		if (mateHolder.friendCount() >= maxCount)
			throw new GeneralException(FriendErrorCodes.FRIEND_FULL_MATE, maxCount);
		
		//标记互为好友
		myHolder.addFriend(mateId, 0);
		mateHolder.addFriend(player.id, 0);
		myHolder.updateFriendType(mateId, FriendType.EachOther);
		mateHolder.updateFriendType(player.id, FriendType.EachOther);
		
		if (matePlayer.isConnected()){
			matePlayer.deliver(new NewFriendNotify(player));
		}
		player.deliver(new NewFriendNotify(matePlayer));
	}
	
	
	/**
	 * 移除好友
	 * 
	 * @param mateIdString对方用户的唯一标识以“,”分隔
	 */
	public ListDto removeFriends(String mateIdString) {
		PokerPlayer player = playerManager.getRequestPlayer();
		
		List<String> mateIdStr = new ArrayList<String>();
		String[] mateIdStrArry = mateIdString.split(",");
		for(int i=0;i<mateIdStrArry.length;i++){
			long mateId = Long.valueOf(mateIdStrArry[i]);
			FriendHolder myHolder = getHolder(player);
			myHolder.removeFriend(mateId);
			PokerPlayer matePlayer = playerManager.getAnyPlayerById(mateId);
			FriendHolder mateHolder = getHolder(matePlayer);
			mateHolder.removeFriend(player.id);
			mateIdStr.add(mateIdStrArry[i]);
		}
		return new ListDto(mateIdStr);
	}
	
}
