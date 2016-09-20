package com.pengpeng.admin.stargame.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午8:59
 */
@Entity
@Table(name="gm_user_action")
public class UserActionModel {
    @Transient
    public static final int T_BAN_PLAYER = 1;
    @Transient
    public static final int T_BAN_CHAT = 2;
    @Transient
    public static final int T_ADD_GOLD_COIN = 3;
    @Transient
    public static final int T_ADD_GAME_COIN = 4;
    @Transient
    public static final int T_ADD_ITEM = 5;


    @Id
    @GenericGenerator(name="gen",strategy="increment")
    @GeneratedValue(generator="gen")
    @Column(name = "id", unique = true, nullable = false, precision = 15, scale = 0)
    private int id;
    @Column
    private String userName;
    @Column
    private int type;
    @Transient
    public String typeName;
    @Column
    private Date date;
    @Column
    private String pid;
@Transient
	public String name;
    @Column
    private int num;
    @Column
    private String reason;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
