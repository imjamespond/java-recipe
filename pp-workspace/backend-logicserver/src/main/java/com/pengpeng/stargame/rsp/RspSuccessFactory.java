package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.model.success.OneSuccess;
import com.pengpeng.stargame.model.success.PlayerSuccessInfo;
import com.pengpeng.stargame.model.wharf.PlayerWharf;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.room.dao.IRoomPlayerDao;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.success.rule.SuccessRule;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.vo.BaseRewardVO;
import com.pengpeng.stargame.vo.success.PlayerSuccessInfoVO;
import com.pengpeng.stargame.vo.success.SuccessVO;
import com.pengpeng.stargame.wharf.dao.IPlayerWharfDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 14-5-5
 * Time: 下午2:23
 */
@Component
public class RspSuccessFactory extends RspFactory {
    @Autowired
    private ISuccessRuleContainer successRuleContainer;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IPlayerWharfDao playerWharfDao;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IRoomPlayerDao roomPlayerDao;
    public PlayerSuccessInfoVO getSVO(PlayerSuccessInfo playerSuccessInfo){
        PlayerSuccessInfoVO playerSuccessInfoVO=new PlayerSuccessInfoVO();
        List<SuccessVO> successVOList=new ArrayList<SuccessVO>();
        for(OneSuccess oneSeccess:playerSuccessInfo.getOneSeccessList()){
            SuccessVO successVO=new SuccessVO();
            SuccessRule successRule=successRuleContainer.getElement(oneSeccess.getId());

            successVO.setId(oneSeccess.getId());
            successVO.setName(successRule.getName());
            successVO.setDes(successRule.getDes());

            successVO.setMyNum(oneSeccess.getMyNum());

            if(successRule.getType()==1){ //农场等级
                int num=taskRuleContainer.getNumById(playerSuccessInfo.getPid(), TaskConstants.CONDI_TYPE_FARM_3,"");
                successVO.setMyNum(num);
            }
            if(successRule.getType()==5){ //家族贡献
                FamilyMemberInfo familyMemberInfo=familyMemberInfoDao.getFamilyMember(playerSuccessInfo.getPid());
                int num=familyMemberInfo.getTotalContribution();
                successVO.setMyNum(num);
            }
            if(successRule.getType()==11){ //时尚指数
                int num= taskRuleContainer.getNumById(playerSuccessInfo.getPid(),TaskConstants.CONDI_TYPE_FASHION_5,"");
                successVO.setMyNum(num);
            }
            if(successRule.getType()==12){ //房间扩建次数
                RoomPlayer roomPlayer=roomPlayerDao.getRoomPlayer(playerSuccessInfo.getPid());
                int num= roomPlayer.getX()+roomPlayer.getY();
                successVO.setMyNum(num);
            }
            if(successRule.getType()==13){ //仓库容量的格字数
                FarmPackage farmPackage=farmPackageDao.getBean(playerSuccessInfo.getPid());
                int num= farmPackage.getSize();
                successVO.setMyNum(num);
            }
            if(successRule.getType()==20){ //连续完成货船 装货次数
                PlayerWharf playerWharf=playerWharfDao.getPlayerWharf(playerSuccessInfo.getPid());
                int num= playerWharf.getCounter();
                successVO.setMyNum(num);
            }

            successVO.setStar(oneSeccess.getStarNum());
            if(playerSuccessInfo.getUseId()!=null&&playerSuccessInfo.getUseId().equals(oneSeccess.getId())){
                successVO.setCurrentUse(1);
            }else {
                successVO.setCurrentUse(0);
            }
            String reward="";
            if(oneSeccess.getStarNum()==0){
                reward=successRule.getReward1();
                successVO.setMaxNum(successRule.getStar1());
            }  if(oneSeccess.getStarNum()==1){
                if(oneSeccess.getRewardStar().contains(oneSeccess.getStarNum())){
                    reward=successRule.getReward2();
                    successVO.setMaxNum(successRule.getStar2());
                } else {
                    reward=successRule.getReward1();
                    successVO.setMaxNum(successRule.getStar1());
                }

            }else  if(oneSeccess.getStarNum()==2){
                if(oneSeccess.getRewardStar().contains(oneSeccess.getStarNum())){
                    reward=successRule.getReward3();
                    successVO.setMaxNum(successRule.getStar3());
                } else {
                    successVO.setMaxNum(successRule.getStar2());
                    reward=successRule.getReward2();
                }
            } else  if(oneSeccess.getStarNum()==3){
                reward=successRule.getReward3();
                successVO.setMaxNum(successRule.getStar3());
            }
            String[] all=reward.split(";");
            List<BaseRewardVO> rewardVOList = new ArrayList<BaseRewardVO>();
            for(String one:all){
                String [] oneReward=one.split(",");
                if(oneReward[0].equals("1")){ //游戏币
                    rewardVOList.add(new BaseRewardVO("", 1, Integer.parseInt(oneReward[1]),""));
                }
                if(oneReward[0].equals("2")){ //农场经验
                    rewardVOList.add(new BaseRewardVO("", 2, Integer.parseInt(oneReward[1]),""));
                }
                if(oneReward[0].equals("4")){ //积分奖励
                    rewardVOList.add(new BaseRewardVO("", 4, Integer.parseInt(oneReward[1]),""));
                }
                if(oneReward[0].equals("6")){ //家族经费
                    rewardVOList.add(new BaseRewardVO("", 6, Integer.parseInt(oneReward[1]),""));
                }
                if(oneReward[0].equals("7")){ //家族贡献
                    rewardVOList.add(new BaseRewardVO("", 7, Integer.parseInt(oneReward[1]),""));
                }
                if(oneReward[0].equals("8")){ //免费音乐赞次数
                    rewardVOList.add(new BaseRewardVO("", 8, Integer.parseInt(oneReward[1]),""));
                }
                if(oneReward[0].equals("5")){ //免费音乐赞次数
                    rewardVOList.add(new BaseRewardVO(oneReward[1], 5, Integer.parseInt(oneReward[2]),baseItemRulecontainer.getElement(oneReward[1]).getName()));
                }
                if(oneReward[0].equals("9")){ //免费音乐赞次数
                    rewardVOList.add(new BaseRewardVO("", 9, Integer.parseInt(oneReward[1]),""));
                }
            }
            if(oneSeccess.getRewardStar().contains(oneSeccess.getStarNum())){
                successVO.setCurrentGet(1);
            }
            if(oneSeccess.getStarNum()==0){
                successVO.setCurrentGet(1);
            }
            successVO.setRewardVO(rewardVOList.toArray(new BaseRewardVO[0]));
            successVOList.add(successVO);

        }
        playerSuccessInfoVO.setSuccessVOs(successVOList.toArray(new SuccessVO[0]));
        return playerSuccessInfoVO;
    }
}
