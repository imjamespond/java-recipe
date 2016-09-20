package com.pengpeng.stargame.small.game.rule;

import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.piazza.cmd.FamilyAssistantRpc;
import com.pengpeng.stargame.small.game.cmd.SmallGameRpc;
import com.pengpeng.stargame.small.game.container.ISmallGameContainer;
import com.pengpeng.stargame.small.game.dao.ISmallGameSetDao;
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
public class SmallGameRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(SmallGameRuleLoader.class);


    @Autowired
    public ISmallGameContainer smallGameContainer;
    @Autowired
    private FamilyAssistantRpc familyAssistantRpc;
    @Autowired
    private SmallGameRpc smallGameRpc;
    @Autowired
    private ISmallGameSetDao smallGameSetDao;

    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<小游戏排行>规则...");

        //连续登陆规则
        List<SmallGameRule> rules = this.getHibernateTemplate().find("from SmallGameRule s");

        int i = 0;
        for(SmallGameRule rule:rules){
            rule.parseItem();
            smallGameContainer.addElement(rule);
        }

/*        SmallGameReq req = new SmallGameReq();
        req.setType(1);
        try{
        smallGameRpc.dayReward(null,req);
        }catch (Exception e){
            e.printStackTrace();
        }*/



        long t = System.currentTimeMillis() - start;
        logger.info("加载<小游戏排行>规则完成,耗时 " + t + "/ms");
    }
}
