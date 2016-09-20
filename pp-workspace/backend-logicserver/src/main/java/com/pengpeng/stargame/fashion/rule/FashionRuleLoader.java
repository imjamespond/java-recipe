package com.pengpeng.stargame.fashion.rule;

import com.pengpeng.stargame.fashion.container.IFashionGiftItemRuleContainer;
import com.pengpeng.stargame.fashion.container.IFashionItemRuleContainer;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.player.rule.BaseGiftRule;
import com.pengpeng.stargame.room.container.IRoomRuleContainer;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:16
 */
@Component
public class FashionRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(FashionRuleLoader.class);

    @Autowired
    private IFashionItemRuleContainer fashionItemRuleContainer;

    @Autowired
    private IRoomRuleContainer roomRuleContainer;

	@Autowired
	private IFashionGiftItemRuleContainer fashionGiftItemRuleContainer;

    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<个人房间>规则...");

        //加载时装物品规则
        List<FashionItemRule> itemRules = this.getHibernateTemplate().find("from FashionItemRule r");
         fashionItemRuleContainer.addElement(itemRules);
        for(FashionItemRule fashionItemRule:itemRules){
            fashionItemRule.init();
        }
        fashionItemRuleContainer.init();

		// 时装礼品
        List<BaseGiftRule> BaseGiftRules = this.getHibernateTemplate().find("from BaseGiftRule r where r.presentType=3");
        for(BaseGiftRule baseGiftRule : BaseGiftRules){
            fashionGiftItemRuleContainer.addElement(baseGiftRule);
        }

        long t = System.currentTimeMillis() - start;
        logger.info("加载<个人房间>规则完成,耗时 " + t + "/ms");

    }

}
