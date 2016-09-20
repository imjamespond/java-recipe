package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.dao.IFarmEvaluateDao;
import com.pengpeng.stargame.farm.dao.IFarmFriendHarvestDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.farm.*;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.vo.farm.FarmActionInfoVO;
import com.pengpeng.stargame.vo.farm.FarmActionVO;
import com.pengpeng.stargame.vo.farm.FarmMessageInfoVO;
import com.pengpeng.stargame.vo.farm.FarmMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-11-5
 * Time: 上午11:39
 */
@Component
public class RspFarmActionFactory extends RspFactory {
   @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFarmEvaluateDao farmEvaluateDao;
    @Autowired
    private IFarmFriendHarvestDao farmFriendHarvestDao;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    public FarmActionInfoVO getFarmActionInfoVO(FarmActionInfo farmActionInfo){

        FarmEvaluate farmEvaluate =farmEvaluateDao.getFarmEvaluate(farmActionInfo.getPid());
        FarmFriendHarvest farmFriendHarvest =farmFriendHarvestDao.getFarmFriendHarvest(farmActionInfo.getId());

        FarmActionInfoVO farmActionInfoVO=new FarmActionInfoVO();
        farmActionInfoVO.setAllVisite(farmActionInfo.getAllVisite());
        farmActionInfoVO.setDayVisite(farmActionInfo.getDayVisite());
        farmActionInfoVO.setFriendHelpNum(farmFriendHarvest.getFriendNum());
        farmActionInfoVO.setWeekFriendHelpNum(farmFriendHarvest.getWeekFriendNum());
        farmActionInfoVO.setGoodReputation(farmEvaluate.getNum());
        farmActionInfoVO.setWeekGoodReputation(farmEvaluate.getWeekNum());
        List<FarmActionVO> farmActionVOList=new ArrayList<FarmActionVO>();
        for(FarmAction farmAction:farmActionInfo.getFarmActionList()){
            Player player= playerDao.getBean(farmAction.getFid());
            FarmActionVO farmActionVO=new FarmActionVO();
            farmActionVO.setFid(farmAction.getFid());
            farmActionVO.setType(farmAction.getType());
            farmActionVO.setDate(farmAction.getDate().getTime());
            farmActionVO.setItemId(farmAction.getItemId());
            BaseItemRule baseItemRule=baseItemRulecontainer.getElement(farmAction.getItemId());
            if(baseItemRule!=null){
                farmActionVO.setItemName(baseItemRule.getName());
            }
            farmActionVO.setNum(farmAction.getNum());
            farmActionVO.setName(player.getNickName());
            farmActionVO.setPortrait(getUserPortrait(player.getUserId()));
            farmActionVOList.add(farmActionVO);
        }
        farmActionInfoVO.setFarmActionVOs(farmActionVOList.toArray(new FarmActionVO[0]));
        return farmActionInfoVO;
    }

    public FarmMessageVO [] getFarmMessageVO(FarmMessageInfo  farmMessageInfo){
        List<FarmMessageVO> farmMessageVOs=new ArrayList<FarmMessageVO>();
        for(FarmMessage farmMessage:farmMessageInfo.getFarmMessageList()){
            Player player= playerDao.getBean(farmMessage.getFid());
            FarmMessageVO farmMessageVO=new FarmMessageVO();
            farmMessageVO.setContent(farmMessage.getContent());
            if(farmMessage.getDate()!=null){
                farmMessageVO.setDate(farmMessage.getDate().getTime());
            }

            farmMessageVO.setFid(farmMessage.getFid());
            farmMessageVO.setName(player.getNickName());
            farmMessageVO.setPortrait(getUserPortrait(player.getUserId()));
            farmMessageVOs.add(farmMessageVO);
        }
        return farmMessageVOs.toArray(new FarmMessageVO[0]);
    }

    public FarmMessageInfoVO getFarmMessageInfoVO(FarmMessageInfo  farmMessageInfo){
        FarmMessageInfoVO farmMessageInfoVO=new FarmMessageInfoVO();
        farmMessageInfoVO.setNum(farmMessageInfo.getDayNum());
        farmMessageInfoVO.setFarmMessageVOs(getFarmMessageVO(farmMessageInfo));
        return farmMessageInfoVO;
    }



}
