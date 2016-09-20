package com.pengpeng.stargame.model.gameEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-12-23
 * Time: 上午10:35
 */

/**
 * 家族银行活动 相关
 */
public class FamilyBankEvent {
    //家族银行活动可以领取 哪些奖励
    private List<String> canReward;
    //家族银行活动  已经领取了 多少奖励
    private List<String> reewarded;

    public FamilyBankEvent(){
        canReward=new ArrayList<String>();
        reewarded=new ArrayList<String>();
    }

    public List<String> getReewarded() {
        return reewarded;
    }

    public void setReewarded(List<String> reewarded) {
        this.reewarded = reewarded;
    }

    public List<String> getCanReward() {
        return canReward;
    }

    public void setCanReward(List<String> canReward) {
        this.canReward = canReward;
    }
}
