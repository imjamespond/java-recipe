package com.pengpeng.stargame.vo.task;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.BaseRewardVO;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-12下午2:49
 */
@Desc("任务奖励")
public class TaskRewardVO extends BaseRewardVO{


    public TaskRewardVO() {
    }
    public TaskRewardVO(String id, int type, int num) {
        this.setId(id);
        this.setType(type);
        this.setNum(num);
    }


    public TaskRewardVO(String id, int type, int num,String name) {
        this.setId(id);
        this.setType(type);
        this.setNum(num);
        this.setItemName(name);
    }


}
