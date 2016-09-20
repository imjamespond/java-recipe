package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * User: mql
 * Date: 13-7-2
 * Time: 下午12:10
 */
@Desc("摇钱树掉落信息的VO")
@EventAnnotation(name="event.moneydrop.update",desc="摇钱树掉落信息的更新")
public class MoneyDropVO {
    @Desc("掉落的钱的信息")
    private MoneyPickInfoVO [] moneyPickInfoVOs;

    public MoneyPickInfoVO[] getMoneyPickInfoVOs() {
        return moneyPickInfoVOs;
    }

    public void setMoneyPickInfoVOs(MoneyPickInfoVO[] moneyPickInfoVOs) {
        this.moneyPickInfoVOs = moneyPickInfoVOs;
    }
}
