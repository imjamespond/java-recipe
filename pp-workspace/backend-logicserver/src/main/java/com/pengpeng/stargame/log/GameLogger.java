package com.pengpeng.stargame.log;

import java.util.Date;

/**
 * User: mql
 * Date: 13-10-14
 * Time: 下午2:22
 */
public class GameLogger {
    //系统数据 与 业务数据 数据分隔符
    public static String B_SPLIT="&&&";
    //数据分割符号
    public static String SPLIT="#";

    // type 1 :用户登录  数据格式：网站用户Id # 类型 # 玩家Pid # 动作发生的日期
    public static  int LOG_1=1;
    // type 2 :用户退出  数据格式：网站用户Id # 类型 # 玩家Pid # 动作发生的日期
    public static  int LOG_2=2;
    // type 3 :用户充值   数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期 # 充值的金额
    public static int LOG_3=3;
    // type 4 :玩家进入地图   数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期 # 进入的地图Id
    public static int LOG_4=4;
    // type 5 :完成任务   数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期 # 完成任务的Id#任务类型#任务子类型
    public static int LOG_5=5;
    // type 6 :幸运翻牌   数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期 # item # num # gamecoin
    public static int LOG_6=6;

    // type 7: 领取音乐先锋棒   网站用户Id #数据格式：类型 # 玩家Pid # 动作发生的日期
    public static int LOG_7=7;

    // type 8: 每日活跃度积分领取  网站用户Id #数据格式：类型 # 玩家Pid # 动作发生的日期 # 领取多少积分
    public static int LOG_8=8;

    // type 9.农场种植  数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期 # 种子的id #田地的id
    public static int LOG_9=9;

    // 10.农场收获  数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期 #  田地的id # 第几熟 # 获得的作物id
    public static int LOG_10=10;


    // 10.协助好有收获  数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期 #  田地的id # 第几熟 # 获得的作物id # 好友Id
    public static int LOG_11=11;

    // 12.农场好评;  数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期 # 好友Id
    public static int LOG_12=12;


    // 13.刷新农场订单;  数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期 # 花费的游戏币
    public static int LOG_13=13;


    // 14.完成农场订单;  数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期 # 订单的ID
    public static int LOG_14=14;


    //  15.购买物品 数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期 # 物品的Id #物品的数量 #物品类型 #物品子类型
    public static int LOG_15=15;

    //  16.卖出物品; 数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期 # 物品的Id #物品的数量 #物品类型 #物品子类型
    public static int LOG_16=16;

    //   17.换装; 数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期
    public static int LOG_17=17;

    // 18、房间装修; 数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期
    public static int LOG_18=18;


    //  19、摇钱树-祝福  ;网站用户Id # 数据格式：类型 # 玩家Pid # 动作发生的日期
    public static int LOG_19=19;

    //  20、赠送明星礼物 ; 网站用户Id #数据格式：类型 # 玩家Pid # 动作发生的日期  #礼物的Id
    public static int LOG_20=20;

    // 21、家族捐献 ; 数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期  # 捐献达人币 # 捐献的游戏币
    public static int LOG_21=21;

    // 22、领取家族奖励 ; 数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期  # 领取的游戏币
    public static int LOG_22=22;

    //23、更换家族,加入家族 ; 数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期  # 原家族ID #更换后的家族ID
    public static int LOG_23=23;

    // 24、玩家注册; 数据格式：网站用户Id #类型 # 玩家Pid # 动作发生的日期
    public static int LOG_24=24;
    //25,用户充值,数据格式:uid#pid#日期#金额
    public static int LOG_25 = 25;
    //26,铲除作物,数据格式:网站用户id#类型#pid#田地的id#日期#种子id
    public static int LOG_26 = 26;
    //27,使用物品,数据格式:网站用户id#类型#pid#日期#itemid#num#type#itemType
    public static int LOG_27 = 27;

    //28,使用达人币,数据格式:网站用户id#类型#pid#日期#num
    public static int LOG_28 = 28;













    private int type;
    private String pid;
    private Date date;

    private String value;


    public GameLogger(int type, String pid, String value){
        this.type=type;
        this.pid=pid;
        this.date=new Date();
        this.value=value;
    }
    public String getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
