package com.pengpeng.stargame.model.player;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 玩家活跃度对象
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-11下午4:38
 */
public class ActivePlayer extends BaseEntity<String> {
    //pid
    private String pid;
    //完成度
    private Map<Integer,Integer> items = null;
    //是否已领取
    private Map<Integer,Boolean> rewards = null;

    private Date refreshDate = null;

    public ActivePlayer(){
        items = new HashMap<Integer,Integer>();
        rewards = new HashMap<Integer,Boolean>();
        refreshDate = new Date();
    }
    public ActivePlayer(String pid){
        this.pid = pid;
        items = new HashMap<Integer,Integer>();
        rewards = new HashMap<Integer,Boolean>();
        refreshDate = new Date();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * 初始化数据
     */
    public void init(){
        //1.当前日期如果跟刷新日期不一致,则清空数据
        refresh(new Date());
    }

    private void refresh(Date now){
        if (refreshDate==null||DateUtil.getDayOfYear(refreshDate) != DateUtil.getDayOfYear(now)) {
            items.clear();
            rewards.clear();
            refreshDate = now;
        }
    }

    public boolean isReward(int active){
        if (rewards.containsKey(active)){
            return rewards.get(active);
        }
        return false;
    }

    public void reward(int type){
        rewards.put(type,true);
    }

    public void setFinish(int type,int num){
        items.put(type,num);
    }
    public void finish(int type,int num){
            int value = getNum(type);
            value+=num;
            items.put(type,value);
    }

    public int getNum(int type){
        if (DateUtil.getDayOfYear(refreshDate)==DateUtil.getDayOfYear(new Date())){
            if(items.containsKey(type)){
                return items.get(type);
            }
        }
        items.put(type,0);
        return 0;
    }

    public boolean isMax(int type,int max){
        int value = getNum(type);
        if (value>=max){
            return true;
        }
        return false;
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
}
