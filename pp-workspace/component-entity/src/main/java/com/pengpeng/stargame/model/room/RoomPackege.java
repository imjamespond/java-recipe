package com.pengpeng.stargame.model.room;

import com.google.gson.Gson;
import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

/**
 * User: mql
 * Date: 13-5-20
 * Time: 下午2:44
 */
@Entity
public class RoomPackege extends BaseEntity<String> {
    private String pid;
    private Map<String, Integer> items;


    public RoomPackege() {
        items = new HashMap<String, Integer>();
    }

    public RoomPackege(String pid) {
        this.pid = pid;
        items = new HashMap<String, Integer>();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Map<String, Integer> getItems() {
        return items;
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

    /**
     * 添加 物品
     *
     * @param itemId
     * @param num
     */
    public void addItem(String itemId, int num) {
        if (!items.containsKey(itemId)) {
            items.put(itemId, num);
        } else {
            items.put(itemId, items.get(itemId) + num);
        }

    }

    /**
     * 是否存在物品
     *
     * @param itemId
     * @return true 表示存在物品
     */
    public boolean hasItem(String itemId) {
        return items.containsKey(itemId);
    }

    /**
     * 使用物品
     *
     * @param itemId
     */
    public void useItem(String itemId) {
        decItem(itemId,1);
    }

    public int count(String itemsId) {
        if (!items.containsKey(itemsId)){
            return 0;
        }
        return items.get(itemsId);
    }

    public void decItem(String itemId, int num) {
        int count = 0;
        if (items.containsKey(itemId)){
            count = items.get(itemId);
        }
        items.put(itemId, count - num);
        if (items.get(itemId) <= 0) {
            items.remove(itemId);
        }
    }

    public static void main(String[] args) {
        RoomPackege roomPackege=new RoomPackege("dfffba3ffaa24d8d838e2c5663f99a4d");
        roomPackege.addItem("items_20100",2);
        roomPackege.addItem("items_20101",2);

        Gson gsom=new Gson();
        System.out.println(gsom.toJson(roomPackege));
    }
}
