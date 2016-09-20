package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.model.player.ActivePlayer;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.player.container.IActiveRewardRuleContainer;
import com.pengpeng.stargame.player.container.IActiveRuleContainer;
import com.pengpeng.stargame.player.dao.IOtherPlayerDao;
import com.pengpeng.stargame.player.rule.ActiveRewardRule;
import com.pengpeng.stargame.player.rule.ActiveRule;
import com.pengpeng.stargame.vo.role.ActiveAwardVO;
import com.pengpeng.stargame.vo.role.ActiveItemVO;
import com.pengpeng.stargame.vo.role.ActiveVO;
import net.sf.cglib.core.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-12下午4:07
 */
@Component
public class RspActiveFactory extends RspFactory {

    @Autowired
    private IActiveRuleContainer activeRuleContainer;

    @Autowired
    private IActiveRewardRuleContainer activeRewardRuleContainer;
    @Autowired
    private MessageSource message;
    @Autowired
    private IOtherPlayerDao otherPlayerDao;

    public ActiveVO newActiveVO(ActivePlayer ap){
        ActiveVO vo = new ActiveVO();
        int dayActive = 0;
        vo.setTitle(message.getMessage("active.title",null, Locale.CHINA));
        ActiveItemVO[] items = null;
        ArrayList<ActiveRule> colls = new ArrayList<ActiveRule>(activeRuleContainer.values());
        Collections.sort(colls,new Comparator<ActiveRule>() {
            @Override
            public int compare(ActiveRule o1, ActiveRule o2) {
                if (o1.getSort()>o2.getSort()){
                    return 1;
                }
                if (o1.getSort()<=o2.getSort()){
                    return -1;
                }
                return 0;
            }
        });
        items = new ActiveItemVO[colls.size()];
        int finished =0 ;
        for(int i=0;i<items.length;i++){
            ActiveRule rule = colls.get(i);
            items[i] = newItemVO(ap,rule);
            if (PlayerConstant.ACTIVE_TYPE_2==rule.getType()){
                OtherPlayer op = otherPlayerDao.getBean(ap.getPid());
                long time = op.getAccumulateOnlineTime(new Date())/60;
                if (time>120){
                    time = 120;
                }
                items[i].setTitle(String.format(rule.getTitle(),time));
            }

            dayActive +=items[i].getActiveNum();
            boolean ismax = ap.isMax(items[i].getType(),rule.getFinishMax());
            if (ismax){
                finished++;
            }
        }
        vo.setFinished(finished);

        ArrayList<ActiveRewardRule> colls1 = new ArrayList<ActiveRewardRule>(activeRewardRuleContainer.values());
        ActiveAwardVO[] awards = new ActiveAwardVO[colls1.size()];
        for(int i=0;i<awards.length;i++){
            awards[i] = newRewardVO(ap,colls1.get(i),dayActive);
        }
        vo.setDayActive(dayActive);
        vo.setItems(items);
        vo.setAwards(awards);
        return vo;
    }

    private ActiveAwardVO newRewardVO(ActivePlayer ap, ActiveRewardRule activeRewardRule,int dayActive) {
        ActiveAwardVO vo = new ActiveAwardVO();
        vo.setActive(activeRewardRule.getActive());
        vo.setScore(activeRewardRule.getScore());
        boolean reward = ap.isReward(activeRewardRule.getActive());
        if(reward){
            vo.setStatus(2);
        }else if (dayActive>=activeRewardRule.getActive()){
            vo.setStatus(1);
        }
        return vo;
    }

    private ActiveItemVO newItemVO(ActivePlayer ap, ActiveRule activeRule){
        //ap.finish(activeRule.getType(),activeRule.getFinishMax());//TODO:测试用
        ActiveItemVO vo = new ActiveItemVO();
        vo.setType(activeRule.getType());
        vo.setActiveMax(activeRule.getActiveMax());
        vo.setFinishMax(activeRule.getFinishMax());
        vo.setFinish(ap.getNum(activeRule.getType()));
        vo.setTitle(activeRule.getTitle());
        vo.setActiveNum(activeRule.getActive()*ap.getNum(activeRule.getType()));
        return vo;
    }
}
