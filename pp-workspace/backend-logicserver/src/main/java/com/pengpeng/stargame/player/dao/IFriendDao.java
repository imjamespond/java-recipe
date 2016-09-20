package com.pengpeng.stargame.player.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.model.player.FriendItem;
import com.pengpeng.stargame.util.Page;

import java.util.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-3-12下午7:52
 */
public interface IFriendDao extends BaseDao<String,Friend> {

    /**
     * 好友分页
     * @param no
     * @param size
     * @return
     */
    public Page<FriendItem> findPage(Friend friend,int no, int size);

    public int getFriendNum(String pid);
}
