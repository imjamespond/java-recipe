package com.pengpeng.stargame.stall.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.model.stall.PlayerStall;
import com.pengpeng.stargame.stall.rule.StallFriShelfRule;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:03
 */
public interface IStallFriShelfContainer extends IMapContainer<String,StallFriShelfRule> {
    /**
     * 获取需要好友数
     * @param
     * @return
     */
    public int getFriend(int shelfNum) ;

    void enable(PlayerStall ps, Friend f) throws AlertException;
}
