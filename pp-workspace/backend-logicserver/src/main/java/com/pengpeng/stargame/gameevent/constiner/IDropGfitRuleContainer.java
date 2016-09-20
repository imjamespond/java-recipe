package com.pengpeng.stargame.gameevent.constiner;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.gameevent.rule.DropGiftRule;
import com.pengpeng.stargame.gameevent.rule.EventRule;

import java.util.List;

/**
 * User: mql
 * Date: 13-12-20
 * Time: 上午10:39
 */
public interface IDropGfitRuleContainer extends IMapContainer<String,DropGiftRule> {
    public List<DropGiftRule> getByType(int type);
}
