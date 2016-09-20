package com.pengpeng.stargame.lucky.tree.rule;

import com.pengpeng.stargame.lucky.tree.container.ILuckyTreeContainer;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeRuleVO;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:29
 */
@Component
public class LuckyTreeRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(LuckyTreeRuleLoader.class);

    @Autowired
    public ILuckyTreeContainer luckyTreeContainer;


    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<招财树>规则...");

        List<LuckyTreeRule> rules = this.getHibernateTemplate().find("from LuckyTreeRule s");
        LuckyTreeRuleVO[] luckyTreeRuleVOs = new LuckyTreeRuleVO[rules.size()];
        int i = 0;
        for(LuckyTreeRule rule:rules){
            rule.parse();
            rule.setWaterCd(rule.getWaterCd() * 1000l);
            luckyTreeContainer.addLuckyTreeRuleMap(rule);

            LuckyTreeRuleVO luckyTreeRuleVO = new LuckyTreeRuleVO();
            luckyTreeRuleVO.setCreditStr(rule.getCreditStr());
            luckyTreeRuleVO.setFreeGameCoin(rule.getFreeGameCoin());
            luckyTreeRuleVO.setFreeMultiple(rule.getFreeMultiple());
            luckyTreeRuleVO.setGoldGameCoin(rule.getGoldGameCoin());
            luckyTreeRuleVO.setGoldMultiple(rule.getGoldMultiple());
            luckyTreeRuleVO.setGoldStr(rule.getGoldStr());
            luckyTreeRuleVO.setLevel(rule.getLevel());
            luckyTreeRuleVO.setWaterCd(rule.getWaterCd());
            luckyTreeRuleVO.setWaterFri(rule.getWaterFri());
            luckyTreeRuleVO.setWaterNum(rule.getWaterNum());
            luckyTreeRuleVOs[i++] = luckyTreeRuleVO;
            luckyTreeContainer.setLuckyTreeRuleVOs(luckyTreeRuleVOs);
        }

        long t = System.currentTimeMillis() - start;
        logger.info("加载<招财树>规则完成,耗时 " + t + "/ms");
    }
}
