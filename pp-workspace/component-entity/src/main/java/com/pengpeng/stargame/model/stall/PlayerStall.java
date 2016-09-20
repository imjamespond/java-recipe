package com.pengpeng.stargame.model.stall;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:07
 */
public class PlayerStall extends BaseEntity<String> {
    private String pid;
    private boolean enable;
    private Date loginDate;//登陆时间
    private int loginDays;//连续登陆
    private Date buyingDate;//购买日期
    private int buyingTimes;//购买次数
    private Date momShelfDate;//亲妈货架日期
    private long hitShelfTime;//上架时间
    private int hitShelfNum;     //上架次数
    private PlayerShelf[] playerShelfs;     //玩家的货架
    private List<PlayerShelf> playerGoldShelf;     //达人币货架
    private List<PlayerShelf> playerFriShelf;     //加好友货架
    private List<PlayerShelf> playerMomShelf;     //亲妈货架
    private List<PlayerShelf> playerVipShelf;     //vip货架
    private Date assistantTime;//助手时间
    private Date assistantNextTime;//助手下次服务时间
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getHitShelfTime() {
        return hitShelfTime;
    }

    public void setHitShelfTime(long hitShelfTime) {
        this.hitShelfTime = hitShelfTime;
    }

    public int getHitShelfNum() {
        return hitShelfNum;
    }

    public void setHitShelfNum(int hitShelfNum) {
        this.hitShelfNum = hitShelfNum;
    }

    public PlayerShelf[] getPlayerShelfs() {
        return playerShelfs;
    }

    public boolean getAnyPlayerShelfByState(int state) {

        for(int i=0;i<playerShelfs.length;i++){
            if(null == playerShelfs[i]){
                playerShelfs[i] = new PlayerShelf();
            }
            if(state == playerShelfs[i].getState()){
                return true;
            }
        }
        for(PlayerShelf shelf:playerGoldShelf){
            if(state == shelf.getState()){
                return true;
            }
        }
        for(PlayerShelf shelf:playerFriShelf){
            if(state == shelf.getState()){
                return true;
            }
        }
        for(PlayerShelf shelf:playerVipShelf){
            if(state == shelf.getState()){
                return true;
            }
        }
        return false;
    }

    public List<PlayerShelf> getPlayerShelfByState(int state) {

        List<PlayerShelf> list = new ArrayList<PlayerShelf>();

        for(int i=0;i<playerShelfs.length;i++){
            if(null == playerShelfs[i]){
                playerShelfs[i] = new PlayerShelf();
            }
            if(state == playerShelfs[i].getState()){
                list.add(playerShelfs[i]);
            }
        }
        for(PlayerShelf shelf:playerGoldShelf){
            if(state == shelf.getState()){
                list.add(shelf);
            }
        }
        for(PlayerShelf shelf:playerFriShelf){
            if(state == shelf.getState()){
                list.add(shelf);
            }
        }
        for(PlayerShelf shelf:playerVipShelf){
            if(state == shelf.getState()){
                list.add(shelf);
            }
        }
        return list;
    }

    public PlayerShelf getPlayerShelfByOrder(int order,int type) {
        PlayerShelf shelf = null;
        if(type == StallConstant.SHELF_TYPE_ORDINARY){
            if(null == playerShelfs[order]){
                playerShelfs[order] = new PlayerShelf();
            }
            shelf = playerShelfs[order];
        } else if(type == StallConstant.SHELF_TYPE_GOLD){
            if(null == playerGoldShelf.get(order)){
                playerGoldShelf.add(order,new PlayerShelf());
            }
            shelf = playerGoldShelf.get(order);
        }  else if(type == StallConstant.SHELF_TYPE_FRIEND){
            if(null == playerFriShelf.get(order)){
                playerFriShelf.add(order,new PlayerShelf());
            }
            shelf = playerFriShelf.get(order);
        }  else if(type == StallConstant.SHELF_TYPE_MOM){
            if(null == playerMomShelf.get(order)){
                playerMomShelf.add(order,new PlayerShelf());
            }
            shelf = playerMomShelf.get(order);
        } else if(type == StallConstant.SHELF_TYPE_VIP){
            if(null == playerVipShelf.get(order)){
                playerVipShelf.add(order,new PlayerShelf());
            }
            shelf = playerVipShelf.get(order);
        }else{
            return null;
        }

        return shelf;
    }

    public void setPlayerShelfs(PlayerShelf[] playerShelfs) {
        this.playerShelfs = playerShelfs;
    }

    public List<PlayerShelf> getPlayerGoldShelf() {
        return playerGoldShelf;
    }

    public void setPlayerGoldShelf(List<PlayerShelf> playerGoldShelf) {
        this.playerGoldShelf = playerGoldShelf;
    }

    public List<PlayerShelf> getPlayerFriShelf() {
        return playerFriShelf;
    }

    public void setPlayerFriShelf(List<PlayerShelf> playerFriShelf) {
        this.playerFriShelf = playerFriShelf;
    }

    public List<PlayerShelf> getPlayerMomShelf() {
        return playerMomShelf;
    }

    public void setPlayerMomShelf(List<PlayerShelf> playerMomShelf) {
        this.playerMomShelf = playerMomShelf;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public int getDay() {
        return loginDays;
    }

    public void setDay(int loginNum) {
        this.loginDays = loginNum;
    }
    public void incDay(int loginNum) {
        this.loginDays += loginNum;
    }

    public Date getBuyingDate() {
        return buyingDate;
    }

    public void setBuyingDate(Date buyingDate) {
        this.buyingDate = buyingDate;
    }

    public int getBuyingTimes() {
        return buyingTimes;
    }

    public void setBuyingTimes(int buyingTimes) {
        this.buyingTimes = buyingTimes;
    }

    public void incBuyingTimes() {

        //当日
        Calendar today = Calendar.getInstance();
        int day = today.get(Calendar.DAY_OF_YEAR);

        //上次
        Calendar last = Calendar.getInstance();
        last.setTime(buyingDate);
        int ref = last.get(Calendar.DAY_OF_YEAR);

        if(day == ref){
            buyingTimes++;
        }else{
            buyingTimes = 1;
        }
    }

    public Date getMomShelfDate() {
        return momShelfDate;
    }

    public void setMomShelfDate(Date momShelfDate) {
        this.momShelfDate = momShelfDate;
    }

    public List<PlayerShelf> getPlayerVipShelf() {
        return playerVipShelf;
    }

    public void setPlayerVipShelf(List<PlayerShelf> playerVipShelf) {
        this.playerVipShelf = playerVipShelf;
    }

    public Date getAssistantTime() {
        return assistantTime;
    }

    public void setAssistantTime(Date assistantTime) {
        this.assistantTime = assistantTime;
    }

    public Date getAssistantNextTime() {
        return assistantNextTime;
    }

    public void setAssistantNextTime(Date assistantNextTime) {
        this.assistantNextTime = assistantNextTime;
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
