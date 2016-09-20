package com.pengpeng.stargame.vo.gm;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-10-10
 * Time: 上午11:56
 */
@Desc("主页的在线信息VO ")
public class OnlineInfoVO {
    @Desc("当前在线玩家")
    private int currentNum;


    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }
}
