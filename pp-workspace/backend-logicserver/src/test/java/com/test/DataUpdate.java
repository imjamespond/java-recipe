package com.test;

import com.pengpeng.stargame.dao.RedisDB;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.piazza.cmd.FamilyInfoRpc;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.container.impl.FamilyRuleContainerImpl;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import org.junit.Ignore;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * User: mql
 * Date: 13-11-1
 * Time: 上午10:49
 */
@Ignore
public class DataUpdate {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context-logicserver.xml", "context-jredis.xml", "beanRefRuleDataAccess.xml"});
        RedisDB redisDB = (RedisDB) ctx.getBean("redisDB");
        IFamilyRuleContainer familyRuleContainer= (IFamilyRuleContainer) ctx.getBean("familyRuleContainerImpl");
        IFamilyDao familyDao= (IFamilyDao) ctx.getBean("familyDao");
        Map<String, IRuleLoader> loaders = ctx.getBeansOfType(IRuleLoader.class);
        for (IRuleLoader loader : loaders.values()) {
            loader.load();
        }
        for(FamilyRule familyRule:familyRuleContainer.getAll()){
//            System.out.println("rule :"+familyRule.getName());
            Family family=familyDao.getBean(familyRule.getId());
            System.out.println(family.getId()+" "+family.getName()+" "+family.getFansValue()+" "+family.getFunds());
            family.setName(familyRule.getName());
//            familyDao.saveBean(family);
        }
    }
}
