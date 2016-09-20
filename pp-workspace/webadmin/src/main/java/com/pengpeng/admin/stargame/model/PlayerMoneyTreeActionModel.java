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
@Table(name="gm_player_money_tree_action")
public class PlayerMoneyTreeActionModel extends Model {

    public PlayerMoneyTreeActionModel() {
    }

    public PlayerMoneyTreeActionModel(String uid, String pid, Date date) {
        super(uid, pid, date);
    }
}
