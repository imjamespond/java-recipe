package com.pengpeng.admin.stargame.manager.impl;

import com.pengpeng.admin.piazza.dao.IFamilyRuleDao;
import com.pengpeng.admin.smallgame.dao.ISmallGameRuleDao;
import com.pengpeng.admin.stargame.common.DateUtil;
import com.pengpeng.admin.stargame.dao.IPlayerOlineInfoDao;
import com.pengpeng.admin.stargame.manager.IPlayerOlineInfoManager;
import com.pengpeng.admin.stargame.model.PlayerOlineInfoModel;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.piazza.cmd.FamilyAssistantRpc;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.small.game.rule.SmallGameRule;
import com.pengpeng.stargame.vo.piazza.FamilyAssistantReq;
import com.pengpeng.stargame.vo.smallgame.SmallGameReq;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午11:31
 * 一些定时任务处理类
 */
@Repository(value = "playerOlineInfoManager")
public class PlayerOlineInfoManagerImpl implements IPlayerOlineInfoManager {
    @Autowired
    @Qualifier(value = "playerOlineInfoDao")
    private IPlayerOlineInfoDao playerOlineInfoDao;
    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private GmRpcRemote gmRpcRemote;
    @Autowired
    private IFamilyRuleDao familyRuleDao;
    @Autowired
    private FamilyAssistantRpcRemote familyAssistantRpcRemote;
    @Autowired
    private SmallGameRpcRemote smallGameRpcRemote;
    @Autowired
    private ISmallGameRuleDao smallGameRuleDao;
    private static final Logger logger = Logger.getLogger("infolog");

    @Override
    public PlayerOlineInfoModel findById(Serializable id) throws NotFoundBeanException {
        PlayerOlineInfoModel playerOlineInfo = playerOlineInfoDao.findById(id);
        return playerOlineInfo;
    }

    @Override
    public void timingSave() throws NotFoundBeanException, BeanAreadyException {
        try {
            Date now = new Date();
            String id = DateUtil.getDateFormat(now, "yyyy-MM-dd");
            PlayerOlineInfoModel playerOlineInfo = playerOlineInfoDao.findById(id);
            int num = statusRemote.size(null, "");
            if (playerOlineInfo == null) {
                playerOlineInfo = new PlayerOlineInfoModel();
                playerOlineInfo.setId(id);
                playerOlineInfo.setMaxNum(num);
                playerOlineInfo.setMinNum(num);
                playerOlineInfo.setMaxTime(now);
                playerOlineInfo.setMinTime(now);
                playerOlineInfoDao.createBean(playerOlineInfo);
            } else {
                if (playerOlineInfo.getMaxNum() < num) {
                    playerOlineInfo.setMaxNum(num);
                    playerOlineInfo.setMaxTime(now);
                    playerOlineInfoDao.updateBean(playerOlineInfo);
                }
                if (playerOlineInfo.getMinNum() > num) {
                    playerOlineInfo.setMinNum(num);
                    playerOlineInfo.setMinTime(now);
                    playerOlineInfoDao.updateBean(playerOlineInfo);
                }
            }
            logger.info("success 5分钟定时器");
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("error 5分钟定时器");
        }

    }

    /**
     * 定时 选明星 助理
     */
    @Override
    public void designate() {
        try {
            List<FamilyRule> familyRuleList = familyRuleDao.findAll();
            for (FamilyRule familyRule : familyRuleList) {
                FamilyAssistantReq familyAssistantReq = new FamilyAssistantReq();
                familyAssistantReq.setFid(familyRule.getId());
                try {
                    familyAssistantRpcRemote.designate(null, familyAssistantReq);
                } catch (GameException e) {
                    e.printStackTrace();
                }
            }
            logger.info("success 定时选明星助理成功....");
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("error 定时选明星助理失败....");
        }
    }

    /**
     * 定时 选小游戏每日奖励
     */
    @Override
    public void smallGameReward1() {
        try {
            SmallGameReq req = new SmallGameReq();
            req.setType(1);
            try {
                smallGameRpcRemote.dayReward(null, req);
            } catch (GameException e) {
                e.printStackTrace();
            }
            logger.info("success 小游戏1定时奖励成功....");
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("error 小游戏1定时奖励失败....");
        }
    }

    /**
     * 定时 选小游戏每日奖励
     */
    @Override
    public void smallGameReward2() {
        try {
            SmallGameReq req = new SmallGameReq();
            req.setType(2);
            try {
                smallGameRpcRemote.dayReward(null, req);
            } catch (GameException e) {
                e.printStackTrace();
            }
            logger.info("success 小游戏2定时奖励成功....");
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("error 小游戏2定时奖励失败....");
        }
    }
    @Override
    public void smallGameReward3() {
        try {
            SmallGameReq req = new SmallGameReq();
            req.setType(3);
            try {
                smallGameRpcRemote.dayReward(null, req);
            } catch (GameException e) {
                e.printStackTrace();
            }
            logger.info("success 小游戏3定时奖励成功....");
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.info("error 小游戏3定时奖励失败....");
        }
    }

    /**
     //     * 定时 选小游戏每日奖励
     //     */
//    @Override
    public void smallGameReward(){
        List<SmallGameRule> smallGameRuleList=smallGameRuleDao.findAll();
        for(SmallGameRule smallGameRule:smallGameRuleList){

            SmallGameReq req = new SmallGameReq();
            req.setType(smallGameRule.getType());
            try {
                smallGameRpcRemote.dayReward(null,req);
            } catch (GameException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 定时 请求游戏 ，控制场景的掉落
     */
    @Override
    public void executeGame() {
        try {
            gmRpcRemote.executeGame(null, null);
            logger.info("success 一分钟定时器");
        } catch (GameException e) {
            logger.info(e.getMessage());
            logger.info("error 一分钟定时器");
        }
    }

    public void init(){
        while (true){
            try {
                Thread.sleep(60*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                gmRpcRemote.executeGame(null, null);
                logger.info("success 一分钟定时器");
            } catch (GameException e) {
                logger.info(e.getMessage());
                logger.info("error 一分钟定时器");
            }
        }
    }

}
