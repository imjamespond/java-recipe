package com.pengpeng.stargame.player.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.model.player.ActivePlayer;
import com.pengpeng.stargame.player.container.IActiveRuleContainer;
import com.pengpeng.stargame.player.rule.ActiveRule;
import com.pengpeng.stargame.vo.role.ActiveItemVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-16下午3:37
 */
@Component
public class ActiveRuleContainerImpl extends HashMapContainer<Integer,ActiveRule> implements IActiveRuleContainer {

    @Override
    public int getFinishNum(ActivePlayer ap) {
        ArrayList<ActiveRule> colls = new ArrayList<ActiveRule>(values());
        int finished =0 ;
        for(int i=0;i<colls.size();i++){
            ActiveRule rule = colls.get(i);
            boolean ismax = ap.isMax(rule.getType(),rule.getFinishMax());
            if (ismax){
                finished++;
            }
        }
        return finished;
    }
    @Override
    public ActiveRule getByTaskId(String taskId) {
        for(ActiveRule activeRule:items.values()){
            String [] ss=null ;
            if(activeRule.getTaskId()!=null&&!activeRule.getTaskId().equals("")&&!activeRule.getTaskId().equals("0")){
                 ss=activeRule.getTaskId().split(",");
            }
            if(ss==null||ss.length==0){
                continue;
            }
            boolean  has=false;
            for(String temp:ss){
                if(temp.equals(taskId)) {
                    has=true;
                }
            }
            if(has){
                return activeRule;
            }
        }
            return null;
    }

}
