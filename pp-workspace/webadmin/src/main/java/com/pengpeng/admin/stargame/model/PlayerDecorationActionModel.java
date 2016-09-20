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
@Table(name="gm_player_decoration_action")
public class PlayerDecorationActionModel extends Model{

    public PlayerDecorationActionModel() {
    }

    public PlayerDecorationActionModel(String uid, String pid, Date date) {
        super(uid, pid, date);
    }
}
