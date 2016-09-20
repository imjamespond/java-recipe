package com.pengpeng.stargame.model.gameEvent;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.List;
import java.util.Map;

/**
 * User: mql
 * Date: 14-2-24
 * Time: 下午5:38
 * 此类是为了 记录玩家 视频直播活动中 ，天线道具的 详细获取记录
 */
public class PlayerVideoInfo extends BaseEntity<String> {
    private String pid;
    //key 日期 String 具体数据  日期#说明#数量#剩余数量
    private Map<String,List> videoInfo;
    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return pid;
    }
}
