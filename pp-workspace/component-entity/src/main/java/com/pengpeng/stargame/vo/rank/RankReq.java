package com.pengpeng.stargame.vo.rank;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 14-4-9
 * Time: 下午4:42
 */
@Desc("排行请求")
public class RankReq  extends BaseReq {
    @Desc("排行类型 10游戏币排行 20家园等级 30房间豪华度 40时尚指数  50家族等级 60家族人数  70周粉丝值" +
            " 1明星连连看 2疯狂狗仔队")
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
