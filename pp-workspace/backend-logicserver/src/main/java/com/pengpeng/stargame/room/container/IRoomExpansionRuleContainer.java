package com.pengpeng.stargame.room.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.room.rule.RoomExpansionRule;

/**
 * User: mql
 * Date: 13-11-22
 * Time: 下午3:55
 */
public interface IRoomExpansionRuleContainer extends IMapContainer<Integer,RoomExpansionRule> {

    public void extensionFinish(RoomPlayer roomPlayer,Player player) throws AlertException;

    public int  extensionNeedGold(RoomPlayer roomPlayer);

    public void extensionStart(RoomPlayer roomPlayer,Player player) throws AlertException;
}
