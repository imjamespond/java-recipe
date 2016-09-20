package com.pengpeng.admin.stargame.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 使用物品
 * User: mql
 * Date: 13-10-17
 * Time: 下午3:08
 */
@Entity
@Table(name="gm_player_item")
public class PlayerItemModel extends Model {
    //买,卖,用
    @Column
    private int action;

    @Column
    private String itemId;
    @Column
    private int num;
    @Column
    private int type;
    @Column
    private int itemType;

    public PlayerItemModel() {
    }

    public PlayerItemModel(String uid, String pid, Date date) {
        super(uid, pid, date);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
