package com.pengpeng.stargame.task.dao;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.task.OneTask;
import com.pengpeng.stargame.model.task.TaskPlayer;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.task.rule.TaskRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-15下午12:25
 */
@Component
@DaoAnnotation(prefix = "task.")
public class TaskDaoImpl extends RedisDao<TaskPlayer> implements ITaskDao {
   @Autowired
    private ITaskRuleContainer taskRuleContainer;

    @Override
    public Class<TaskPlayer> getClassType() {
        return TaskPlayer.class;
    }

    @Override
    public TaskPlayer getBean(String index) {
        TaskPlayer tp  = super.getBean(index);

        if (null==tp){
            tp = new TaskPlayer(index);
            saveBean(tp);
        }

        /**
         * 数据检测   如果数据库内任务删掉，那么 清除玩家已经接受到的任务
         */

        List<String> removeIds = new ArrayList<String>();
        for (OneTask oneTask : tp.getAllTasks()) {
            TaskRule taskRule =taskRuleContainer. getElement(oneTask.getTaskId());
            if (taskRule==null) {
                removeIds.add(oneTask.getTaskId());
            }
        }
        if(removeIds.size()>0){
            for (String taskId : removeIds) {
                tp.removeTask(taskId);
            }
            saveBean(tp);
        }

        /////////////////////////////////////////////


        if(taskRuleContainer.getTaskPlayerByTime(tp)){
            saveBean(tp);
        }
        return tp;
    }
}
