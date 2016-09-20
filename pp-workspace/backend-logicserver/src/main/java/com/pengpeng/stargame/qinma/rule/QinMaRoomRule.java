package com.pengpeng.stargame.qinma.rule;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.room.DecoratePosition;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * User: mql
 * Date: 13-8-13
 * Time: 下午3:36
 */
@Entity
@Table(name="sg_rule_qinma_room")
public class QinMaRoomRule extends BaseEntity<String> {
    @Id
    @Column
    private String id;
    @Column
    private String name;
    @Column
    private int x;//多少行
    @Column
    private int y;//到少列
    @Column
    private String   decoratePositions ;

    @Transient
    private Map<String,DecoratePosition> items;

    public QinMaRoomRule() {
        items=  new HashMap<String,DecoratePosition>();
    }

    public QinMaRoomRule(String id, String name, int x, int y, String decoratePositions) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.decoratePositions = decoratePositions;
        this.items = new HashMap<String,DecoratePosition>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
       this.id = id;
    }

    @Override
    public String getKey() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDecoratePositions() {
        return decoratePositions;
    }

    public void setDecoratePositions(String decoratePositions) {
        this.decoratePositions = decoratePositions;
    }

    public Map<String, DecoratePosition> getItems() {
        return items;
    }

    public void setItems(Map<String, DecoratePosition> items) {
        this.items = items;
        Gson gson = new Gson();
        decoratePositions = gson.toJson(items);
    }

//    public static class DecoratePosition {
//
//        private String id;  //唯一Id
//
//        private String itemId;//物品Id
//        /**
//         * 格式    "1111,226,1"
//         */
//        private String position; //位置
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getItemId() {
//            return itemId;
//        }
//
//        public void setItemId(String itemId) {
//            this.itemId = itemId;
//        }
//
//        public String getPosition() {
//            return position;
//        }
//
//        public void setPosition(String position) {
//            this.position = position;
//        }
//    }


    public void init() {
        Gson gson = new Gson();
        Map<String,DecoratePosition> lists = gson.fromJson(decoratePositions,new TypeToken<Map<String,DecoratePosition>>(){}.getType());
        items = lists;

    }

}
