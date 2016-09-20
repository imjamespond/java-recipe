package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.gameevent.GameEventConstant;
import com.pengpeng.stargame.gameevent.constiner.IDropGfitRuleContainer;
import com.pengpeng.stargame.gameevent.constiner.IEventRuleContainer;
import com.pengpeng.stargame.gameevent.rule.DropGiftRule;
import com.pengpeng.stargame.gameevent.rule.EventRule;
import com.pengpeng.stargame.model.gameEvent.EventDrop;
import com.pengpeng.stargame.model.gameEvent.FamilyBankEvent;
import com.pengpeng.stargame.model.gameEvent.FamilyEventValue;
import com.pengpeng.stargame.model.gameEvent.OneDrop;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.vo.event.*;
import org.apache.taglibs.standard.lang.jpath.expression.NowFunction;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: mql
 * Date: 13-12-20
 * Time: 下午3:00
 */
@Component
public class RspGameEventFactory extends RspFactory {
    @Autowired
    private IEventRuleContainer eventRuleContainer;
    @Autowired
    private IDropGfitRuleContainer dropGfitRuleContainer;
    @Autowired
    private IPlayerDao playerDao;
    /**
     * 掉落活动 信息
     * @param eventDrop
     * @return
     */
    public EventDropInfoVO getEventDropInfoVO(EventDrop eventDrop,String senceId){
        EventDropInfoVO eventDropInfoVO=new EventDropInfoVO();
        eventDropInfoVO.setSceneId(senceId);
        List<DropGiftVO> dropGiftVOs=new ArrayList<DropGiftVO>();
        Date now=new Date();
        for(OneDrop oneDrop:eventDrop.getOneDropList()){
            DropGiftRule dropGiftRule=dropGfitRuleContainer.getElement(oneDrop.getGiftId());
            DropGiftVO dropGiftVO=new DropGiftVO();
            dropGiftVO.setId(oneDrop.getUid());
            dropGiftVO.setType(dropGiftRule.getType());
            dropGiftVO.setName(dropGiftRule.getName());
            dropGiftVO.setPosition(oneDrop.getPosition());
            if(oneDrop.getExpiration()!=null){
                dropGiftVO.setExpirationTime(now.getTime()-oneDrop.getExpiration().getTime());
            }
            if(dropGiftRule.getType()==6){
                dropGiftVO.setIcon(dropGiftRule.getIcon());
                dropGiftVO.setWord(oneDrop.getWord());
                dropGiftVO.setfName(oneDrop.getfName());
                dropGiftVO.setPname(playerDao.getBean(oneDrop.getPid()).getNickName());
            }
            dropGiftVOs.add(dropGiftVO);
        }
        eventDropInfoVO.setDropGiftVOs(dropGiftVOs.toArray(new DropGiftVO[0]));
        return eventDropInfoVO;
    }
    public FamilyBankEventVO getFamilybankEventVO(int gameMoney,FamilyBankEvent familyBankEvent,int gold){
        FamilyBankEventVO familyBankEventVO=new FamilyBankEventVO();
        EventRule eventRule=eventRuleContainer.getFamilyBankEventRule();
        familyBankEventVO.setGameMoney(gameMoney);
        StringBuffer info=new StringBuffer();
        //活动的奖励信息 说明 ：游戏币，达人币，状态（0未达到 1已经领取 2可以领取）
        for(String s:eventRule.getFamilyBankRewards()){
            String [] oneReward=s.split(",");
            if(familyBankEvent.getReewarded().contains(oneReward[0])){
                info.append(oneReward[0]+","+oneReward[1]+","+"1"+";");
                continue;
            }
            if(!familyBankEvent.getCanReward().contains(oneReward[0])){
                info.append(oneReward[0]+","+oneReward[1]+","+"0"+";");
                continue;
            }
            info.append(oneReward[0]+","+oneReward[1]+","+"2"+";");
        }
        familyBankEventVO.setInfo(info.toString());
        familyBankEventVO.setGold(gold);
        return familyBankEventVO;

    }

    public SpringEventVO getSpringEventVO(String familyId,FamilyEventValue familyEventValue,int start){
        SpringEventVO springEventVO=new SpringEventVO();
        springEventVO.setMaxValue(GameEventConstant.SPRING_EVENT_VALUE);
        springEventVO.setStatus(familyEventValue.getStatus());
        springEventVO.setValue(familyEventValue.getFirecrackerValue());
        if(familyEventValue.getStatus()==2){
            long time= familyEventValue.getTime().getTime()- new Date().getTime();
            if(time<0){
                time=0;
            }
            springEventVO.setSurplusTime(time);
        }
        springEventVO.setFamilyId(familyId);
        springEventVO.setStart(start);
        return springEventVO;
    }

    public BalloonPanelInfoVO getBalloonPanelInfoVO(){
        BalloonPanelInfoVO balloonPanelInfoVO=new BalloonPanelInfoVO();
         List<BalloonVO> balloonVOs=new ArrayList<BalloonVO>();
        for(DropGiftRule dropGiftRule:dropGfitRuleContainer.getByType(6)){
            BalloonVO balloonVO=new BalloonVO();
            balloonVO.setId(dropGiftRule.getId());
            balloonVO.setIcon(dropGiftRule.getIcon());
            balloonVO.setName(dropGiftRule.getName());
            balloonVOs.add(balloonVO);
        }
        balloonPanelInfoVO.setBalloonVOs(balloonVOs.toArray(new BalloonVO[0]));
        int [] times=new int[GameEventConstant.BALLOON.keySet().size()] ;
        int [] gamemoneys=new int[GameEventConstant.BALLOON.keySet().size()];
        int i=0;
        for(Map.Entry<Integer,Integer> entry:GameEventConstant.BALLOON.entrySet()){
            times[i]=entry.getKey();
            gamemoneys[i]=entry.getValue();
            i++;
        }

        balloonPanelInfoVO.setTimes(times);
        balloonPanelInfoVO.setGameCoin(gamemoneys);
        return balloonPanelInfoVO;
    }
}
