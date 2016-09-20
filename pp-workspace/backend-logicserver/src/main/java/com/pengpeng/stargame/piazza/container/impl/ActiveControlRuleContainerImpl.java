package com.pengpeng.stargame.piazza.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.piazza.container.IActiveControlRuleContainer;
import com.pengpeng.stargame.piazza.rule.ActiveControlRule;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-7-2
 * Time: 下午7:07
 */
@Component
public class ActiveControlRuleContainerImpl extends HashMapContainer<String,ActiveControlRule> implements IActiveControlRuleContainer {
    @Override
    public List<ActiveControlRule> getAll(int level) {
        Date date =new Date();
        List<ActiveControlRule> activeControlRuleList= new ArrayList<ActiveControlRule>(items.values());
        List<ActiveControlRule> removes=new ArrayList<ActiveControlRule>();

        for(ActiveControlRule activeControlRule:activeControlRuleList){
            if(activeControlRule.getActiveId().equals("active_11")){
                removes.add(activeControlRule);
            }
            if(activeControlRule.getLevel()<level){
                removes.add(activeControlRule);
            }
            Date startDate= DateUtil.convertStringToDate(null,activeControlRule.getOpenDate()+" "+activeControlRule.getOpenTime());
            Date endDate= DateUtil.convertStringToDate(null,activeControlRule.getEndDate()+" "+activeControlRule.getEndTime());

            if(date.before(startDate)){
                removes.add(activeControlRule);

            }
            if(date.after(endDate)) {
                removes.add(activeControlRule);
            }
        }
        activeControlRuleList.removeAll(removes);
        return activeControlRuleList;
    }
}
