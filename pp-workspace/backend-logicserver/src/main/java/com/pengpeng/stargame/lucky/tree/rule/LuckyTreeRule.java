package com.pengpeng.stargame.lucky.tree.rule;

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
@Table(name="sg_rule_lucky_tree")
public class LuckyTreeRule extends BaseEntity<String> {

    @Id
    private int level;//从1开始,
    @Column
    private String name;
    @Column
    private int needExp;//表示下一级所需经验

    @Column
    private int freeCallNum;//免费摇树次数
    @Column
    private int freeGameCoin;//免费摇树金额
    @Column
    private int freeCritical;//免费暴击几率
    @Column
    private int freeMultiple;//免费暴击倍数

    @Column
    private int goldCallNum;//达人币摇树次数
    @Column
    private int goldGameCoin;//达人币摇树金额
    @Column
    private int goldCritical;//达人币暴击几率
    @Column
    private int goldMultiple;//达人币暴击倍数
    @Column
    private String goldStr;//花费达人币编辑

    @Column
    private String creditStr;//花费达人币编辑

    @Column
    private int waterExp;//每次浇水经验
    @Column
    private long waterCd;//浇水间隔时间（S）
    @Column
    private int waterNum;//每日浇水数
    @Column
    private int waterFri;//好友每日可浇水次数

    @Transient
    private Map<Integer,LuckyTreeDataRule> dataMap = new HashMap<Integer,LuckyTreeDataRule>();// 次数->数据

    @Override
    public String getId() {
        return String.valueOf(level);
    }

    @Override
    public void setId(String id) {
        level = Integer.valueOf(id);
    }

    @Override
    public String getKey() {
        return String.valueOf(level);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNeedExp() {
        return needExp;
    }

    public void setNeedExp(int needExp) {
        this.needExp = needExp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFreeCallNum() {
        return freeCallNum;
    }

    public void setFreeCallNum(int freeCallNum) {
        this.freeCallNum = freeCallNum;
    }

    public int getFreeGameCoin() {
        return freeGameCoin;
    }

    public void setFreeGameCoin(int freeGameCoin) {
        this.freeGameCoin = freeGameCoin;
    }

    public int getFreeCritical() {
        return freeCritical;
    }

    public void setFreeCritical(int freeCritical) {
        this.freeCritical = freeCritical;
    }

    public int getFreeMultiple() {
        return freeMultiple;
    }

    public void setFreeMultiple(int freeMultiple) {
        this.freeMultiple = freeMultiple;
    }

    public int getGoldCallNum() {
        return goldCallNum;
    }

    public void setGoldCallNum(int goldCallNum) {
        this.goldCallNum = goldCallNum;
    }

    public int getGoldGameCoin() {
        return goldGameCoin;
    }

    public void setGoldGameCoin(int goldGameCoin) {
        this.goldGameCoin = goldGameCoin;
    }

    public int getGoldCritical() {
        return goldCritical;
    }

    public void setGoldCritical(int goldCritical) {
        this.goldCritical = goldCritical;
    }

    public int getGoldMultiple() {
        return goldMultiple;
    }

    public void setGoldMultiple(int goldMultiple) {
        this.goldMultiple = goldMultiple;
    }

    public String getGoldStr() {
        return goldStr;
    }

    public void setGoldStr(String goldStr) {
        this.goldStr = goldStr;
    }

    public String getCreditStr() {
        return creditStr;
    }

    public void setCreditStr(String creditStr) {
        this.creditStr = creditStr;
    }

    public int getWaterExp() {
        return waterExp;
    }

    public void setWaterExp(int waterExp) {
        this.waterExp = waterExp;
    }

    public long getWaterCd() {
        return waterCd;
    }

    public void setWaterCd(long waterCd) {
        this.waterCd = waterCd;
    }

    public int getWaterNum() {
        return waterNum;
    }

    public void setWaterNum(int waterNum) {
        this.waterNum = waterNum;
    }

    public int getWaterFri() {
        return waterFri;
    }

    public void setWaterFri(int waterFri) {
        this.waterFri = waterFri;
    }

    public Map<Integer, LuckyTreeDataRule> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<Integer, LuckyTreeDataRule> dataMap) {
        this.dataMap = dataMap;
    }

    //解析
    public void parse(){

        String[] arr = goldStr.split(";");
        int i = 1;
        for(String gold:arr){
            if(null != gold){
                Integer value = Integer.valueOf(gold);
                if(!dataMap.containsKey(i)){
                    LuckyTreeDataRule rule = new LuckyTreeDataRule();
                    rule.gold = value;
                    dataMap.put(i,rule);
                }else {
                    LuckyTreeDataRule rule = dataMap.get(i);
                    rule.gold = value;
                }
            }
            i++;
        }

        arr = creditStr.split(";");
        for(String credit:arr){
            if(null != credit){
                String[] pair = credit.split(",");
                if(pair.length == 2){
                    Integer key = Integer.valueOf(pair[0]);
                    Integer value = Integer.valueOf(pair[1]);
                    if(!dataMap.containsKey(key)){
                        LuckyTreeDataRule rule = new LuckyTreeDataRule();
                        rule.credit = value;
                        dataMap.put(key,rule);
                    }else {
                        LuckyTreeDataRule rule = dataMap.get(key);
                        rule.credit = value;
                    }
                }
            }
        }
    }
}
