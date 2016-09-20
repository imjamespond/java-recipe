package com.pengpeng.stargame.model.player;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.*;

/**
 * 玩家的好友数据模型
 * @author jinli.yuan@com.pengpeng.com
 * @since 13-3-25 上午10:14
 */
public class Friend extends BaseEntity<String> {
	public String pid;

    /**
     * 在审核的朋友
     */
    private Map<String,FriendItem> unknowns;
    /**
     * 审核通过的朋友
     */
    private Map<String,FriendItem> friends;

    //邀请过的好友(邀请过的好友pid)
    private Map<String,FriendItem> invites;



	public Friend() {
        unknowns = new HashMap<String, FriendItem>();
        friends = new HashMap<String,FriendItem>();
        invites = new HashMap<String,FriendItem>();
	}

	public String getId() {
		return pid;
	}

	public void setId(String id) {
		this.pid = id;
	}

	@Override
	public String getKey() {
		return this.pid;
	}

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
    public Map<String,FriendItem> getUnknownsFriends() {
        return unknowns;
    }

    public Map<String,FriendItem> getFriends() {
        return friends;
    }

    public void addFriend(FriendItem item){
        friends.put(item.getFid(),item);

    }

    public void removeFriend(String fid){
        unknowns.remove(fid);
        friends.remove(fid);
        invites.remove(fid);
    }

    public void addUnknownsFriend(FriendItem friendItem) {
        this.unknowns.put(friendItem.getFid(),friendItem);
    }

    //邀请信息
    public void invite(String fid){
        invites.put(fid, new FriendItem(fid, new Date()));
    }

    /**
     * 审核通过
     * @param fId
     */
    public void approve(String fId){
        if (unknowns.containsKey(fId)){
            FriendItem item = unknowns.get(fId);
            unknowns.remove(fId);
            addFriend(item);
        }
    }

    public void approveInvite(String fid){
        if (invites.containsKey(fid)){
            FriendItem item = invites.get(fid);
            invites.remove(fid);
            addFriend(item);
        }
    }
    /**
     * 直接加好友
     * @param fId
     */
    public void addFriend(String fId){
        FriendItem item =new FriendItem(fId,new Date()) ;
        addFriend(item);
    }

    /**
     * 审核拒绝
     * @param fId
     */
    public void reject(String fId){
        if (unknowns.containsKey(fId)){
            unknowns.remove(fId);
        }
    }
    public boolean isFriend(String fid){
        return friends.containsKey(fid);
    }

    public int getFriendSize() {
        return friends.size();
    }

    public Map<String, FriendItem> getInviteFriends() {
        return invites;
    }
}
