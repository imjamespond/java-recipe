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
@Table(name="gm_player_activity_action")
public class PlayerActivityActionModel extends Model{
     @Column
    private int point;

    public PlayerActivityActionModel() {
    }

    public PlayerActivityActionModel(String uid, String pid, Date date) {
        super(uid, pid, date);
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
