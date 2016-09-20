package com.pengpeng.stargame.farm.rule;

import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.farm.container.*;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.player.rule.BaseGiftRule;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

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
    public IBaseItemRulecontainer baseItemRulecontainer;

	@Autowired
	public IFarmWareHouseContainer farmWareHouseContainer;

	@Autowired
	public IFarmGiftItemRuleContainer farmGiftItemRuleContainer;
    @Autowired
    public IFarmProcessRuleContainer farmProcessRuleContainer;
    @Autowired
    private IFarmDecorateRuleContainer farmDerocateRuleContainer;
    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }



    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<农场>规则...");
        //农场等级规则
        List<FarmLevelRule> rules = this.getHibernateTemplate().find("from FarmLevelRule r");
        farmLevelRuleContainer.addElement(rules);

        //订单规则
        List<FarmOrderRule> orderRules = this.getHibernateTemplate().find("from FarmOrderRule r");
        farmOrderRuleContainer.addElement(orderRules);

        List<BaseItemRule> baseItemRules  = this.getHibernateTemplate().find("from BaseItemRule r");
        BaseItemRule baseItemRule1=new BaseItemRule();
        baseItemRule1.setId(Constant.GAME_MONEY_ID);
        baseItemRule1.setName("游戏币");
        baseItemRule1.setIcon("gamePriceIcon");
        baseItemRule1.setDesc("游戏币");
        baseItemRulecontainer.addElement(baseItemRule1);

        BaseItemRule baseItemRule2=new BaseItemRule();
        baseItemRule2.setId(Constant.GOLD_MONEY_ID);
        baseItemRule2.setName("达人币");
        baseItemRule2.setIcon("moneyIcon");
        baseItemRule2.setDesc("达人币");
        baseItemRulecontainer.addElement(baseItemRule2);

        BaseItemRule baseItemRule3=new BaseItemRule();
        baseItemRule3.setId(Constant.FARM_EXP_ID);
        baseItemRule3.setName("农场经验");
        baseItemRule3.setIcon("expIcon");
        baseItemRule3.setDesc("农场经验");
        baseItemRulecontainer.addElement(baseItemRule3);

        BaseItemRule baseItemRule4=new BaseItemRule();
        baseItemRule4.setId(Constant.INTEGRAL_ID);
        baseItemRule4.setName("网站积分");
        baseItemRule4.setIcon("integration");
        baseItemRule4.setDesc("网站积分");
        baseItemRulecontainer.addElement(baseItemRule4);

        for(BaseItemRule baseItemRule:baseItemRules){
            baseItemRule.init();
            baseItemRulecontainer.addElement(baseItemRule);
        }
//        baseItemRulecontainer.addElement(baseItemRules);

        //农场农田规则
        List<FarmSeedRule> itemRules = this.getHibernateTemplate().find("from FarmSeedRule r");
        for(FarmSeedRule rule:itemRules){
            rule.init();
            baseItemRulecontainer.addElement(rule);
        }


        //农场仓库规则
		List<FarmWareHouseRule> farmWareHouseRules = this.getHibernateTemplate().find("from FarmWareHouseRule r");
		for(FarmWareHouseRule rule:farmWareHouseRules){
			rule.init();
			farmWareHouseContainer.addElement(rule);
		}

        //农场订单规则
        List<FarmOrderRule> farmOrderRules=this.getHibernateTemplate().find("from FarmOrderRule r");
        for(FarmOrderRule farmOrderRule:farmOrderRules){
            farmOrderRule.initOrderItem();
            farmOrderRuleContainer.addElement(farmOrderRule);
        }

		// 农场礼物列表
		List<BaseGiftRule> BaseGiftRules = this.getHibernateTemplate().find("from BaseGiftRule r where r.presentType=1");
		for(BaseGiftRule baseGiftRule : BaseGiftRules){
			farmGiftItemRuleContainer.addElement(baseGiftRule);
		}

        //农场  加工数据
        List<FarmProcessRule> farmProcessRules= this.getHibernateTemplate().find("from FarmProcessRule r ");
        for(FarmProcessRule farmProcessRule:farmProcessRules){
            farmProcessRule.init();
            farmProcessRuleContainer.addElement(farmProcessRule);
        }
        //农家 装饰 数据
        List<FarmDecorateRule> farmDecorateRuleList= this.getHibernateTemplate().find("from FarmDecorateRule b");
        for(FarmDecorateRule farmDecorateRule:farmDecorateRuleList){
            farmDecorateRule.init();
            farmDerocateRuleContainer.addElement(farmDecorateRule);
        }
        long t = System.currentTimeMillis() - start;
        logger.info("加载<农场>规则完成,耗时 " + t + "/ms");

    }
}
