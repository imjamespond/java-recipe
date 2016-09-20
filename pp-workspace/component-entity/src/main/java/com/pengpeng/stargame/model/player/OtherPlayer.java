package com.pengpeng.stargame.model.player;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.DateUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-15下午3:19
 */
public class OtherPlayer extends BaseEntity<String> {
    //pid
    private String pid;
    //禁言时间,如果无限期,则设置很长时间比如10年
    private long speakTime =0;
    //封号时间,如果是无限期封号,则时间设置10年或更长
    private long freezeTime =0;

    //当天的累积在线时间
    private long dayLoginTime;

    //进入游戏时间
    private Date loginTime;
    //退出游戏时间
    private Date logoutTime;

    //音乐榜期间,登录在线时赠送的礼物  是否已经赠送
    private boolean loginGive;

    //公测期间是否已领取奖励
    private Date claimDate;

    //日清理时间
    private Date clearTime;
    public OtherPlayer(){

    }
    public OtherPlayer(String pid) {
        this.pid = pid;
    }

    public boolean isLoginGive() {
        return loginGive;
    }

    public void setLoginGive(boolean loginGive) {
        this.loginGive = loginGive;
    }

    public boolean isClaim(){
        if (null==claimDate){
            return false;
        }
        if (DateUtil.getDayOfYear(claimDate)!=DateUtil.getDayOfYear(new Date())){
            return false;
        }
        return true;
    }
    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public long getDayLoginTime() {
        return dayLoginTime;
    }

    public void setDayLoginTime(long dayLoginTime) {
        this.dayLoginTime = dayLoginTime;
    }

    public Date getClearTime() {
        return clearTime;
    }

    public void setClearTime(Date clearTime) {
        this.clearTime = clearTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public long getSpeakTime() {
        return speakTime;
    }

    public void setSpeakTime(long speakTime) {
        this.speakTime = speakTime;
    }

    public long getFreezeTime() {
        return freezeTime;
    }

    public void setFreezeTime(long freezeTime) {
        this.freezeTime = freezeTime;
    }

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {
        this.pid = id;
    }

    @Override
    public String getKey() {
        return pid;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void doLogin(Date time){
        if(time == null){
            return ;
        }
        loginTime = time;

    }
    //计算在线时间
    public void doLogout(Date time){
        if(null!=loginTime&&DateUtil.getDayOfYear(loginTime)==DateUtil.getDayOfYear(time)){
            //如果登录时间和退出时间是同一天  累加在线时间
            dayLoginTime+= calcOnlineTime(loginTime,time);
        }else{
            //如果是跨天数   不累加在线时间
            dayLoginTime= calcOnlineTime(loginTime,time);
        }
        logoutTime = time;
    }
    public boolean isTodayLogin(){
        if (loginTime==null){
            return false;
        }
        return DateUtil.getDayOfYear(loginTime)==DateUtil.getDayOfYear(new Date());
    }

    /**
     * 以秒为单位
     * @return 返回 玩家当天累积在线时间
     */
    public long getAccumulateOnlineTime(Date date){
        if (null!=loginTime&&DateUtil.getDayOfYear(loginTime)==DateUtil.getDayOfYear(date)){
            //从登录开始算,并累计今天登录的时间
            return dayLoginTime+calcOnlineTime(loginTime, date);
        }
        return dayLoginTime;
    }

    private long calcOnlineTime(Date sDate,Date eDate){
        if (sDate==null){
            return 0;
        }
        if(eDate == null){
            eDate = new Date();
        }
        if(sDate.after(eDate)){//如果开始时间大于结束时间
            eDate = new Date();
        }
        if (DateUtil.getDayOfYear(sDate)!=DateUtil.getDayOfYear(eDate)){
            //如果跨天
            sDate = DateUtil.getZeroTime(eDate);
        }

        long time = eDate.getTime() - sDate.getTime();
        return time/1000;
    }

//    public static void main(String[] args) throws ParseException {
//        OtherPlayer op = new OtherPlayer();
//        op.init(DateUtil.toUtilDate("2013-10-10 21:00:00","yyyy-MM-dd HH:mm:ss"));
//        op.doLogin(DateUtil.toUtilDate("2013-10-10 23:00:00","yyyy-MM-dd HH:mm:ss"));
//        op.doLogout(DateUtil.toUtilDate("2013-10-11 1:15:00","yyyy-MM-dd HH:mm:ss"));
//
//        op.doLogin(DateUtil.toUtilDate("2013-10-11 02:00:00","yyyy-MM-dd HH:mm:ss"));
//        op.doLogout(DateUtil.toUtilDate("2013-10-11 02:15:00","yyyy-MM-dd HH:mm:ss"));
//
////        op.doLogin(DateUtil.toUtilDate("2013-10-11 23:00:00","yyyy-MM-dd HH:mm:ss"));
////        op.doLogout(DateUtil.toUtilDate("2013-10-11 23:45:00","yyyy-MM-dd HH:mm:ss"));
//        op.init(new Date());
//        long time = op.getAccumulateOnlineTime(new Date());
//        System.out.println(time);
//    }

    public boolean init(Date now) {
        if (clearTime==null){
            dayLoginTime =0;
            clearTime = now;
            return true;
        }
        if(DateUtil.getDayOfYear(clearTime)!=DateUtil.getDayOfYear(now)){
            dayLoginTime =0;
            clearTime = now;
            return true;
        }
        return false;
    }
}
