package com.pengpeng.stargame.stall.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.stall.PlayerStall;
import com.pengpeng.stargame.stall.rule.StallGoldShelfRule;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:03
 */
public interface IStallGoldShelfContainer extends IMapContainer<String,StallGoldShelfRule> {
    /**
     * 获取需要达人币数
     * @param
     * @return
     */
    public int getGold(int shelfNum);

    void enable(PlayerStall ps, Player p) throws AlertException;
}
