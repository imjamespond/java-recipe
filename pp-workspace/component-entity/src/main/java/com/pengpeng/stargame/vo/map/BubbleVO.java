package com.pengpeng.stargame.vo.map;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;
import com.pengpeng.stargame.vo.BaseRewardVO;

/**
 * @auther james
 * @since: 14-5-14上午11:18
 */
@Desc("泡泡信息")
@EventAnnotation(name="event.bubble",desc="气泡推送")
public class BubbleVO {
    @Desc("人物id")
    private String id;
    @Desc("附件")
    private BaseRewardVO attach;
    @Desc("剩余次数")
    private int restTime;

    public BubbleVO() {
    }

    public BubbleVO(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BaseRewardVO getAttach() {
        return attach;
    }

    public void setAttach(BaseRewardVO attach) {
        this.attach = attach;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }
}
