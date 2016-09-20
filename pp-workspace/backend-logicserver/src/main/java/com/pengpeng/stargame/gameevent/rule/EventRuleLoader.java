package com.pengpeng.stargame.gameevent.rule;

import com.pengpeng.stargame.gameevent.constiner.IDropGfitRuleContainer;
import com.pengpeng.stargame.gameevent.constiner.IEventRuleContainer;
import com.pengpeng.stargame.manager.IRuleLoader;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: mql
 * Date: 13-12-9
 * Time: 上午9:42
 */
@Component
public class EventRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(EventRuleLoader.class);
    @Autowired
    private IEventRuleContainer eventRuleContainer;
    @Autowired
    private IDropGfitRuleContainer dropGfitRuleContainer;
    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }



    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<节日活动>规则...");
        //
        List<EventRule> rules = this.getHibernateTemplate().find("from EventRule r");
        for(EventRule eventRule:rules){
            eventRule.init();
            eventRuleContainer.addElement(eventRule);
        }
        List<DropGiftRule> dropGiftRules = this.getHibernateTemplate().find("from DropGiftRule r");
        for(DropGiftRule dropGiftRule:dropGiftRules){
            dropGiftRule.init();
            dropGfitRuleContainer.addElement(dropGiftRule);
        }

        long t = System.currentTimeMillis() - start;
        logger.info("加载<节日活动>规则完成,耗时 " + t + "/ms");
    }
}
