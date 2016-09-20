package com.pengpeng.stargame.stall.rule;

import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.model.player.Mail;
import com.pengpeng.stargame.model.stall.StallConstant;
import com.pengpeng.stargame.player.cmd.MailRpc;
import com.pengpeng.stargame.player.dao.IMailDao;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspStallFactory;
import com.pengpeng.stargame.stall.cmd.StallPassengerRpc;
import com.pengpeng.stargame.stall.cmd.StallRpc;
import com.pengpeng.stargame.stall.container.IStallFriShelfContainer;
import com.pengpeng.stargame.stall.container.IStallGoldShelfContainer;
import com.pengpeng.stargame.stall.container.IStallPassengerContainer;
import com.pengpeng.stargame.stall.container.IStallPriceContainer;
import com.pengpeng.stargame.stall.dao.IPlayerStallPassengerDao;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.farm.FarmItemVO;
import com.pengpeng.stargame.vo.stall.StallAdvReq;
import com.pengpeng.stargame.vo.stall.StallAssistantReq;
import com.pengpeng.stargame.vo.stall.StallReq;
import com.pengpeng.stargame.wharf.cmd.WharfRpc;
import com.pengpeng.stargame.wharf.container.impl.WharfInvoiceContainerImpl;
import com.pengpeng.stargame.wharf.dao.IPlayerWharfDao;
import com.pengpeng.stargame.wharf.rule.WharfInvoiceRule;
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
public class StallRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(StallRuleLoader.class);

    @Autowired
    private IStallPriceContainer stallPriceContainer;
    @Autowired
    private StallRpc stallRpc;
    @Autowired
    private IStallGoldShelfContainer stallGoldShelfContainer;
    @Autowired
    private IStallFriShelfContainer stallFriShelfContainer;
    @Autowired
    private IStallPassengerContainer stallPassengerContainer;

    @Autowired
    private RspStallFactory rspStallFactory;



    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<摆摊>规则...");

        stallPriceContainer.setAdvPrice(15,1);
        stallPriceContainer.setAdvPrice(3*60,5);
        stallPriceContainer.setAdvPrice(24*60,30);

        stallPriceContainer.setAssistantPrices(1, 15);
        stallPriceContainer.setAssistantPrices(7, 80);
        stallPriceContainer.setAssistantPrices(30, 300);



        List<BaseItemRule> baseItemRules  = this.getHibernateTemplate().find("from BaseItemRule r");
        for(BaseItemRule baseItemRule:baseItemRules){
            if(baseItemRule.getType()==1){
            stallPriceContainer.addRandomItem(baseItemRule);
            }
            if(baseItemRule.getShelfPrice()>0){
                FarmItemVO vo = new FarmItemVO();
                vo.setItemId(baseItemRule.getItemsId());
                rspStallFactory.addAssistantItem(vo);
            }

        }

        List<StallFriShelfRule> rules1 = this.getHibernateTemplate().find("from StallFriShelfRule s");
        for(StallFriShelfRule rule:rules1){
            stallFriShelfContainer.addElement(rule);
        }

        List<StallGoldShelfRule> rules2 = this.getHibernateTemplate().find("from StallGoldShelfRule s");
        for(StallGoldShelfRule rule:rules2){
            stallGoldShelfContainer.addElement(rule);
        }



        List<StallPassengerFashionRule> rules4 = this.getHibernateTemplate().find("from StallPassengerFashionRule s");
        for(StallPassengerFashionRule rule:rules4){
            stallPassengerContainer.addFashionRule(rule);
        }

        List<StallPassengerPurchaseRule> rules5 = this.getHibernateTemplate().find("from StallPassengerPurchaseRule s");
        for(StallPassengerPurchaseRule rule:rules5){
            stallPassengerContainer.addPurchaseRule(rule);
        }

        long t = System.currentTimeMillis() - start;
        logger.info("加载<摆摊>规则完成,耗时 " + t + "/ms");
    }
}
