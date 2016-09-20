package com.pengpeng.stargame.vo.smallgame;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("小游戏列表")
public class SmallGameListVO {

    @Desc("小游戏列表")
    private SmallGameRankVO[] smallGameRankVOs;
    @Desc("不可购买类型 gold*100+type")
    private int[] buyList;


    public SmallGameRankVO[] getSmallGameRankVOs() {
        return smallGameRankVOs;
    }

    public void setSmallGameRankVOs(SmallGameRankVO[] smallGameRankVOs) {
        this.smallGameRankVOs = smallGameRankVOs;
    }

    public int[] getBuyList() {
        return buyList;
    }

    public void setBuyList(int[] buyList) {
        this.buyList = buyList;
    }
}
