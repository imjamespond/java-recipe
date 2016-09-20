package com.pengpeng.stargame.statistics;

import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.dao.RedisKeyValueDB;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.statistic.StatisticConstant;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.player.dao.IOtherPlayerDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 14-2-19
 * Time: 下午5:47
 * 5月14号
 */
@Component
public class Statistics implements IStatistics {
    @Autowired
    private RedisKeyValueDB redisKeyValueDB;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IOtherPlayerDao otherPlayerDao;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private IFamilyMemberDao familyMemberDao;

    private static List<Integer> uids=new ArrayList<Integer>();
    static {

        uids.add(107449);
        uids.add(1889698);
        uids.add(1563870);
        uids.add(1558705);
        uids.add(203904);
        uids.add(30107);
        uids.add(1900160);
        uids.add(207314);
        uids.add(1565493);
        uids.add(1175241);
        uids.add(1889698);
        uids.add(1889698);
        uids.add(1889698);
        uids.add(1889698);
        uids.add(1889698);
        uids.add(1889698);
    }


    /**
     * 以家族为单位 统计 “天线” 的数量  传正数表示加 传负数表示减去
     *
     * @param pid
     * @param itemId
     * @param num
     */
    @Override
    public void addItemNumByFamily(String pid, String itemId, int num) {
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);

        if (itemId.equals(FarmConstant.TIANXIN_ID)) {
            String key = "familyItemNum_" + familyMemberInfo.getFamilyId() + "_" + itemId;
            redisKeyValueDB.incrBy(key, num);
        }
    }

    /**
     * 记录一次登录
     * @param pid
     * @param player
     * @param otherPlayer
     */
    @Override
    public void recordLogin(String pid, Player player, OtherPlayer otherPlayer) {
        Family family = familyDao.getBeanByStarId(player.getStarId());
        if (family == null) {//如果没有
            family = familyDao.getBeanByStarId(familyRuleContainer.getDefaultStarId());
        }
        Date now = new Date();
        if (otherPlayer.getLoginTime()==null||DateUtil.getDayOfYear(now) != DateUtil.getDayOfYear(otherPlayer.getLoginTime())) {
            /**
             * 添加 总 登录用户数，和 家族登录用户数
             */
            redisKeyValueDB.incrBy(StatisticConstant.getDayLoginKey(now, ""), 1); //总的
            redisKeyValueDB.incrBy(StatisticConstant.getDayLoginKey(now, family.getId()), 1);//单个家族的
            /**
             * 留存率统计  记录1日留存 和 7日流程
             */
            int dayInterval=DateUtil.getDayOfYear(now)-DateUtil.getDayOfYear(player.getCreateTime());
            if(dayInterval==1){
                redisKeyValueDB.incrBy(StatisticConstant.getsKeepDayKey(1,now,""),1);
                redisKeyValueDB.incrBy(StatisticConstant.getsKeepDayKey(1,now,family.getId()),1);
            }
            if(dayInterval==7){
                redisKeyValueDB.incrBy(StatisticConstant.getsKeepDayKey(7,now,""),1);
                redisKeyValueDB.incrBy(StatisticConstant.getsKeepDayKey(7,now,family.getId()),1);
            }
        }
    }

    /**
     * 记录一次 注册
     * @param pid
     * @param player
     */
    @Override
    public void recordRegister(String pid,Player player) {
        Family family = familyDao.getBeanByStarId(player.getStarId());
        if (family == null) {//如果没有
            family = familyDao.getBeanByStarId(familyRuleContainer.getDefaultStarId());
        }
        Date now = new Date();

        //全服日注册
        redisKeyValueDB.incrBy(StatisticConstant.getDayRegKey(now,""),1);

        /**
         * 这里 把之前的数据 加上去
         */
        String allKey=StatisticConstant.getAllRegKey("");
        if(redisKeyValueDB.get(allKey)==null){
            redisKeyValueDB.incrBy(allKey,redisKeyValueDB.keys("uid..*").size());//总的
        }
        redisKeyValueDB.incrBy(allKey,1);


        String familyKey=StatisticConstant.getAllRegKey(family.getId());
        if(redisKeyValueDB.get(familyKey)==null){
            redisKeyValueDB.incrBy(familyKey,familyMemberDao.size(family.getId()));//家族的
        }
        redisKeyValueDB.incrBy(familyKey,1);//单个家族的
    }

    /**
     * 获取积分
     * @param pid
     * @param player
     * @param type
     * @param num
     */
    @Override
    public void recordAddIntegral(String pid, Player player, int type, int num) {
        if(uids.contains(player.getUserId())){
            return;
        }
        Family family = familyDao.getBeanByStarId(player.getStarId());
        if (family == null) {//如果没有
            family = familyDao.getBeanByStarId(familyRuleContainer.getDefaultStarId());
        }
        Date now = new Date();
        //全部 跟 家族 的总量
        redisKeyValueDB.incrBy(StatisticConstant.getDayIntegrateKey(now,""),num);
        redisKeyValueDB.incrBy(StatisticConstant.getDayIntegrateKey(now,family.getId()),num);

        //全服 的 具体到类型的总量
        redisKeyValueDB.incrBy(StatisticConstant.getDayIntegrateTypeKey(now,type,""),num);
    }

    /**
     * 改变家族
     * @param pid
     * @param player
     */
    @Override
    public void recordChangeFamily(String pid, Player player) {
        Family family = familyDao.getBeanByStarId(player.getStarId());
        if (family == null) {//如果没有
            family = familyDao.getBeanByStarId(familyRuleContainer.getDefaultStarId());
        }
        String familyKey=StatisticConstant.getAllRegKey(family.getId());
        if(redisKeyValueDB.get(familyKey)==null){
            redisKeyValueDB.incrBy(familyKey,familyMemberDao.size(family.getId()));//家族的
        }
        redisKeyValueDB.incrBy(familyKey,1);//单个家族的
    }

    /**
     * 记录 达人币消耗
     * @param pid
     * @param player
     * @param type
     * @param num
     */
    @Override
    public void recordDeductGold(String pid, Player player, int type, int num) {
        if(uids.contains(player.getUserId())){
            return;
        }
        Date now = new Date();
        //全服 的 具体到类型的总量
        redisKeyValueDB.incrBy(StatisticConstant.getDayGoldTypeKey(now,type,""),num);
    }

    @Override
    public void recordRecharge(String pid, Player player, int num) {
        Family family = familyDao.getBeanByStarId(player.getStarId());
        if (family == null) {//如果没有
            family = familyDao.getBeanByStarId(familyRuleContainer.getDefaultStarId());
        }
        Date now = new Date();
        redisKeyValueDB.incrBy(StatisticConstant.getDayS_RECHAGE(now,""),num);//总收入
        //记录 充值人数
        if(player.getRechargeTime()==null||DateUtil.getDayOfYear(now)!=DateUtil.getDayOfYear(player.getRechargeTime())){
            redisKeyValueDB.incrBy(StatisticConstant.getDayS_RECHAGE_P(now,""),1);
            //首次充值人数
            if(DateUtil.getDayOfYear(now) == DateUtil.getDayOfYear(player.getCreateTime())){
                redisKeyValueDB.incrBy(StatisticConstant.getDayS_RECHAGE_P_F(now, ""),1);
            }
        }
        /**
         * 充值日期 刚好是 注册日期 算在首充内
         */
        if(DateUtil.getDayOfYear(now) == DateUtil.getDayOfYear(player.getCreateTime())){
            redisKeyValueDB.incrBy(StatisticConstant.getDayS_RECHAGE_F(now,""),num);//总收入
        }

    }

    @Override
    public void recordFinishNewTask(String pid, Player player, String taskId) {
        Family family = familyDao.getBeanByStarId(player.getStarId());
        if (family == null) {//如果没有
            family = familyDao.getBeanByStarId(familyRuleContainer.getDefaultStarId());
        }
        Date now = new Date();
        if(taskId.equals("task_805")){
            if(DateUtil.getDayOfYear(now) == DateUtil.getDayOfYear(player.getCreateTime())){
                redisKeyValueDB.incrBy(StatisticConstant.getDayS_F_REG_LOG(now,""),1);//首日登录
            }
        }

    }
}
