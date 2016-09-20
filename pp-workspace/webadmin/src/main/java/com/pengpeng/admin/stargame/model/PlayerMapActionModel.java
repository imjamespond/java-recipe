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
@Table(name="gm_player_map_action")
public class PlayerMapActionModel extends Model{

    @Column
    private String mapId;

    public PlayerMapActionModel() {
    }

    public PlayerMapActionModel(String uid, String pid, Date date) {
        super(uid, pid, date);
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }
}
