package com.pengpeng.stargame.piazza.rule;

import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.piazza.container.*;
import com.pengpeng.stargame.player.rule.PlayerRule;
import com.pengpeng.stargame.player.rule.SceneRule;
import com.pengpeng.stargame.player.rule.TransferRule;
import com.pengpeng.stargame.player.rule.WordRule;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringTokenizer;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-4下午2:49
 */
@Component()
@Scope(value="prototype")
public class PiazzaLoader extends HibernateDaoSupport implements IRuleLoader {

    @Autowired
    private IFamilyBuildingRuleContainer familyBuildingRuleContainer;

    @Autowired
    private IFamilyRuleContainer familyRuleContainer;

    @Autowired
    private IMoneyTreeRuleContainer moneyTreeRuleContainer;

    @Autowired
    private IActiveControlRuleContainer activeControlRuleContainer;

    @Autowired
    private IIdentityRuleContainer identityRuleContainer;

    @Autowired
    private IMusicBoxRuleContainer musicBoxRuleContainer;
    @Autowired
    private IFamilyCollectRuleContainer familyCollectRuleContainer;

    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();

        logger.info("正在加载<IdentityRule>规则...");
        List<IdentityRule> identityRules = this.getHibernateTemplate().find("from IdentityRule r");
        identityRuleContainer.addElement(identityRules);
        identityRuleContainer.init();
        logger.info("<IdentityRule>规则:"+identityRules.size());

        logger.info("正在加载<FamilyRule>规则...");
        List<FamilyRule> rules = this.getHibernateTemplate().find("from FamilyRule r");
        familyRuleContainer.addElement(rules);
        familyRuleContainer.init();
        logger.info("<FamilyRule>规则:"+rules.size());

        logger.info("正在加载<FamilyBuildingRule>规则...");
        List<FamilyBuildingRule> buildRules = this.getHibernateTemplate().find("from FamilyBuildingRule r");
        familyBuildingRuleContainer.addElement(buildRules);
        familyBuildingRuleContainer.init();
        logger.info("<FamilyBuildingRule>规则:"+buildRules.size());

        logger.info("正在加载<MoneyTreeRule>规则...");
        List<MoneyTreeRule> treeRules = this.getHibernateTemplate().find("from MoneyTreeRule r");
        moneyTreeRuleContainer.addElement(treeRules);
        for(MoneyTreeRule moneyTreeRule:treeRules){
            moneyTreeRule.initPositions();
        }
        moneyTreeRuleContainer.init();
        logger.info("<MoneyTreeRule>规则:"+treeRules.size());

        logger.info("正在加载<ActiveControlRule>规则...");
        List<ActiveControlRule> actRules = this.getHibernateTemplate().find("from ActiveControlRule r");
        activeControlRuleContainer.addElement(actRules);
        activeControlRuleContainer.init();
        logger.info("<ActiveControlRule>规则:"+actRules.size());

        musicBoxRuleContainer.addElement(new MusicBoxRule());

        List<FamilyCollectRule> familyCollectRuleList= this.getHibernateTemplate().find("from FamilyCollectRule r");
        for(FamilyCollectRule familyCollectRule:familyCollectRuleList){
            familyCollectRule.init();
            familyCollectRuleContainer.addElement(familyCollectRule);
        }

        long t = System.currentTimeMillis() - start;
        logger.info("加载<FamilyRule,FamilyBuildingRule,MoneyTreeRule,ActiveControlRule>规则完成,耗时 " + t + "/ms");
    }
}
