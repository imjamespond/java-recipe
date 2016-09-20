package com.pengpeng.stargame.task.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.fashion.cmd.FashionRpc;
import com.pengpeng.stargame.gameevent.GameEventConstant;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.task.OneTask;
import com.pengpeng.stargame.model.task.TaskPlayer;
import com.pengpeng.stargame.player.container.IActivePlayerContainer;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.rsp.RspTaskFactory;
import com.pengpeng.stargame.statistics.IStatistics;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.IChaptersRuleContainer;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.task.container.impl.TaskRuleContainerImpl;
import com.pengpeng.stargame.task.dao.ITaskDao;
import com.pengpeng.stargame.task.rule.ChaptersRule;
import com.pengpeng.stargame.task.rule.TaskRule;
import com.pengpeng.stargame.vo.task.ChaptersInfoVO;
import com.pengpeng.stargame.vo.task.TaskInfoVO;
import com.pengpeng.stargame.vo.task.TaskReq;
import com.pengpeng.stargame.vo.task.TaskVO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 任务指令
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-11下午2:33
 */
@Component
public class TaskRpc extends RpcHandler {
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;

    @Autowired
    private ITaskDao taskDao;

    @Autowired
    private RspTaskFactory rspTaskFactory;

    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IChaptersRuleContainer chaptersRuleContainer;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    IActivePlayerContainer activePlayerContainer;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private FashionRpc fashionRpc;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Autowired
    private IStatistics statistics;
    @Autowired
    private StatusRemote statusService;
    private static  final Logger logger = Logger.getLogger("rpc");

    @RpcAnnotation(cmd = "task.list", req = TaskReq.class, name = "取得任务列表", vo = TaskInfoVO.class)
    public TaskInfoVO getTaskList(Session session, TaskReq req) {
        TaskPlayer tp = taskDao.getBean(session.getPid());
//        if(taskRuleContainer.autoAcceptTask(session.getPid()).size()>0){
//            return rspTaskFactory.newTaskInfoVO(tp,1);
//        }
        return rspTaskFactory.newTaskInfoVO(tp, taskRuleContainer.autoAcceptTask(tp));
    }

    @RpcAnnotation(cmd = "task.finish", req = TaskReq.class, name = "立即完成任务", vo = TaskVO.class)
    public TaskVO finished(Session session, TaskReq req) throws AlertException {

        TaskPlayer tp = taskDao.getBean(session.getPid());
        Player player = playerDao.getBean(session.getPid());

        taskRuleContainer.checkOneFinish(player, req.getId());

        OneTask oneTask = taskRuleContainer.oneFinish(player, req.getId(), tp);

//        taskRuleContainer.addTaskReward(req.getId(),session.getPid());
        taskDao.saveBean(tp);

        BroadcastHolder.putStatus(3);

        TaskRule rule = taskRuleContainer.getElement(req.getId());
        TaskVO vo = rspTaskFactory.newOneTaskVO(session.getPid(), rule, oneTask, 0);

        return vo;
    }

    @RpcAnnotation(cmd = "task.getreward", req = TaskReq.class, name = "领取任务奖励", vo = TaskVO.class, lock = true)
    public TaskVO getreward(Session session, TaskReq req) throws AlertException, RuleException {
        TaskPlayer tp = taskDao.getBean(session.getPid());
        Player player = playerDao.getBean(session.getPid());
        /**
         * 检测任务是否完成
         */
        OneTask oneTask = taskRuleContainer.checkFinished(tp, req.getId());

        TaskRule rule = taskRuleContainer.getElement(req.getId());

        int addP = 1;
        /**
         * 检测能否领取奖励 (背包容量够不够)
         */
        if (rule.getType() != TaskConstants.TASK_TYPE_3) {
            req.setType(3);
        }
        if (req.getType() == 4) {  //类型等于四  完美领取
            if (player.getGoldCoin() < TaskConstants.GET_GOLD) {
                exceptionFactory.throwAlertException("goldcoin.notenough");
            }
            addP = taskRuleContainer.getAddP(session.getPid());//完美领取 增加的倍数
        }

        taskRuleContainer.checkGetReward(req.getId(), session.getPid());
        /**
         * 检测背包
         */

        for (int i = 0; i < addP; i++) {
            taskRuleContainer.checkGetReward(req.getId(), session.getPid());
        }


        /**
         * 完成任务
         */
        taskRuleContainer.finished(tp, oneTask);

        if (req.getType() == 4) {
//            player.decGoldCoin(TaskConstants.GET_GOLD);
            playerRuleContainer.decGoldCoin(player,TaskConstants.GET_GOLD,PlayerConstant.GOLD_ACTION_15);
            playerDao.saveBean(player);
            Session[] mysessions = {session};
            frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        }
        /**
         * 保存 任务数据
         */
        taskDao.saveBean(tp);

        /**
         * 领取奖励
         */

        for (int i = 0; i < addP; i++) {
            taskRuleContainer.addTaskReward(req.getId(), session.getPid());
        }



        BroadcastHolder.putStatus(3);


        /**
         * 如果  此任务是 章节的最后一个任务，那么 发放章节奖励
         */
        ChaptersRule chaptersRule = chaptersRuleContainer.getChaptersRuleByTaskId(req.getId());
        if (chaptersRule != null) {
            chaptersRuleContainer.addChaptersReward(chaptersRule.getChaptersId(), tp.getPid());
            Session[] mysessions = {session};
            frontendService.broadcast(mysessions, rspTaskFactory.newchaptersRewardVO(chaptersRule));
        }
        TaskVO vo = rspTaskFactory.newOneTaskVO(session.getPid(), rule, oneTask, req.getType());

        /**
         * 活跃度
         */
        activePlayerContainer.finish(session,session.getPid(),rule.getId());
        /**
         * 完成任务的时候  判断 是否i是圣诞任务跟元旦任务 如果是 返回时装广播
         */
        if(rule.getId().equals("task_9041")){
            /**
             * 广播数据
             */
            String channelId = SessionUtil.getChannelScene(session);
            Session[] sessions = statusService.getMember(session, channelId);
            frontendService.broadcast(sessions,fashionRpc.getPlayerFashionVOByPlayerId(session.getPid()));

        }
        //日志
        String value = req.getId() +GameLogger.SPLIT+rule.getType()+GameLogger.SPLIT+rule.getSubtype();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_5, session.getPid(), value));

        return vo;
    }

    @RpcAnnotation(cmd = "task.chapters", req = TaskReq.class, name = "获取章节信息", vo = ChaptersInfoVO.class)
    public ChaptersInfoVO chapters(Session session, TaskReq req) throws AlertException, RuleException {

        TaskPlayer tp = taskDao.getBean(session.getPid());
        return rspTaskFactory.newChaptersInfoVO(chaptersRuleContainer.getElement(req.getCharpterId()), taskRuleContainer.getAllTasksByChaptersId(req.getCharpterId()), tp);
    }

    @RpcAnnotation(cmd = "task.finishcourse", req = TaskReq.class, name = "完成新手教程任务", vo = TaskVO.class)
    public TaskVO finishcourse(Session session, TaskReq req) throws AlertException, RuleException {
        TaskPlayer tp = taskDao.getBean(session.getPid());
        Player player=playerDao.getBean(session.getPid());

        if (tp == null) {
            return null;
        }
//        logger.info(">>>>>>>>>>>>>>>>>>完成新手任务："+req.getId()+"玩家Id:"+session.getPid());
        taskRuleContainer.checkFinishedCourse(tp, req.getId());
        OneTask oneTask = taskRuleContainer.finishedcourse(req.getId(), tp);

        taskDao.saveBean(tp);
        /**
         * 统计 首次登录玩家
         */
        statistics.recordFinishNewTask(session.getPid(),player,req.getId());

        TaskRule rule = taskRuleContainer.getElement(req.getId());
        TaskVO vo = rspTaskFactory.newOneTaskVO(session.getPid(), rule, oneTask, 0);

        return vo;

    }

    @RpcAnnotation(cmd = "task.finishspecial", req = TaskReq.class, name = "完成特殊类型任务需要客户端通知", vo = TaskVO.class)
    public void finishspecial(Session session, TaskReq req) throws AlertException, RuleException {
        taskRuleContainer.checkFinishspecialTask(req.getTaskType());
        taskRuleContainer.updateTaskNum(session.getPid(), req.getTaskType(), "", 1);

    }
}
