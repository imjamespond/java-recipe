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
@Table(name="gm_player_order_reset_action")
public class PlayerOrderResetActionModel extends Model{// 订单重置

    @Column
    private int cost;//花费

    public PlayerOrderResetActionModel() {
    }

    public PlayerOrderResetActionModel(String uid, String pid, Date date) {
        super(uid, pid, date);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
