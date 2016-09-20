package com.pengpeng.stargame.room.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Entity;

/**
 * 个人房间规则
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-22下午4:53
 */
@Entity(name="sg_rule_room")
public class RoomRule extends BaseEntity<String> {
    private String id;
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
    }

    @Override
    public String getKey() {
        return id;
    }
}
