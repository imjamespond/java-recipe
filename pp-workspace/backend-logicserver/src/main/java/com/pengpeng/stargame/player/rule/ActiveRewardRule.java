package com.pengpeng.stargame.player.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-13上午11:52
 */
@Entity
@Table(name="sg_rule_activereward")
public class ActiveRewardRule extends BaseEntity<Integer> {
    @Id
    @Column
    private int active;
    @Column
    private int score;
    @Column
    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public Integer getId() {
        return active;
    }

    @Override
    public void setId(Integer id) {
        this.active = id;
    }

    @Override
    public Integer getKey() {
        return active;
    }
}
