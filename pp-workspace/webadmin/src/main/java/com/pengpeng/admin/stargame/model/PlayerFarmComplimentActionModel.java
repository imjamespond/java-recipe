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
@Table(name="gm_player_farm_compliment_action")
public class PlayerFarmComplimentActionModel extends Model {

    @Column
    private String friendId;

    public PlayerFarmComplimentActionModel() {
    }

    public PlayerFarmComplimentActionModel(String uid, String pid, Date date) {
        super(uid, pid, date);
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}
