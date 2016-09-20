package com.pengpeng.admin.analyse;

import com.pengpeng.admin.analyse.model.CommonData;
import com.pengpeng.admin.analyse.model.DataFactory;
import com.pengpeng.admin.analyse.model.FarmData;
import com.pengpeng.admin.stargame.common.DateUtil;
import com.tongyi.action.BaseAction;
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
public class FamilyAnalyseAction extends BaseAction {
    @Autowired
    private HibernateTemplate template;

    //家族历史用户数
    private static String SQL_1 ="";
    //进入家族用户数
    private static String SQL_2 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "    from gm_player_family_action\n" +
            "    where date BETWEEN :sdate and :edate\n" +
            "    group by DATE_FORMAT(date,'%Y-%m-%d')";
    //进入明星广场用户数
    private static String SQL_3 = "select b.date,count(*) from (select DATE_FORMAT(date,'%Y-%m-%d') as date,pid\n" +
            "    from gm_player_map_action\n" +
            "    where mapId='map_10' and date BETWEEN :sdate and :edate\n" +
            "    group by DATE_FORMAT(date,'%Y-%m-%d'),pid) b group by b.date";
    //礼物盒统计
    private static String SQL_4 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "    from gm_player_giving_action\n" +
            "    where date BETWEEN :sdate and :edate\n" +
            "    group by DATE_FORMAT(date,'%Y-%m-%d')";
    //摇钱树统计
    private static String SQL_5 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "    from gm_player_money_tree_action\n" +
            "    where date BETWEEN :sdate and :edate\n" +
            "    group by DATE_FORMAT(date,'%Y-%m-%d')";
    //家族任务统计
    private static String SQL_6 =  "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "    from gm_player_task_action\n" +
            "    where type=3 and date BETWEEN :sdate and :edate\n" +
            "    group by DATE_FORMAT(date,'%Y-%m-%d')";
    //家族游戏币捐献统计
    private static String SQL_7 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "    from gm_player_donation_action\n" +
            "    where gameCoin>0 and date BETWEEN :sdate and :edate\n" +
            "    group by DATE_FORMAT(date,'%Y-%m-%d')";
    //家族达人币捐献统计
    private static String SQL_8 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "    from gm_player_donation_action\n" +
            "    where goldCoin>0 and date BETWEEN :sdate and :edate\n" +
            "    group by DATE_FORMAT(date,'%Y-%m-%d')";
    //家族福利领取统计    gm_player_family_award_action
    private static String SQL_9 = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "    from gm_player_family_award_action\n" +
            "    where date BETWEEN :sdate and :edate\n" +
            "    group by DATE_FORMAT(date,'%Y-%m-%d')";


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

    @RequestMapping(value="/family",method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView family(){
        Map<String,String> map = new HashMap<String,String>();
        Date sdate = DateUtil.addDate(new Date(), -7);
        map.put("sdate",DateUtil.parseDateString(sdate,"yyyy-MM-dd"));
        map.put("edate",DateUtil.parseDateString(new Date(),"yyyy-MM-dd"));
        return new ModelAndView("analyse/family",map);
    }

    //日期	明星	家族历史用户数	进入家族用户数	进入明星广场用户数	礼物盒统计	摇钱树统计	家族任务统计	家族游戏币捐献统计	家族达人币捐献统计	家族福利领取统计
    @RequestMapping(value="/family/list",method={RequestMethod.GET})
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
        //factory.sql1(map,count(SQL_1,sdate,edate));
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
