package com.pengpeng.stargame.model.statistic;


import com.pengpeng.stargame.util.DateUtil;

import java.util.Date;

/**
 * User: mql
 * Date: 14-3-5
 * Time: 上午9:41
 * 统计数据的 一些常量
 */
public class StatisticConstant {
    private final static String DELEMETER="_";
    private final static String S_LOG="S_LOG"; //登录统计Key 日
    private final static String S_REG="S_REG"; //注册统计Key  日
    private final static String S_KEEP_DAY="S_KEEP_DAY";//日留存  日
    private final static String S_ALL_NUM="S_ALL_NUM";//游戏总用户数
    private final static String S_INTEGRATE="S_INTEGRATE";//游戏内的日积分获取  日 总量

    private final static String S_INTEGRATE_TYPE="S_INTEGRATE_TYPE";//游戏内的日积分获取  日 具体到操作的总量
    private final static String S_GOLD_D_TYPE="S_GOLD_D_TYPE";//游戏内 达人币的 消耗  具体到具体分类

    private final static String S_F_REG_LOG="S_F_REG_LOG"; //首日注册登录 首日注册 就完成新手教程
    private final static String S_RECHAGE="S_RECHAGE";//充值
    private final static String S_RECHAGE_P="S_RECHAGE_P";//充值 人数
    private final static String S_RECHAGE_F="S_RECHAGE_F";//充值  首充
    private final static String S_RECHAGE_P_F="S_RECHAGE_P_F";//首充人数


    /**
     * 获取首充 人数
     * @param date
     * @param familyId
     * @return
     */
    public static String getDayS_RECHAGE_P_F(Date date,String familyId){
        StringBuffer sb=new StringBuffer(S_RECHAGE_P_F+DELEMETER);
        if(familyId!=null&&!familyId.equals("")){
            sb.append(familyId+DELEMETER);
        }
        sb.append(DateUtil.getDateFormat(date,"yyyy-MM-dd"));
        return sb.toString();
    }

    /**
     * 获取首充
     * @param date
     * @param familyId
     * @return
     */
    public static String getDayS_RECHAGE_F(Date date,String familyId){
        StringBuffer sb=new StringBuffer(S_RECHAGE_F+DELEMETER);
        if(familyId!=null&&!familyId.equals("")){
            sb.append(familyId+DELEMETER);
        }
        sb.append(DateUtil.getDateFormat(date,"yyyy-MM-dd"));
        return sb.toString();
    }

    /**
     * 获取充值   人数
     * @param date
     * @param familyId
     * @return
     */
    public static String getDayS_RECHAGE_P(Date date,String familyId){
        StringBuffer sb=new StringBuffer(S_RECHAGE_P+DELEMETER);
        if(familyId!=null&&!familyId.equals("")){
            sb.append(familyId+DELEMETER);
        }
        sb.append(DateUtil.getDateFormat(date,"yyyy-MM-dd"));
        return sb.toString();
    }
    /**
     * 获取充值
     * @param date
     * @param familyId
     * @return
     */
    public static String getDayS_RECHAGE(Date date,String familyId){
        StringBuffer sb=new StringBuffer(S_RECHAGE+DELEMETER);
        if(familyId!=null&&!familyId.equals("")){
            sb.append(familyId+DELEMETER);
        }
        sb.append(DateUtil.getDateFormat(date,"yyyy-MM-dd"));
        return sb.toString();
    }


    /**
     * 获取 首日注册登录 首日注册 就完成新手教程
     * @param date
     * @param familyId
     * @return
     */
    public static String getDayS_F_REG_LOG(Date date,String familyId){
        StringBuffer sb=new StringBuffer(S_F_REG_LOG+DELEMETER);
        if(familyId!=null&&!familyId.equals("")){
            sb.append(familyId+DELEMETER);
        }
        sb.append(DateUtil.getDateFormat(date,"yyyy-MM-dd"));
        return sb.toString();
    }

    /**
     * 获取 游戏内 达人币消耗，具体统计到 某个类型
     * @param date
     * @param type
     * @param familyId
     * @return
     */
    public static String getDayGoldTypeKey(Date date,int type,String familyId){
        StringBuffer sb=new StringBuffer(S_GOLD_D_TYPE+DELEMETER+String.valueOf(type)+DELEMETER);
        if(familyId!=null&&!familyId.equals("")){
            sb.append(familyId+DELEMETER);
        }
        sb.append(DateUtil.getDateFormat(date,"yyyy-MM-dd"));
        return sb.toString();
    }
    /**
     * 获取 游戏内 积分获取，具体统计到 某个类型
     * @param date
     * @param type
     * @param familyId
     * @return
     */
   public static String getDayIntegrateTypeKey(Date date,int type,String familyId){
       StringBuffer sb=new StringBuffer(S_INTEGRATE_TYPE+DELEMETER+String.valueOf(type)+DELEMETER);
       if(familyId!=null&&!familyId.equals("")){
           sb.append(familyId+DELEMETER);
       }
       sb.append(DateUtil.getDateFormat(date,"yyyy-MM-dd"));
       return sb.toString();
   }

    /**
     * 获取日获得积分 Key
     * @param date
     * @param familyId
     * @return
     */
    public static String getDayIntegrateKey(Date date,String familyId){
        StringBuffer sb=new StringBuffer(S_INTEGRATE+DELEMETER);
        if(familyId!=null&&!familyId.equals("")){
            sb.append(familyId+DELEMETER);
        }
        sb.append(DateUtil.getDateFormat(date,"yyyy-MM-dd"));
        return sb.toString();
    }
    /**
     * 获取所有注册 Key
     * @param familyId
     * @return
     */
    public static String getAllRegKey(String familyId){
        StringBuffer sb=new StringBuffer(S_ALL_NUM);
        if(familyId!=null&&!familyId.equals("")){
            sb.append(DELEMETER+familyId);
        }
        return sb.toString();
    }

    /**
     * 获取 日注册Key
     * @param date
     * @param familyId
     * @return
     */
     public static String getDayRegKey(Date date,String familyId){
         StringBuffer sb=new StringBuffer(S_REG+DELEMETER);
         if(familyId!=null&&!familyId.equals("")){
             sb.append(familyId+DELEMETER);
         }
         sb.append(DateUtil.getDateFormat(date,"yyyy-MM-dd"));
         return sb.toString();
     }
    /**
     * 获取日登陆用户数量key
     * @param date
     * @param familyId
     * @return
     */
    public static String getDayLoginKey(Date date,String familyId){
        StringBuffer sb=new StringBuffer(S_LOG+DELEMETER);
        if(familyId!=null&&!familyId.equals("")){
            sb.append(familyId+DELEMETER);
        }
        sb.append(DateUtil.getDateFormat(date,"yyyy-MM-dd"));
        return sb.toString();
    }

    /**
     * 获取日留存的key
     * @param day
     * @param date
     * @return
     */
    public static String getsKeepDayKey(int day,Date date,String familyId){
        StringBuffer sb=new StringBuffer(S_KEEP_DAY+DELEMETER+String.valueOf(day)+DELEMETER);
        if(familyId!=null&&!familyId.equals("")){
            sb.append(familyId+DELEMETER);
        }
        sb.append(DateUtil.getDateFormat(date,"yyyy-MM-dd"));
        return sb.toString();
    }
}
