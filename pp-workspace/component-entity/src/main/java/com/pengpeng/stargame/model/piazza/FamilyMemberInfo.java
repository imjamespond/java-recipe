package com.pengpeng.stargame.model.piazza;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.DateUtil;

import java.util.Date;

/**
 * 家族成员模型
 * User: mql
 * Date: 13-6-26
 * Time: 下午5:43
 */
public class FamilyMemberInfo extends BaseEntity<String> {
    private String pid;
    private String familyId;//自己所属的家族的Id
    private int dayFunds; //日捐献
    private Date dayFundsTime;  //日捐献清理时间
    private int totalContribution;//个人累计贡献
    private int  contribution;//家族贡献(更换家族后清零)
    private int dayContribution;//日贡献
    private Date dayTime; //日贡献 清零时间
    private int weekContribution;//周贡献
    private Date weekTime; //周贡献 清零时间
    private  int  identity ;//家族身份  1明星   2明星助理
    private Date boonTime;//福利领取时间
    private Date changeFamilyTime;//更换家族时间
    private int sendGiftNum; //每日送明星礼物个数
    private Date sendGiftDate; //每日送明星礼物 时间
    private int changeFamilyNum;//更换家族的次数
    public FamilyMemberInfo(){
    }

    public FamilyMemberInfo(String pid){
        this.pid=pid;
    }

    public void init(){
        Date date = new Date();
        incDevote(0,date);
        incFunds(0,date);
        incGiftNum(0,date);
    }

    public int getDayFunds() {
        return dayFunds;
//        if (DateUtil.getDayOfYear(dayFundsTime)==DateUtil.getDayOfYear(new Date())){
//            return dayFunds;
//        }else{
//            dayFunds = 0;
//            dayFundsTime = new Date();
//            return dayFunds;
//        }
    }

    public void changeFamily(Date date){
        this.changeFamilyTime = date;
    }
    public boolean isChangeFamily(Date date){
        if (null==changeFamilyTime){
            return false;
        }
        if (DateUtil.getDayOfYear(changeFamilyTime)==DateUtil.getDayOfYear(date)){
            return true;
        }
        return false;
    }

    public void setDayFunds(int dayFunds) {
        this.dayFunds = dayFunds;
    }

    public Date getDayFundsTime() {
        return dayFundsTime;
    }

    public void setDayFundsTime(Date dayFundsTime) {
        this.dayFundsTime = dayFundsTime;
    }

    public int getTotalContribution() {
        return totalContribution;
    }

    public void setTotalContribution(int totalContribution) {
        this.totalContribution = totalContribution;
    }

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return pid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public int getDayContribution() {
        return dayContribution;
    }

    public void setDayContribution(int dayContribution) {
        this.dayContribution = dayContribution;
    }

    public Date getDayTime() {
        return dayTime;
    }

    public void setDayTime(Date dayTime) {
        this.dayTime = dayTime;
    }

    public int getWeekContribution() {
        return weekContribution;
    }

    public void setWeekContribution(int weekContribution) {
        this.weekContribution = weekContribution;
    }

    public Date getWeekTime() {
        return weekTime;
    }

    public void setWeekTime(Date weekTime) {
        this.weekTime = weekTime;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getSendGiftNum() {
        return sendGiftNum;
    }

    public void setSendGiftNum(int sendGiftNum) {
        this.sendGiftNum = sendGiftNum;
    }

    public Date getSendGiftDate() {
        return sendGiftDate;
    }

    public void setSendGiftDate(Date sendGiftDate) {
        this.sendGiftDate = sendGiftDate;
    }

    public int getChangeFamilyNum() {
        return changeFamilyNum;
    }

    public void setChangeFamilyNum(int changeFamilyNum) {
        this.changeFamilyNum = changeFamilyNum;
    }

    /**
     * 是否已经领取
     * @return
     */
    public boolean isBoon(Date time){
        if (boonTime==null){
            return false;
        }
        //如果领取时间大于指定时间
        if (DateUtil.getDayOfYear(boonTime)==DateUtil.getDayOfYear(time)){
            return true;
        }
        return false;
    }

    /**
     * 设置领取时间
     * @param time
     */
    public void receiveBoon(Date time){
        boonTime = time;
    }
    //增加 送明星礼物个数
    public void incGiftNum(int value,Date now){
        if(sendGiftDate==null){
            sendGiftDate=now;
        }
        if (DateUtil.getDayOfYear(sendGiftDate)==DateUtil.getDayOfYear(now)){
            sendGiftNum +=value;
            sendGiftDate = now;
        }else{
            sendGiftNum = value;
            sendGiftDate = now;
        }
    }
    //增加对家族的贡献
    public void incDevote(int value,Date now){
        totalContribution+=value;
        contribution+=value;
        if (dayTime ==null){
            dayTime = now;
        }
        if (weekTime==null){
            weekTime = now;
        }
        if (DateUtil.getDayOfYear(dayTime)==DateUtil.getDayOfYear(now)){
            dayContribution +=value;
            dayTime = now;
        }else{
            dayContribution = value;
            dayTime = now;
        }
        if (DateUtil.getWeekOfYear(weekTime)==DateUtil.getWeekOfYear(now)){
            weekContribution+=value;
            weekTime = now;
        }else{
            weekContribution = value;
            weekTime = now;
        }
    }

    /**
     * 增加个人捐献经费
     * @param money
     * @param now
     */
    protected void incFunds(int money, Date now) {
        if(dayFundsTime==null){
            dayFundsTime=now;
        }
        if (DateUtil.getDayOfYear(dayFundsTime)==DateUtil.getDayOfYear(now)){
            dayFunds += money;
        }else{
            dayFunds = money;
            dayFundsTime = now;
        }

    }

    /**
     * 捐献
     * @param devote
     * @param date
     */
    public void endow(int devote, Date date) {
        //这里 加个人贡献 改成外面加，为了 任务统计，玩家 每日获得的贡献值
//        incDevote(devote,date);
        incFunds(devote,date);
    }

    //本周是否有过家族变更
    public boolean isWeekChangeFamily(Date now) {
        if (null==changeFamilyTime){
            return false;
        }
        if (DateUtil.getWeekOfYear(changeFamilyTime)==DateUtil.getWeekOfYear(now)){
            return true;
        }
        return false;
    }
}
