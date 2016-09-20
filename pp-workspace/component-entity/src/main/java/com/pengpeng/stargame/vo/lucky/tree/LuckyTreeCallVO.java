package com.pengpeng.stargame.vo.lucky.tree;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("招财树招财")
public class LuckyTreeCallVO {
	@Desc("暴击倍数")
	private int critical;
    @Desc("游戏币")
    private int gameCoin;
    @Desc("积分")
    private int credit;

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
