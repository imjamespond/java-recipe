package com.pengpeng.stargame.task;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务系统常量
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-18下午3:00
 */
public class TaskConstants {

    //1表示主线任务、2表示支线任务、3表示家族任务、4表示活动任务
    public static final int TASK_TYPE_1=1;
    public static final int TASK_TYPE_2=2;
    public static final int TASK_TYPE_3=3;
    public static final int TASK_TYPE_4=4;
    public static final int TASK_TYPE_5=5;
    public static final int TASK_TYPE_6=6;


    //任务的 子 类型    用于区分活动类型的任务，非活动类型任务为0,1表示音乐旋律任务，2表示音乐先锋任务
    public static final int TASK_SUB_TYPE_1=1;
    public static final int TASK_SUB_TYPE_2=2;

    //正常完成
    public static final int FINISH_TYPE1=1;
    //达人币完成
    public static final int FINISH_TYPE2=2;



    //任务条件
    public static final int CONDI_TYPE_NPC_1=1;//1表示NPC对话、
    public static final int CONDI_TYPE_ITEM_2=2;//2表示收集道具、
    public static final int CONDI_TYPE_FARM_3=3;//3表示满足农场等级、
    public static final int CONDI_TYPE_ROOM_4=4;//4表示满足房间豪华度、
    public static final int CONDI_TYPE_FASHION_5=5;//5表示满足时尚指数、
    public static final int CONDI_TYPE_BUSINESS_6=6;//6表示满足商业等级、
    public static final int CONDI_TYPE_7=7;//7给明星赠送道具、
    public static final int CONDI_TYPE_8=8;//8农场/房间好评次数、
    public static final int CONDI_TYPE_9=9;//9协助好友农场采摘次数、
    public static final int CONDI_TYPE_10=10;//10完成农场订单数量、
    public static final int CONDI_TYPE_11=11;//11已激活的土地数量、
    public static final int CONDI_TYPE_12=12;//12家族贡献、
    public static final int CONDI_TYPE_13=13;//13收藏游戏操作、
    public static final int CONDI_TYPE_14=14;//14好友数量、
    public static final int CONDI_TYPE_15=15;//15给摇钱树祝福次数、
    public static final int CONDI_TYPE_16=16;//16家族捐献游戏币数量、
    public static final int CONDI_TYPE_17=17;//17表示家族等级
    public static final int CONDI_TYPE_18=18;//18表示玩小游戏  分数
    public static final int CONDI_TYPE_19=19;//19招财树次数
    public static final int CONDI_TYPE_20=20;//20每日积分获得
    public static final int CONDI_TYPE_21=21;//21、每日消费达人币
    public static final int CONDI_TYPE_22=22;// 22、每日获得家族贡献
    public static final int CONDI_TYPE_23=23;//23、每日船运起航次数
    public static final int CONDI_TYPE_24=24;//24、每日协助玩家装箱次数
    //任务条件


    /**
     * 需要实时查询统计的任务类型
     */
    public static List<Integer> C_TASK_TYPE=new ArrayList<Integer>();
    static {
        C_TASK_TYPE.add(CONDI_TYPE_ITEM_2);
        C_TASK_TYPE.add(CONDI_TYPE_FARM_3);
        C_TASK_TYPE.add(CONDI_TYPE_ROOM_4);
        C_TASK_TYPE.add(CONDI_TYPE_FASHION_5);
        C_TASK_TYPE.add(CONDI_TYPE_11);
        C_TASK_TYPE.add(CONDI_TYPE_14);
        C_TASK_TYPE.add(CONDI_TYPE_12);
        C_TASK_TYPE.add(CONDI_TYPE_17);
    }

    /**
     * 具体到某个Id 的任务类型
     */
    public static List<Integer> ID_TASK_TYPE=new ArrayList<Integer>();
    static {
        ID_TASK_TYPE.add(CONDI_TYPE_ITEM_2);
        ID_TASK_TYPE.add(CONDI_TYPE_7);
    }

    /**
     * 需要客户端发消息才能知道是否完成的 消息类型
     */
    public static List<Integer> REQ_TASK_TYPE=new ArrayList<Integer>();
    static {
        REQ_TASK_TYPE.add(CONDI_TYPE_13);
    }
    /**
     * 涉及到 实时统计的 的消息类型  ,每次这些消息获得物品 ，需要 广播 任务数据
     */
    public static List<String> COUNT_TYPE=new ArrayList<String>();
    static {

        COUNT_TYPE.add("farmpkg.buy");
        COUNT_TYPE.add("farmpkg.remove");
        COUNT_TYPE.add("farm.plant");
        COUNT_TYPE.add("farm.harvest");

        COUNT_TYPE.add("room.save");
        COUNT_TYPE.add("room.sale");

        COUNT_TYPE.add("fashion.change");
        COUNT_TYPE.add("fashion.takeOff");
        COUNT_TYPE.add("fashion.takeOffAll");
        COUNT_TYPE.add("fashion.randomFromPkg");
        COUNT_TYPE.add("fashion.change");
        COUNT_TYPE.add("fashionpkg.buy");
        COUNT_TYPE.add("fashionpkg.remove");


        COUNT_TYPE.add("family.endow") ;
        COUNT_TYPE.add("moneytree.blessing");
        COUNT_TYPE.add("farm.finish.order");

        COUNT_TYPE.add("task.getreward"); //完成任务会加农场 经验
        COUNT_TYPE.add("farm.friend.harvestAll");
        COUNT_TYPE.add("farm.harvestAll");
        COUNT_TYPE.add("farm.process.getProcessItem");  //掉落物品
        COUNT_TYPE.add("lotter.draw");  //抽奖
        COUNT_TYPE.add("family.up.level");  //升级建筑
        COUNT_TYPE.add("update.rank"); //小游戏积分 有更改
        COUNT_TYPE.add("giftbag.open");//领取礼包

    }
    /**
     * 不需要广播 任务数据的 消息类型，这些消息还没进入游戏
     */
    public static List<String> NO_AUTO=new ArrayList<String>();
    static {
        NO_AUTO.add("p.get.id");
        NO_AUTO.add("p.create");
    }

    /**
     * 完美领取任务奖励 需要的 达人币
     */
    public static int GET_GOLD=5;
    /**
     *  完美领取 是普通领取的多少倍
     */
    public static int ADD_P=2;
    /**
     *  完美领取 多少
     */
    public static int ADD_P_B=100;
    /**
     * 最后一个新手任务Id
     */
   public static String LAST_NEW_TASK="task_806";



}
