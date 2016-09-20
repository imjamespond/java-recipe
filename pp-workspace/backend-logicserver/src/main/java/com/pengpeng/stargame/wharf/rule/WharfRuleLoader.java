package com.pengpeng.stargame.wharf.rule;

import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.wharf.cmd.WharfRpc;
import com.pengpeng.stargame.wharf.container.impl.WharfInvoiceContainerImpl;
import com.pengpeng.stargame.wharf.dao.IPlayerWharfDao;
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
public class WharfRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(WharfRuleLoader.class);


    @Autowired
    public WharfInvoiceContainerImpl wharfInvoiceContainer;
    @Autowired
    public WharfRpc wharfRpc;
    @Autowired
    private IPlayerWharfDao playerWharfDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;

    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<码头>规则...");

        //码头货单规则
        List<WharfInvoiceRule> rules = this.getHibernateTemplate().find("from WharfInvoiceRule s");
        int i = 0;
        for(WharfInvoiceRule rule:rules){
            wharfInvoiceContainer.addElement(rule);
        }
/*
        ///PlayerWharf playerWharf =  playerWharfDao.getPlayerWharf("d5de90cff9194100bb13967ee8c9c3b9");
        //FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer("d5de90cff9194100bb13967ee8c9c3b9",System.currentTimeMillis());
        try{
            //wharfInvoiceContainer.generateInvoices(playerWharf,farmPlayer);
            Session session = new Session("d5de90cff9194100bb13967ee8c9c3b9",null);
            WharfReq req = new WharfReq();
            req.setPid("d5de90cff9194100bb13967ee8c9c3b9");
            req.setInvoiceOrder(0);
            wharfRpc.shipSail(session,req);
        }catch (Exception e){
            e.printStackTrace();
        }
        //PlayerWharfInvoice invoice = new PlayerWharfInvoice();
        //invoice.setCompleted(true);
        //invoice.setHelp(false);
        //map.put("invoice_01",invoice);
        //playerWharfDao.saveBean(playerWharf);
        */
        long t = System.currentTimeMillis() - start;
        logger.info("加载<码头>规则完成,耗时 " + t + "/ms");
    }
}
