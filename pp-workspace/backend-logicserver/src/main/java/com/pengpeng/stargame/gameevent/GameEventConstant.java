package com.pengpeng.stargame.gameevent;

import java.util.HashMap;
import java.util.Map;

/**
 * User: mql
 * Date: 13-12-6
 * Time: 下午4:18
 */
public class GameEventConstant {

    /**
     *圣诞活动的 任务Id
     */
    public static String   EVENT_1_TASKID="task_9000";
    /**
     * 元旦活动的 任务ID
     */
    public static String   EVENT_2_TASKID="task_9001";
    /**
     * 元旦活动的 任务ID
     */
    public static String   EVENT_3_TASKID="task_9041";
    /**
     * 春节活动 鞭炮最大值
     */
    public static int SPRING_EVENT_VALUE=100;
    /**
     * 春节活动 鞭炮到达最大值后多久 点燃
     */
    public static int SPRING_EVENT_STIME=2;

    public static Map<Integer,Integer> BALLOON=new HashMap<Integer, Integer>();

    static {
        BALLOON.put(1,30000);
        BALLOON.put(2,50000);
        BALLOON.put(4,80000);
    }
}
