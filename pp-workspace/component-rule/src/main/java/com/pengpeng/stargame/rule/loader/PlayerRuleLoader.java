package com.pengpeng.stargame.rule.loader;

import com.pengpeng.stargame.container.role.IPlayerRuleContainer;
import com.pengpeng.stargame.container.role.ISceneRuleContainer;
import com.pengpeng.stargame.container.role.ITransferRuleContainer;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.rule.role.PlayerRule;
import com.pengpeng.stargame.rule.role.SceneRule;
import com.pengpeng.stargame.rule.role.TransferRule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-3-12下午10:02
 */
@Component()
@Scope(value="prototype")
public class PlayerRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final  Logger logger = Logger.getLogger(PlayerRuleLoader.class);
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;

    @Autowired
    private ISceneRuleContainer sceneRuleContainer;

    @Autowired
    private ITransferRuleContainer transferRuleContainer;
    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }
    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<Player,Scene,Transfer>规则...");
        List<PlayerRule> rules = this.getHibernateTemplate().find("from PlayerRule r");
//        logger.info("角色规则:" + rules.size());
        playerRuleContainer.addElement(rules);
        logger.info("玩家规则:"+rules.size());

        List<SceneRule> sceneRules = this.getHibernateTemplate().find("from SceneRule r");
        sceneRuleContainer.addElement(sceneRules);
        logger.info("地图规则:"+sceneRules.size());

        List<TransferRule> transferRules = this.getHibernateTemplate().find("from TransferRule r");
        transferRuleContainer.addElement(transferRules);
        logger.info("传输口规则:"+transferRules.size());

        long t = System.currentTimeMillis() - start;
        logger.info("加载<Player,Scene,Transfer>规则完成,耗时 " + t + "/ms");
    }
}
