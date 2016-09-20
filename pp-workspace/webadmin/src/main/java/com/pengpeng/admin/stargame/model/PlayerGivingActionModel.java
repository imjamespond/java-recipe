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
@Table(name="gm_player_giving_action")
public class PlayerGivingActionModel extends Model{

    @Column
    private String itemId;

    public PlayerGivingActionModel() {
    }

    public PlayerGivingActionModel(String uid, String pid, Date date) {
        super(uid, pid, date);
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
