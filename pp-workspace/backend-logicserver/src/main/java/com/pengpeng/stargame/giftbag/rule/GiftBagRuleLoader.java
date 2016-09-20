package com.pengpeng.stargame.giftbag.rule;

import com.pengpeng.stargame.giftbag.container.IGiftBagRuleContainer;
import com.pengpeng.stargame.giftbag.rule.GiftBagRule;
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
public class GiftBagRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(GiftBagRuleLoader.class);
    @Autowired
    private IGiftBagRuleContainer giftBagRuleContainer;
    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }



    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<礼包>规则...");
        //农场等级规则
        List<GiftBagRule> rules = this.getHibernateTemplate().find("from GiftBagRule r");

        for(GiftBagRule giftBagRule:rules){
            giftBagRule.init();
            giftBagRuleContainer.addElement(giftBagRule);
        }

        long t = System.currentTimeMillis() - start;
        logger.info("加载<礼包>规则完成,耗时 " + t + "/ms");
    }
}
