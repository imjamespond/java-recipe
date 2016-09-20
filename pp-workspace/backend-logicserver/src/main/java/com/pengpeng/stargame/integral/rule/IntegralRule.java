package com.pengpeng.stargame.integral.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: mql
 * Date: 14-4-22
 * Time: 下午4:57
 */
@Entity
@Table(name="sg_rule_integral")
public class IntegralRule  extends BaseEntity<String> {
    @Id
    private String id;
    @Column
    private String type;
    @Column
    private String name;
    @Column
    private String des;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getKey() {
        return id;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
