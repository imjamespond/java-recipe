package com.pengpeng.stargame.model.piazza;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * User: mql
 * Date: 13-12-5
 * Time: 上午11:08
 */
public class FamilyBank extends BaseEntity<String> {
    private String familyId;
    private Map<String,Integer> rankInfo;

    public FamilyBank() {
        this.rankInfo = new HashMap<String, Integer>();
    }

    public FamilyBank(Map<String, Integer> rankInfo) {
        this.rankInfo = rankInfo;
    }


    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public Map<String, Integer> getRankInfo() {
        return rankInfo;
    }

    public void setRankInfo(Map<String, Integer> rankInfo) {
        this.rankInfo = rankInfo;
    }

    public void  delete(String pid){
        rankInfo.remove(pid);
    }

    /**
     * 存钱
     * @param pid
     * @param gamemoney
     */
    public void saveMoney(String pid,int gamemoney){
        if(rankInfo.containsKey(pid)){
            rankInfo.put(pid,rankInfo.get(pid)+gamemoney);
        }else {
            rankInfo.put(pid,gamemoney);
        }
    }

    /**
     * 取钱
     * @param pid
     * @param gamemoney
     */
    public void getMoney(String pid,int gamemoney){
        if(rankInfo.containsKey(pid)){
            if(rankInfo.get(pid)<gamemoney){
                rankInfo.put(pid,0);
            }else {
                rankInfo.put(pid,rankInfo.get(pid)-gamemoney);
            }

        }
    }

    /**
     * 查询某个人存的钱
     * @param pid
     * @return
     */
    public int getMoenyByPid(String pid){
        if(rankInfo.containsKey(pid)){
           return rankInfo.get(pid);
        }
        return 0;
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
