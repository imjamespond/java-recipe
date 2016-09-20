package com.pengpeng.admin.stargame.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-11-20下午2:21
 */
@MappedSuperclass
public class Model {
    @Id
    @GenericGenerator(name="gen",strategy="increment")
    @GeneratedValue(generator="gen")
    @Column(name = "id", unique = true, nullable = false, precision = 15, scale = 0)
    private long id;
    @Column
    private String pid;
    @Transient
    public String name;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column
    private String uid;

    public Model(){

    }
    public Model(String uid,String pid,Date date){
        this.uid = uid;
        this.pid = pid;
        this.date = date;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
