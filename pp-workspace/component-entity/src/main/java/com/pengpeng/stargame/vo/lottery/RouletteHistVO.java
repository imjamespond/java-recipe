package com.pengpeng.stargame.vo.lottery;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.BaseRewardVO;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("轮盘历史信息")
public class RouletteHistVO {
	@Desc("获奖玩家名称")
	private String name;
    private BaseRewardVO item;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseRewardVO getItem() {
        return item;
    }

    public void setItem(BaseRewardVO item) {
        this.item = item;
    }
}
