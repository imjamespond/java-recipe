package com.pengpeng.admin.analyse;

import com.pengpeng.admin.analyse.model.CommonData;
import com.pengpeng.admin.analyse.model.DataFactory;
import com.pengpeng.admin.analyse.model.FarmData;
import com.pengpeng.admin.stargame.common.DateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-11-13上午11:22
 */
@Controller
@RequestMapping("/analyse")
public class FashionAnalyseAction {
    @Autowired
    private HibernateTemplate template;
    //进行换装用户数
    private static String SQL_1 = "select a.date,count(*)\n" +
            "from (select DATE_FORMAT(date,'%Y-%m-%d') as date,pid as pid from gm_player_item where action=17 and type = 3 and date BETWEEN :sdate and :edate group by DATE_FORMAT(date,'%Y-%m-%d'),pid) a \n" +
            "group by a.date";
    //换发部统计
    private static String SQL_2 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_item\n" +
            "     where action=17 and type=3 and itemType=1 and date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";
    //换脸部统计
    private static String SQL_3 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_item\n" +
            "     where action=17 and type=3 and itemType=2 and date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";
    //换上装统计
    private static String SQL_4 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_item\n" +
            "     where action=17 and type=3 and itemType=3 and date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";
    //换下装统计
    private static String SQL_5 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_item\n" +
            "     where action=17 and type=3 and itemType=4 and date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";
    //换套装统计
    private static String SQL_6 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_item\n" +
            "     where action=17 and type=3 and itemType=5 and date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";
    //换配饰统计
    private static String SQL_7 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_item\n" +
            "     where action=17 and type=3 and itemType=6 and date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";
    //换背饰统计
    private static String SQL_8 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_item\n" +
            "     where action=17 and type=3 and itemType=7 and date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";

    //换脚底统计
    private static String SQL_9 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_item\n" +
            "     where action=17 and type=3 and itemType=8 and date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";

    private Map<String,Object> count(final String sql,final String sdate,final String edate){
        Map<String,Object> map = (Map<String,Object>)template.execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql);
                query.setString("sdate",sdate);
                query.setString("edate",edate);
                Map<String,Object> map = new HashMap<String,Object>();
                List<Object[]> list = query.list();
                for(Object[] objs:list){
                    map.put((String)objs[0],objs[1]);
                }
                return map;
            }
        });
        return map;
    }
    @RequestMapping(value="/fashion",method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView fashion(){
        Map<String,String> map = new HashMap<String,String>();
        Date sdate = DateUtil.addDate(new Date(), -7);
        map.put("sdate",DateUtil.parseDateString(sdate,"yyyy-MM-dd"));
        map.put("edate",DateUtil.parseDateString(new Date(),"yyyy-MM-dd"));
        return new ModelAndView("analyse/fashion",map);
    }

    //进行换装用户数	换发部统计	换脸部统计	换上装统计	换下装统计	换套装统计	换配饰统计	换背饰统计
    @RequestMapping(value="/fashion/list",method={RequestMethod.GET})
    public @ResponseBody
    List<CommonData> list(@RequestParam String sdate,@RequestParam String edate){
        Map<String,CommonData> map = new HashMap<String,CommonData>();
        Date s = DateUtil.toDate(sdate,"yyyy-MM-dd");
        Date e = DateUtil.toDate(edate,"yyyy-MM-dd");
        while(true){
            if (e.before(s)){
                break;
            }
            map.put(DateUtil.parseDateString(s,"yyyy-MM-dd"),new CommonData(s));
            s = DateUtil.addDate(s,1);
        }
        DataFactory factory = new DataFactory();
        factory.sql1(map,count(SQL_1,sdate,edate));
        factory.sql2(map, count(SQL_2, sdate, edate));
        factory.sql3(map, count(SQL_3, sdate, edate));
        factory.sql4(map, count(SQL_4, sdate, edate));
        factory.sql5(map, count(SQL_5, sdate, edate));
        factory.sql6(map, count(SQL_6, sdate, edate));
        factory.sql7(map, count(SQL_7, sdate, edate));
        factory.sql8(map, count(SQL_8, sdate, edate));
        factory.sql9(map, count(SQL_9, sdate, edate));

        List<CommonData> list = new ArrayList<CommonData>(map.values());
        Collections.sort(list, new Comparator<CommonData>() {
            @Override
            public int compare(CommonData o1, CommonData o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        return list;
    }
}
