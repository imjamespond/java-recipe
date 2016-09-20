package com.pengpeng.stargame;

import com.pengpeng.admin.analyse.FarmAnalyseAction;
import com.pengpeng.admin.analyse.model.FarmData;
import com.pengpeng.admin.stargame.manager.ModelFactory;
import com.pengpeng.admin.stargame.manager.impl.LineHandleManager;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-11-12下午5:13
 */
public class LogServer {

    public static void main(String[] args){
        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"classpath*:spring/context-log.xml"});
        LineHandleManager m = ctx.getBean(LineHandleManager.class);

        System.out.println("start scan log.");
        if (args==null||args.length<=0){
            System.out.println("scan and check");
            m.scan();
        }else{
            System.out.println("scan and store");
            m.scanAndStore(args[0]);
        }
        System.out.println("OK.");
        System.exit(0);
    }
}
