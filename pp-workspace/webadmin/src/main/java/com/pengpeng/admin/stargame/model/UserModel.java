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
@Table(name="users")
public class UserModel {

    @Id
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private short enabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public short getEnabled() {
        return enabled;
    }

    public void setEnabled(short enabled) {
        this.enabled = enabled;
    }
}
