package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-12-12下午3:06
 */
@Desc("赞返回对象")
public class MusicZanVO {
    @Desc("赞次数")
    private int num;
    @Desc("赞一次多少达人币")
    private int coin;
    @Desc("赞免费次数")
    private int free;

    public MusicZanVO(){

    }

    public MusicZanVO(int num, int coin, int free) {
        this.num = num;
        this.coin = coin;
        this.free = free;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }
}
