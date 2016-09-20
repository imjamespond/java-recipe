package com.pengpeng.admin.analyse;

import com.pengpeng.admin.analyse.model.FarmData;
import com.pengpeng.admin.stargame.common.DateUtil;
import com.pengpeng.admin.stargame.dao.IPlayerMapActionModelDao;
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

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-11-13上午11:22
 */
@Controller
@RequestMapping("/analyse")
public class FarmAnalyseAction {
    @Autowired
    private HibernateTemplate template;
    @Autowired
    private RedisKeyValueDB redisKeyValueDB;

    /** 进入农场的统计
     select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count
    from gm_player_map_action
    where mapId='map_01' and date BETWEEN '2013-11-01' and '2013-11-13'
    group by DATE_FORMAT(date,'%Y-%m-%d');
     */
//    private static String ENTER_FARM = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
//            "    from gm_player_map_action\n" +
//            "    where mapId='map_01' and date BETWEEN :sdate and :edate\n" +
//            "    group by DATE_FORMAT(date,'%Y-%m-%d')";
    private static String ENTER_FARM = "select b.date,count(*)\n" +
            "    from(select DATE_FORMAT(date,'%Y-%m-%d') as date,pid\n" +
            "    from gm_player_map_action\n" +
            "    where mapId='map_01' and date BETWEEN :sdate and :edate\n" +
            "    group by DATE_FORMAT(date,'%Y-%m-%d'),pid) b\n" +
            "    group by b.date";

    /** 农场种植
     * select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count
     from gm_player_harvest_action
     where type =9 and date BETWEEN '2013-11-08' and '2013-11-09'
     group by DATE_FORMAT(date,'%Y-%m-%d')
     * @return
     */
    private static String FARM_ZZ = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_harvest_action\n" +
            "     where type =10 and date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";
    /** 农场收获
     * select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count
     from gm_player_harvest_action
     where type =10 and date BETWEEN '2013-11-08' and '2013-11-09'
     group by DATE_FORMAT(date,'%Y-%m-%d');
     * @return
     */
    private static String FARM_SH = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_harvest_action\n" +
            "     where type =10 and date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";
    /** 协助好友收获
     * select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count
     from gm_player_harvest_action
     where type =11 and date BETWEEN '2013-11-08' and '2013-11-09'
     group by DATE_FORMAT(date,'%Y-%m-%d');
     * @return
     */
    private static String FARM_XZ = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_harvest_action\n" +
            "     where type =11 and date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";
    /** 农场好评
     * select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count
     from gm_player_farm_compliment_action
     where date BETWEEN '2013-11-08' and '2013-11-09'
     group by DATE_FORMAT(date,'%Y-%m-%d');
     * @return
     */
    private static String FARM_HP = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_farm_compliment_action\n" +
            "     where date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";
    /** 刷新农场订单
     * select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count
     from gm_player_order_reset_action
     where date BETWEEN '2013-11-08' and '2013-11-09'
     group by DATE_FORMAT(date,'%Y-%m-%d');
     * @return
     */
    private static String FARM_SX = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_order_reset_action\n" +
            "     where date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";
    /**
     * 购买种子(未单独统计,不好区分)
     * select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count
     from gm_player_buy_action
     where type=1 and itemType=1 and date BETWEEN '2013-11-08' and '2013-11-09'
     group by DATE_FORMAT(date,'%Y-%m-%d');
     * @return
     */
    private static String FARM_GM = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_buy_action\n" +
            "     where type=1 and itemType=1 and date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";
    /** 使用铲子
     * select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count
     from gm_player_harvest_action
     where type =26 and date BETWEEN '2013-11-08' and '2013-11-09'
     group by DATE_FORMAT(date,'%Y-%m-%d')
     * @return
     */
    private static String FARM_CZ = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_harvest_action\n" +
            "     where type =26 and date BETWEEN :sdate and :edate\n" +
            "     group by DATE_FORMAT(date,'%Y-%m-%d')";
    /**
     * 卖出作物统计
     * select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count
     from gm_player_item
     where type=1 and itemType=1 date BETWEEN '2013-11-08' and '2013-11-09'
     group by DATE_FORMAT(date,'%Y-%m-%d');
     * @return
     */
    private static String FARM_ZWTJ = "select DATE_FORMAT(date,'%Y-%m-%d') as date,count(id) as count\n" +
            "     from gm_player_item\n" +
            "     where type=1 and itemType=1 and date BETWEEN :sdate and :edate\n" +
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

    @RequestMapping(value="/operation",method={RequestMethod.GET})
    public ModelAndView operation(){
        Map<String,String> map = new HashMap<String,String>();
        Date sdate = DateUtil.addDate(new Date(),-1);
        map.put("sdate",DateUtil.parseDateString(sdate,"yyyy-MM-dd"));
        map.put("edate",DateUtil.parseDateString(new Date(),"yyyy-MM-dd"));
        return new ModelAndView("analyse/operation",map);
    }

    @RequestMapping(value="/operation/list",method={RequestMethod.GET})
    public @ResponseBody List<FarmData> analysoperation(@RequestParam String sdate,@RequestParam String edate){
        Map<String,FarmData> map = new HashMap<String,FarmData>();
        Date s = DateUtil.toDate(sdate,"yyyy-MM-dd");
        Date e = DateUtil.toDate(edate,"yyyy-MM-dd");
        List<FarmData> list = new ArrayList<FarmData>(map.values());
        while(true){

            if (e.before(s)){
                break;
            }
            FarmData farmData=new FarmData();
            farmData.setDate(s);

            String st1="0";
            if(redisKeyValueDB.get(StatisticConstant.getDayRegKey(s,""))!=null){
                st1=redisKeyValueDB.get(StatisticConstant.getDayRegKey(s, ""));
            }
            farmData.setSt1(st1);

            String st2="0";
            if(redisKeyValueDB.get(StatisticConstant.getDayLoginKey(s, ""))!=null){
                st2=redisKeyValueDB.get(StatisticConstant.getDayLoginKey(s, ""));
            }
            farmData.setSt2(st2);

            String st3="0";
            if(redisKeyValueDB.get(StatisticConstant.getDayS_F_REG_LOG(s, ""))!=null){
                st3=redisKeyValueDB.get(StatisticConstant.getDayS_F_REG_LOG(s, ""));
            }
            farmData.setSt3(st3);

            String st4="0";
            if(redisKeyValueDB.get(StatisticConstant.getDayS_RECHAGE(s, ""))!=null){
                st4=redisKeyValueDB.get(StatisticConstant.getDayS_RECHAGE(s, ""));
            }
            farmData.setSt4(st4);


            String st5="0";
            if(redisKeyValueDB.get(StatisticConstant.getDayS_RECHAGE_P(s, ""))!=null){
                st5=redisKeyValueDB.get(StatisticConstant.getDayS_RECHAGE_P(s, ""));
            }
            farmData.setSt5(st5);

            String st6="0";
            if(redisKeyValueDB.get(StatisticConstant.getDayS_RECHAGE_F(s, ""))!=null){
                st6=redisKeyValueDB.get(StatisticConstant.getDayS_RECHAGE_F(s, ""));
            }
            farmData.setSt6(st6);


            String st7="0";
            if(redisKeyValueDB.get(StatisticConstant.getDayS_RECHAGE_P_F(s, ""))!=null){
                st7=redisKeyValueDB.get(StatisticConstant.getDayS_RECHAGE_P_F(s, ""));
            }
            farmData.setSt7(st7);

            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(1);
            float a = Float.parseFloat(farmData.getSt5())/Float.parseFloat(farmData.getSt2());
            float percent = a * 100;
            String result = numberFormat.format(percent);
            farmData.setSt8(result);
            if(farmData.getSt2().equals("0")){
                farmData.setSt8("0");
            }

            a= Float.parseFloat(farmData.getSt7())/Float.parseFloat(farmData.getSt3());
            percent = a * 100;
            result = numberFormat.format(percent);
            farmData.setSt9(result);
            if(farmData.getSt3().equals("0")){
                farmData.setSt9("0");
            }


            a= Float.parseFloat(farmData.getSt4())/Float.parseFloat(farmData.getSt5());
//            percent = a * 100;
//            result = numberFormat.format(percent);
            DecimalFormat fnum = new DecimalFormat("##0.00");
            farmData.setSt10(fnum.format(a));
            if(farmData.getSt5().equals("0")){
                farmData.setSt10("0");
            }

            a= Float.parseFloat(farmData.getSt6())/Float.parseFloat(farmData.getSt7());
//            percent = a * 100;
//            result = numberFormat.format(percent);
            farmData.setSt11(fnum.format(a));
            if(farmData.getSt7().equals("0")){
                farmData.setSt11("0");
            }


            a= Float.parseFloat(farmData.getSt4())/Float.parseFloat(farmData.getSt1());
//            percent = a * 100;
//            result = numberFormat.format(percent);
            farmData.setSt12(fnum.format(a));
            if(farmData.getSt1().equals("0")){
                farmData.setSt12("0");
            }

            a= Float.parseFloat(farmData.getSt6())/Float.parseFloat(farmData.getSt4());
            percent = a * 100;
            result = numberFormat.format(percent);
            farmData.setSt13(result);
            if(farmData.getSt4().equals("0")){
                farmData.setSt13("0");
            }

            String st14="0";
            if(redisKeyValueDB.get(StatisticConstant.getsKeepDayKey(1,s, ""))!=null){
                st14=redisKeyValueDB.get(StatisticConstant.getsKeepDayKey(1,s, ""));
            }
            farmData.setSt14(st14);
            list.add(farmData);
            s = DateUtil.addDate(s,1);
        }

        return list;
    }

    @RequestMapping(value="/goldStat",method={RequestMethod.GET})
    public ModelAndView goldStat(){
        Map<String,String> map = new HashMap<String,String>();
        Date sdate = DateUtil.addDate(new Date(),-1);
        map.put("sdate",DateUtil.parseDateString(sdate,"yyyy-MM-dd"));
        map.put("edate",DateUtil.parseDateString(new Date(),"yyyy-MM-dd"));
        return new ModelAndView("analyse/goldStat",map);
    }
    @RequestMapping(value="/goldStat/list",method={RequestMethod.GET})
    public @ResponseBody List<FarmData> analysgoldStat(@RequestParam String sdate,@RequestParam String edate){
        Map<String,FarmData> map = new HashMap<String,FarmData>();
        Date s = DateUtil.toDate(sdate,"yyyy-MM-dd");
        Date e = DateUtil.toDate(edate,"yyyy-MM-dd");
        List<FarmData> list = new ArrayList<FarmData>(map.values());
        while(true){

            if (e.before(s)){
                break;
            }
            FarmData farmData=new FarmData();
            farmData.setDate(s);
            for(int i=1;i<51;i++){
                if(redisKeyValueDB.get(StatisticConstant.getDayGoldTypeKey(s,i,""))!=null){
                    Field field = null;
                    try {
                        field = farmData.getClass().getDeclaredField("st"+String.valueOf(i));
                        field.setAccessible(true);
                        field.set(farmData, redisKeyValueDB.get(StatisticConstant.getDayGoldTypeKey(s,i,"")));
                    } catch (NoSuchFieldException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                } else {

                }
            }

            list.add(farmData);

            s = DateUtil.addDate(s,1);
        }

        return list;
    }


    @RequestMapping(value="/farm",method={RequestMethod.GET})
    public ModelAndView farm(){
        Map<String,String> map = new HashMap<String,String>();
        Date sdate = DateUtil.addDate(new Date(),-1);
        map.put("sdate",DateUtil.parseDateString(sdate,"yyyy-MM-dd"));
        map.put("edate",DateUtil.parseDateString(new Date(),"yyyy-MM-dd"));
        return new ModelAndView("analyse/farm",map);
    }

    @RequestMapping(value="/farm/list",method={RequestMethod.GET})
    public @ResponseBody List<FarmData> analyseFarm(@RequestParam String sdate,@RequestParam String edate){
        Map<String,FarmData> map = new HashMap<String,FarmData>();
        Date s = DateUtil.toDate(sdate,"yyyy-MM-dd");
        Date e = DateUtil.toDate(edate,"yyyy-MM-dd");
        List<FarmData> list = new ArrayList<FarmData>(map.values());
        while(true){

            if (e.before(s)){
                break;
            }
            FarmData farmData=new FarmData();
            farmData.setDate(s);
            for(int i=1;i<11;i++){
                if(redisKeyValueDB.get(StatisticConstant.getDayIntegrateTypeKey(s,i,""))!=null){
                    Field field = null;
                    try {
                    field = farmData.getClass().getDeclaredField("st"+String.valueOf(i));
                    field.setAccessible(true);
                    field.set(farmData, redisKeyValueDB.get(StatisticConstant.getDayIntegrateTypeKey(s,i,"")));
                    } catch (NoSuchFieldException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                } else {

                }
            }

            list.add(farmData);

            s = DateUtil.addDate(s,1);
        }

        return list;
    }


}
