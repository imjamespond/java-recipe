package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.piazza.MoneyPick;
import com.pengpeng.stargame.model.piazza.MoneyTree;
import com.pengpeng.stargame.model.piazza.PlayerBlessing;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.rule.MoneyTreeRule;
import com.pengpeng.stargame.vo.piazza.MoneyDropVO;
import com.pengpeng.stargame.vo.piazza.MoneyPickInfoVO;
import com.pengpeng.stargame.vo.piazza.MoneyTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-7-1
 * Time: 下午2:11
 */
@Component()
public class RspMoneyTreeFactory extends  RspFactory {
     @Autowired
     private IFamilyMemberInfoDao familyMemberInfoDao;
    public MoneyPickInfoVO getMoneyPickInfoVO(MoneyPick moneyPick){
        MoneyPickInfoVO moneyPickInfoVO=new MoneyPickInfoVO();
        moneyPickInfoVO.setId(moneyPick.getId());
        moneyPickInfoVO.setMoney(moneyPick.getMoney());
        moneyPickInfoVO.setPosition(moneyPick.getPosition());
        return moneyPickInfoVO;
    }

    public MoneyDropVO getMoneyDropVO(MoneyTree moneyTree){
        MoneyDropVO moneyDropVO=new MoneyDropVO();
        List<MoneyPickInfoVO> moneyPickInfoVOList=new ArrayList<MoneyPickInfoVO>();
        for(MoneyPick moneyPick:moneyTree.getMoneyPickList()){
            moneyPickInfoVOList.add(getMoneyPickInfoVO(moneyPick));
        }
        moneyDropVO.setMoneyPickInfoVOs(moneyPickInfoVOList.toArray(new MoneyPickInfoVO [0]));
        return moneyDropVO;

    }

    public MoneyTreeVO getMoneyTreeVO(String pid,MoneyTree moneyTree,MoneyTreeRule moneyTreeRule){
        MoneyTreeVO moneyTreeVO=new MoneyTreeVO();
        /**
         * 设置 树信息
         */
        moneyTreeVO.setBlessingMax(moneyTreeRule.getBlessingMax());
        moneyTreeVO.setBlessingValue(moneyTree.getBlessingValue());
        moneyTreeVO.setLevel(moneyTreeRule.getId());
        moneyTreeVO.setStatus(moneyTree.getStatus());
        Date cDate=new Date();
        if(moneyTree.getStatus()==2){
          moneyTreeVO.setRipeSurplusTime(moneyTree.getRipeEndTime().getTime()-cDate.getTime());
        }
        if(moneyTree.getStatus()==1){
            moneyTreeVO.setRipeTime(moneyTree.getRipeTime().getTime()-cDate.getTime());
        }

        /**
         * 设置 玩家 祝福信息
         */

        PlayerBlessing playerBlessing=moneyTree.getPlayerBlessing(pid);
        moneyTreeVO.setPlayerBlessingValue(playerBlessing.getNum());
        moneyTreeVO.setPlayerBlessingMax(moneyTreeRule.getNumberOfBlessing());
        long nextBTime=0;
        if(playerBlessing.getNextTime().getTime()>cDate.getTime()){
            nextBTime=playerBlessing.getNextTime().getTime()-cDate.getTime();
        }
        moneyTreeVO.setNextBlessingTime(nextBTime);

        /**
         * 是否可以摇钱
         */
        FamilyMemberInfo familyMemberInfo=familyMemberInfoDao.getFamilyMember(pid);
        if(familyMemberInfo.getIdentity()== FamilyConstant.TYPE_ZL){
            if(moneyTree.getShakeNum(pid)>=FamilyConstant.ZL_ROCK_NUM){
                moneyTreeVO.setShakeStatus(1);
            }
        } else{
            if(moneyTree.getPlayerShake().containsKey(pid)){
                moneyTreeVO.setShakeStatus(1);
            }
        }


        List<MoneyPickInfoVO> moneyPickInfoVOList=new ArrayList<MoneyPickInfoVO>();
        for(MoneyPick moneyPick:moneyTree.getMoneyPickList()){
            moneyPickInfoVOList.add(getMoneyPickInfoVO(moneyPick));
        }
        moneyTreeVO.setMoneyPickInfoVOs(moneyPickInfoVOList.toArray(new MoneyPickInfoVO [0]));
        return moneyTreeVO;
    }
}
