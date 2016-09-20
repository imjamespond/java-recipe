package com.pengpeng.stargame.model.success;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 14-5-4
 * Time: 上午11:03
 */
public class OneSuccess {
    private String id;
    private int starNum; // 目前成就达到的星星数量
    private int myNum;
    private List<Integer> rewardStar;

    public OneSuccess(){
    }

    public OneSuccess(String id){
        this.id=id;
        rewardStar=new ArrayList<Integer>();
    }
    public void addReward(int star){
        rewardStar.add(star);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStarNum() {
        return starNum;
    }

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public int getMyNum() {
        return myNum;
    }

    public void setMyNum(int myNum) {
        this.myNum = myNum;
    }

    public List<Integer> getRewardStar() {
        return rewardStar;
    }

    public void setRewardStar(List<Integer> rewardStar) {
        this.rewardStar = rewardStar;
    }
}
