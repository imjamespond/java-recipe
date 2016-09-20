package com.pengpeng.stargame.player.rule;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.Indexable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sg_rule_npc")
public class NpcRule extends BaseEntity<String> {
    @Id
    private String npcID;
    @Column
    private String npcName;
    @Column
    private String npcTitle;
    @Column
    private int npcLv;
    @Column
    private String dialogue;
    @Column
    private String options1;
    @Column
    private String function1;
    @Column
    private String options2;
    @Column
    private String function2;
    @Column
    private String options3;
    @Column
    private String function3;
    @Column
    private String npcImage;


    @Override
    public String getId() {
        return npcID;
    }

    @Override
    public void setId(String id) {
        npcID = id;
    }

    @Override
    public String getKey() {
        return npcID;
    }
}
