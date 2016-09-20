package com.pengpeng.stargame.qinma.rule;

import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.qinma.container.IQinMaFarmDecorateRuleContainer;
import com.pengpeng.stargame.qinma.container.IQinMaFarmRuleContainer;
import com.pengpeng.stargame.qinma.container.IQinMaRoomRuleContainer;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: mql
 * Date: 13-8-14
 * Time: 下午4:26
 */
@Component
public class QinMaRuleLoader extends HibernateDaoSupport implements IRuleLoader {

    private static final Logger logger = Logger.getLogger(QinMaRuleLoader.class);

    @Autowired
    private IQinMaRoomRuleContainer qinMaRoomRuleContainer;
    @Autowired
    private IQinMaFarmRuleContainer qinMaFarmRuleContainer;
    @Autowired
    private IQinMaFarmDecorateRuleContainer qinMaFarmDecorateRuleContainer;

    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }


    @Override
    public void load() {

        logger.info("正在加载<亲妈>规则...");
        long start = System.currentTimeMillis();
        //亲妈房间规则
        List<QinMaRoomRule> qinMaRoomRules = this.getHibernateTemplate().find("from QinMaRoomRule r");
        for(QinMaRoomRule rule:qinMaRoomRules){
            rule.init();
            qinMaRoomRuleContainer.addElement(rule);
        }


        //农场亲妈规则
        List<QinMaFarmRule> qinMaFarmRules = this.getHibernateTemplate().find("from QinMaFarmRule r");
        for(QinMaFarmRule rule:qinMaFarmRules){
            rule.init();
            qinMaFarmRuleContainer.addElement(rule);
        }

        //亲妈 装饰
        List<QinMaFarmDecorateRule> qinMaFarmDecorateRuleList = this.getHibernateTemplate().find("from QinMaFarmDecorateRule r");
        for(QinMaFarmDecorateRule rule:qinMaFarmDecorateRuleList){
            qinMaFarmDecorateRuleContainer.addElement(rule);
        }
        long t = System.currentTimeMillis() - start;

        logger.info("加载<亲妈>规则完成,耗时 " + t + "/ms");


    }
}
