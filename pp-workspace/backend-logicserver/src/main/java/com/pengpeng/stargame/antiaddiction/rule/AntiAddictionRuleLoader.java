package com.pengpeng.stargame.antiaddiction.rule;

import com.pengpeng.stargame.antiaddiction.container.IAntiAddictionContainer;
import com.pengpeng.stargame.antiaddiction.dao.IPlayerAntiAddictionDao;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.model.antiaddiction.PlayerAntiAddiction;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:29
 */
@Component
public class AntiAddictionRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(AntiAddictionRuleLoader.class);

    @Autowired
    public IPlayerAntiAddictionDao antiAddictionDao;

    @Autowired
    private IAntiAddictionContainer antiAddictionContainer;

    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<防沉迷>规则...");

         /*
        PlayerAntiAddiction p = antiAddictionDao.getPlayerAntiAddiction("123");
        String identity = "440112201012260939";
        try{
            p.setBirth(antiAddictionContainer.check(identity));
        }catch(Exception e){
            e.printStackTrace();
        }
        p.setName("西门吹雪");p.setIndentity(identity);
        System.out.println(p.getName()+" "+p.getIndentity()+" "+antiAddictionContainer.isEighteen(p.getBirth()));
         */
        long t = System.currentTimeMillis() - start;
        logger.info("加载<防沉迷>规则完成,耗时 " + t + "/ms");
    }
}
