package com.pengpeng.stargame.player.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.model.player.ActivePlayer;
import com.pengpeng.stargame.player.rule.ActiveRule;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-13上午11:44
 */
public interface IActiveRuleContainer extends IMapContainer<Integer,ActiveRule> {

    public int getFinishNum(ActivePlayer activePlayer);

    /**
     * 通过任务Id 获取
     * @param taskId
     * @return
     */
    public ActiveRule getByTaskId(String taskId);
}
