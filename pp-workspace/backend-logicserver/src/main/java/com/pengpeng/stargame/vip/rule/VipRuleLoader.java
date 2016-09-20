package com.pengpeng.stargame.vip.rule;

import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: mql
 * Date: 13-11-27
 * Time: 下午6:08
 */
@Component()
public class VipRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;

    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<PayMemberRule>规则...");
        List<PayMemberRule> payMemberRuleList= this.getHibernateTemplate().find("from PayMemberRule c");
        for(PayMemberRule rule:payMemberRuleList){

            payMemberRuleContainer.addElement(rule);
        }
        long t = System.currentTimeMillis() - start;
        logger.info("加载<PayMemberRule>规则完成,耗时 " + t + "/ms");
    }
}
