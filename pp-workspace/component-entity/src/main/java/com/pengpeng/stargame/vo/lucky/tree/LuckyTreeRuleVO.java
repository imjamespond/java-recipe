package com.pengpeng.stargame.vo.lucky.tree;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("招财树")
@EventAnnotation(name="event.lucky.tree",desc="幸运树推送VO")
public class LuckyTreeRuleVO { 
	@Desc("等级")
	private int level;

    @Desc("免费摇树金额")
    private int freeGameCoin;//免费摇树金额

    @Desc("免费暴击倍数")
    private int freeMultiple;//免费暴击倍数

    @Desc("达人币摇树金额")
    private int goldGameCoin;//达人币摇树金额

    @Desc("达人币暴击倍数")
    private int goldMultiple;//达人币暴击倍数
    @Desc("花费达人币编辑")
    private String goldStr;//花费达人币编辑

    @Desc("花费达人币编辑")
    private String creditStr;//花费达人币编辑


    @Desc("浇水间隔时间")
    private long waterCd;//浇水间隔时间（S）
    @Desc("每日浇水数")
    private int waterNum;//每日浇水数
    @Desc("好友每日可浇水次数")
    private int waterFri;//好友每日可浇水次数

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFreeGameCoin() {
        return freeGameCoin;
    }

    public void setFreeGameCoin(int freeGameCoin) {
        this.freeGameCoin = freeGameCoin;
    }

    public int getFreeMultiple() {
        return freeMultiple;
    }

    public void setFreeMultiple(int freeMultiple) {
        this.freeMultiple = freeMultiple;
    }

    public int getGoldGameCoin() {
        return goldGameCoin;
    }

    public void setGoldGameCoin(int goldGameCoin) {
        this.goldGameCoin = goldGameCoin;
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
}
