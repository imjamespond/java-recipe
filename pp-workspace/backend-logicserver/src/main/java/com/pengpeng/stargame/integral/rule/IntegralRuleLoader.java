package com.pengpeng.stargame.integral.rule;

import com.pengpeng.stargame.integral.container.IIntegralRuleContainer;
import com.pengpeng.stargame.lottery.container.ILotteryContainer;
import com.pengpeng.stargame.lottery.container.ILotteryTypeContainer;
import com.pengpeng.stargame.lottery.dao.ILotteryInfoListDao;
import com.pengpeng.stargame.lottery.rule.LotteryRule;
import com.pengpeng.stargame.lottery.rule.LotteryTypeRule;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.rsp.RspLotteryFactory;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: mql
 * Date: 14-4-22
 * Time: 下午4:58
 */
@Component
public class IntegralRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(IntegralRuleLoader.class);
    @Autowired
    private IIntegralRuleContainer iIntegralRuleContainer;

    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载积分统计规则>规则...");

        //积分统计规则
        List<IntegralRule> rules = this.getHibernateTemplate().find("from IntegralRule r");
        for(IntegralRule rule:rules){
            iIntegralRuleContainer.addElement(rule);
        }
        long t = System.currentTimeMillis() - start;
        logger.info("加载<积分统计规则>规则完成,耗时 " + t + "/ms");
    }
}
