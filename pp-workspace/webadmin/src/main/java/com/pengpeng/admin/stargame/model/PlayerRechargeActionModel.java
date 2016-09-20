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
@Table(name="gm_player_recharge_action")
public class PlayerRechargeActionModel extends Model{

    @Column
    private int money;

    public PlayerRechargeActionModel() {
    }

    public PlayerRechargeActionModel(String uid, String pid, Date date) {
        super(uid, pid, date);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
