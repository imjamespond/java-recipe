package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.model.task.OneTask;
import com.pengpeng.stargame.model.task.TaskConditionInfo;
import com.pengpeng.stargame.model.task.TaskPlayer;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.IChaptersRuleContainer;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.task.rule.ChaptersRule;
import com.pengpeng.stargame.task.rule.TaskRule;
import com.pengpeng.stargame.vo.task.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-15下午2:37
 */
@Component
public class RspTaskFactory extends RspFactory {
    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private IChaptersRuleContainer chaptersRuleContainer;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;

    public TaskInfoVO newTaskInfoVO(TaskPlayer tp, List newIds) {
        TaskInfoVO taskInfoVO = new TaskInfoVO();
//        taskInfoVO.setHasnew(hasnew);

        if (!tp.isFinished(TaskConstants.LAST_NEW_TASK)) {
            if (tp.getChaptersId() != null) {
                ChaptersRule chaptersRule = chaptersRuleContainer.getElement(tp.getChaptersId());
                taskInfoVO.setChaptersId(tp.getChaptersId());
                taskInfoVO.setChaptersName(chaptersRule.getName());
            }
        }

        taskInfoVO.setTaskVOs(newTaskVO(tp,newIds));
        taskInfoVO.setAddP(taskRuleContainer.getAddP(tp.getPid()));
        taskInfoVO.setNeedGold(TaskConstants.GET_GOLD);
        return taskInfoVO;

    }

    public ChaptersInfoVO newChaptersInfoVO(ChaptersRule chaptersRule, List<TaskRule> taskRuleList, TaskPlayer taskPlayer) {
        ChaptersInfoVO chaptersInfoVO = new ChaptersInfoVO();
        if (chaptersRule != null) {
            chaptersInfoVO.setChaptersId(chaptersRule.getChaptersId());
            chaptersInfoVO.setChaptersName(chaptersRule.getName());
        }

        List<TaskVO> taskVOList = new ArrayList<TaskVO>();
        for (TaskRule rule : taskRuleList) {
            TaskVO taskVO = new TaskVO();
            taskVO.setId(rule.getId());
            taskVO.setIcon(rule.getIcon());
            taskVO.setMemo(rule.getMemo());
            taskVO.setName(rule.getName());
            taskVO.setGold(rule.getGold());
            taskVO.setType(rule.getType());
            if (taskPlayer.getFinished().contains(rule.getId())) {
                taskVO.setState(1);
            } else {
                taskVO.setState(0);
            }

            taskVO.setLink(rule.getLink());
            taskVO.setLinkDesc(rule.getLinkDesc());

            taskVOList.add(taskVO);
        }
        chaptersInfoVO.setTaskVOs(taskVOList.toArray(new TaskVO[0]));
        return chaptersInfoVO;
    }

    public TaskVO[] newTaskVO(TaskPlayer tp,List newIds) {

        List<TaskVO> taskVOList = new ArrayList<TaskVO>();
        for (OneTask oneTask : tp.getAllTasks()) {
            TaskVO taskVO=  newOneTaskVO(tp.getPid(), taskRuleContainer.getElement(oneTask.getTaskId()), oneTask,0) ;
            if(newIds.contains(taskVO.getId())){
                taskVO.setNewstate(1);
            }
            taskVOList.add(taskVO);
        }


        return taskVOList.toArray(new TaskVO[0]);
    }

    public TaskVO newOneTaskVO(String pId, TaskRule rule, OneTask oneTask,int type) {
        TaskVO taskVO = new TaskVO();

        taskVO.setId(rule.getId());
        taskVO.setIcon(rule.getIcon());
        taskVO.setMemo(rule.getMemo());
        taskVO.setName(rule.getName());
        taskVO.setGold(rule.getGold());
        taskVO.setType(rule.getType());
        taskVO.setState(oneTask.getStatus());
        taskVO.setLink(rule.getLink());
        taskVO.setLinkDesc(rule.getLinkDesc());
        taskVO.setNewTask(rule.getNewtTask());

//        BeanUtils.copyProperties(rule,taskVO);


        /**
         * 设置 任务 条件
         */
        if (rule.getNewtTask() != 1) {//如果是新手任务 就不用设置 任务达成的条件
            String[] dess = rule.getConditionsDesc().split(";");
            List<TaskConditionVO> taskConditionVOList = new ArrayList<TaskConditionVO>();
            int i = 0;
            for (TaskConditionInfo taskConditionInfo : oneTask.getTaskConditionInfoList()) {
                TaskConditionVO taskConditionVO = new TaskConditionVO();
                BeanUtils.copyProperties(taskConditionInfo, taskConditionVO);

                if (TaskConstants.C_TASK_TYPE.contains(taskConditionInfo.getType())) {
                    taskConditionVO.setmNum(taskRuleContainer.getNumById(pId, taskConditionInfo.getType(), taskConditionInfo.getId()));
                }
                if(dess.length>i){
                    taskConditionVO.setDes(dess[i]);
                }
                i++;
                taskConditionVOList.add(taskConditionVO);

            }
            taskVO.setConditions(taskConditionVOList.toArray(new TaskConditionVO[0]));
        }


        /**
         * 设置奖励
         */
        List<TaskRewardVO> taskRewardVOList = new ArrayList<TaskRewardVO>();
        int  add=1;
        if(type==4){
            add=taskRuleContainer.getAddP(pId);
        }
        if (rule.getGameCoin() > 0) {
            taskRewardVOList.add(new TaskRewardVO("", 1, rule.getGameCoin()*add));
        }
        if (rule.getFarmExp() > 0) {
            taskRewardVOList.add(new TaskRewardVO("", 2, rule.getFarmExp()*add));
        }
        if (rule.getBonusScore() > 0) {
            taskRewardVOList.add(new TaskRewardVO("", 4, rule.getBonusScore()*add));
        }
        if (rule.getFamilyDevote() > 0) {
            taskRewardVOList.add(new TaskRewardVO("", 7, rule.getFamilyDevote()*add));
        }

        if (rule.getFamilyFunds() > 0) {
            taskRewardVOList.add(new TaskRewardVO("", 6, rule.getFamilyFunds()*add));
        }
        if (rule.getZanNum() > 0) {
            taskRewardVOList.add(new TaskRewardVO("", 8, rule.getZanNum()*add));
        }
        for (TaskRule.TaskItem item : rule.getTaskItems()) {
            taskRewardVOList.add(new TaskRewardVO(item.getItemId(), 5, item.getNum()*add,baseItemRulecontainer.getElement(item.getItemId()).getName()));
        }

        taskVO.setRewards(taskRewardVOList.toArray(new TaskRewardVO[0]));

        return taskVO;
    }

    public ChaptersRewardVO newchaptersRewardVO(ChaptersRule chaptersRule) {
        ChaptersRewardVO chaptersRewardVO = new ChaptersRewardVO();
        chaptersRewardVO.setChaptersId(chaptersRule.getChaptersId());
        chaptersRewardVO.setChaptersName(chaptersRule.getName());


        /**
         * 设置奖励
         */
        List<TaskRewardVO> taskRewardVOList = new ArrayList<TaskRewardVO>();
        if (chaptersRule.getGameCoin() > 0) {
            taskRewardVOList.add(new TaskRewardVO("", 1, chaptersRule.getGameCoin()));
        }
        if (chaptersRule.getFarmExp() > 0) {
            taskRewardVOList.add(new TaskRewardVO("", 2, chaptersRule.getFarmExp()));
        }
        if (chaptersRule.getFamilyDevote() > 0) {
            taskRewardVOList.add(new TaskRewardVO("", 7, chaptersRule.getFamilyDevote()));
        }

        if (chaptersRule.getFamilyFunds() > 0) {
            taskRewardVOList.add(new TaskRewardVO("", 6, chaptersRule.getFamilyFunds()));
        }

        for (ItemData item : chaptersRule.getItemRewardList()) {
            taskRewardVOList.add(new TaskRewardVO(item.getItemId(), 5, item.getNum(),baseItemRulecontainer.getElement(item.getItemId()).getName()));
        }

        chaptersRewardVO.setRewards(taskRewardVOList.toArray(new TaskRewardVO[0]));

        return chaptersRewardVO;
    }
}
