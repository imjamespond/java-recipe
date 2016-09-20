package com.pengpeng.stargame.success.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: mql
 * Date: 14-5-4
 * Time: 下午12:11
 */
@Entity
@Table(name = "sg_rule_success")
public class SuccessRule extends BaseEntity<String> {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String des;
    @Column
    private int type;
    @Column
    private int star1;
    @Column
    private String reward1;
    @Column
    private int star2;
    @Column
    private String reward2;
    @Column
    private int star3;
    @Column
    private String reward3;
    @Column
    private String title;
    @Column
    private String itemId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStar1() {
        return star1;
    }

    public void setStar1(int star1) {
        this.star1 = star1;
    }

    public String getReward1() {
        return reward1;
    }

    public void setReward1(String reward1) {
        this.reward1 = reward1;
    }

    public int getStar2() {
        return star2;
    }

    public void setStar2(int star2) {
        this.star2 = star2;
    }

    public String getReward2() {
        return reward2;
    }

    public void setReward2(String reward2) {
        this.reward2 = reward2;
    }

    public int getStar3() {
        return star3;
    }

    public void setStar3(int star3) {
        this.star3 = star3;
    }

    public String getReward3() {
        return reward3;
    }

    public void setReward3(String reward3) {
        this.reward3 = reward3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public String getKey() {
        return id;
    }
}
