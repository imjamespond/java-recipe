package com.pengpeng.stargame.piazza.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.piazza.MoneyTree;
import com.pengpeng.stargame.piazza.PiazzaBuilder;
import com.pengpeng.stargame.piazza.container.IMoneyTreeRuleContainer;
import com.pengpeng.stargame.piazza.dao.IMoneyTreeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 13-7-1
 * Time: 上午10:44
 */
@Component("moneytreeDao")
@DaoAnnotation(prefix = "pza.moneytree.")
public class MoneyTreeDaoImpl extends RedisDao<MoneyTree> implements IMoneyTreeDao{
    @Autowired
    private PiazzaBuilder piazzaBuilder;
    @Autowired
    private IMoneyTreeRuleContainer moneyTreeRuleContainer;
    @Override
    public Class<MoneyTree> getClassType() {
        return  MoneyTree.class;
    }


    @Override
    public MoneyTree getMoneyTree(String familyId, Date date) {
        MoneyTree moneyTree=getBean(familyId);
        if(moneyTree==null){
            moneyTree= piazzaBuilder.newMoneyTree(familyId);
            saveBean(moneyTree);
        }
        if(moneyTreeRuleContainer.getRunMoneyTree(moneyTree,new Date())){
            saveBean(moneyTree);
        }
        return moneyTree;
    }
}
