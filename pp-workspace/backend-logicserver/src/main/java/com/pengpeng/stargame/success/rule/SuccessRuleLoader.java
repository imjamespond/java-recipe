package com.pengpeng.stargame.success.rule;

import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.task.rule.TaskRule;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: mql
 * Date: 14-5-4
 * Time: 下午2:45
 */
@Component
public class SuccessRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(SuccessRuleLoader.class);
    @Autowired
    private ISuccessRuleContainer successRuleContainer;

    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }
    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<SuccessRule>规则...");
        List<SuccessRule> rules= this.getHibernateTemplate().find("from SuccessRule w");
        for(SuccessRule rule:rules){
            successRuleContainer.addElement(rule);
        }
        successRuleContainer.init();
        logger.info("SuccessRule:"+rules.size());

        long t = System.currentTimeMillis() - start;
        logger.info("加载<TaskRule>规则完成,耗时 " + t + "/ms");

    }
}
