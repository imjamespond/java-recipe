package com.pengpeng.stargame.model.piazza;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.*;

/**
 * User: mql
 * Date: 13-6-28
 * Time: 下午4:23
 */
public class MoneyTree extends BaseEntity<String> {
    private String familyId;
    private int status;//摇钱树状态 1生长  2成熟
    private Date ripeTime; //成熟时间
    private Date ripeEndTime;//成熟结束时间
    private int blessingValue;//祝福值  成熟结束后 清0
    private Date nextDropTime;//下次掉落 时间
    private Map<String,PlayerBlessing> playerBlessings;  //玩家祝福的数据
    private Map<String,String> playerShake;//摇钱的玩家数据
    private List<MoneyPick> moneyPickList;//摇钱树掉落的钱信息


    /**
     * 清理 玩家摇钱信息
     * 清理 玩家摇钱信息
     */
    public void clearPlayerShake(){
        this.playerShake.clear();
    }
    /**
     * 清理玩家祝福数据
     */
    public void clearPlayerBlessings(){
        this.playerBlessings.clear();
    }

    /**
     * 获取 单个 玩家祝福 数据
     * @param pid
     * @return
     */
    public PlayerBlessing getPlayerBlessing(String pid){
       if(playerBlessings.get(pid)==null){
           PlayerBlessing playerBlessing=new PlayerBlessing(pid);
           playerBlessings.put(pid,playerBlessing);
       }
       return playerBlessings.get(pid);
    }

    /**
     * 获取玩家 摇钱了多少次
     */
    public int getShakeNum(String pid){
        String num= playerShake.get(pid);
        int inum=0 ;
        if(num!=null){
            inum=Integer.parseInt(num);
        }
        return inum;
    }
    public MoneyTree(){

    }
    public MoneyTree(String familyId){
        this.familyId=familyId;
        playerBlessings=new HashMap<String, PlayerBlessing>();
        playerShake=new HashMap<String, String>();
        moneyPickList=new ArrayList<MoneyPick>();
    }
    public Date getNextDropTime() {
        return nextDropTime;
    }

    public void setNextDropTime(Date nextDropTime) {
        this.nextDropTime = nextDropTime;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getRipeTime() {
        return ripeTime;
    }

    public void setRipeTime(Date ripeTime) {
        this.ripeTime = ripeTime;
    }

    public Date getRipeEndTime() {
        return ripeEndTime;
    }

    public void setRipeEndTime(Date ripeEndTime) {
        this.ripeEndTime = ripeEndTime;
    }

    public int getBlessingValue() {
        return blessingValue;
    }

    public void setBlessingValue(int blessingValue) {
        this.blessingValue = blessingValue;
    }

    public Map<String, PlayerBlessing> getPlayerBlessings() {
        return playerBlessings;
    }

    public void setPlayerBlessings(Map<String, PlayerBlessing> playerBlessings) {
        this.playerBlessings = playerBlessings;
    }

    public Map getPlayerShake() {
        return playerShake;
    }

    public void setPlayerShake(Map playerShake) {
        this.playerShake = playerShake;
    }

    public List<MoneyPick> getMoneyPickList() {
        return moneyPickList;
    }

    public void setMoneyPickList(List<MoneyPick> moneyPickList) {
        this.moneyPickList = moneyPickList;
    }

    @Override
    public String getId() {
        return familyId;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return familyId;
    }
}
