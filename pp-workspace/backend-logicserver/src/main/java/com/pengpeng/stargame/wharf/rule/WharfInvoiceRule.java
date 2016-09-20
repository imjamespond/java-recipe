package com.pengpeng.stargame.wharf.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:04
 */

@Entity
@Table(name="sg_rule_wharf_invoice")
public class WharfInvoiceRule extends BaseEntity<String> {

    @Id
    private String invoiceId;
    @Column
    private int level;
    @Column
    private int gameCoin;
    @Column
    private int goldCoin;
    @Column
    private int farmExp;
    @Column
    private String itemId;
    @Column
    private int itemNum;
    @Column
    private int credit;
    @Column
    private int contribution;



    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return invoiceId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(int goldCoin) {
        this.goldCoin = goldCoin;
    }

    public int getFarmExp() {
        return farmExp;
    }

    public void setFarmExp(int farmExp) {
        this.farmExp = farmExp;
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

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }



}
