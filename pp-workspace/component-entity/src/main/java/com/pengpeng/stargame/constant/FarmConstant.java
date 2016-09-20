package com.pengpeng.stargame.constant;

/**
 * User: mql
 * Date: 13-4-27
 * Time: 下午2:13
 */
public class FarmConstant {
    /**
     * 田地 的 总数量
     */
    public final static int FIELD_ALL_NUM=24;
    public final static int FIELD_SEND_NUM=10;//送的土地数量   直接放入到农田中，不用手托
    public final static int FIELD_MAX_BUY=20;//土地 一共可以购买的数量
    /**
     * 田地状态 相关
     */
    public final static int FIELD_STATUS_FREE=0;//空闲
    public final static int FIELD_STATUS_PLANT=1;//种植中
    public final static int FIELD_STATUS_RIP=2;//成熟
    public final static int FIELD_CLOSE=3;//锁住的

    public final static int ORDER_ALL_NUM=15;//订单的数量

    public final static int HELP_ALL_NUM=200;//帮助好友收获的最大数量

    public final static int D_GOLD=5;// 施肥 需要的 达人币

    public final static String H_F="items_13004";  //化肥的Id

    public final static int GRID_NUM=5;//玩家初始化的 生产队列格子数量

    public final static int MAX_SEND_NUM=20;//送礼物最大次数

    public final static String TIANXIN_ID="items_13014";//天线Id


    public final static String FIELD_ID="items_15001"; //土地的Id
    public final static int FIELD_GOLD_B=2;//购买土地的平方基数

    public final static String Y_S="items_16002";//鼹鼠的Id
    public final static String X_J="items_16001";//母鸡的Id
    public final static String M_F="items_16003";//蜜蜂的Id
    public final static String H_D="items_16004";//蝴蝶的Id
    public final static String X_N="items_16005";//小鸟的Id
    public final static String P_C="items_16006";//瓢虫的Id

    public final static int OPEN_BOX=3;//打开宝箱需要的达人币
}
