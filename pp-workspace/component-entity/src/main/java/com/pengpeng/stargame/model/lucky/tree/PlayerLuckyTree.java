package com.pengpeng.stargame.model.lucky.tree;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:07
 */
public class PlayerLuckyTree extends BaseEntity<String> {
    private String pid;
    private int level;//等级
    private int exp;//经验
    private int freeNum;//免费次数
    private Date freeDate;//免费次数日期

    private int goldNum;//达人币次数
    private int goldExtra;//达人币增加次数
    private Date goldDate;//达人币日期
    private Date goldExtraDate;//达人币日期

    private int waterNum;//浇水次数
    private Date waterDate;//浇水日期

    private int waterFri;//好友浇水次数
    //private Date waterFriDate;//好友浇水日期

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getGoldExtra() {
        return goldExtra;
    }

    public void setGoldExtra(int goldExtra) {
        this.goldExtra = goldExtra;
    }

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

    public int getFreeNum() {
        return freeNum;
    }

    public void setFreeNum(int freeNum) {
        this.freeNum = freeNum;
    }

    public int getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(int goldNum) {
        this.goldNum = goldNum;
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

    public Date getFreeDate() {
        return freeDate;
    }

    public void setFreeDate(Date freeDate) {
        this.freeDate = freeDate;
    }

    public Date getWaterDate() {
        return waterDate;
    }

    public void setWaterDate(Date waterDate) {
        this.waterDate = waterDate;
    }

    public Date getGoldExtraDate() {
        return goldExtraDate;
    }

    public void setGoldExtraDate(Date goldExtraDate) {
        this.goldExtraDate = goldExtraDate;
    }
/*    public Date getWaterFriDate() {
        return waterFriDate;
    }

    public void setWaterFriDate(Date waterFriDate) {
        this.waterFriDate = waterFriDate;
    }*/

    public Date getGoldDate() {
        return goldDate;
    }

    public void setGoldDate(Date goldDate) {
        this.goldDate = goldDate;
    }

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getKey() {
        return pid;
    }

}
