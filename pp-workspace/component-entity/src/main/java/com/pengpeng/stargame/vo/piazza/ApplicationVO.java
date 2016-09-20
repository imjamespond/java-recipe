package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.lottery.LotteryGoodsVO;
import com.pengpeng.stargame.vo.lottery.LotteryInfoVO;

import java.util.Date;

/**
 * @author james
 * @since 13-11-29 上午10:46
 */
@Desc("申请助理")
public class ApplicationVO {
	@Desc("家族id")
	private String pid;
    @Desc("昵称")
    private String name;
    @Desc("uid")
    private int uid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
