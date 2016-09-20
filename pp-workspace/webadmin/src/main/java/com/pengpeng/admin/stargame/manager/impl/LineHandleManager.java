package com.pengpeng.admin.stargame.manager.impl;

import com.pengpeng.admin.stargame.common.DateUtil;
import com.pengpeng.admin.stargame.dao.*;
import com.pengpeng.admin.stargame.manager.ILineHandleManager;
import com.pengpeng.admin.stargame.manager.ModelFactory;
import com.pengpeng.admin.stargame.model.*;
import com.pengpeng.stargame.tool.LineCallback;
import com.pengpeng.stargame.tool.LogParser;
import com.tongyi.exception.BeanAreadyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午9:11
 */
@Repository(value = "lineHandleManager")
public class LineHandleManager implements ILineHandleManager {
    @Autowired
    @Qualifier(value="playeractiondao")
    private IPlayerActionDao playerActionDao;
    @Autowired
    private IPlayerLoginActionModelDao playerLoginActionModelDao;
    @Autowired
    private IPlayerMapActionModelDao playerMapActionModelDao;
    @Autowired
    private IPlayerLotteryActionModelDao playerLotteryActionModelDao;
    @Autowired
    private IPlayerActionModelDao playerActionModelDao;
    @Autowired
    private IPlayerActivityActionModelDao playerActivityActionModelDao;
    @Autowired
    private IPlayerFarmComplimentActionModelDao playerFarmComplimentActionModelDao;
    @Autowired
    private IPlayerPioneerActionModelDao playerPioneerActionModelDao;
    @Autowired
    private IPlayerHarvestActionModelDao playerHarvestActionModelDao;
    @Autowired
    private IPlayerOrderResetActionModelDao playerOrderResetActionModelDao;

    @Autowired
    private IPlayerItemModelDao playerItemModelDao;
    @Autowired
    private IPlayerRechargeActionModelDao playerRechargeActionModelDao;
    @Autowired
    private IPlayerRegisterModelDao playerRegisterModelDao;
    @Autowired
    private IPlayerTaskActionModelDao playerTaskActionModelDao;
    @Autowired
    private IPlayerGivingActionModelDao playerGivingActionModelDao;
    @Autowired
    private IPlayerDecorationActionModelDao playerDecorationActionModelDao;
    @Autowired
    private IPlayerMoneyTreeActionModelDao playerMoneyTreeActionModelDao;
    @Autowired
    private IPlayerFamilyActionModelDao playerFamilyActionModelDao;
    @Autowired
    private IPlayerFamilyAwardActionModelDao playerFamilyAwardActionModelDao;
    @Autowired
    private IPlayerDonationActionModelDao playerDonationActionModelDao;
    @Autowired
    private IUseGoldModelDao useGoldModelDao;

    @Autowired
    private ModelFactory factory;

    private List<String> logPath;

    public LineHandleManager(){
        logPath = new ArrayList<String>();
        logPath.add("e:/stargame/logs");
        logPath.add("/opt/stargame/gamelog/logic-1");
        logPath.add("/opt/stargame/gamelog/logic-2");
    }
    public void scan(){
        for (String path:logPath){
            File[] files = new File(path).listFiles();
            if (files==null){
                continue;
            }
            for(File file:files){
                if (file.isDirectory()){
                    continue;
                }
                analyse(file.getAbsolutePath());
            }
        }
    }
    public void scanAndStore(final String date){
        for (String path:logPath){
            File[] files = new File(path).listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.indexOf(date)>=0;
                }
            });
            if (files==null){
                continue;
            }
            for(File file:files){
                System.out.println("analyse:"+file.getAbsolutePath());
                List<String> list = analyse(file.getAbsolutePath());
                System.out.println("store:"+file.getAbsolutePath());
                store(list);
            }
        }
    }
    public List<String> analyse(String fileName){
        System.out.println("check file:"+fileName);
        final List<String> list = new ArrayList<String>();
        LogParser.readFileByLines(fileName,new LineCallback() {
            @Override
            public void handle(String line) {
                try{
                    parse(line);//检查日志数据是否正确
                    list.add(line);
                }catch (Exception e){
                    System.out.println(line);
                }
            }
        });
        return list;
    }

    public void store(List<String> lines){
        int count = lines.size();
        System.out.println("count:"+count);
        int idx = 0;
        for(String line:lines){
            handle(line);//保存数据
            idx++;
            if (idx%1000==0){
                System.out.println("remain line:"+(count - idx));
            }
        }
    }

    private String[] format(String line){
        String[] arrStr1 = line.split("&&&");
        if(arrStr1.length<2){
            return null;
        }
        String[] arrStr2 = arrStr1[1].split("#");
        if(arrStr2.length<4){
            return null;
        }
        return arrStr2;
    }

    private void parse(String line){
        String[] strs = format(line);
        if(strs==null){
            return;
        }

        PlayerActionModel pa = new PlayerActionModel();
        pa.setUid(strs[0]);
        pa.setType(Integer.valueOf(strs[1]));
        pa.setPid(strs[2]);
        pa.setDate(new Date(Long.valueOf(strs[3])));

        if(strs.length>=5){
            pa.setAction(strs[4]);
        }else {
            pa.setAction("");
        }
        switch (pa.getType()) {
            //登陆,退出
            case 1:
            case 2:
                factory.newPlayerLoginActionModel(pa,strs);
                break;
            //充值统计
            case 3:
                factory.newPlayerRechargeActionModel(pa, strs);
                break;
            //进入地图
            case 4:
                factory.newPlayerMapActionModel(pa, strs);
                break;

            //完成任务
            case 5:
                factory.newPlayerTaskActionModel(pa, strs);
                break;

            //幸运翻牌
            case 6:
                factory.newPlayerLotteryActionModel(pa, strs);
                break;

            //领取音乐先锋
            case 7:
                factory.newPlayerPioneerActionModel(pa, strs);
                break;

            //领取每日活跃度积分
            case 8:
                factory.newPlayerActivityActionModel(pa, strs);
                break;

            //农场种植
            case 9:
                factory.newPlayerHarvestActionModel(pa, strs);
                break;
            //农场收获
            case 10:
                //协助好友收获
            case 11:
                factory.newPlayerHarvestActionModel11(pa, strs);
                break;

            //农场好评
            case 12:
                factory.newPlayerFarmComplimentActionModel(pa, strs);
                break;

            //刷新农场订单
            case 13:
                factory.newPlayerOrderResetActionModel(pa, strs);
                break;

            //完成农场订单
            case 14:

                break;

            //购买道具
            case 15:
            //卖出作物
            case 16:
            //换装
            case 17:
            //房间装修
            case 18:
                factory.newPlayerItemModel(pa, strs);
                break;
            //  19、摇钱树-祝福
            case 19:
                factory.newPlayerMoneyTreeActionModel(pa, strs);
                break;
            //  20、赠送明星礼物
            case 20:
                factory.newPlayerItemModel(pa, strs);
                break;
            // 21、家族捐献
            case 21:
                factory.newPlayerDonationActionModel(pa, strs);
                break;
            // 22、领取家族奖励
            case 22:
                factory.newPlayerFamilyAwardActionModel(pa, strs);
                break;
            //23、更换家族
            case 23:
                factory.newPlayerFamilyActionModel(pa, strs);
                break;
            // 24、玩家注册
            case 24:
               factory.newPlayerRegisterModel(pa, strs);
                break;
            case 25://25,用户充值
                factory.newPlayerRechargeActionModel(pa, strs);
                break;
            case 26: //26,铲除作物
                factory.newPlayerHarvestActionModel26(pa, strs);
                break;
            case 27://27,使用物品
                factory.newPlayerItemModel(pa, strs);
                break;
            case 28: //使用达人币
                factory.newUseGoldModel(pa, strs);
                break;
            default:
                throw new RuntimeException("Parse Exception:"+line);
        }
    }
    @Override
    public void handle(String line) {
        String[] strs = format(line);
        if (strs==null){
            return ;
        }

        PlayerActionModel pa = new PlayerActionModel();
        pa.setUid(strs[0]);
        pa.setType(Integer.valueOf(strs[1]));
        pa.setPid(strs[2]);
        pa.setDate(new Date(Long.valueOf(strs[3])));


        if(strs.length>=5){
            pa.setAction(strs[4]);
        }else {
            pa.setAction("");
        }
        try {
            switch (pa.getType()){
                //登陆,
                case 1:
                    PlayerLoginActionModel plogin = factory.newPlayerLoginActionModel(pa,strs);
                    playerLoginActionModelDao.createBean(plogin);
                    break;
                case 2://退出
                    PlayerLoginActionModel plogout = factory.newPlayerLogoutActionModel(pa,strs);
                    playerLoginActionModelDao.createBean(plogout);
                    break;
                //充值统计
                case 3:
                    PlayerRechargeActionModel pRecharge = factory.newPlayerRechargeActionModel(pa, strs);
                    playerRechargeActionModelDao.createBean(pRecharge);
                    break;
                //进入地图
                case 4:
                    PlayerMapActionModel pMap =  factory.newPlayerMapActionModel(pa, strs);
                    playerMapActionModelDao.createBean(pMap);
                    break;

                //完成任务
                case 5:
                    PlayerTaskActionModel pTask =factory.newPlayerTaskActionModel(pa, strs);
                    playerTaskActionModelDao.createBean(pTask);
                    break;

                //幸运翻牌
                case 6:
                    PlayerLotteryActionModel pLottery = factory.newPlayerLotteryActionModel(pa, strs);
                    playerLotteryActionModelDao.createBean(pLottery);
                    break;

                //领取音乐先锋
                case 7:
                    PlayerPioneerActionModel pPioneer =factory.newPlayerPioneerActionModel(pa, strs);
                    playerPioneerActionModelDao.createBean(pPioneer);
                    break;

                //领取每日活跃度积分
                case 8:
                    PlayerActivityActionModel pActivity = factory.newPlayerActivityActionModel(pa, strs);
                    playerActivityActionModelDao.createBean(pActivity);
                    break;

                //农场种植
                case 9:
                    PlayerHarvestActionModel pPlant = factory.newPlayerHarvestActionModel(pa, strs);
                    playerHarvestActionModelDao.createBean(pPlant);
                    break;
                //农场收获
                case 10:
                    //协助好友收获
                case 11:
                    PlayerHarvestActionModel pHarvest = factory.newPlayerHarvestActionModel11(pa, strs);
                    playerHarvestActionModelDao.createBean(pHarvest);
                    break;

                //农场好评
                case 12:
                    PlayerFarmComplimentActionModel pFarm = factory.newPlayerFarmComplimentActionModel(pa, strs);
                    playerFarmComplimentActionModelDao.createBean(pFarm);
                    break;

                //刷新农场订单
                case 13:
                    PlayerOrderResetActionModel pOrder =factory.newPlayerOrderResetActionModel(pa, strs);
                    playerOrderResetActionModelDao.createBean(pOrder);
                    break;

                //完成农场订单
                case 14:
                    break;

                //购买道具
                case 15:
                //卖出作物
                case 16:
                //换装
                case 17:
                //房间装修
                case 18:
                    PlayerItemModel pSale =factory.newPlayerItemModel(pa, strs);
                    playerItemModelDao.createBean(pSale);
                    break;
                //  19、摇钱树-祝福
                case 19:
                    PlayerMoneyTreeActionModel pMoneyTree = factory.newPlayerMoneyTreeActionModel(pa, strs);
                    playerMoneyTreeActionModelDao.createBean(pMoneyTree);
                    break;
                //  20、赠送明星礼物
                case 20:
                    PlayerItemModel pGiving = factory.newPlayerItemModel(pa, strs);
                    playerItemModelDao.createBean(pGiving);
                    break;
                // 21、家族捐献
                case 21:
                    PlayerDonationActionModel pDonation =  factory.newPlayerDonationActionModel(pa, strs);
                    playerDonationActionModelDao.createBean(pDonation);
                    break;
                // 22、领取家族奖励
                case 22:
                    PlayerFamilyAwardActionModel pFamilyAward = factory.newPlayerFamilyAwardActionModel(pa, strs);
                    playerFamilyAwardActionModelDao.createBean(pFamilyAward);
                    break;
                //23、更换家族
                case 23:
                    PlayerFamilyActionModel pFamily = factory.newPlayerFamilyActionModel(pa, strs);
                    playerFamilyActionModelDao.createBean(pFamily);
                    break;
                // 24、玩家注册
                case 24:
                    PlayerRegisterModel pRegistry = factory.newPlayerRegisterModel(pa, strs);
                    playerRegisterModelDao.createBean(pRegistry);
                    break;
                case 25: //用户充值
                    PlayerRechargeActionModel model = factory.newPlayerRechargeActionModel(pa, strs);
                    playerRechargeActionModelDao.createBean(model);
                    break;
                case 26://铲除作物
                    PlayerHarvestActionModel playerHarvestActionModel = factory.newPlayerHarvestActionModel26(pa, strs);
                    playerHarvestActionModelDao.createBean(playerHarvestActionModel);
                    break;
                case 27://使用物品
                    PlayerItemModel playerItemModel = factory.newPlayerItemModel(pa, strs);
                    playerItemModelDao.createBean(playerItemModel);
                    break;
                case 28://使用达人币
                    UseGoldModel useGoldModel= factory.newUseGoldModel(pa, strs);
                    useGoldModelDao.createBean(useGoldModel);
                    break;

                default:
                    pa.setState(1);//没有被正确识别的日志
                    playerActionDao.createBean(pa);
                    break;
            }


        } catch (BeanAreadyException e) {
            e.printStackTrace();
        }
    }
}
