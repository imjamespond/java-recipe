package com.pengpeng.admin.stargame.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * User: mql
 * Date: 13-10-17
 * Time: 下午3:08
 */
@Entity
@Table(name="gm_player_register")
public class PlayerRegisterModel extends Model {

    @Column
    private int type;

    public PlayerRegisterModel() {
    }

    public PlayerRegisterModel(String uid, String pid, Date date) {
        super(uid, pid, date);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
