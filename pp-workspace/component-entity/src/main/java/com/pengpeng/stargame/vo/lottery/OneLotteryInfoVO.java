package com.pengpeng.stargame.vo.lottery;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.BasePkgVO;
import com.pengpeng.stargame.vo.farm.GoodsVO;

import java.util.Date;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("抽奖信息")
public class OneLotteryInfoVO {
	@Desc("获奖玩家名称")
	private String name;
    @Desc("获奖道具名称")
    private String item;
    @Desc("获奖道具数量")
    private int num;
    @Desc("获奖游戏币")
    private int gameCoin;
    @Desc("获奖日期")
    private long date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }
}
