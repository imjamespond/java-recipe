package com.pengpeng.stargame.vo.success;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.BaseRewardVO;

/**
 * User: mql
 * Date: 14-5-4
 * Time: 下午2:54
 */
@Desc("单个成就VO")
public class SuccessVO {
    @Desc("成就Id")
    private String id;
    @Desc("成就名字")
    private String name;
    @Desc("成就描述")
    private String des;
    @Desc("完成几颗星 0表示没有 3表示全部完成")
    private int star;
    @Desc("这个成就的目标最大值")
    private int maxNum;
    @Desc("自己的数值")
    private int myNum;
    @Desc("是否是当前称号 0不是 1是")
    private int currentUse;
    @Desc("当前星星的奖励")
    private BaseRewardVO [] rewardVO;
    @Desc("当前的奖励是否已经领取了 0没有 1领取了")
    private int currentGet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getMyNum() {
        return myNum;
    }

    public void setMyNum(int myNum) {
        this.myNum = myNum;
    }



    public BaseRewardVO[] getRewardVO() {
        return rewardVO;
    }

    public void setRewardVO(BaseRewardVO[] rewardVO) {
        this.rewardVO = rewardVO;
    }

    public int getCurrentUse() {
        return currentUse;
    }

    public void setCurrentUse(int currentUse) {
        this.currentUse = currentUse;
    }

    public int getCurrentGet() {
        return currentGet;
    }

    public void setCurrentGet(int currentGet) {
        this.currentGet = currentGet;
    }
}
