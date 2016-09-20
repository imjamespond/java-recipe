package com.pengpeng.stargame.piazza.rule;

import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午4:37
 */
@Entity
@Table(name="sg_rule_familycollect")
public class FamilyCollectRule extends BaseEntity<String> {
    @Id
    private String collectId;
    @Column
    private String familyId;
    @Column
    private String itemsEditor;
    @Column
    private String activetyDesc;
    @Column

    private int gameCoin;
    @Column
    private int exp;
    @Column
    private int funding;
    @Column
    private int fanValue;
    @Column
    private String  rewardDesc;
    @Transient
    private String itemId;
    @Transient
    private int itemNum;



    public void init(){
        String [] ss=itemsEditor.split(",");
        this.itemId=ss[0];
        this.itemNum=Integer.parseInt(ss[1]);
    }
    public String getCollectId() {
        return collectId;
    }

    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getItemsEditor() {
        return itemsEditor;
    }

    public void setItemsEditor(String itemsEditor) {
        this.itemsEditor = itemsEditor;
    }

    public String getActivetyDesc() {
        return activetyDesc;
    }

    public void setActivetyDesc(String activetyDesc) {
        this.activetyDesc = activetyDesc;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getFunding() {
        return funding;
    }

    public void setFunding(int funding) {
        this.funding = funding;
    }

    public int getFanValue() {
        return fanValue;
    }

    public void setFanValue(int fanValue) {
        this.fanValue = fanValue;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public String getRewardDesc() {
        return rewardDesc;
    }

    public void setRewardDesc(String rewardDesc) {
        this.rewardDesc = rewardDesc;
    }

    @Override
    public String getId() {
        return collectId;
    }


    @Override
    public void setId(String id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getKey() {
       return collectId;
    }
}
