package com.pengpeng.admin.analyse;

import com.pengpeng.admin.analyse.model.CommonData;
import com.pengpeng.admin.analyse.model.DataFactory;
import com.pengpeng.admin.analyse.model.FarmData;
import com.pengpeng.admin.stargame.common.DateUtil;
import com.pengpeng.stargame.dao.RedisKeyValueDB;
import com.pengpeng.stargame.model.statistic.StatisticConstant;
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-11-13上午11:22
 */
@Controller
@RequestMapping("/analyse")
public class TotalAnalyseAction {
    @Autowired
    private HibernateTemplate template;
    @Autowired
    private RedisKeyValueDB redisKeyValueDB;
    //游戏历史注册用户数
    private String SQL_1="select :sdate as date,count(id) as count\n" +
            "from gm_player_register\n" +
            "where DATE_FORMAT(date,'%Y-%m-%d')<= :sdate";
    //游戏当天登录用户数
    private String SQL_2="select DATE_FORMAT(b.date,'%Y-%m-%d') as date,count(*)\n" +
            "    from (select DATE_FORMAT(date,'%Y-%m-%d') as date,pid\n" +
            "    from gm_player_login_action\n" +
            "    where type=1 and date BETWEEN :sdate and :edate\n" +
            "    group by DATE_FORMAT(date,'%Y-%m-%d'),pid) b\n" +
            "    group by DATE_FORMAT(b.date,'%Y-%m-%d') ";

    //用户流失率   //游戏当天活跃用户规模/历史注册总量
    private String SQL_3="";
    //平均在线(单位:人/小时)  (缺数据)
    private String SQL_4="";
    //最高在线(单位:人)
    private String SQL_5="select DATE_FORMAT(maxTime,'%Y-%m-%d') as date,maxNum as count\n" +
            "from gm_player_oline\n" +
            "where DATE_FORMAT(maxTime,'%Y-%m-%d')=:sdate";
    //最低在线(单位:人)
    private String SQL_6="select DATE_FORMAT(minTime,'%Y-%m-%d') as date,minNum\n" +
            "from gm_player_oline\n" +
            "where DATE_FORMAT(minTime,'%Y-%m-%d')=:sdate";
    //活跃数
    private String SQL_7="select DATE_FORMAT(a.date,'%Y-%m-%d'),count(*)\n" +
            "from gm_player_login_action a ,gm_player_register b\n" +
            "where a.type =1 and a.pid=b.pid and DATE_FORMAT(b.date,'%Y-%m-%d')=:sdate\n" +
            "and DATE_FORMAT(a.date,'%Y-%m-%d')=:edate\n" +
            "group by DATE_FORMAT(a.date,'%Y-%m-%d')";
    //7天活跃数
    private String SQL_8="select DATE_FORMAT(a.date,'%Y-%m-%d'),count(*)\n" +
            "from gm_player_login_action a ,gm_player_register b\n" +
            "where a.type =1 and a.pid=b.pid and DATE_FORMAT(b.date,'%Y-%m-%d')=:sdate\n" +
            "and DATE_FORMAT(a.date,'%Y-%m-%d')=:edate\n" +
            "group by DATE_FORMAT(a.date,'%Y-%m-%d')";
    //充值金额
    private String SQL_9="select DATE_FORMAT(date,'%Y-%m-%d') as date,sum(money) as count\n" +
            "from gm_player_recharge_action\n" +
            "where date BETWEEN :sdate and :edate\n" +
            "group by DATE_FORMAT(date,'%Y-%m-%d')";
    //付费人数
    private String SQL_10="select b.date,count(*)\n" +
            "from (\n" +
            "select DATE_FORMAT(date,'%Y-%m-%d') as date,pid\n" +
            "from gm_player_recharge_action\n" +
            "where date BETWEEN :sdate and :edate\n" +
            "group by DATE_FORMAT(date,'%Y-%m-%d'),pid\n" +
            ") b\n" +
            "group by b.date";
    //ARPU值   //充值金额/付费人数
    private String SQL_11="";
    //付费率  //付费用户/活跃用户*100%
    private String SQL_12="";
    //当天游戏积分获取数  gm_player_activity_action
    private String SQL_13="select DATE_FORMAT(date,'%Y-%m-%d') as date,sum(point)\n" +
            "from gm_player_activity_action\n" +
            "where date BETWEEN :sdate and :edate\n" +
            "group by DATE_FORMAT(date,'%Y-%m-%d')";;

    private Map<String,Object> count(final String sql,final String sdate,final String edate){
        Map<String,Object> map = (Map<String,Object>)template.execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql);
                if (null!=sdate){
                    query.setString("sdate",sdate);
                }
                if (null!=edate){
                    query.setString("edate",edate);
                }
                Map<String,Object> map = new HashMap<String,Object>();
                List<Object[]> list = query.list();
                for(Object[] objs:list){
                    if (objs[1] instanceof Integer){
                        map.put((String)objs[0],new BigInteger(objs[1].toString()));
                    }else if (objs[1] instanceof BigDecimal){
                        map.put((String)objs[0],new BigInteger(objs[1].toString()));
                    }else{
                        map.put((String)objs[0],objs[1]);
                    }

                }
                return map;
            }
        });
        return map;
    }



    @RequestMapping(value="/total",method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView family(){
        Map<String,String> map = new HashMap<String,String>();
        Date sdate = DateUtil.addDate(new Date(), -1);
        map.put("sdate",DateUtil.parseDateString(sdate,"yyyy-MM-dd"));
        map.put("edate",DateUtil.parseDateString(new Date(),"yyyy-MM-dd"));
        return new ModelAndView("analyse/total",map);
    }
    //日期	明星	游戏历史注册用户数	游戏当天登录用户数	用户流失率	平均在线(单位:人/小时)	最高在线(单位:人)	最低在线(单位:人)	活跃数	7天活跃数	充值金额	付费人数	ARPU值	付费率	当天游戏积分获取数
    @RequestMapping(value="/total/list",method={RequestMethod.GET})
    public @ResponseBody
    List<CommonData> list(@RequestParam String sdate,@RequestParam String edate,@RequestParam String fid){
        Map<String,CommonData> map = new HashMap<String,CommonData>();
        Date s = DateUtil.toDate(sdate,"yyyy-MM-dd");
        Date e = DateUtil.toDate(edate,"yyyy-MM-dd");
        DataFactory factory = new DataFactory();

        List<CommonData> list = new ArrayList<CommonData>();
        while(true){
            if (e.before(s)){
                break;
            }
            CommonData commonData=new CommonData();
            commonData.setDate(s);
            int num1=0;
            if(redisKeyValueDB.get(StatisticConstant.getAllRegKey(fid))!=null){
                num1=Integer.parseInt(redisKeyValueDB.get(StatisticConstant.getAllRegKey(fid)));
            }
            commonData.setNum1(num1);



            int num2=0;
            if(redisKeyValueDB.get(StatisticConstant.getDayLoginKey(s, fid))!=null){
                num2=Integer.parseInt(redisKeyValueDB.get(StatisticConstant.getDayLoginKey(s, fid)));
            }
            commonData.setNum2(num2);
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(1);
            float a = (float) ((commonData.getNum2() * 1.0) / (commonData.getNum1()  * 1.0));
            float percent = a * 100;
            String result = numberFormat.format(percent);
            commonData.setString1(result);

            Map<String,Object> maxOnLine=  count(SQL_5, DateUtil.parseDateString(s, "yyyy-MM-dd"), null);
            Map<String,Object> minOnLine=count(SQL_6, DateUtil.parseDateString(s,"yyyy-MM-dd"), null);
            commonData.setNum5(((BigInteger) maxOnLine.get(DateUtil.parseDateString(s, "yyyy-MM-dd"))).intValue());
            commonData.setNum6(((BigInteger) minOnLine.get(DateUtil.parseDateString(s, "yyyy-MM-dd"))).intValue());

            int num7=0;
            if(redisKeyValueDB.get(StatisticConstant.getsKeepDayKey(1,s,fid))!=null){
               num7=Integer.parseInt(redisKeyValueDB.get(StatisticConstant.getsKeepDayKey(1,s,fid)));
            }
            commonData.setNum7(num7);

            int num8=0;
            if(redisKeyValueDB.get(StatisticConstant.getsKeepDayKey(7,s,fid))!=null){
                num8=Integer.parseInt(redisKeyValueDB.get(StatisticConstant.getsKeepDayKey(7,s,fid)));
            }
            commonData.setNum8(num8);

            int num9=0;
            if(redisKeyValueDB.get(StatisticConstant.getDayIntegrateKey(s,fid))!=null){
                num9=Integer.parseInt(redisKeyValueDB.get(StatisticConstant.getDayIntegrateKey(s,fid)));
            }
            commonData.setNum9(num9);

            s = DateUtil.addDate(s,1);
            list.add(commonData);
        }


        return list;
    }

}
