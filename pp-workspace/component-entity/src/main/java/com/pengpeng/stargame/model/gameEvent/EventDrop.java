package com.pengpeng.stargame.model.gameEvent;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-12-20
 * Time: 上午11:20
 */
public class EventDrop extends BaseEntity<String> {
    //场景的Id
    private String id;
    //场景的下次掉落时间
    private Date nextTime;
    //掉落列表
    private List<OneDrop> oneDropList;


    public EventDrop(){

    }
    /**
     * 清理吊 过期的 礼物
     */
    public boolean  clear(){
        boolean save=false;
        List<OneDrop> delete=new ArrayList<OneDrop>();
        Date now=new Date();
       for(OneDrop oneDrop:oneDropList){
           if(oneDrop.getExpiration()==null){
               continue;
           }
           if(oneDrop.getExpiration().before(now)){
               delete.add(oneDrop);
           }
       }
        if(delete.size()>0){
            oneDropList.removeAll(delete);
            save=true;
        }
        return save;
    }

    /**
     * 添加
     * @param oneDrop
     */
    public void addOneDrop(OneDrop oneDrop){
        oneDropList.add(oneDrop);
    }

    /**
     * 通过id 查找
     * @param id
     * @return
     */
    public OneDrop getOneDropById(String id){
        for(OneDrop oneDrop:oneDropList){
            if(oneDrop.getUid()==null){
                continue;
            }
            if(oneDrop.getUid().equals(id)){
                return oneDrop;
            }
        }
        return null;
    }

    /**
     * 移除
     * @param oneDrop
     */

    public void remove(OneDrop oneDrop){
        oneDropList.remove(oneDrop);
    }

    /**
     * 通过玩家id 查找
     * @param pid
     */
    public int getBalloonNun(String pid){
        int num=0;
        for(OneDrop oneDrop:oneDropList){
            if(oneDrop.getPid()==null){
                continue;
            }
            if(oneDrop.getPid().equals(pid)){
                num++;
            }
        }
        return num;
    }

    public EventDrop(String id){
        this.id=id;
        nextTime=new Date();
        oneDropList=new ArrayList<OneDrop>();
    }

    public List<OneDrop> getOneDropList() {
        return oneDropList;
    }

    public void setOneDropList(List<OneDrop> oneDropList) {
        this.oneDropList = oneDropList;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return id;
    }

}
