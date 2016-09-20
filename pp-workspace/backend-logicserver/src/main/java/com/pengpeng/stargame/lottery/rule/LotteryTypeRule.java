package com.pengpeng.stargame.lottery.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:04
 */

@Entity
@Table(name="sg_rule_lottery_type")
public class LotteryTypeRule extends BaseEntity<String> {

    @Id
    private String id;
    @Column
    private String name;
    @Column
    private int cost;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
