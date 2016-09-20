package com.pengpeng.stargame.rule.farm;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.Indexable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: mql
 * Date: 13-4-24
 * Time: 下午3:25
 */
@Entity
@Table(name="sg_rule_farm_levels")
public class FarmLevelRule extends BaseEntity<Integer> {
    @Id
    private int level;
    @Column
    private int needExp;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNeedExp() {
        return needExp;
    }

    public void setNeedExp(int needExp) {
        this.needExp = needExp;
    }

    @Override
    public Integer getId() {
        return level;
    }

    @Override
    public void setId(Integer id) {
        this.level=id;
    }

    @Override
    public Integer getKey() {
        return level;
    }
}
