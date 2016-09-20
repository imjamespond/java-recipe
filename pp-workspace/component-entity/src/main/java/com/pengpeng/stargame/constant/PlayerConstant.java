package com.pengpeng.stargame.constant;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-20上午10:19
 */
public class PlayerConstant {
    //网站性别数据0女,1男
    //男
    public static final int SEX_TYPE_MAN_1 = 1;
    //女
    public static final int SEX_TYPE_WOMAN_1 = 0;


    //  1）每日登陆游戏，玩家当天登陆粉丝达人游戏即可完成
    //2）在线累计时间超过120分钟，玩家当天在线累计时间超过120秒即可完成任务
    //3）完成家族任务数量，玩家当天完成的家族任务数量达，总共有5个家族任务，每个家族任务单独奖励4点活跃度
    //4）完成音乐旋律活动任务，玩家当天完成的音乐旋律获得任务数量，总共有20个该任务，每个音乐旋律活动任务奖励3点活跃度
    //5）完成音乐先锋活动任务，玩家当天完成的音乐先锋获得任务数量，总共有20个该任务，每个音乐先锋活动任务奖励10点活跃度
    //6）每日抽奖次数，玩家当天通过幸运抽奖的次数，总共有5次，每抽奖一次可获得4点活跃度奖励
    //7玩小游戏
    //8给明星送礼
    //14投票
    public static final int ACTIVE_TYPE_1 =1;
    public static final int ACTIVE_TYPE_2 =2;
    public static final int ACTIVE_TYPE_3 =3;
    public static final int ACTIVE_TYPE_4 =4;
    public static final int ACTIVE_TYPE_5 =5;
    public static final int ACTIVE_TYPE_6 =6;
    public static final int ACTIVE_TYPE_7 =7;
    public static final int ACTIVE_TYPE_8 =8;
    public static final int ACTIVE_TYPE_14 =14;

    /**
     *     达人币 操作统计
     */
    public  static int GOLD_ACTION_1=1;//农场作物直接催熟作物
    public  static int GOLD_ACTION_2=2;//农场开宝箱
    public  static int GOLD_ACTION_3=3;//农场装饰购买达人币物品
    public  static int GOLD_ACTION_4=4;//订单快速货运
    public  static int GOLD_ACTION_5=5;//加工队列立即完成单个
    public  static int GOLD_ACTION_6=6;//加工队列加速
    public  static int GOLD_ACTION_7=7;// 加工队列开通格子
    public  static int GOLD_ACTION_8=8;//加工队列完成所有
    public  static int GOLD_ACTION_9=9;//时装购买达人币时装
    public  static int GOLD_ACTION_10=10;//家族达人币捐献
    public  static int GOLD_ACTION_11=11;//赞音乐次数
    public  static int GOLD_ACTION_12=12;//大喇叭
    public  static int GOLD_ACTION_13=13;//房间扩建
    public  static int GOLD_ACTION_14=14;//房间 购买达人币装饰物品
    public  static int GOLD_ACTION_15=15;//任务 完美领取
    public  static int GOLD_ACTION_16=16;//任务 立即完成
    public  static int GOLD_ACTION_17=17;//送明星精美礼物
    public  static int GOLD_ACTION_18=18;//购买仓库升级材料
    public static int GOLD_ACTION_19=19;//成就系统 添加达人币




    public static final int GOLD_ACTION_20 =20;//招财树增加次数
    public static final int GOLD_ACTION_21 =21;//招财树 招财
    public static final int GOLD_ACTION_25 =25;//抽奖
    public static final int GOLD_ACTION_26 =25;//轮盘
    public static final int GOLD_ACTION_30 =30;//连续登陆 奖励
    public static final int GOLD_ACTION_35 =35;//码头
    public static final int GOLD_ACTION_40 =40;//摆摊,开启摊位
    public static final int GOLD_ACTION_41 =41;//摆摊 广告
    public static final int GOLD_ACTION_42 =42;//摆摊 开启
    public static final int GOLD_ACTION_43 =43;//摆摊 助手
    public static final int GOLD_ACTION_44 =44;//摆摊 下架
    public static final int GOLD_ACTION_45 =45;//小游戏


    public static int GOLD_ACTION_50=50;//农场开宝箱 奖励



    public static final long OFFLINE_7DAY = 7 * 24 * 3600 * 1000l ;



}
