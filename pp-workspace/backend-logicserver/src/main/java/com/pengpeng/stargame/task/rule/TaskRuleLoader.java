package com.pengpeng.stargame.task.rule;

import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.player.rule.WordRule;
import com.pengpeng.stargame.task.container.IChaptersRuleContainer;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务规则数据加载
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-18下午5:33
 */
@Component
public class TaskRuleLoader extends HibernateDaoSupport implements IRuleLoader {

    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private IChaptersRuleContainer chaptersRuleContainer;

    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<TaskRule>规则...");
        List<TaskRule> rules= this.getHibernateTemplate().find("from TaskRule w");
        for(TaskRule rule:rules){
            rule.init();
            taskRuleContainer.addElement(rule);
        }
        taskRuleContainer.init();
        logger.info("TaskRule规则:"+rules.size());


        List<ChaptersRule> chaptersRules= this.getHibernateTemplate().find("from ChaptersRule c");
        for(ChaptersRule rule:chaptersRules){
            rule.init();
            chaptersRuleContainer.addElement(rule);
        }
        chaptersRuleContainer.init();
        logger.info("ChaptersRule:"+chaptersRules.size());

        long t = System.currentTimeMillis() - start;
        logger.info("加载<TaskRule>规则完成,耗时 " + t + "/ms");
    }
}
