package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-11-11
 * Time: 下午6:10
 */
@Desc("留言VO")
public class FarmMessageInfoVO {
    @Desc("今日留言数量")
    private int num;
    @Desc("留言列表")
    FarmMessageVO [] farmMessageVOs;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public FarmMessageVO[] getFarmMessageVOs() {
        return farmMessageVOs;
    }

    public void setFarmMessageVOs(FarmMessageVO[] farmMessageVOs) {
        this.farmMessageVOs = farmMessageVOs;
    }
}
