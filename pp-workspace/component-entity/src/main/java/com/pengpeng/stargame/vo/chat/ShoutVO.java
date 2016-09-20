package com.pengpeng.stargame.vo.chat;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-12-2
 * Time: 下午5:09
 */
@Desc("千里传音VO")
public class ShoutVO {
    @Desc("免费剩余次数")
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
