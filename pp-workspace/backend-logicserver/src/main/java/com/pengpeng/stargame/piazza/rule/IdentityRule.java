package com.pengpeng.stargame.piazza.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-12下午5:34
 */
@Entity
@Table(name="sg_rule_identity")
public class IdentityRule extends BaseEntity<Integer> {
    @Id
    private int type;
    @Column
    private float boonRate;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getBoonRate() {
        return boonRate;
    }

    public void setBoonRate(float boonRate) {
        this.boonRate = boonRate;
    }

    @Override
    public Integer getId() {
        return type;
    }

    @Override
    public void setId(Integer id) {
        this.type = id;
    }

    @Override
    public Integer getKey() {
        return type;
    }
}
