package com.pengpeng.stargame.task.container.impl;

import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.cmd.FarmRpc;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.container.IFarmRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.fashion.container.IFashionGiftRuleContainer;
import com.pengpeng.stargame.fashion.container.IFashionItemRuleContainer;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.fashion.dao.IFashionPlayerDao;
import com.pengpeng.stargame.integral.container.impl.IIntegralRuleContainerImpl;
import com.pengpeng.stargame.model.GiftPlayer;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.piazza.MusicBox;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.FashionPkg;
import com.pengpeng.stargame.model.task.OneTask;
import com.pengpeng.stargame.model.task.TaskConditionInfo;
import com.pengpeng.stargame.model.task.TaskPlayer;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.container.IMusicBoxRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyBuildDao;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.dao.IMusicBoxDao;
import com.pengpeng.stargame.player.cmd.PlayerRpc;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.player.dao.IGiftPlayerDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.room.container.IRoomItemRuleContainer;
import com.pengpeng.stargame.room.dao.IRoomPlayerDao;
import com.pengpeng.stargame.rpc.BroadcastHolder;
import com.pengpeng.stargame.rsp.RspFashionPkgFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.rsp.RspTaskFactory;
import com.pengpeng.stargame.small.game.dao.IPlayerSmallGameDao;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.success.dao.IPlayerSuccessDao;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.IChaptersRuleContainer;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.task.dao.ITaskDao;
import com.pengpeng.stargame.task.rule.TaskRule;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 任务规则
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-11下午2:40
 */
@Component("taskRuleContainerImpl")
public class TaskRuleContainerImpl extends HashMapContainer<String, TaskRule> implements ITaskRuleContainer {
    @Autowired
    private ITaskDao taskDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private IFashionPlayerDao fashionPlayerDao;
    @Autowired
    private IRoomPlayerDao roomPlayerDao;
    @Autowired
    private IFashionItemRuleContainer fashionItemRuleContainer;
    @Autowired
    private IRoomItemRuleContainer roomItemRuleContainer;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFarmRuleContainer farmRuleContainer;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private PlayerRpc playerRpc;
    @Autowired
    private FarmRpc farmRpc;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private RspTaskFactory rspTaskFactory;
    @Autowired
    private IFashionGiftRuleContainer fashionGiftRuleContainer;
    @Autowired
    private IFriendDao friendDao;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    @Autowired
    private IFamilyBuildDao familyBuildDao;
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private IMusicBoxDao musicBoxDao;
    @Autowired
    private IIntegralRuleContainerImpl iIntegralRuleContainer;

    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;
    @Autowired
    private IPlayerSuccessDao playerSuccessDao;
    @Override
    public OneTask checkFinished(TaskPlayer tp, String id) throws RuleException, AlertException {
        TaskRule taskRule = getElement(id);
        OneTask oneTask = tp.getOneTask(id);
        if (oneTask == null) {
            exceptionFactory.throwAlertException("task.reward");
        }
        //先移除掉
        tp.removeTask(id);

        if (oneTask.getStatus() == 0) {   //状态为0 未完成的时候 需要检测

            for (TaskConditionInfo taskConditionInfo : oneTask.getTaskConditionInfoList()) {

                if (TaskConstants.C_TASK_TYPE.contains(taskConditionInfo.getType())) {
                    //实时统计类型的任务 数量查询
                    if (getNumById(tp.getPid(), taskConditionInfo.getType(), taskConditionInfo.getId()) < taskConditionInfo.getNum()) {
                        exceptionFactory.throwAlertException("task.no.finish");
                    }
                } else {
                    if (taskConditionInfo.getNum() > taskConditionInfo.getmNum()) {
                        exceptionFactory.throwAlertException("task.no.finish");
                    }
                }
            }
        }
        return oneTask;

    }

    @Override
    public OneTask finished(TaskPlayer tp, OneTask oneTask) {
        TaskRule taskRule = getElement(oneTask.getTaskId());
        if (oneTask.getStatus() == 0) {
            for (TaskConditionInfo taskConditionInfo : oneTask.getTaskConditionInfoList()) {
                //收集道具   需要扣除 道具
                if (taskConditionInfo.getType() == TaskConstants.CONDI_TYPE_ITEM_2) {
                    baseItemRulecontainer.useGoods(tp.getPid(), taskConditionInfo.getId(), taskConditionInfo.getNum());
                }
            }
        }
        /**
         * 如果是家族每日刷新任务 活动任务 则放到其它地方 ,家族任务是 每天刷新的
         */
        if (taskRule.getType() == TaskConstants.TASK_TYPE_3 ||taskRule.getType() == TaskConstants.TASK_TYPE_4|| taskRule.getSubtype() == 1 || taskRule.getSubtype() == 2) {
            tp.addFamilyFinishs(oneTask.getTaskId());
        } else {
            tp.addFinished(oneTask.getTaskId());
        }


        return oneTask;

    }

    @Override
    public void checkGetReward(String taskId, String pid) throws AlertException {
        TaskRule taskRule = getElement(taskId);
        FarmPackage farmPackage = farmPackageDao.getBean(pid);
        FashionCupboard fashionCupboard = fashionCupboardDao.getBean(pid);
        for (TaskRule.TaskItem taskItem : taskRule.getTaskItems()) {
            BaseItemRule baseItemRule = baseItemRulecontainer.getElement(taskItem.getItemId());
            if (baseItemRule.getType() == 1) {   //农场物品
                if (farmPackage.isMaxBySize(taskItem.getItemId(), taskItem.getNum(), baseItemRule.getOverlay())) {
                    exceptionFactory.throwAlertException("task.no.farm");
                } else {
                    farmPackage.addItem(taskItem.getItemId(), taskItem.getNum(), baseItemRule.getOverlay(),baseItemRule.getValidDete());
                }
            }
            if (baseItemRule.getType() == 2) { //时装道具
                FashionPkg fashionPkg = fashionCupboard.getFashionPkg(String.valueOf(baseItemRule.getItemtype()));
                if (fashionPkg.isMaxBySize(baseItemRule.getItemsId(),taskItem.getNum(), baseItemRule.getOverlay())) {
                    exceptionFactory.throwAlertException("task.no.fasion");
                } else {
                    fashionPkg.addItem(taskItem.getItemId(), taskItem.getNum(), baseItemRule.getOverlay(), baseItemRule.getValidDete());
                }
            }
        }


    }

    @Override
    public void addTaskReward(String taskId, String pid) throws RuleException {
        TaskRule taskRule = getElement(taskId);
        FashionCupboard fashionCupboard = fashionCupboardDao.getBean(pid);

        Player player = playerDao.getBean(pid);
        for (TaskRule.TaskItem taskItem : taskRule.getTaskItems()) {
            baseItemRulecontainer.addGoods(player, taskItem.getItemId(), taskItem.getNum());
        }

        if (taskRule.getGameCoin() > 0) {
            player.incGameCoin(taskRule.getGameCoin());
            playerDao.saveBean(player);
            BroadcastHolder.add(roleFactory.newPlayerVO(player));
        }
        if (taskRule.getFarmExp() > 0) {
            FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());
            farmLevelRuleContainer.addFarmExp(farmPlayer, taskRule.getFarmExp());
            farmPlayerDao.saveBean(farmPlayer);
            BroadcastHolder.add(farmRpc.getFarmVoByPlayerId(pid));
        }
        if (taskRule.getFamilyDevote() > 0) { //家族贡献
            FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
            Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
            if (familyRuleContainer.isMember(family, familyMemberInfo)) {
                familyRuleContainer.addFamilyDevote(familyMemberInfo,taskRule.getFamilyDevote());
                familyMemberInfoDao.saveBean(familyMemberInfo);
            }
        }

        if (taskRule.getFamilyFunds() > 0) {
            FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
            Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
            if (familyRuleContainer.isMember(family, familyMemberInfo)) {
//                family.incFunds(taskRule.getFamilyFunds());
                familyRuleContainer.addFailyFunds(family,taskRule.getFamilyFunds());
                familyDao.saveBean(family);
            }
        }
        /**
         * 积分
         */
        if(taskRule.getBonusScore()>0){
            try {
                /**
                 * 记录积分获取动作
                 */
                iIntegralRuleContainer.addIntegralAction(pid, IIntegralRuleContainerImpl.INTEGRAL_ACTION_8, taskRule.getBonusScore());
            } catch (Exception e) {
                exceptionFactory.throwRuleException("active.gamecoin");
            }

        }
        /**
         * 免费音乐盒 赞次数
         */
        if(taskRule.getZanNum()>0){
            MusicBox musicBox=musicBoxDao.getBean(pid);
            musicBox.setFreeNum(musicBox.getFreeNum()+taskRule.getZanNum());
            musicBoxDao.saveBean(musicBox);
        }

        fashionCupboardDao.saveBean(fashionCupboard);


    }

    public void getFamilyTask(TaskPlayer taskPlayer, String pid) {
        Collection<TaskRule> taskRuleList = items.values();
        for (TaskRule taskRule : taskRuleList) {
            if (taskRule.getType() == TaskConstants.TASK_TYPE_3) {
                int farmLevel = farmPlayerDao.getFarmLevel(pid);
                int fashionValue = fashionItemRuleContainer.getFasionValue(fashionPlayerDao.getFashionPlayer(pid), fashionCupboardDao.getBean(pid));
                int luxuryValue = roomItemRuleContainer.getLuxuryValue(roomPlayerDao.getRoomPlayer(pid));

                if (taskRule.getFarmLevel() > farmLevel) {
                    continue;
                }
                if (taskRule.getRoomDegree() > luxuryValue) {
                    continue;
                }
                if (taskRule.getFashionIndex() > fashionValue) {
                    continue;
                }
                taskPlayer.addOneTask(taskRuleToOneTask(taskRule));
            }
        }
    }

    @Override
    public List<String> autoAcceptTask(TaskPlayer taskPlayer) {
        List<String> idList = new ArrayList<String>();
        Collection<TaskRule> taskRuleList = items.values();

        for (TaskRule taskRule : taskRuleList) {
            //主线任务
            if (taskRule.getType() == TaskConstants.TASK_TYPE_1) {
                if (taskPlayer.getOneTask(taskRule.getId()) != null) {
                    continue;
                }
                if (taskPlayer.isFinished(taskRule.getId())) {
                    continue;
                }
                if (taskRule.getParentId() != null && !taskRule.getParentId().equals("") && !taskPlayer.getFinished().contains(taskRule.getParentId())) {
                    continue;
                }
                taskPlayer.addOneTask(taskRuleToOneTask(taskRule));
                //设置章节
                if (taskRule.getChapters() != null && !taskRule.getChapters().equals("")) {
                    taskPlayer.setChaptersId(taskRule.getChapters());
                }
                idList.add(taskRule.getId());
                continue;
            }

            //支线任务   家族任务  活动任务
            if (taskRule.getType() == TaskConstants.TASK_TYPE_2 || taskRule.getType() == TaskConstants.TASK_TYPE_3 ||taskRule.getType() == TaskConstants.TASK_TYPE_4|| taskRule.getSubtype() == 1 || taskRule.getSubtype() == 2) {
                if (taskPlayer.getOneTask(taskRule.getId()) != null) {
                    continue;
                }
                if (taskPlayer.isFinished(taskRule.getId())) {
                    continue;
                }
                if (taskRule.getParentId() != null && !taskRule.getParentId().equals("") && !taskPlayer.isFinished(taskRule.getParentId())) {
                    continue;
                }
                int farmLevel = farmPlayerDao.getFarmLevel(taskPlayer.getPid());
                int fashionValue = fashionItemRuleContainer.getFasionValue(fashionPlayerDao.getFashionPlayer(taskPlayer.getPid()), fashionCupboardDao.getBean(taskPlayer.getPid()));
                int luxuryValue = roomItemRuleContainer.getLuxuryValue(roomPlayerDao.getRoomPlayer(taskPlayer.getPid()));

                if (taskRule.getFarmLevel() > farmLevel) {
                    continue;
                }
                if (taskRule.getRoomDegree() > luxuryValue) {
                    continue;
                }
                if (taskRule.getFashionIndex() > fashionValue) {
                    continue;
                }


                taskPlayer.addOneTask(taskRuleToOneTask(taskRule));
                idList.add(taskRule.getId());
            }

        }

        /**
         * 容错机制   , 如果 还有新手 任务，又没有亲妈礼物 则送亲妈礼物
         */
//        if (taskPlayer.isHasTask("task_701")) {
//
//            GiftPlayer giftPlayer = giftPlayerDao.getBean(taskPlayer.getPid());
//            if (!giftPlayer.hasGift(Constant.QINMA_ID)) {
//                Player player = playerDao.getBean(taskPlayer.getPid());
//                //性别,0女,1男
//                if (player.getType() == 0) {
//                    fashionGiftRuleContainer.qinMaGive(taskPlayer.getPid(), Constant.FASHION_WOMEN);
//                } else {
//                    fashionGiftRuleContainer.qinMaGive(taskPlayer.getPid(), Constant.FASHION_MAN);
//
//                }
//            }
//
//        }

        if (idList.size() > 0) {
            /**
             * 如果 接到 特殊的新任务 需要 特殊处理 一些逻辑
             */
            //田地里面的所有作物立即成熟
            if (taskPlayer.isHasTask("task_305") || taskPlayer.isHasTask("task_403")) {
                FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(taskPlayer.getPid(), System.currentTimeMillis());
                farmRuleContainer.setRipe(farmPlayer);
                farmPlayerDao.saveBean(farmPlayer);
                BroadcastHolder.add(farmRpc.getFarmVoByPlayerId(taskPlayer.getPid()));
            }
            if (taskPlayer.isHasTask("task_701")){
                Player player = playerDao.getBean(taskPlayer.getPid());
                //性别,0女,1男
                if (player.getType() == 0) {
                    fashionGiftRuleContainer.qinMaGive(taskPlayer.getPid(), Constant.FASHION_WOMEN);
                } else {
                    fashionGiftRuleContainer.qinMaGive(taskPlayer.getPid(), Constant.FASHION_MAN);

                }
            }
        }
        return idList;
    }



    /**
     * @param pid
     * @return
     */
    @Override
    public List<String> autoAcceptTask(String pid, String code) {
        TaskPlayer taskPlayer = taskDao.getBean(pid);
        List<String> idList = autoAcceptTask(taskPlayer);
        if (idList.size() > 0) {
            taskDao.saveBean(taskPlayer);
        }

        if (!TaskConstants.NO_AUTO.contains(code)) {
            if (code.equals("scene.enter")) { //进入场景  必须返回任务 消息
                if (idList.size() > 0) {
                    BroadcastHolder.add(rspTaskFactory.newTaskInfoVO(taskPlayer, idList));
                } else {
                    BroadcastHolder.add(rspTaskFactory.newTaskInfoVO(taskPlayer, idList));
                }
            } else {  //其它消息 如果任务有更改 返回
                //任务 只是完成 了
                if (idList.size() <= 0 && BroadcastHolder.containStatus(3)) {
                    BroadcastHolder.add(rspTaskFactory.newTaskInfoVO(taskPlayer, idList));
                }
                //任务的 只是 进度  有更改
                if (idList.size() <= 0 && BroadcastHolder.containStatus(2)) {
                    BroadcastHolder.add(rspTaskFactory.newTaskInfoVO(taskPlayer, idList));
                }
                //涉及到 统计任务 数据的更改
                if (idList.size() <= 0 && TaskConstants.COUNT_TYPE.contains(code)) {
                    BroadcastHolder.add(rspTaskFactory.newTaskInfoVO(taskPlayer, idList));
                }
                //有新任务
                if (idList.size() > 0) {
                    BroadcastHolder.add(rspTaskFactory.newTaskInfoVO(taskPlayer, idList));
                }

            }
        }

        /**
         * 成就处理
         */
        playerSuccessDao.getPlayerSuccessInfo(pid);
        return idList;
    }

    @Override
    public int getNumById(String pid, int type, String relevanceId) {


        //收集道具
        if (type == TaskConstants.CONDI_TYPE_ITEM_2) {
            return baseItemRulecontainer.getGoodsNum(pid, relevanceId);
        }
        //农场等级
        if (type == TaskConstants.CONDI_TYPE_FARM_3) {
            return farmPlayerDao.getFarmLevel(pid);
        }
        //房间 豪华度
        if (type == TaskConstants.CONDI_TYPE_ROOM_4) {
            return roomItemRuleContainer.getLuxuryValue(roomPlayerDao.getRoomPlayer(pid));
        }
        //时尚指数
        if (type == TaskConstants.CONDI_TYPE_FASHION_5) {
            return fashionItemRuleContainer.getFasionValue(fashionPlayerDao.getFashionPlayer(pid), fashionCupboardDao.getBean(pid));
        }

        //11、已激活的土地数量	地数量	11,10
        if (type == TaskConstants.CONDI_TYPE_11) {
            return farmRuleContainer.getOpenFieldNum(pid);
        }
        //14、好友数量	具体数量	14,5
        if (type == TaskConstants.CONDI_TYPE_14) {
            return friendDao.getFriendNum(pid);
        }
        //家族贡献
        if (type == TaskConstants.CONDI_TYPE_12) {
            return familyMemberInfoDao.getBean(pid).getContribution();
        }
        //家族等级
        if (type == TaskConstants.CONDI_TYPE_17) {
            FamilyBuildInfo bi = familyBuildDao.getBean(familyMemberInfoDao.getBean(pid).getFamilyId());
            int level=0;
            if (bi!=null){
                level=bi.getLevel(FamilyConstant.BUILD_CASTLE);
            }
            return level;
        }
        return 0;

    }

    @Override
    public void checkOneFinish(Player player, String taskId) throws AlertException {
        TaskRule taskRule = getElement(taskId);
        if (taskRule.getGold() == 0) {
            exceptionFactory.throwAlertException("task.no.one");
        }
        if (player.getGoldCoin() < taskRule.getGold()) {
            exceptionFactory.throwAlertException("goldcoin.notenough");
        }
    }

    @Override
    public OneTask oneFinish(Player player, String taskId, TaskPlayer taskPlayer) {
        TaskRule taskRule = getElement(taskId);
//        player.decGoldCoin(taskRule.getGold());
        playerRuleContainer.decGoldCoin(player,taskRule.getGold(), PlayerConstant.GOLD_ACTION_16);

        OneTask oneTask = taskPlayer.getOneTask(taskId);
        oneTask.setStatus(1);

        return oneTask;
    }

    @Override
    public void updateTaskNum(String pid, int type, String relevanceId, int num) {

        boolean ischange = false;
        TaskPlayer tp = taskDao.getBean(pid);

        //如果任务是 查询统计的，那么 不更新 数量
        if (TaskConstants.C_TASK_TYPE.contains(type)) {
            return;
        }


        /**
         * 修改数量
         */
        for (OneTask oneTask : tp.getAllTasks()) {

            //如果任务已经完成了 ，不需要 统计数量
            if (oneTask.getStatus() == 1) {
                continue;
            }

            for (TaskConditionInfo taskConditionInfo : oneTask.getTaskConditionInfoList()) {
                if (taskConditionInfo.getType() == type) {

                    //任务类型 是不需要 累积的，而是 每次判断是否最大，取最大值
                    if (TaskConstants.CONDI_TYPE_18==type) {
                        if(taskConditionInfo.getmNum()<num){
                            taskConditionInfo.setmNum(num);
                            ischange = true;
                        }

                        continue;
                    }

                    //需要具体到 Id的任务类型
                    if (TaskConstants.ID_TASK_TYPE.contains(type)) {
                        if (relevanceId.equals(taskConditionInfo.getId())) {
                            taskConditionInfo.incrMnum(num);
                            ischange = true;
                        }
                    } else {
                        taskConditionInfo.incrMnum(num);
                        ischange = true;
                    }
                }
            }//end for
        }

        if (ischange) {
            taskDao.saveBean(tp);
            BroadcastHolder.putStatus(2);
        }
    }

    @Override
    public boolean getTaskPlayerByTime(TaskPlayer taskPlayer) {
        Date now = new Date();
        if (taskPlayer.getNextTime() == null || taskPlayer.getNextTime().before(now)) {

            List<String> removeIds = new ArrayList<String>();
            //删除掉旧的每日刷新任务（家族任务 跟 活动任务 目前）
            for (OneTask oneTask : taskPlayer.getAllTasks()) {
                TaskRule taskRule = getElement(oneTask.getTaskId());
                if (taskRule.getType() == TaskConstants.TASK_TYPE_3 || taskRule.getSubtype() == 1 || taskRule.getSubtype() == 2||taskRule.getType()==TaskConstants.TASK_TYPE_4) {
                    removeIds.add(oneTask.getTaskId());
//                    taskPlayer.removeTask(oneTask.getTaskId());
                }
            }
            for (String taskId : removeIds) {
                taskPlayer.removeTask(taskId);
            }
            taskPlayer.clearFamilyFinishTask();

            //刷新家族任务
            getFamilyTask(taskPlayer, taskPlayer.getPid());
            taskPlayer.setNextTime(DateUtil.getNextCountTime());
            return true;

        }
        return false;
    }

    @Override
    public List<TaskRule> getAllTasksByChaptersId(String chaptersId) {
        List<TaskRule> taskRuleList = new ArrayList<TaskRule>();
        for (TaskRule taskRule : items.values()) {
            if (taskRule.getChapters().equals(chaptersId)) {
                taskRuleList.add(taskRule);
            }
        }
        return taskRuleList;
    }

    @Override
    public void checkFinishedCourse( TaskPlayer taskPlayer,String taskId) throws AlertException {
        OneTask oneTask = taskPlayer.getOneTask(taskId);
        if(null==oneTask){
            logger.error("不存在任务数据任务Id："+taskId);
            exceptionFactory.throwAlertException("不存在任务数据");
        }
        TaskRule taskRule = getElement(taskId);
        if (taskRule.getNewtTask() != 1) {
            exceptionFactory.throwAlertException("不是新手任务，不能直接完成");
        }
    }

    @Override
    public OneTask finishedcourse(String taskId, TaskPlayer taskPlayer) {
        OneTask oneTask = taskPlayer.getOneTask(taskId);
        oneTask.setStatus(1);
        return oneTask;
    }

    @Override
    public void gmFinish(String taskId, TaskPlayer taskPlayer) {
        Collection<OneTask> oneTaskCollection = taskPlayer.getAllTasks();
        List<String> finishIds = new ArrayList<String>();
        for (OneTask oneTask : oneTaskCollection) {
            TaskRule taskRule = getElement(oneTask.getTaskId());
            if (taskRule.getType() != TaskConstants.TASK_TYPE_1) {
                continue;
            }
            String id = oneTask.getTaskId();
            try {
                addTaskReward(oneTask.getTaskId(), taskPlayer.getPid());
            } catch (RuleException e) {
                e.printStackTrace();
            }
            finishIds.add(oneTask.getTaskId());
        }
        for (String id : finishIds) {
            TaskRule taskRule = getElement(id);
            /**
             * 如果是家族每日刷新任务 则放到其它地方 ,家族任务是 每天刷新的
             */
            if (taskRule.getType() != TaskConstants.TASK_TYPE_3) {
                taskPlayer.addFinished(id);
            } else {
                taskPlayer.addFamilyFinishs(id);
            }
            taskPlayer.removeTask(id);

        }
    }


    @Override
    public void finishCurrentNewTask(String pId) {
       TaskPlayer taskPlayer=taskDao.getBean(pId);
        List<String> finishIds = new ArrayList<String>();
       for(OneTask oneTask:taskPlayer.getAllTasks()){
           TaskRule taskRule=getElement(oneTask.getTaskId());
           if(taskRule.getNewtTask()==1){
               String id = oneTask.getTaskId();
               try {
                   addTaskReward(oneTask.getTaskId(), taskPlayer.getPid());
               } catch (RuleException e) {
                   e.printStackTrace();
               }
               finishIds.add(oneTask.getTaskId());
           }
       }
        for (String id : finishIds) {
            TaskRule taskRule = getElement(id);
            /**
             * 如果是家族每日刷新任务 则放到其它地方 ,家族任务是 每天刷新的
             */
            if (taskRule.getType() != TaskConstants.TASK_TYPE_3) {
                taskPlayer.addFinished(id);
            } else {
                taskPlayer.addFamilyFinishs(id);
            }
            taskPlayer.removeTask(id);

        }
        taskDao.saveBean(taskPlayer);
    }

    @Override
    public void finishAllNewTask(String pId) {
        TaskPlayer taskPlayer=taskDao.getBean(pId);
        List<String> finishIds = new ArrayList<String>();
        for(OneTask oneTask:taskPlayer.getAllTasks()){
            TaskRule taskRule=getElement(oneTask.getTaskId());
            if(taskRule.getNewtTask()==1){
                String id = oneTask.getTaskId();
                try {
                    addTaskReward(oneTask.getTaskId(), taskPlayer.getPid());
                } catch (RuleException e) {
                    e.printStackTrace();
                }
                finishIds.add(oneTask.getTaskId());
            }
        }
        for (String id : finishIds) {
            TaskRule taskRule = getElement(id);
            /**
             * 如果是家族每日刷新任务 则放到其它地方 ,家族任务是 每天刷新的
             */
            if (taskRule.getType() != TaskConstants.TASK_TYPE_3) {
                taskPlayer.addFinished(id);
            } else {
                taskPlayer.addFamilyFinishs(id);
            }
            taskPlayer.removeTask(id);

        }
        autoAcceptTask(taskPlayer);
        taskDao.saveBean(taskPlayer);
        boolean  has=false;
        for(OneTask oneTask:taskPlayer.getAllTasks()){
            TaskRule taskRule=getElement(oneTask.getTaskId());
            if(taskRule.getNewtTask()==1){
              has=true;
            }
        }
        if(has){
            finishAllNewTask(pId);
        }
        return;
    }

    @Override
    public int getAddP(String pid) {
        int add= TaskConstants.ADD_P;
        if(payMemberRuleContainer.isPayMember(pid)){
            add=payMemberRuleContainer.getFamilyTaskAdd(pid);
        }
        return add;
    }

    @Override
    public boolean isFinishAllNewTask(String pid) {
        TaskPlayer taskPlayer=taskDao.getBean(pid);
        if(taskPlayer.isFinished(TaskConstants.LAST_NEW_TASK)){
            return true;
        }
        return false;
    }


    @Override
    public void checkFinishspecialTask(int type) throws AlertException {
        if (!TaskConstants.REQ_TASK_TYPE.contains(type)) {
            exceptionFactory.throwAlertException("不是特殊任务类型，非法请求");
        }
    }


    public OneTask taskRuleToOneTask(TaskRule taskRule) {
        OneTask oneTask = new OneTask(taskRule.getId());
        for (TaskRule.TaskCondition taskCondition : taskRule.getTaskConditions()) {
            TaskConditionInfo taskConditionInfo = new TaskConditionInfo();
            BeanUtils.copyProperties(taskCondition, taskConditionInfo);
            oneTask.addConditon(taskConditionInfo);
        }
        return oneTask;
    }
}
