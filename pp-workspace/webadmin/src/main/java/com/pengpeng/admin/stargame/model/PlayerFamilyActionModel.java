package com.pengpeng.admin.stargame.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-10-16
 * Time: 上午11:17
 */
@Entity
@Table(name="gm_player_family_action")
public class PlayerFamilyActionModel extends Model {
    @Column
    private String preFamily;
    @Column
    private String curFamily;

    public PlayerFamilyActionModel(){

    }
    public PlayerFamilyActionModel(String uid, String pid, Date date) {
        super(uid, pid, date);
    }

    public String getPreFamily() {
        return preFamily;
    }

    public void setPreFamily(String preFamily) {
        this.preFamily = preFamily;
    }

    public String getCurFamily() {
        return curFamily;
    }

    public void setCurFamily(String curFamily) {
        this.curFamily = curFamily;
    }
}
