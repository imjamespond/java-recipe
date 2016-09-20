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
@Table(name="sg_rule_gold_shelf")
public class StallGoldShelfRule extends BaseEntity<String> {

    @Id
    private String shelfNum; //当前货架数 从0开始哦
    @Column
    private int needGold;   //新开货架达人币




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

    public int getNeedGold() {
        return needGold;
    }

    public void setNeedGold(int needGold) {
        this.needGold = needGold;
    }
}
