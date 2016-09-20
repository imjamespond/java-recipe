package com.pengpeng.stargame.player.rule;

import com.pengpeng.stargame.constant.BaseRewardConstant;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.player.container.*;
import com.pengpeng.stargame.manager.IRuleLoader;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

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
    private IChatContainer chatContainer;

    @Autowired
    private IActiveRuleContainer activeRuleContainer;

    @Autowired
    private IActiveRewardRuleContainer activeRewardRuleContainer;

    @Autowired
    private IBubbleRuleContainer bubbleRuleContainer;
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

        logger.info("正在加载<敏感词>规则...");
        List<WordRule> words = this.getHibernateTemplate().find("from WordRule w where w.state=0");
        for(WordRule rule:words){
            String row = rule.getWords();
            StringTokenizer tokenizer = new StringTokenizer(row,",");
            while(tokenizer.hasMoreElements()){
                String word = tokenizer.nextToken();
                chatContainer.addWord(word);
            }
        }
        chatContainer.init();
        logger.info("敏感词规则:"+words.size());

        List<ActiveRule> ars = this.getHibernateTemplate().find("from ActiveRule r");
        logger.info("活跃度规则:"+ars.size());
        for(ActiveRule activeRule:ars){
            if(activeRule.getDisplay()==1){
                activeRuleContainer.addElement(activeRule);
            }
        }
//        activeRuleContainer.addElement(ars);

        List<ActiveRewardRule> arrs = this.getHibernateTemplate().find("from ActiveRewardRule r");
        activeRewardRuleContainer.addElement(arrs);
        logger.info("活跃度奖励规则:"+arrs.size());


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


        List<BubbleRewardRule> brrs = this.getHibernateTemplate().find("from BubbleRewardRule r");
        logger.info("泡泡规则:"+brrs.size());
        int probability = 0;
        for(BubbleRewardRule brr:brrs){
            probability += brr.getProbability();
            if(brr.getCredit()>0){
                bubbleRuleContainer.addAttachList("","",brr.getCredit(),BaseRewardConstant.TYPE_CREDIT,probability);
            }else if(brr.getFarmExp()>0){
                bubbleRuleContainer.addAttachList("","",brr.getFarmExp(),BaseRewardConstant.TYPE_FARMEXP,probability);
            }else if(brr.getGameCoin()>0){
                bubbleRuleContainer.addAttachList("","",brr.getGameCoin(),BaseRewardConstant.TYPE_GAMECOIN,probability);
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
                bubbleRuleContainer.addAttachList(brr.getItemId(),list.get(0).getName(),brr.getItemNum(),BaseRewardConstant.TYPE_ITEM,probability);
            }
        }


        long t = System.currentTimeMillis() - start;
        logger.info("加载<Player,Scene,Transfer>规则完成,耗时 " + t + "/ms");
    }
}
