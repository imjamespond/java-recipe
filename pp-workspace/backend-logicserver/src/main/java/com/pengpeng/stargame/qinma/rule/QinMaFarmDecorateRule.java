package com.pengpeng.stargame.qinma.rule;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.Indexable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: mql
 * Date: 14-4-2
 * Time: 下午4:50
 */
@Entity
@Table(name="sg_rule_qinma_farm_decorate")
public class QinMaFarmDecorateRule extends BaseEntity<String> {
    @Id
    private String id;
    @Column
    private String value;

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

    @Override
    public String getKey() {
        return id;
    }
}
