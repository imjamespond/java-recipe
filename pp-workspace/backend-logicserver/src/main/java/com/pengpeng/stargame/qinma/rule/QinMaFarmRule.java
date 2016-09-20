package com.pengpeng.stargame.qinma.rule;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;
import java.util.*;

/**
 * User: mql
 * Date: 13-8-13
 * Time: 下午2:56
 */
@Entity
@Table(name="sg_rule_qinma_farm")
public class QinMaFarmRule extends BaseEntity<String> {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private int level;
    @Column
    private String fieldsValue;

    @Transient
    private List<FarmField> fields;

    public QinMaFarmRule() {
        fields = new ArrayList<FarmField>();
    }

    public QinMaFarmRule(String id, String name, int level, String fieldsValue) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.fieldsValue = fieldsValue;
        fields = new ArrayList<FarmField>();
        init();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getKey() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getFieldsValue() {
        return fieldsValue;
    }

    public void setFieldsValue(String fieldsValue) {
        this.fieldsValue = fieldsValue;
    }

    public List<FarmField> getFields() {
        return fields;
    }

    public void setFields(List<FarmField> fields) {
        this.fields = fields;
        Gson gson = new Gson();
        fieldsValue = gson.toJson(fields);
    }

    public static class FarmField {
        private String id;//田地的 id  从 1 开始
        private String seedId;//种子Id
        /**
         * 如果 让田地直接显示成熟状态 那么 是 2
         */
        private int status;//状态  0 空闲  1生长  2全部成熟

        private String position;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSeedId() {
            return seedId;
        }

        public void setSeedId(String seedId) {
            this.seedId = seedId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }

    public void init() {
        Gson gson = new Gson();
        List<FarmField> lists = gson.fromJson(fieldsValue,new TypeToken<List<FarmField>>(){}.getType());
        fields = lists;
    }
}
