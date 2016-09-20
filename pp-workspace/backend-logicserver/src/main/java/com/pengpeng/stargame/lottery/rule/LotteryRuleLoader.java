package com.pengpeng.stargame.lottery.rule;

import com.pengpeng.stargame.constant.BaseRewardConstant;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.lottery.container.ILotteryContainer;
import com.pengpeng.stargame.lottery.container.ILotteryTypeContainer;
import com.pengpeng.stargame.lottery.dao.ILotteryInfoListDao;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.model.lottery.Lottery;
import com.pengpeng.stargame.model.lottery.OneLotteryInfo;
import com.pengpeng.stargame.player.rule.BubbleRewardRule;
import com.pengpeng.stargame.rsp.RspLotteryFactory;
import com.pengpeng.stargame.vo.lottery.LotteryInfoVO;
import com.pengpeng.stargame.vo.lottery.LotteryVO;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:29
 */
@Component
public class LotteryRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(LotteryRuleLoader.class);

    @Autowired
    public ILotteryContainer lotteryContainer;
    @Autowired
    public ILotteryTypeContainer lotteryTypeContainer;
    @Autowired
    private RspLotteryFactory lotteryFactory;
    @Autowired
    private ILotteryInfoListDao lotteryInfoListDao;

    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<抽奖>规则...");

        //抽奖规则
        List<LotteryRule> rules = this.getHibernateTemplate().find("from LotteryRule r");
        for(LotteryRule rule:rules){
            lotteryContainer.addElement(rule);
            lotteryContainer.addElementAndSort(rule);
        }

        List<LotteryTypeRule> tRules = this.getHibernateTemplate().find("from LotteryTypeRule");
        for(LotteryTypeRule rule:tRules){
            lotteryTypeContainer.addElement(rule);
        }

        List<RouletteRule> brrs = this.getHibernateTemplate().find("from RouletteRule r order by r.id asc ");
        logger.info("轮盘规则:"+brrs.size());
        for(RouletteRule brr:brrs){
            if(brr.getGoldCoin()>0){
                lotteryContainer.addRouletteList("","",brr.getGoldCoin(), BaseRewardConstant.TYPE_GOLDCOIN,brr.getProbability(),brr.getGender(), brr.isSpeaker());
            }else if(brr.getFarmExp()>0){
                lotteryContainer.addRouletteList("","",brr.getFarmExp(),BaseRewardConstant.TYPE_FARMEXP,brr.getProbability(),brr.getGender(), brr.isSpeaker());
            }else if(brr.getGameCoin()>0){
                lotteryContainer.addRouletteList("","",brr.getGameCoin(),BaseRewardConstant.TYPE_GAMECOIN,brr.getProbability(),brr.getGender(), brr.isSpeaker());
            }else if(!brr.getItemId().equals("0")){
                final String itemId = brr.getItemId();
                List<BaseItemRule> list = (List<BaseItemRule>) this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
                    @Override
                    public Object doInHibernate(Session session) throws HibernateException, SQLException {
                        Query query = session.createQuery("from BaseItemRule r where r.itemsId = :itemsId");

                        query.setString("itemsId", itemId);

                        return query.setFirstResult(0).setMaxResults(1).list();
                    }
                });
                System.out.println(list.get(0).getName());
                lotteryContainer.addRouletteList(brr.getItemId(),list.get(0).getName(),brr.getItemNum(),BaseRewardConstant.TYPE_ITEM,brr.getProbability(),brr.getGender(), brr.isSpeaker());
            }
        }

        long t = System.currentTimeMillis() - start;
        logger.info("加载<抽奖>规则完成,耗时 " + t + "/ms");
    }
}
