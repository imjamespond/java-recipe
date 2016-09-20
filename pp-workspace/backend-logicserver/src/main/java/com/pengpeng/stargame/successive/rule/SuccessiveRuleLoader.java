package com.pengpeng.stargame.successive.rule;

import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.rsp.RspSuccessiveFactory;
import com.pengpeng.stargame.successive.container.ISuccessiveContainer;
import com.pengpeng.stargame.vo.successive.SuccessiveItemVO;
import com.pengpeng.stargame.vo.successive.SuccessivePrizeVO;
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
public class SuccessiveRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(SuccessiveRuleLoader.class);

    @Autowired
    public ISuccessiveContainer successiveContainer;

    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;

    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<连续登陆>规则...");

        //连续登陆规则
        List<SuccessiveRule> rules = this.getHibernateTemplate().find("from SuccessiveRule s");
        SuccessivePrizeVO[] spvos = new SuccessivePrizeVO[rules.size()];
        int i = 0;
        for(SuccessiveRule rule:rules){
            rule.parseItem();
            successiveContainer.addElement(rule);

            SuccessivePrizeVO spvo = new SuccessivePrizeVO();
            spvo.setDay(rule.getDay());
            SuccessiveItemVO[] sivos = new SuccessiveItemVO[rule.getItemNumList().size()];
            int j = 0;
            for(ItemData ir:rule.getItemNumList()){
                SuccessiveItemVO sivo = new SuccessiveItemVO();
                BaseItemRule item_ = baseItemRulecontainer.getElement(ir.getItemId());
                if(item_ != null){
                    sivo.setName(item_.getName());
                    sivo.setType(item_.getType());
                }
                sivo.setItemId(ir.getItemId());
                sivo.setNum(ir.getNum());
                sivos[j++] = sivo;
            }
            spvo.setGameCoin(rule.getGameCoin());
            spvo.setGoldCoin(rule.getGoldCoin());
            spvo.setItemVO(sivos);
            spvos[i++] = spvo;
        }
        RspSuccessiveFactory.setVO(spvos);

        long t = System.currentTimeMillis() - start;
        logger.info("加载<连续登陆>规则完成,耗时 " + t + "/ms");
    }
}
