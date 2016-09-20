package com.pengpeng.stargame.task.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.task.OneTask;
import com.pengpeng.stargame.model.task.TaskPlayer;
import com.pengpeng.stargame.task.rule.TaskRule;

import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-11下午2:40
 */
public interface ITaskRuleContainer extends IMapContainer<String,TaskRule> {
    /**
     * 检测某个任务是否完成
     * @param tp
     * @param id
     * @throws RuleException
     * @throws AlertException
     */
    public OneTask checkFinished(TaskPlayer tp, String id)throws RuleException,AlertException;

    /**
     * 领取任务奖励的时候  调用
     * @param tp
     */
    public OneTask finished(TaskPlayer tp, OneTask oneTask);

    /**
     * 检测领取奖励的时候 背包 是否已满
     * @param
     */
    public void checkGetReward(String taskId,String pid) throws AlertException;

    /**
     * 任务加奖励
     * @param
     */
    public void addTaskReward(String taskId,String pid) throws RuleException;


    /**
     * 自动 接任务
     * @param pid
     * @return
     */
    public List<String> autoAcceptTask(String pid,String code);

    /**
     * 通过类型 获取 当前数量
     * @param pid
     * @param type
     * @param relevanceId
     * @return
     */
    public int getNumById(String pid,int type,String relevanceId );

    /**
     * 检测可否一键完成
     * @param player
     * @param taskId
     */
    public void checkOneFinish(Player player,String taskId) throws AlertException;

    /**
     * 立即 完成 任务
     * @param player
     * @param taskId
     * @param taskPlayer
     */
    public OneTask oneFinish(Player player,String taskId,TaskPlayer taskPlayer);

    /**
     * 更新任务 数据
     * @param pid
     * @param type
     * @param relevanceId
     * @param num
     */
    public void updateTaskNum(String pid,int type,String relevanceId,int num);

    /**
     *
     * @param taskPlayer
     * @return
     */
    public boolean getTaskPlayerByTime(TaskPlayer taskPlayer);

    /**
     * 通过章节id  获取任务列表
     * @param chaptersId
     * @return
     */
    List<TaskRule> getAllTasksByChaptersId(String  chaptersId);

    /**
     * 检测是否是 新手任务 （新手任务才能  靠 客户端请求完成）
     * @param taskId
     * @throws AlertException
     */

    void checkFinishedCourse( TaskPlayer taskPlayer,String taskId) throws AlertException;

    /**
     *  完成新手任务
     * @param taskId
     * @param taskPlayer
     * @return
     */

    OneTask finishedcourse(String taskId,TaskPlayer taskPlayer);

    /**
     *
     * @param taskId
     * @param taskPlayer
     */

    void gmFinish(String taskId,TaskPlayer taskPlayer);

    /**
     *  检测是否可以完成 特殊任务
     */
    void checkFinishspecialTask(int type) throws AlertException;

    /**
     * 取得任务列表
     * @param taskPlayer
     * @return
     */
    public List<String> autoAcceptTask(TaskPlayer taskPlayer);

    /**
     * 完成当前 新手任务
     * @param pId
     */
    public void finishCurrentNewTask(String pId);


    /**
     * 完成所有的 新手任务
     * @param pId
     */
    public void finishAllNewTask(String pId);

    /**
     * 获取 家族任务完美领取 添加的倍数
     * @param pid
     * @return
     */
    public int getAddP(String pid);

    /**
     * 判断玩家是否完成了所有的新手任务
     * @param pid
     * @return
     */
    public boolean isFinishAllNewTask(String pid);
}
