package com.pengpeng.stargame.rule.loader;

import com.pengpeng.stargame.container.farm.IFarmCropRuleContainer;
import com.pengpeng.stargame.container.farm.IFarmItemRuleContainer;
import com.pengpeng.stargame.container.farm.IFarmLevelRuleContainer;
import com.pengpeng.stargame.container.farm.IFarmOrderRuleContainer;
import com.pengpeng.stargame.container.farm.impl.FarmLevelRuleContainerImpl;
import com.pengpeng.stargame.container.role.IPlayerRuleContainer;
import com.pengpeng.stargame.container.role.ISceneRuleContainer;
import com.pengpeng.stargame.container.role.ITransferRuleContainer;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.rule.farm.FarmCropRule;
import com.pengpeng.stargame.rule.farm.FarmItemRule;
import com.pengpeng.stargame.rule.farm.FarmLevelRule;
import com.pengpeng.stargame.rule.farm.FarmOrderRule;
import com.pengpeng.stargame.rule.role.PlayerRule;
import com.pengpeng.stargame.rule.role.SceneRule;
import com.pengpeng.stargame.rule.role.TransferRule;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
/**
 * 农场规则加载器
 * User: mql
 * Date: 13-4-24
 * Time: 下午3:21
 */
@Component
public class FarmRuleLoader  extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(FarmRuleLoader.class);

    @Autowired
    public IFarmLevelRuleContainer farmLevelRuleContainer;

    @Autowired
    public IFarmOrderRuleContainer farmOrderRuleContainer;

    @Autowired
    public IFarmItemRuleContainer farmItemRuleContainer;

    public IFarmCropRuleContainer farmCropRuleContainer;
    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }



    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<农场>规则...");
        List<FarmLevelRule> rules = this.getHibernateTemplate().find("from FarmLevelRule r");
        farmLevelRuleContainer.addElement(rules);

        List<FarmOrderRule> orderRules = this.getHibernateTemplate().find("from FarmOrderRule r");
        farmOrderRuleContainer.addElement(orderRules);

        List<FarmItemRule> itemRules = this.getHibernateTemplate().find("from FarmItemRule r");
        farmItemRuleContainer.addElement(itemRules);

        List<FarmCropRule> creops=this.getHibernateTemplate().find("from FarmCropRule r");
        farmCropRuleContainer.addElement(creops);

        long t = System.currentTimeMillis() - start;
        logger.info("加载<农场>规则完成,耗时 " + t + "/ms");


    }
}
