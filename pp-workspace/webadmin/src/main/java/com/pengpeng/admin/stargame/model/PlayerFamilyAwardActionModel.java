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
@Table(name="gm_player_family_award_action")
public class PlayerFamilyAwardActionModel extends Model {

    @Column
    private int gameCoin;

    public PlayerFamilyAwardActionModel() {
    }

    public PlayerFamilyAwardActionModel(String uid, String pid, Date date) {
        super(uid, pid, date);
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }
}
