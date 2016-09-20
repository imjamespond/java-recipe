package com.pengpeng.stargame.piazza.container;

import com.pengpeng.stargame.model.piazza.Family;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-1上午10:29
 */
public class FamilyConstant {

    /**
     * 明星
     */
    public static final int TYPE_MX = 1;
    /**
     * 明星助理
     */
    public static final int TYPE_ZL = 2;
    /**
     * 超级粉丝
     */
    public static final int TYPE_CJFS = 3;
    /**
     * 粉丝
     */
    public static final int TYPE_FS = 4;

    //明星城堡
    public static final int BUILD_CASTLE=1;
    //摇钱树
    public static final int BUILD_TREE=2;
    //家族银行
    public static final int BUILD_BANK=3;
    //家族仓库
    public static final int BUILD_WAREHOUSE=4;
    //家族商店
    public static final int BUILD_STORE=5;
    //明星助理可以摇钱的次数
    public static final int ZL_ROCK_NUM=5;
    //家族收集活动 获得的积分奖励
    public static Map<Integer,Integer> COLLECT_REWARD=new HashMap<Integer,Integer>();
    static {
        COLLECT_REWARD.put(1,4000);
        COLLECT_REWARD.put(2,2500);
        COLLECT_REWARD.put(3,1000);
    }
    //收集活动 间隔的天数
    public static final int COLLECT_DAY=2;

    public static String getChannelId(Family family){
        return family.getId();
    }

    public static String getChannelId(String familyId){
        return familyId;
    }
}
