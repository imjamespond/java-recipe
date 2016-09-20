package com.pengpeng.stargame.stall.rule;

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
@Table(name="sg_rule_fri_shelf")
public class StallFriShelfRule extends BaseEntity<String> {

    @Id
    private String shelfNum;   //好友数目
    @Column
    private int friNum;   //货架数目





    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return shelfNum;
    }

    public String getShelfNum() {
        return shelfNum;
    }

    public void setShelfNum(String shelfNum) {
        this.shelfNum = shelfNum;
    }

    public int getFriNum() {
        return friNum;
    }

    public void setFriNum(int friNum) {
        this.friNum = friNum;
    }
}
