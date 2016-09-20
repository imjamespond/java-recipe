package com.pengpeng.stargame.vo.event;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * User: mql
 * Date: 13-12-20
 * Time: 上午10:50
 */
@Desc("活动时候场景内掉落的信息")
@EventAnnotation(name="event.dropgift.update",desc="活动时候场景内掉落的信息")
public class EventDropInfoVO {
    @Desc("礼物数组 dropGiftVo数组")
    private DropGiftVO [] dropGiftVOs;
    @Desc("场景Id")
    private String sceneId;
    public DropGiftVO[] getDropGiftVOs() {
        return dropGiftVOs;
    }

    public void setDropGiftVOs(DropGiftVO[] dropGiftVOs) {
        this.dropGiftVOs = dropGiftVOs;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
}
