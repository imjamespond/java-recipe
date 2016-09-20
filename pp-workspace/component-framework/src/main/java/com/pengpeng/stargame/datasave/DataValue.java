package com.pengpeng.stargame.datasave;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: mql
 * Date: 14-1-13
 * Time: 下午2:02
 */
@Entity
@Table(name = "dataValue")
public class DataValue {
    @Id
    private String id;
    @Column
    private String value;
    @Column
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
