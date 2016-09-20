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
@Table(name="gm_player_buy_action")
public class PlayerBuyActionModel extends Model {  //玩家购买道具
     @Column
    private String itemId;
    @Transient
    public String itemName;
    @Column
    private int num;

    @Column
    private int type;

    @Column
    private int itemType;

    public PlayerBuyActionModel() {
    }

    public PlayerBuyActionModel(String uid, String pid, Date date) {
        super(uid, pid, date);
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
