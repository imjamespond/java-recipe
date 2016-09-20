package com.pengpeng.stargame.rule.farm;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.Indexable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-3-7下午6:26
 */
@Entity
@Table(name="sg_rule_farm_order")
public class FarmOrderRule extends BaseEntity<String> {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private int farmLevel;
    @Column
    private int currencyReward;
    @Column
    private int expReward;
    @Column
    private String orderRequest ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFarmLevel() {
        return farmLevel;
    }

    public void setFarmLevel(int farmLevel) {
        this.farmLevel = farmLevel;
    }

    public int getCurrencyReward() {
        return currencyReward;
    }

    public void setCurrencyReward(int currencyReward) {
        this.currencyReward = currencyReward;
    }

    public int getExpReward() {
        return expReward;
    }

    public void setExpReward(int expReward) {
        this.expReward = expReward;
    }

    public String getOrderRequest() {
        return orderRequest;
    }

    public void setOrderRequest(String orderRequest) {
        this.orderRequest = orderRequest;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }

    @Override
    public String getKey() {
        return id;
    }
}
