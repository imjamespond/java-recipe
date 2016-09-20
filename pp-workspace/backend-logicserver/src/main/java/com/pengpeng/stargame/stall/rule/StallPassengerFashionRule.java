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
@Table(name="sg_rule_stall_fashion")
public class StallPassengerFashionRule extends BaseEntity<String> {

    @Id
    private String id;
    @Column
    private int gender;   //性别
    @Column
    private String hair;   //发型
    @Column
    private String face;   //脸
    @Column
    private String cloth;   //衣服
    @Column
    private String pants;   //裤子

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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getCloth() {
        return cloth;
    }

    public void setCloth(String cloth) {
        this.cloth = cloth;
    }

    public String getPants() {
        return pants;
    }

    public void setPants(String pants) {
        this.pants = pants;
    }
}
