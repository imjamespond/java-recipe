package com.pengpeng.stargame.success.container.impl;

import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.cmd.FarmRpc;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.container.IFarmRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.fashion.container.IFashionGiftRuleContainer;
import com.pengpeng.stargame.fashion.container.IFashionItemRuleContainer;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.fashion.dao.IFashionPlayerDao;
import com.pengpeng.stargame.integral.container.impl.IIntegralRuleContainerImpl;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.piazza.MusicBox;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.model.success.OneSuccess;
import com.pengpeng.stargame.model.success.PlayerSuccessInfo;
import com.pengpeng.stargame.model.wharf.PlayerWharf;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyBuildDao;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.dao.IMusicBoxDao;
import com.pengpeng.stargame.player.cmd.PlayerRpc;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.container.IRoomItemRuleContainer;
import com.pengpeng.stargame.room.dao.IRoomPlayerDao;
import com.pengpeng.stargame.rpc.BroadcastHolder;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.rsp.RspTaskFactory;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.success.dao.IPlayerSuccessDao;
import com.pengpeng.stargame.success.rule.SuccessRule;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.task.rule.TaskRule;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.wharf.dao.IPlayerWharfDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 14-5-4
 * Time: 下午2:38
 */
@Component
public class SuccessRuleContainerImpl extends HashMapContainer<String, SuccessRule> implements ISuccessRuleContainer {
    @Autowired
    private IPlayerSuccessDao playerSuccessDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private IFashionPlayerDao fashionPlayerDao;
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
    private IMusicBoxDao musicBoxDao;
    @Autowired
    private IIntegralRuleContainerImpl iIntegralRuleContainer;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private IPlayerWharfDao playerWharfDao;
    @Autowired
    private IRoomPlayerDao roomPlayerDao;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Override
    public boolean init(PlayerSuccessInfo playerSuccessInfo) {

        List<SuccessRule> successRules=new ArrayList<SuccessRule>();
        for(SuccessRule successRule:items.values()){
            boolean  isHas=false;
            for(OneSuccess oneSeccess:playerSuccessInfo.getOneSeccessList()){
                if(oneSeccess.getId().equals(successRule.getId())){
                    isHas=true;
                    break;
                }
            }
            if(!isHas){
                successRules.add(successRule);
            }
        }
        if(successRules.size()>0){
            for(SuccessRule successRule:successRules){
                playerSuccessInfo.addSuccess(new OneSuccess(successRule.getId()));
            }
            return true;
        }
        return false;
    }

    @Override
    public void updateSuccessNum(String pid, int type, int num,String id) {
        PlayerSuccessInfo playerSuccessInfo=playerSuccessDao.getPlayerSuccessInfo(pid);
        for(OneSuccess oneSeccess:playerSuccessInfo.getOneSeccessList()){
            SuccessRule successRule=getElement(oneSeccess.getId());
            if(successRule.getType()==type){
                if(type==16){
                    if(!id.equals(successRule.getItemId())){
                        continue;
                    }
                }
               setStarNum(oneSeccess,num,successRule);
            }
        }
        playerSuccessDao.saveBean(playerSuccessInfo);
    }

    @Override
    public boolean checkSuccess(PlayerSuccessInfo playerSuccessInfo) {
        boolean ischange=false;
        for(OneSuccess oneSeccess:playerSuccessInfo.getOneSeccessList()){
            if(oneSeccess.getStarNum()==3){
                continue;
            }
            SuccessRule successRule=getElement(oneSeccess.getId());
            if(successRule.getType()==1){ //农场等级
               int num=taskRuleContainer.getNumById(playerSuccessInfo.getPid(),TaskConstants.CONDI_TYPE_FARM_3,"");
                if(setStarNum(oneSeccess,num,successRule)){
                    ischange=true;
                }
                oneSeccess.setMyNum(0);
            }
            if(successRule.getType()==5){ //家族贡献
                FamilyMemberInfo familyMemberInfo=familyMemberInfoDao.getFamilyMember(playerSuccessInfo.getPid());
                int num=familyMemberInfo.getTotalContribution();
                if(setStarNum(oneSeccess,num,successRule)){
                    ischange=true;
                }
                oneSeccess.setMyNum(0);
            }
            if(successRule.getType()==11){ //时尚指数
               int num= taskRuleContainer.getNumById(playerSuccessInfo.getPid(),TaskConstants.CONDI_TYPE_FASHION_5,"");
                if(setStarNum(oneSeccess,num,successRule)){
                    ischange=true;
                }
                oneSeccess.setMyNum(0);
            }
            if(successRule.getType()==12){ //房间扩建次数
                RoomPlayer roomPlayer=roomPlayerDao.getRoomPlayer(playerSuccessInfo.getPid());
                int num= roomPlayer.getX()+roomPlayer.getY();
                if(setStarNum(oneSeccess,num,successRule)){
                    ischange=true;
                }
                oneSeccess.setMyNum(0);
            }
            if(successRule.getType()==13){ //仓库容量的格字数
                FarmPackage farmPackage=farmPackageDao.getBean(playerSuccessInfo.getPid());
                int num= farmPackage.getSize();
                if(setStarNum(oneSeccess,num,successRule)){
                    ischange=true;
                }
                oneSeccess.setMyNum(0);
            }
            if(successRule.getType()==20){ //连续完成货船 装货次数
                PlayerWharf playerWharf=playerWharfDao.getPlayerWharf(playerSuccessInfo.getPid());
                int num= playerWharf.getCounter();
                if(setStarNum(oneSeccess,num,successRule)){
                    ischange=true;
                }
                oneSeccess.setMyNum(0);
            }

            if(setStarNum(oneSeccess,0,successRule)){
                ischange=true;
            }
        }
        return ischange;
    }

    @Override
    public void addReward(String pid, OneSuccess oneSeccess) throws RuleException {
        SuccessRule successRule=getElement(oneSeccess.getId());
        String reward="";
        if(oneSeccess.getStarNum()==1){
            reward=successRule.getReward1();
        } else  if(oneSeccess.getStarNum()==2){
            reward=successRule.getReward2();
        }else  if(oneSeccess.getStarNum()==3){
            reward=successRule.getReward3();
        }
        String[] all=reward.split(";");
        Player player = playerDao.getBean(pid);
        for(String one:all){
            String [] oneReward=one.split(",");
            if(oneReward[0].equals("1")){ //游戏币
//                player.incGameCoin(Integer.parseInt(oneReward[1]));
                playerRuleContainer.incGameCoin(player,Integer.parseInt(oneReward[1]));
                playerDao.saveBean(player);
                BroadcastHolder.add(roleFactory.newPlayerVO(player));
            }
            if(oneReward[0].equals("9")){ //游戏币
//                player.incGoldCoin(Integer.parseInt(oneReward[1]));
                playerRuleContainer.incGoldCoin(player,Integer.parseInt(oneReward[1]), PlayerConstant.GOLD_ACTION_19);
                playerDao.saveBean(player);
                BroadcastHolder.add(roleFactory.newPlayerVO(player));
            }
            if(oneReward[0].equals("2")){ //农场经验
                FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());
                farmLevelRuleContainer.addFarmExp(farmPlayer,Integer.parseInt(oneReward[1]));
                farmPlayerDao.saveBean(farmPlayer);
                BroadcastHolder.add(farmRpc.getFarmVoByPlayerId(pid));
            }
            if(oneReward[0].equals("4")){ //积分奖励
                try {
                    iIntegralRuleContainer.addIntegralAction(pid, IIntegralRuleContainerImpl.INTEGRAL_ACTION_10, Integer.parseInt(oneReward[1]));
                } catch (Exception e) {
                    exceptionFactory.throwRuleException("active.gamecoin");
                }
            }
            if(oneReward[0].equals("5")){ //道具的奖励
                baseItemRulecontainer.addGoods(player, oneReward[1], Integer.parseInt(oneReward[2]));
            }
            if(oneReward[0].equals("6")){ //家族经费
                FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
                Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
                if (familyRuleContainer.isMember(family, familyMemberInfo)) {
                    family.incFunds(Integer.parseInt(oneReward[1]));
                    familyDao.saveBean(family);
                }
            }
            if(oneReward[0].equals("7")){ //家族贡献
                FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
                Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
                if (familyRuleContainer.isMember(family, familyMemberInfo)) {
                    familyMemberInfo.incDevote(Integer.parseInt(oneReward[1]), new Date());
                    familyMemberInfoDao.saveBean(familyMemberInfo);
                }
            }
            if(oneReward[0].equals("8")){ //免费音乐赞次数
                MusicBox musicBox=musicBoxDao.getBean(pid);
                musicBox.setFreeNum(musicBox.getFreeNum()+Integer.parseInt(oneReward[1]));
                musicBoxDao.saveBean(musicBox);
            }
        }
    }

    /**
     * 设置 成就 星星 数量
     * @param oneSeccess
     * @param num
     * @param successRule
     * @return
     */
    public boolean setStarNum(OneSuccess oneSeccess,int num,SuccessRule successRule){
        boolean  ischange=false;
        int sNum=0;
        int star=0;
        oneSeccess.setMyNum(oneSeccess.getMyNum()+num);
        if(oneSeccess.getStarNum()==0){
            sNum=successRule.getStar1();
            star=1;
        } else  if(oneSeccess.getStarNum()==1){
            sNum=successRule.getStar2();
            star=2;
        }else  if(oneSeccess.getStarNum()==2){
            sNum=successRule.getStar3();
            star=3;
        }
        if(sNum==0){
            ischange= false;
        }
        if(oneSeccess.getMyNum()>=sNum){
            int lastStar=star-1;
            if(lastStar!=0){
                if(oneSeccess.getRewardStar().contains(lastStar)){//上一个奖励已经领取了,在设置下一个星星
                    oneSeccess.setStarNum(star);
                    ischange=true;
                }
            } else {
                oneSeccess.setStarNum(star);
                ischange=true;
            }

        }
        if(ischange){
            BroadcastHolder.add(new MsgVO(EventConstant.EVENT_NEN_S,1));
        }
        return ischange;

    }
}
