package com.pengpeng.stargame.task.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.task.rule.ChaptersRule;
import com.pengpeng.stargame.task.rule.TaskRule;

/**
 * User: mql
 * Date: 13-8-2
 * Time: 下午2:25
 */
public interface IChaptersRuleContainer extends IMapContainer<String,ChaptersRule> {
    /**
     * 通过任务id  获取章节信息，如果 有，表示此任务是该章节最后一个任务
     * @param taskId
     * @return
     */
    ChaptersRule getChaptersRuleByTaskId(String taskId);

    /**
     *章节加奖励
     * @param
     */
    public void addChaptersReward(String chaptersId,String pid);



}
