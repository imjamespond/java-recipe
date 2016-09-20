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
@Table(name="gm_player_harvest_action")
public class PlayerHarvestActionModel extends Model {

    @Column
    private int type;//0为种植 1为收获 2为协助收获
    @Column
    private String fieldId;
    @Column
    private int ripenNum;
    @Column
    private String plantId;
    @Column
    private String friendId;

    public PlayerHarvestActionModel() {
    }

    public PlayerHarvestActionModel(String uid, String pid, Date date) {
        super(uid, pid, date);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public int getRipenNum() {
        return ripenNum;
    }

    public void setRipenNum(int ripenNum) {
        this.ripenNum = ripenNum;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}
