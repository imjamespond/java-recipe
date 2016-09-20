package com.pengpeng.stargame.model.stall;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 14-1-14
 * Time: 上午9:57
 */
public class StallConstant {

    //开启等级
    public static final int ENABLE_LEVEL = 35;
    //开启游戏币
    public static final int ENABLE_GAME_COIN = 500000;
    //开启达人币
    public static final int ENABLE_GOLD_COIN = 100;

    //购买次数
    public static int BUYING_NUM = 10;
    public static int BUYING_VIP_NUM = 30;

    //上架次数
    public static int HIT_SHELF_NUM = 10;
    public static int HIT_SHELF_VIP_NUM = 30;

    //下架时长限制
    public static long OFF_SHELF_TIME = 48l*3600l*1000l;
    public static int OFF_SHELF_GOLD = 1;

    //货架数量
    public static int SHELF_NUM = 5;
    public static int SHELF_VIP_NUM = 2;

    //货架类型
    public static int SHELF_TYPE_ORDINARY = 0;
    public static int SHELF_TYPE_GOLD = 1;
    public static int SHELF_TYPE_FRIEND = 2;
    public static int SHELF_TYPE_VIP = 3;
    public static int SHELF_TYPE_MOM = 4;


    //货架状态
    public static int SHELF_FREE = 0; //空闲
    public static int SHELF_ON_SALE = 1;//出售中
    public static int SHELF_SOLD = 2;//已被购买
    public static int SHELF_UNOPENED_REQUIREMENT = 3;//货架未开放，显示所需要的前置需求
    public static int SHELF_UNOPENED_GOLD = 4;//货架未开放，显示所需要的达人币


    public static final int PASSENGER_NUM = 2;//路人个数
    public static final long PASSENGER_REFRESH = 30l*60l*1000l;//路人刷新时间
    public static final int PASSENGER_WALK = 1;//路人行走
    public static final int PASSENGER_CREDIT= 1;//路人每日积分限制

    //public static final int ASSISTANT_LEVEL_LIMIT= 20;//助手等级限制
    public static final long ASSISTANT_TRIAL= 4*3600*1000l;//助手试用
    public static final long ASSISTANT_COOLDOWN= 3600*1000l;//助手冷却
}
