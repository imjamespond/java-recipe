package com.pengpeng.stargame.vo.lucky.tree;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("招财树")
@EventAnnotation(name="event.lucky.tree",desc="幸运树推送VO")
public class LuckyTreeVO {
	@Desc("等级")
	private int level;
    @Desc("经验")
    private int exp;
    @Desc("下级经验")
    private int needExp;

    @Desc("主人可浇水数")
    private int waterNum;
    @Desc("好友可浇水数")
    private int waterFri;

    @Desc("浇水时间cd")
    private long waterCd;

    @Desc("免费招财次数")
    private int freeCallNum;
    @Desc("达人币招财次数")
    private int goldCallNum;

    @Desc("达人币数招财")
    private int goldCall;

    @Desc("规则")
    private LuckyTreeRuleVO[] ruleVOs;


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getNeedExp() {
        return needExp;
    }

    public void setNeedExp(int needExp) {
        this.needExp = needExp;
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

    public long getWaterCd() {
        return waterCd;
    }

    public void setWaterCd(long waterCd) {
        this.waterCd = waterCd;
    }

    public int getFreeCallNum() {
        return freeCallNum;
    }

    public void setFreeCallNum(int freeCallNum) {
        this.freeCallNum = freeCallNum;
    }

    public int getGoldCallNum() {
        return goldCallNum;
    }

    public void setGoldCallNum(int goldCallNum) {
        this.goldCallNum = goldCallNum;
    }

    public int getGoldCall() {
        return goldCall;
    }

    public void setGoldCall(int goldCall) {
        this.goldCall = goldCall;
    }

    public LuckyTreeRuleVO[] getRuleVOs() {
        return ruleVOs;
    }

    public void setRuleVOs(LuckyTreeRuleVO[] ruleVOs) {
        this.ruleVOs = ruleVOs;
    }
}
