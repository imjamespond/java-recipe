package com.pengpeng.stargame.vo.piazza.bank;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-12-5
 * Time: 上午10:39
 */
@Desc("家族银行请求")
public class FamilyBankReq extends BaseReq {
    @Desc("家族Id")
    private String familyId;
    @Desc("游戏币 存入 取出")
    private int gameMoney;

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public int getGameMoney() {
        return gameMoney;
    }

    public void setGameMoney(int gameMoney) {
        this.gameMoney = gameMoney;
    }
}
