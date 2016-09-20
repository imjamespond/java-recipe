package com.pengpeng.stargame.vo.farm.decorate;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.BaseShopItemVO;

/**
 * User: mql
 * Date: 14-3-14
 * Time: 下午3:07
 */
@Desc("单个农场装扮信息VO")
public class FarmDecorateShopItemVO extends BaseShopItemVO {
    @Desc("图片")
    private String image;
    @Desc("是否可以旋转 O不可以 1可以")
    private int ratation;
    @Desc("是否可以清除 O不可以 1可以")
    private int remove;
    @Desc("是否可以回收 O不可以 1可以")
    private int  recycle;
    @Desc("是否可以移动 O不可以 1可以")
    private int move;
    @Desc("在农场可以出现的最大个数 0表示不受限制")
    private int maxNum;
    @Desc("清除需要的游戏币，如果大于0表示此物件 是清除")
    private int gameCost;
    @Desc("扩充需要的达人币，如果大于0表示此物件 是扩充")
    private int goldCost;
    public int getRatation() {
        return ratation;
    }

    public void setRatation(int ratation) {
        this.ratation = ratation;
    }

    public int getRemove() {
        return remove;
    }

    public void setRemove(int remove) {
        this.remove = remove;
    }

    public int getRecycle() {
        return recycle;
    }

    public void setRecycle(int recycle) {
        this.recycle = recycle;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getGameCost() {
        return gameCost;
    }

    public void setGameCost(int gameCost) {
        this.gameCost = gameCost;
    }

    public int getGoldCost() {
        return goldCost;
    }

    public void setGoldCost(int goldCost) {
        this.goldCost = goldCost;
    }
}


