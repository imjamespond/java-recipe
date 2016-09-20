package com.pengpeng.stargame.constant;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-20上午10:19
 */
public class BaseRewardConstant {
    //"奖励类型:1:游戏币,9:达人币,2:农场经验,3:商业经验,4:积分奖励,5:道具奖励 6：家族经费 7：家族贡献 8:免费赞音乐的次数"
    public static final int TYPE_GAMECOIN =1;

    public static final int TYPE_FARMEXP =2;
    public static final int TYPE_BUSINESS =3;
    public static final int TYPE_CREDIT =4;
    public static final int TYPE_ITEM =5;
    public static final int TYPE_FAMILY_FEE =6;
    public static final int TYPE_FAMILY_CONTRIBUTION =7;
    public static final int TYPE_PRAISE =8;
    public static final int TYPE_GOLDCOIN =9;

    public static final int NOTIFY_GAMECOIN =1;

    public static final int NOTIFY_FARMEXP =2;
    public static final int NOTIFY_BUSINESS =4;
    public static final int NOTIFY_CREDIT =8;
    public static final int NOTIFY_ITEM =16;
    public static final int NOTIFY_FAMILY_FEE =32;
    public static final int NOTIFY_FAMILY_CONTRIBUTION =64;
    public static final int NOTIFY_PRAISE =128;
    public static final int NOTIFY_GOLDCOIN =256;

    public static String toString(int type){
        switch (type){
            case TYPE_GAMECOIN:
                return "游戏币";
            case TYPE_FARMEXP:
                return "农场经验";
            case TYPE_BUSINESS:
                return "商业经验";
            case TYPE_CREDIT:
                return "积分奖励";
            case TYPE_ITEM:
                return "道具奖励";
            case TYPE_FAMILY_FEE:
                return "家族经费";
            case TYPE_FAMILY_CONTRIBUTION:
                return "家族贡献";
            case TYPE_PRAISE:
                return "免费赞音乐的次数";
        }
        return null;
    }
}
