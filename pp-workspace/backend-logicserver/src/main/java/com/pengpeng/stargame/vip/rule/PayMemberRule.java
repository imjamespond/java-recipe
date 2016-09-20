package com.pengpeng.stargame.vip.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: mql
 * Date: 13-11-19
 * Time: 上午11:53
 * 超级粉丝规则
 */
@Entity
@Table(name="sg_rule_paymember")
public class PayMemberRule extends BaseEntity<String> {
    @Id
    private String id;
    @Column
    private String des;
    @Column
    private float data;
    @Column
    private int level;
    @Column
    private int type;


    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public float getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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
