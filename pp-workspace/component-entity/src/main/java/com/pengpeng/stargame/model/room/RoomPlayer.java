package com.pengpeng.stargame.model.room;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.room.DecoratePosition;

import javax.persistence.Entity;
import java.util.*;

/**
 * User: mql
 * Date: 13-5-20
 * Time: 下午2:21
 */
@Entity
public class RoomPlayer extends BaseEntity<String> {
    private String pid;
    private int x;//左边
    private int y;//右边
    private Map<String,DecoratePosition> items;
    private int cExpansionId;//已经扩建到了 哪个Id
    private int expansionId;//正在扩建的Id ，为0表示没有在扩建
    private Date expansionEndTime;//扩建结束日期

    public RoomPlayer(){
        items=new HashMap<String,DecoratePosition>();
    }
    public RoomPlayer(String pid){
        this.pid=pid;
        items=new HashMap<String,DecoratePosition>();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getcExpansionId() {
        return cExpansionId;
    }

    public void setcExpansionId(int cExpansionId) {
        this.cExpansionId = cExpansionId;
    }

    public List<DecoratePosition> getDecoratePositionList() {
        return new ArrayList<DecoratePosition>(items.values());
    }

    public List<String> getDecorateIds(){
       List<String>  list=new ArrayList<String>();
       for(DecoratePosition decoratePosition:this.getDecoratePositionList()){
           list.add(decoratePosition.getItemId());

       }
        return list;
    }

    public void updateDecorate(String id,DecoratePosition decoratePosition){
        items.put(id,decoratePosition);
    }

    public boolean hasDecorate(String id){
        return items.containsKey(id);
    }

    public void add(DecoratePosition position){
        items.put(position.getId(),position);
    }
    public void remove(String id){
        items.remove(id);
    }
    public DecoratePosition getItem(String id){
        return items.get(id);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Map<String, DecoratePosition> getItems() {
        return items;
    }

    public void setItems(Map<String, DecoratePosition> items) {
        this.items = items;
    }

    public int getExpansionId() {
        return expansionId;
    }

    public void setExpansionId(int expansionId) {
        this.expansionId = expansionId;
    }

    public Date getExpansionEndTime() {
        return expansionEndTime;
    }

    public void setExpansionEndTime(Date expansionEndTime) {
        this.expansionEndTime = expansionEndTime;
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
}
