package com.pengpeng.stargame.room.rule;

import com.pengpeng.stargame.farm.container.IFarmGiftItemRuleContainer;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.player.rule.BaseGiftRule;
import com.pengpeng.stargame.room.container.IRoomExpansionRuleContainer;
import com.pengpeng.stargame.room.container.IRoomGiftItemRuleContainer;
import com.pengpeng.stargame.room.container.IRoomItemRuleContainer;
import com.pengpeng.stargame.room.container.IRoomRuleContainer;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 个人放假规则加载器
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-22下午4:49
 */
@Component
public class RoomRuleLoader extends HibernateDaoSupport implements IRuleLoader {
    private static final Logger logger = Logger.getLogger(RoomRuleLoader.class);

    @Autowired
    private IRoomItemRuleContainer roomItemRuleContainer;

    @Autowired
    private IRoomRuleContainer roomRuleContainer;

    @Autowired
    private IRoomGiftItemRuleContainer roomGiftItemRuleContainer;
    @Autowired
    private IRoomExpansionRuleContainer roomExpansionRuleContainer;

    @Autowired
    @Qualifier("ruleSession")
    public void setSession(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        logger.info("正在加载<个人房间>规则...");

        //加载房间规则
//        List<RoomRule> roomRules = this.getHibernateTemplate().find("from RoomRule r");
//        for(RoomRule rule:roomRules){
//            roomRuleContainer.addElement(rule);
//        }

        //加载房间物品规则
        List<RoomItemRule> itemRules = this.getHibernateTemplate().find("from RoomItemRule r");
        for(RoomItemRule rule:itemRules){
            roomItemRuleContainer.addElement(rule);
        }

        // 房间礼物列表
        List<BaseGiftRule> BaseGiftRules = this.getHibernateTemplate().find("from BaseGiftRule r where r.presentType=2");
        for(BaseGiftRule baseGiftRule : BaseGiftRules){
            roomGiftItemRuleContainer.addElement(baseGiftRule);
        }

        // 房间扩建信息
        List<RoomExpansionRule>  roomExpansionRules = this.getHibernateTemplate().find("from RoomExpansionRule r ");
        for(RoomExpansionRule roomExpansionRule : roomExpansionRules){
            roomExpansionRuleContainer.addElement(roomExpansionRule);
        }

        long t = System.currentTimeMillis() - start;
        logger.info("加载<个人房间>规则完成,耗时 " + t + "/ms");

    }
}
