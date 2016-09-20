package com.pengpeng.stargame.player.rule;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.Indexable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-1-16上午11:55
 */
@Entity
@Table(name="sg_rule_transfer")
public class TransferRule extends BaseEntity<String> {
    @Id
    private String id;
    @Column
    private String currId;
    @Column
    private int currX;
    @Column
    private int currY;
    @Column
    private String targetId;
    @Column
    private int targetX;
    @Column
    private int targetY;


    @Override
    public String getKey() {
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrId() {
        return currId;
    }

    public void setCurrId(String currId) {
        this.currId = currId;
    }

    public int getCurrX() {
        return currX;
    }

    public void setCurrX(int currX) {
        this.currX = currX;
    }

    public int getCurrY() {
        return currY;
    }

    public void setCurrY(int currY) {
        this.currY = currY;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String target) {
        this.targetId = target;
    }

    public int getTargetX() {
        return targetX;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }
}
